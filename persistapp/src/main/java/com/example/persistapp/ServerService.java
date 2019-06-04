package com.example.persistapp;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.RingtoneManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import android.util.Log;

import java.util.List;

public class ServerService extends Service {

    public final static String TAG = ServerService.class.getSimpleName();
    private IBinder clientBinder;

    private class ServerBinder extends Server.Stub {

        @Override
        public int getPid() throws RemoteException {
            return Process.myPid();
        }

        @Override
        public void setBinder(IBinder binder) throws RemoteException {
            clientBinder = binder;
            clientBinder.linkToDeath(new MyDeathRecipant(), 0);
        }
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "成功连接上客户端");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "与客户端断开连接");
        }
    };

    private class MyDeathRecipant implements IBinder.DeathRecipient {

        @Override
        public void binderDied() {
            if (!clientBinder.isBinderAlive()) {
                Log.d(TAG, "Client has died");
                //启动客户端
                startClient();
                //绑定客户端
                bindClient();
            }
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind");
        return new ServerBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        startForeground(getPackageName().hashCode(), createNotification());
        //服务端一起来就拉活客户端
        startClient();
        bindClient();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind");
        return super.onUnbind(intent);
    }

    private void startClient() {
        Intent intent = new Intent();
        intent.setPackage("com.example.service");
        intent.setAction("com.example.service.client");
        startForegroundService(intent);
    }

    private void bindClient() {
        Intent intent = new Intent();
        intent.setPackage("com.example.service");
        intent.setAction("com.example.service.client");
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }

    private Notification createNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel(getPackageName(), getPackageName(), NotificationManager.IMPORTANCE_HIGH);
        channel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), null);
        channel.enableVibration(true);
        channel.enableLights(true);
        channel.setVibrationPattern(new long[]{200});
        notificationManager.createNotificationChannel(channel);
        Notification notification= new Notification.Builder(this, getPackageName())
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("应用正在运行中")
                .setTicker("来了一条通知")
                .setWhen(System.currentTimeMillis())
                .build();
        return notification;
    }

    /**
     * 获取当前进程pid和名称
     * @return
     */
    private String getProcessName() {
        int miPid = Process.myPid();
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processInfos =  activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (miPid == info.pid) {
                return info.processName;
            }
        }
        return null;
    }
}
