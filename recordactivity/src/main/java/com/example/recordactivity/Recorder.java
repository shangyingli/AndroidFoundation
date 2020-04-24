package com.example.recordactivity;

import android.app.Service;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;

import java.io.File;
import java.io.IOException;

public class Recorder {

    public final static String TAG = Recorder.class.getSimpleName();
    private Context mContext;
    private AudioManager mAudioManager;
    private MediaRecorder mRecorder = null;
    private MediaPlayer mPlayer = null;
    private static int DEFAULT_RATE = 48 * 1000;
    public File mSampleFile;
    public String mSampleFileName;
    private static final String INTERNAL = Environment.getExternalStorageDirectory().getPath();
    public static String RECORD_FILE_PATH = INTERNAL + "/Record/SoundRecord/";
    private OnStateChangeListener mOnStateChangeListener;
    public static final int IDLE_STATE = 0;
    public static final int RECORDING_STATE = 1;
    public static final int PLAYING_STATE = 2;
    public static final int RECORD_PAUSE_STATE = 3;
    public static final int PLAY_PAUSE_STATE = 4;

    public static final int SDCARD_ACCESS_ERROR = 1;
    public static final int INTERNAL_ERROR = 2;
    public static final int IN_CALL_RECORD_ERROR = 3;
    public static final int FILE_SIZE_REACH = 4;

    int mState = IDLE_STATE;

    private long mSampleStart;
    private long mSampleLength;
    private long mSampleLengthResume;

    public Recorder(Context context) {
        mContext = context;
        mAudioManager = (AudioManager) context.getSystemService(Service.AUDIO_SERVICE);
    }

    public interface OnStateChangeListener {
        void onStateChanged(int state);
        void onError(int error);
    }

    public void setOnStateChangeListener(OnStateChangeListener onStateChangeListener) {
        mOnStateChangeListener = onStateChangeListener;
    }

    public void resumeRecording() {
        if (mRecorder == null) {
            return;
        }
        mRecorder.resume();
        mSampleStart = SystemClock.elapsedRealtime();
        setState(RECORDING_STATE);
    }

    public File sampleFile() {
        return mSampleFile;
    }

    private void setSampleLengthNull() {
        mSampleLength = 0;
        mSampleFile = null;
    }

    public void startRecording(int outputFormat, String extension, long limitSize) {
        Log.d(TAG, "startRecording");
        stop();
        setSampleLengthNull();
        mRecorder = new MediaRecorder();
        if (mSampleFile == null) {
            createRecordFilePath();
            long time = SystemClock.elapsedRealtime();
            mSampleFile = new File(RECORD_FILE_PATH, time + extension);
            mSampleFileName = mSampleFile.getName();
        }
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        mRecorder.setAudioChannels(1);
        mRecorder.setAudioSamplingRate(DEFAULT_RATE);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mRecorder.setAudioEncodingBitRate(156000);
        if (limitSize != -1) {
            try {
                mRecorder.setMaxFileSize(limitSize);
            } catch (Exception e) {
                e.printStackTrace();
            }
            mRecorder.setOnInfoListener(new MediaRecorder.OnInfoListener() {
                @Override
                public void onInfo(MediaRecorder mr, int what, int extra) {
                    if (what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_FILESIZE_REACHED) {
                        Log.d(TAG, "reach max filsize");
                        stopRecording();
                        setError(FILE_SIZE_REACH);
                    }
                }
            });
        }
        mRecorder.setOutputFile(mSampleFile.getAbsolutePath());
        try {
            mRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            mRecorder.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mSampleStart = SystemClock.elapsedRealtime();
        setState(RECORDING_STATE);
    }


    public void pauseRecording() {
        Log.i(TAG, "pauseRecording mRecorder = " + mRecorder + "mState = " + mState);
        if (mRecorder == null || mState != RECORDING_STATE) {
            return;
        }
        Log.d(TAG, "mRecorder.pause");
        mRecorder.pause();
        mSampleLength += SystemClock.elapsedRealtime() - mSampleStart;
        mSampleLengthResume = mSampleLength;
        setState(RECORD_PAUSE_STATE);
    }

    public void setState(int state) {
        if (state == mState) {
            return;
        }
        mState = state;
        signalStateChanged(state);
    }

    public int state() {
        return mState;
    }

    public long progress() {
        if (mState == RECORDING_STATE) {
            return mSampleLengthResume + SystemClock.elapsedRealtime() - mSampleStart;
        }
        return 0;
    }

    public long sampleLength() {
        return mSampleLength;
    }

    private void setError(int error) {
        if (mOnStateChangeListener != null) {
            mOnStateChangeListener.onError(error);
        }
    }

    private void signalStateChanged(int state) {
        if (mOnStateChangeListener != null)
            mOnStateChangeListener.onStateChanged(state);
    }

    public void stop() {
        stopRecording();
    }

    private void stopRecording() {
        if (mRecorder == null) {
            return;
        }

        if (mState == RECORDING_STATE) {
            mSampleLength += SystemClock.elapsedRealtime() - mSampleStart;
        }

        try {
            mRecorder.stop();
            mRecorder.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mRecorder = null;
        mSampleLengthResume = 0;
        setState(IDLE_STATE);
    }

    private File createRecordFilePath() {
        File sdcardFilePath = new File(RECORD_FILE_PATH);
        if (!sdcardFilePath.exists()) {
            sdcardFilePath.mkdirs();
        }
        return sdcardFilePath;
    }
}
