package com.example.recordactivity;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.recordactivity.service.IOpRecordService;
import com.example.recordactivity.service.IRecorderServiceCallback;
import com.example.recordactivity.service.OPRecordService;

import java.io.File;
import java.util.ArrayList;

public class OPSoundRecordActivity extends Activity implements View.OnClickListener, OnSaveDialogClickListener {

    private ImageView mRecordBtn;
    private ImageView mStopBtn;
    private ImageView mDeleteBtn;
    private SeekBar mSeekBr;
    private TextView mStateView;
    private LinearLayout mTimerLayout;
    private TextView mHourView;
    private TextView mMinView;
    private TextView mSecView;
    private IOpRecordService mIopRecordService;
    private long mMaxFileSize = -1;
    private ArrayList<String> mPermissions = new ArrayList<>();
    private static final int REQUEST_WRITE_STORGE_INIT = 1;
    private IRecorderServiceCallback mIRecorderServiceCallback;
    private boolean isButtonPress = false;
    private Handler mHandler = new Handler();
    private int mState = 0;
    private Toolbar toolbar;

    public static final String ARM_END = ".amr";
    public static final String AAC_END = ".aac";
    public static final String WAV_END = ".wav";

    private String savedRecordFilePath = "";
    private static final int SAVE_REQUEST_CODE = 0x01;

