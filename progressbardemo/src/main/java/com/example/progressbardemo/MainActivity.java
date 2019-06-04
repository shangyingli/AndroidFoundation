package com.example.progressbardemo;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public final static String TAG = MainActivity.class.getSimpleName();
    private ProgressBar progressBar;
    private Button button;
    private static final int MSG_UPDATE = 0x1;

    private UpdateProgressHandler handler;

    private static class UpdateProgressHandler extends Handler {

        private WeakReference<Activity> weakView;
        private ProgressBar progressBar;
        private Context context;

        public UpdateProgressHandler(Activity activity) {
            weakView = new WeakReference<>(activity);
            progressBar = ((MainActivity) activity).progressBar;
            context = ((MainActivity) activity).getApplicationContext();
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_UPDATE) {
                Log.d(TAG, "接收到message!");
                this.sendEmptyMessageDelayed(MSG_UPDATE, 100);
            } else {
                removeMessages(MSG_UPDATE);
            }
            int progress = progressBar.getProgress();
            if (progress < 100) {
                progress++;
            } else {
                Toast.makeText(context, "下载完成！", Toast.LENGTH_LONG).show();
                removeMessages(MSG_UPDATE);
                return;
            }
            progressBar.setProgress(progress);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        progressBar = findViewById(R.id.progress_bar);
        progressBar.setMax(100);
        progressBar.setProgress(0);
        button = findViewById(R.id.start);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == button) {
            handler = new UpdateProgressHandler(this);
            handler.sendEmptyMessage(MSG_UPDATE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }
}
