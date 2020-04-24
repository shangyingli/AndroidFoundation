package com.example.recordactivity.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.RemoteException;
import android.util.Log;

import com.example.recordactivity.Recorder;

import java.io.File;
import java.lang.ref.WeakReference;

public class OPRecordService extends Service implements Recorder.OnStateChangeListener {

    public final static String TAG = OPRecordService.class.getSimpleName();
    private Recorder mRecorder;
    private MediaPlayer mPlayer;
    private long mMaxSize;
    public static final String ARM_END = ".amr";
    public static final String AAC_END = ".aac";
    public static final String WAV_END = ".wav";

    private static final String CHANNEL_ID = "default_chanel";
    private IRecorderServiceCallback mIRecordServiceCallback;
    private PowerManager.WakeLock mWakeLock;
    private AudioManager mAudioManager;

    // play state
    private int mPlayState = 0;
    public static final int STATE_IDLE = 0;
    public static final int STATE_STARTED = 1;
    public static final int STATE_PLAYING = 2;
    public static final int STATE_PAUSE = 3;

    @Override
    public void onCreate() {
        super.onCreate();
        mRecorder = new Recorder(this);
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        mWakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, ":record");
        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        initPlayer();
        mRecorder.setOnStateChangeListener(this);
    }

    private MediaPlayer.OnCompletionListener mOnCompletionListener = new MediaPlayer.OnCompletionListener() {

        @Override
        public void onCompletion(MediaPlayer mp) {
            Log.d(TAG, "onCompletion");
            if (mRecorder == null) {
                return;
            }
            if (mWakeLock != null && mWakeLock.isHeld()) {
                mWakeLock.release();
            }
            mRecorder.setState(Recorder.IDLE_STATE);
            setPlayState(STATE_IDLE);
        }
    };

    private MediaPlayer.OnErrorListener onErrorListener = new MediaPlayer.OnErrorListener() {

        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            stopPlay();
            return false;
        }
    };

    private MediaPlayer.OnPreparedListener mOnPreparedListener = new MediaPlayer.OnPreparedListener() {

        @Override
        public void onPrepared(MediaPlayer mp) {
            Log.d(TAG, "mOnPreparedListener");
            mp.start();
//            if (mVisualizer != null) {
//                mVisualizer.setEnabled(true);
//            }
            setPlayState(STATE_PLAYING);
            mRecorder.setState(Recorder.PLAYING_STATE);
//            mHandler.post(mUpdatePlayWaveRunnable);
        }
    };

    private AudioManager.OnAudioFocusChangeListener mAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {

        @Override
        public void onAudioFocusChange(int focusChange) {
            Log.i(TAG, "focusChange:" + focusChange);
            if (mPlayer == null) {
                return;
            }
            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_GAIN:
//                    if (mPauseByfocus) {
//                        pauseOrResum();
//                    }
//                    mPauseByfocus = false;
                    break;
                case AudioManager.AUDIOFOCUS_LOSS:
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
//                    mPauseByfocus = true;
//                    pauseOrResum();
                    break;
                default:
                    break;
            }
        }
    };

    private void initPlayer() {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mPlayer.setOnCompletionListener(mOnCompletionListener);
            mPlayer.setOnErrorListener(onErrorListener);
            mPlayer.setOnPreparedListener(mOnPreparedListener);
        } catch (Exception e) {
            mPlayer = null;
            return;
        }
    }

    private void stopPlay() {

    }

    private int getPlayState() {
        return mPlayState;
    }

    private void setPlayState(final int state) {
        if (state == mPlayState) {
            return;
        }
        mPlayState = state;
        try {
            mIRecordServiceCallback.onStateChanged(state);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "state:" + state);
        switch (state) {
            case STATE_IDLE:
            case STATE_PAUSE:
//                if(!mPauseByfocus){
//                    mAudioManager.abandonAudioFocus(mAudioFocusChangeListener);
//                }
                break;
            case STATE_PLAYING:
                mAudioManager.requestAudioFocus(mAudioFocusChangeListener,
                        AudioManager.STREAM_MUSIC,
                        AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                break;
            default:
                break;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        startForeground(2, createNotification());
        return START_NOT_STICKY;
    }

    private Notification createNotification() {
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "record", NotificationManager.IMPORTANCE_DEFAULT);
        nm.createNotificationChannel(channel);
        Notification notification = new Notification.Builder(getApplicationContext(), CHANNEL_ID).build();
        return notification;
    }

    @Override
    public void onStateChanged(int state) {
        try {
            mIRecordServiceCallback.onStateChanged(state);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(int error) {
        try {
            mIRecordServiceCallback.onError(error);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    class IServiceConnection extends IOpRecordService.Stub {

        private WeakReference<OPRecordService> mOpRecordServiceReference;

        public IServiceConnection(OPRecordService opRecordService) {
            mOpRecordServiceReference = new WeakReference<>(opRecordService);
        }

        @Override
        public void startRecord(long size) throws RemoteException {
            OPRecordService service = mOpRecordServiceReference.get();
            if (service != null) {
                service.startRecorder(size);
            }
        }

        @Override
        public void stopRecorder() throws RemoteException {
            OPRecordService service = mOpRecordServiceReference.get();
            if (service != null) {
                service.stopRecord();
            }
        }

        @Override
        public void pauseRecorder() throws RemoteException {
            OPRecordService service = mOpRecordServiceReference.get();
            if (service != null) {
                service.pauseRecorder();
            }
        }

        @Override
        public int getState() throws RemoteException {
            OPRecordService service = mOpRecordServiceReference.get();
            if (service != null) {
                return service.getState();
            }
            return 0;
        }

        @Override
        public int setState(int state) throws RemoteException {
            OPRecordService service = mOpRecordServiceReference.get();
            if (service != null) {
                service.setState(state);
            }
            return 0;
        }

        @Override
        public long getProgress() throws RemoteException {
            OPRecordService service = mOpRecordServiceReference.get();
            if (service != null) {
                return service.getProgress();
            }
            return 0;
        }

        @Override
        public String getRecordName() throws RemoteException {
            OPRecordService service = mOpRecordServiceReference.get();
            if (service != null) {
                return service.getRecorderName();
            }
            return "";
        }

        @Override
        public String getSampleFilePath() throws RemoteException {
            OPRecordService service = mOpRecordServiceReference.get();
            if (service != null) {
                return service.getSampleFilePath();
            }
            return "";
        }

        @Override
        public boolean isRecording() throws RemoteException {
            OPRecordService service = mOpRecordServiceReference.get();
            if (service != null) {
                return service.isRecording();
            }
            return false;
        }

        @Override
        public void playRecord(String path) throws RemoteException {
            OPRecordService service = mOpRecordServiceReference.get();
            if (service != null) {
                service.playRecord(path);
            }
        }

        @Override
        public void pauseOrResum() throws RemoteException {
            OPRecordService service = mOpRecordServiceReference.get();
            if (service != null) {
                service.pauseOrResum();
            }
        }

        @Override
        public boolean isPlaying() throws RemoteException {
            return false;
        }

        @Override
        public long getCurrentPosition() throws RemoteException {
            return 0;
        }

        @Override
        public long getDuration() throws RemoteException {
            OPRecordService service = mOpRecordServiceReference.get();
            if (service != null) {
                return service.getDuration();
            }
            return 0;
        }

        @Override
        public void seekPlay(long pos) throws RemoteException {

        }

        @Override
        public int getPlayState() throws RemoteException {
            return 0;
        }

        @Override
        public void registerCallback(IRecorderServiceCallback callback) throws RemoteException {
            OPRecordService service = mOpRecordServiceReference.get();
            if (service != null) {
                service.addIRecordServiceCallback(callback);
            }
        }

        @Override
        public void unregisterRecorderCallback(IRecorderServiceCallback callback) throws RemoteException {

        }
    }

    private void addIRecordServiceCallback(IRecorderServiceCallback recorderServiceCallback) {
        mIRecordServiceCallback = recorderServiceCallback;
    }

    private void setState(int state) {
        if (mRecorder != null) {
            mRecorder.setState(state);
        }
    }
    private String getRecorderName() {
        if (mRecorder == null) {
            return "";
        }
        return mRecorder.mSampleFileName;
    }

    private String getSampleFilePath() {
        File sampleFile = getSampleFile();
        if (sampleFile == null) {
            return null;
        }
        return sampleFile.getAbsolutePath();
    }

    private File getSampleFile() {
        if (mRecorder == null) {
            return null;
        }
        return mRecorder.sampleFile();
    }


    private long getProgress() {
        if (mRecorder == null) {
            return 0;
        }
        if (getState() == Recorder.PLAYING_STATE
                || getState() == Recorder.PLAY_PAUSE_STATE) {
            return getCurrentPosition();
        } else {
            long progress = mRecorder.progress() > 0 ? mRecorder.progress() : mRecorder.sampleLength();
            if (progress < 150) {
                progress = 0;
            } else {
                progress -= 150;
            }
            return progress;
        }
    }

    private long getDuration() {
        if (mPlayer == null || mPlayState < STATE_PLAYING) {
            return 0;
        }
        Log.d(TAG, "getDuration = " + mPlayer.getDuration());
        return mPlayer.getDuration();
    }

    private long getCurrentPosition() {
        if (mPlayer == null || mPlayState < STATE_PLAYING) {
            return 0;
        }
        long position = mPlayer.getCurrentPosition();
        long duration = getDuration();
        Log.d(TAG, "position = " + position + "duration = " + duration);
        if (position > duration) {
            position = duration;
        }
        return position;
    }

    private int getState() {
        if (mRecorder == null) {
            return Recorder.IDLE_STATE;
        }
        return mRecorder.state();
    }

    private void pauseOrResum() {
        if (mPlayer == null || mRecorder == null) {
            return;
        }
        if (mPlayer.isPlaying()) {
            mPlayer.pause();
            mRecorder.setState(Recorder.PLAY_PAUSE_STATE);
            setPlayState(STATE_PAUSE);
        } else {
            mPlayer.start();
            mRecorder.setState(Recorder.PLAYING_STATE);
            setPlayState(STATE_PLAYING);
        }
    }

    private void stopRecord() {
        if (mRecorder == null) {
            return;
        }
        mRecorder.stop();
    }

    private void startRecorder(long size) {
        if (mRecorder == null) {
            return;
        }
        mMaxSize = size;
        int format = MediaRecorder.OutputFormat.AMR_NB;
        String extension = ARM_END;
        if (Recorder.RECORD_PAUSE_STATE == mRecorder.state()) {
            mRecorder.resumeRecording();
        } else {
            mRecorder.startRecording(format, extension, size);
        }
    }

    private void playRecord(String path) {
        Log.i(TAG, "mPlayer = " + mPlayer);
        if (mPlayer == null || mRecorder == null) {
            return;
        }

        try {
            if (mWakeLock != null && mWakeLock.isHeld()) {
                mWakeLock.release();
            }
            mWakeLock.acquire();
            mPlayer.reset();
            if (path.startsWith("content://")) {
                mPlayer.setDataSource(this, Uri.parse(path));
            } else {
                mPlayer.setDataSource(path);
            }
            mPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
        setPlayState(STATE_STARTED);
    }

//    private Runnable mPauseSaveRunnable = new Runnable() {
//        @Override
//        public void run() {
//            if (isRecording()) {
//
//            }
//        }
//    };

    private void pauseRecorder() {
        if (mRecorder == null) {
            return;
        }
        mRecorder.pauseRecording();
    }

    private boolean isRecording() {
        if (mRecorder == null) {
            return false;
        }
        return mRecorder.state() == Recorder.RECORDING_STATE
                || mRecorder.state() == Recorder.RECORD_PAUSE_STATE;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new IServiceConnection(this);
    }
}
