package com.example.service;

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
import android.os.RemoteException;
import android.util.Log;

import com.example.persistapp.Server;

//客户端服务挂掉后， 通知服务端（服务端通常为常驻进程）
public class MyService extends Service {

    public final static String TAG = MyService.class.getSimpleName();
    private Server serverBinder;
    private Binder clientBinder = new Binder();

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected");
            serverBinder = Server.Stub.asInterface(service);
            try {
                int pid = serverBinder.getPid();
                Log.d(TAG, " remote pid is : " + pid);
                serverBinder.setBinder(clientBinder);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected");
            unbindService(serviceConnection);
        }
    };

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        startForeground(getPackageName().hashCode(), createNotification());
        Intent remoteInten = new Intent();
        remoteInten.setPackage("com.example.persistapp");
        remoteInten.setAction("com.app.server");
        startService(remoteInten);
        bindService(remoteInten, serviceConnection,  Context.BIND_AUTO_CREATE);
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, " 服务端将我拉活了");
        return clientBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind");
        return super.onUnbind(intent);
    }

    private Notification createNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel(getPackageName(), getPackageName(), NotificationManager.IMPORTANCE_HIGH);
        channel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), null);
        channel.enableVibration(true);
        channel.enableLights(true);
        channel.setVibrationPattern(new long[]{200});
        notificationManager.createNotificationChannel(channel);
        Notification notification = new Notification.Builder(this, getPackageName())
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("客户端应用正在运行中")
                .setTicker("来了一条通知")
                .setWhen(System.currentTimeMillis())
                .build();
        return notification;
    }

}
