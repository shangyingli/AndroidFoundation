package com.example.servicelearn;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class NormalService extends Service {

    public final static String TAG = NormalService.class.getSimpleName();

    public NormalService() {
        Log.d(TAG, "NormalService");
    }

    class DownloadBinder extends Binder {
        public void startDown() {
            Log.d(TAG, "开始下载");
        }

        public int getProgress() {
            Log.d(TAG, "当前进度");
            return 100;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, " onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        Thread thread = new Thread(new Task());
        thread.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind");
        return new DownloadBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) { //使用bindService时， 若客户端调用了unbindService或客户端ondestroy掉，服务会挂掉
        Log.d(TAG, "onUnbind" );
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        Log.d(TAG, "onRebind");
    }

    class Task implements Runnable {
        @Override
        public void run() {
            Log.d(TAG, "执行任务中...");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "任务执行结束");
//            stopSelf();
        }
    }

}
