package com.example.leackcanary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private MyHandler myHandler = new MyHandler();

    /**
     * 持有activity的引用
     */
    private  class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }

    /**
     * 持有activity的引用
     */
    private class Task implements Runnable {
        @Override
        public void run() {
            //...
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = findViewById(R.id.install);
        Task task = new Task();
        myHandler.postDelayed(task, 10 * 60 * 1000); //消息队列中持有handler的引用
        final Intent intent = new Intent(MainActivity.this, SecondActivity.class);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
