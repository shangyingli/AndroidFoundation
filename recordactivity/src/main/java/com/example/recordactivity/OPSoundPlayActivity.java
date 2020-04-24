package com.example.recordactivity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.recordactivity.service.IOpRecordService;
import com.example.recordactivity.service.IRecorderServiceCallback;
import com.example.recordactivity.service.OPRecordService;

import java.io.File;


public class OPSoundPlayActivity extends Activity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private ImageButton mDeleteRecordBtn;
    private ImageView mPlayRecordBtn;
    private ImageView mStopReCordBtn;
    private SeekBar mPlayPregressBr;
    private RecordView mRecoedView;
    private TextView mTotalTimeView;
    private IRecorderServiceCallback mIRecorderServiceCallback;
    private IOpRecordService mIOpRecordService;
    private int mState;
    public final static String TAG = OPSoundPlayActivity.class.getSimpleName();
    private String mFilePath;
    private long mRecordLength;
    private Handler mHandler = new Handler();
    private Toolbar toolbar;
    private boolean hasChange = false;
    private static final int SAVE_REQUEST_CODE = 0x01;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.op_sound_play);
        toolbar = findViewById(R.id.toolbar);
        setActionBar(toolbar);
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(getResources().getString(R.string.title));
        }
        Intent intent = getIntent();
        if (intent != null) {
            mFilePath = intent.getStringExtra("filePath");
            mRecordLength = intent.getLongExtra("recordLength", 0);
        }
        Log.d(TAG, "mFilePath  = " + mFilePath + "mRecordLength = " + mRecordLength);
        bindService();
        initView();
    }

    private void initView() {
        mDeleteRecordBtn = findViewById(R.id.id_delete);
        mDeleteRecordBtn.setOnClickListener(this);
        mPlayRecordBtn = findViewById(R.id.id_play);
        mPlayRecordBtn.setOnClickListener(this);
        mStopReCordBtn = findViewById(R.id.id_stop);
        mStopReCordBtn.setOnClickListener(this);
        mPlayPregressBr = findViewById(R.id.play_id_sound_seekbar);
        mPlayPregressBr.setOnSeekBarChangeListener(this);
        mRecoedView = findViewById(R.id.recor_view);
        mTotalTimeView = findViewById(R.id.total_time);
    }

    public void updateUI(int state) {
        Log.d(TAG, "updateUI state = " + state);
        switch (state) {
            case Recorder.IDLE_STATE:
                Log.d(TAG, "idle state");
                if (mRecoedView != null) {
                    mRecoedView.mStateView.setText("");
                }
                pausePlayAnimation();
                mStopReCordBtn.setEnabled(false);
                mPlayPregressBr.setEnabled(false);
                mPlayPregressBr.setClickable(false);
                mPlayPregressBr.setProgress(0);
                mTotalTimeView.setText(formatShowTime(mRecordLength, false));
                break;
            case Recorder.PLAYING_STATE:
                Log.d(TAG, "isPlaying");
                if (mRecoedView != null) {
                    mRecoedView.mStateView.setText(getResources().getString(R.string.op_record_state_play_string));
                }
                startPlayAnimation();
                mStopReCordBtn.setEnabled(true);
                mDeleteRecordBtn.setEnabled(true);
                mPlayPregressBr.setEnabled(true);
                mPlayPregressBr.setClickable(true);
                mPlayPregressBr.setSelected(true);
                break;
            case Recorder.PLAY_PAUSE_STATE:
                if (mRecoedView != null) {
                    mRecoedView.mStateView.setText(getResources().getString(R.string.op_record_state_pause_string));
                }
                pausePlayAnimation();
                mStopReCordBtn.setEnabled(true);
                mDeleteRecordBtn.setEnabled(true);
                break;
        }
        mState = state;
        updateTimerViewer();
    }

    private Runnable mUpdateTimer = new Runnable() {
        @Override
        public void run() {
            updateTimerViewer();
        }
    };

    private void updateTimerViewer() {
        if (mIOpRecordService == null) {
            return;
        }
        try {
            int state = mIOpRecordService.getState();
            long progress = mIOpRecordService.getProgress();
            Log.d(TAG, "state = " + state + "progress = " + progress + "mIOpRecordService.getDuration() = " + mIOpRecordService.getDuration());
            mHandler.removeCallbacks(mUpdateTimer);
            if (state == Recorder.IDLE_STATE) {
                progress = 0;
            }
            if (mRecoedView != null) {
                mRecoedView.updateTimer(formatShowTime(progress, false),
                        mState != Recorder.RECORD_PAUSE_STATE && mState != Recorder.PLAY_PAUSE_STATE);
            }
            if (state == Recorder.PLAYING_STATE) {
                mHandler.postDelayed(mUpdateTimer, 90);
                if (mPlayPregressBr != null) {
                    updatePlayProgress(progress, mIOpRecordService.getDuration());
                }
            } else if (state == Recorder.PLAY_PAUSE_STATE) {

            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void updatePlayProgress(long time, long total) {
        Log.d(TAG, "time = " + time + "total = " + total);
        if (total > 0) {
            mPlayPregressBr.setMax((int) total);
            Log.d(TAG, "mPlayPregressBr.getProgress() = " + mPlayPregressBr.getProgress());
            if (mPlayPregressBr.getProgress() < time) {
                int mProgress = (int) (time);
                mPlayPregressBr.setProgress(mProgress);
            }
        }
    }

    private String formatShowTime(long time, boolean canshow) {
        long ms = (time % 1000) / 100;
        time = time / 1000;
        long h = time / 3600;
        long m = (time % 3600) / 60;
        long s = time % 60;

        if (h >= 0) {
            return canshow ? String.format("%02d:%02d:%02d.%d", h, m, s, ms)
                    : String.format("%02d:%02d:%02d", h, m, s);
        } else {
            return canshow ? String.format("%02d:%02d.%d", m, s, ms) : String
                    .format("%02d:%02d", m, s);
        }
    }

    public void pausePlayAnimation() {
        mPlayRecordBtn.setImageResource(R.drawable.op_sound_start_icon);
    }

    private void startPlayAnimation() {
        mPlayRecordBtn.setImageResource(R.drawable.op_record_pause_icon);
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected");
            mIOpRecordService = IOpRecordService.Stub.asInterface(service);
            try {
                if (mIOpRecordService != null) {
                    mIOpRecordService.registerCallback(mIRecorderServiceCallback);
                    updateUI(mIOpRecordService.getState());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private boolean deleteRecordingFile(String filePath) {
        File curFile = new File(filePath);
        if (curFile.exists()) {
            return curFile.delete();
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_play:
                if (mIOpRecordService == null) {
                    break;
                }
                try {
                    Log.d(TAG, "mIOpRecordService.getState() = " + mIOpRecordService.getState());
                    if (mIOpRecordService.getState() == Recorder.PLAY_PAUSE_STATE
                            || mIOpRecordService.getState() == Recorder.PLAYING_STATE) {
                        mIOpRecordService.pauseOrResum();
                        break;
                    }
                    if (mIOpRecordService.getState() == Recorder.IDLE_STATE) {
                        mIOpRecordService.playRecord(mFilePath);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.id_delete:
                //delete current recording file and return record interface
                if (deleteRecordingFile(mFilePath)) {
                    try {
                        mIOpRecordService.setState(Recorder.IDLE_STATE);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(this, "delete success", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_CANCELED);
                    finish();
                }
                break;

        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    class RecordServiceCallback extends IRecorderServiceCallback.Stub {

        @Override
        public void onStateChanged(int state) throws RemoteException {
            updateUI(state);
        }

        @Override
        public void onError(int error) throws RemoteException {

        }

        @Override
        public void onPlayChanged(int state) throws RemoteException {
            Log.d(TAG, "STATE = " + state);
            if (state == Recorder.PLAYING_STATE) {
                if (mIOpRecordService != null && mPlayPregressBr != null)
                    mIOpRecordService.seekPlay(mPlayPregressBr.getProgress());
            }
        }

        @Override
        public void onWaveChanged(float[] waves, int availableWave) throws RemoteException {

        }
    }

    private void bindService() {
        Intent service = new Intent(this, OPRecordService.class);
        startForegroundService(service);
        mIRecorderServiceCallback = new RecordServiceCallback();
        bindService(service, connection, Service.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mIOpRecordService != null) {
            try {
                mIOpRecordService.unregisterRecorderCallback(mIRecorderServiceCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            unbindService(connection);
            mIOpRecordService = null;
            mIRecorderServiceCallback = null;
            connection = null;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       getMenuInflater().inflate(R.menu.menu, menu);
       return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_save:
                // TODO: 2020/4/24   delete the old greeting
                Toast.makeText(this, "save success", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                try {
                    mIOpRecordService.setState(Recorder.IDLE_STATE);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                finish();
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (hasChange) {

        } else {
            finish();
        }
    }
}