    public final static String TAG = OPSoundRecordActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.op_home_main);
        toolbar = findViewById(R.id.toolbar);
        setActionBar(toolbar);
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(getResources().getString(R.string.title));
        }
        initView();
        mMaxFileSize = 1000000;
        bindService();
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkAllPermissions();
        if (mPermissions.size() > 0) {
            String[] permissions = new String[mPermissions.size()];
            for (int j = 0; j < mPermissions.size(); j++) {
                permissions[j] = mPermissions.get(j);
            }
            ActivityCompat
                    .requestPermissions(
                            this,
                            permissions,
                            REQUEST_WRITE_STORGE_INIT);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void checkAllPermissions() {
        mPermissions.clear();
        checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
        checkPermission(Manifest.permission.RECORD_AUDIO);
    }

    public void checkPermission(String permission) {
        if (ContextCompat.checkSelfPermission(this,
                permission) != PackageManager.PERMISSION_GRANTED) {
            mPermissions.add(permission);
        }
    }

    private void initView() {
        mRecordBtn = findViewById(R.id.id_record);
        mStopBtn = findViewById(R.id.id_stop);
        mRecordBtn.setOnClickListener(this);
        mStopBtn.setOnClickListener(this);
        mStateView = (TextView) findViewById(R.id.id_stateView);
        mTimerLayout = (LinearLayout) findViewById(R.id.id_timerView1);
        mHourView = (TextView) findViewById(R.id.op_record_page_hourView);
        mMinView = (TextView) findViewById(R.id.op_record_page_minView);
        mSecView = (TextView) findViewById(R.id.op_record_page_secView);
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected");
            mIopRecordService = IOpRecordService.Stub.asInterface(service);
            try {
                mIopRecordService.registerCallback(mIRecorderServiceCallback);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    public boolean onSave(String rename) {
        if (rename.trim().equals("")) {
            Toast.makeText(this, R.string.input_file_name_please, Toast.LENGTH_SHORT).show();
            return false;
        }
        updateTimerView();
        if (saveRecordFile(rename, false)) {
            Log.d(TAG, "save success");
            //文件保存成功后， 更新界面为播放模式
            Toast.makeText(this, getResources().getString(R.string.record_completed), Toast.LENGTH_SHORT).show();
            long recordLength = 0;
            try {
                recordLength = mIopRecordService.getProgress();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            goPlayRecoed(savedRecordFilePath, recordLength);
        }
        return true;
    }

    @Override
    public void onDelete() {

    }

    private void goPlayRecoed(String filePath, long recordLength) {
        Log.d(TAG, "filePath = " + filePath + "recordLength = " + recordLength);
        Intent intent = new Intent(this, OPSoundPlayActivity.class);
        intent.putExtra("filePath", filePath);
        intent.putExtra("recordLength", recordLength);
        startActivityForResult(intent, SAVE_REQUEST_CODE);
    }

    private boolean saveRecordFile(String rename, boolean addPrefix) {
        String newFileName = "";
        newFileName = rename + AAC_END;
        File newFile = null;
        if (mIopRecordService == null) {
            return false;
        }
        try {
            if (mIopRecordService.isRecording()) {
                mIopRecordService.stopRecorder();
            }
            File oldFile = new File(mIopRecordService.getSampleFilePath());
            String path = Recorder.RECORD_FILE_PATH;
            newFile = new File(path, newFileName);
            oldFile.renameTo(newFile);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        savedRecordFilePath = newFile.getAbsolutePath();
        Log.d(TAG, "savedRecordFilePath = " + savedRecordFilePath);
        return true;
    }

    class RecordServiceCallback extends IRecorderServiceCallback.Stub {

        @Override
        public void onStateChanged(int state) throws RemoteException {
            if (state > 0) {
                isButtonPress = true;
            }
            Log.d(TAG, "onStateChanged state = " + state);
            updateUI(state);
        }

        @Override
        public void onError(int error) throws RemoteException {
            Log.d(TAG, "error = " + error);
            switch (error) {
                case Recorder.FILE_SIZE_REACH:
                    if (mIopRecordService != null) {
                        //到达长度时自动保存
                        Toast.makeText(OPSoundRecordActivity.this, getResources().getString(R.string.max_length_reached), Toast.LENGTH_LONG).show();
                    }
                    break;
            }
        }

        @Override
        public void onPlayChanged(int state) throws RemoteException {

        }

        @Override
        public void onWaveChanged(float[] waves, int availableWave) throws RemoteException {

        }
    }

    private void updateUI(int state) {
        switch (state) {
            case Recorder.IDLE_STATE:
                if (mStateView != null) {
                    mStateView.setText("");
                }
                if (isButtonPress) {
                    isButtonPress = false;
                    if (mState == Recorder.RECORDING_STATE) {
                        if (mRecordBtn.isEnabled()) {
                            pauseRecordAnimation();
                        }
                    } else {
                        if (mRecordBtn.isEnabled()) {
                            mRecordBtn.setImageResource(R.drawable.op_sound_record_icon);
                        }
                    }
                } else {
                    if (mRecordBtn.isEnabled()) {
                        mRecordBtn.setImageResource(R.drawable.op_sound_record_icon);
                    }
                }
                mStopBtn.setEnabled(false);
                break;
            case Recorder.RECORDING_STATE:
                if (mStateView != null) {
                    mStateView.setText(getResources().getString(R.string.op_record_state_record_string));
                }
                if (isButtonPress) {
                    isButtonPress = false;
                    startRecordAnimation();
                    mStopBtn.setEnabled(true);
                }
                break;
            case Recorder.RECORD_PAUSE_STATE:
                if (mStateView != null) {
                    mStateView.setText(getResources().getString(R.string.op_record_state_pause_string));
                }
                pauseRecordAnimation();
                mStopBtn.setEnabled(true);
                break;
//            case Recorder.PLAY_PAUSE_STATE:
//                if (mStateView != null) {
//                    mStateView.setText(getResources().getString(R.string.op_record_state_pause_string));
//                }
//                pausePlayAnimation();
//                mDeleteBtn.setVisibility(View.VISIBLE);
//                mDeleteBtn.setOnClickListener(this);
//                mSeekBr.setVisibility(View.VISIBLE);
//                break;
//            case Recorder.PLAYING_STATE:
//
//                break;
        }
        mState = state;
        updateTimerView();

    }

    public void pausePlayAnimation() {
        mRecordBtn.setImageResource(R.drawable.op_sound_start_icon);
    }

    private Runnable mUpdateTimer = new Runnable() {
        @Override
        public void run() {
            updateTimerView();
        }
    };

    public void updateTimerView() {
        Log.d(TAG, "updateTimerView");
        if (mIopRecordService == null) {
            return;
        }
        try {
            int state = mIopRecordService.getState();
            long time = mIopRecordService.getProgress();
            Log.d(TAG, "state = " + state + "time = " + time);
            mHandler.removeCallbacks(mUpdateTimer);
            updateTimer(formatShowTime(time, false), state != Recorder.PLAY_PAUSE_STATE);
            if (state == Recorder.RECORDING_STATE) {
                mHandler.postDelayed(mUpdateTimer, 50);
            } else if (state == Recorder.RECORD_PAUSE_STATE) {

            }

        } catch (RemoteException e) {
            e.printStackTrace();
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

    public void updateTimer(String time, boolean is) {
        String[] times = time.split(":");

        if (mHourView != null) {
            mHourView.setText(times[0]);
        }
        if (mMinView != null) {
            mMinView.setText(times[1]);
        }
        if (mSecView != null) {
            mSecView.setText(times[2]);
        }
    }

    private void startRecordAnimation() {
        if (mRecordBtn == null) {
            return;
        }
        mRecordBtn.setImageResource(R.drawable.op_record_pause_icon);
    }

    private void pauseRecordAnimation() {
        if (mRecordBtn == null) {
            return;
        }
        mRecordBtn.setImageResource(R.drawable.op_sound_record_icon);
    }

    private void bindService() {
        Intent service = new Intent(this, OPRecordService.class);
        startForegroundService(service);
        mIRecorderServiceCallback = new RecordServiceCallback();
        bindService(service, connection, Service.BIND_AUTO_CREATE);
    }

    private void unbindService() {
        if (mIopRecordService != null) {
            try {
                mIopRecordService
                        .unregisterRecorderCallback(mIRecorderServiceCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            unbindService(connection);
            mIopRecordService = null;
            mIRecorderServiceCallback = null;
            connection = null;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_record:
                if (mIopRecordService == null) {
                    return;
                }
                try {
//                    if (mIopRecordService.getState() == Recorder.PLAYING_STATE
//                    || mIopRecordService.getState() == Recorder.PLAY_PAUSE_STATE) {
//
//                        break;
//                    }
                    Log.d(TAG, "mIopRecordService.getState() = " +mIopRecordService.getState());
                    if (mIopRecordService.getState() == Recorder.RECORD_PAUSE_STATE
                            || mIopRecordService.getState() == Recorder.IDLE_STATE) {
                        isButtonPress = true;
                        mIopRecordService.startRecord(mMaxFileSize);
                    } else if (mIopRecordService.getState() == Recorder.RECORDING_STATE) {
                        isButtonPress = true;
                        Log.d(TAG, "pauseRecorder click");
                        mIopRecordService.pauseRecorder();
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.id_stop:
                if (mIopRecordService == null) {
                    return;
                }
                try {
                    Log.d(TAG, "stopRecorder click ");
                    if (Recorder.RECORD_PAUSE_STATE == mIopRecordService.getState()
                            || Recorder.RECORDING_STATE == mIopRecordService.getState()) {
                        long progress = mIopRecordService.getProgress();
                        Log.d(TAG, "progress = " + progress);
                        if (progress < 1000) {
                            Toast.makeText(this, R.string.op_too_low_tips,
                                    Toast.LENGTH_SHORT).show();
                            break;
                        }
                        mIopRecordService.stopRecorder();
                        showSaveDialog();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private SaveDialogFragment mSaveDialogFragment;

    private void showSaveDialog() {
        Log.d(TAG, "showSaveDialog");
        if (mIopRecordService == null) {
            return;
        }
        try {
            mSaveDialogFragment = new SaveDialogFragment();
            mSaveDialogFragment.setCancelable(false);
            mSaveDialogFragment.setRecordName(mIopRecordService.getRecordName());
            FragmentManager fm = getFragmentManager();
            mSaveDialogFragment.show(fm, "save");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SAVE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                unbindService();
                finish();
            } else {

            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mHandler.removeCallbacks(mUpdateTimer);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService();
    }
}
