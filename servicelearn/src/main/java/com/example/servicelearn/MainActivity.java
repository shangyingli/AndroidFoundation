package com.example.servicelearn;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.server.IRemote;

public class MainActivity extends Activity {

    public final static String TAG = MainActivity.class.getSimpleName();

    private NormalService.DownloadBinder downloadBinder;
    private boolean isConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    ServiceConnection remoteServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "remote service onServiceConnected");
            IRemote binder = IRemote.Stub.asInterface(service);
            try {
                binder.shakeHand();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected");
            downloadBinder = (NormalService.DownloadBinder) service;
            downloadBinder.startDown();
            int currentProgress = downloadBinder.getProgress();
            Log.d(TAG, "当前进度为: " + currentProgress);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected");
        }
    };

    public void startService(View view) {
        Intent intent = new Intent(MainActivity.this, NormalService.class);
        startService(intent);
    }

    public void stopService(View view) {
        Intent intent = new Intent(MainActivity.this, NormalService.class);
        stopService(intent);
    }

    public void bindService(View view) {
        Intent intent = new Intent(MainActivity.this, NormalService.class);
        isConnected = bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解绑服务
        if (isConnected) {
            unbindService(serviceConnection);
            isConnected = false;
        }
        Log.d(TAG, "onDestroy");
    }

    public void unbind_service(View view) {
        if (isConnected) {
            unbindService(serviceConnection);
            isConnected = false;
        }
    }

    public void startRemote(View view) {
        Intent intent = new Intent(MainActivity.this, RemoteService.class);
        startService(intent);
    }

    public void bind_remote_service(View view) {
        Intent intent = new Intent(MainActivity.this, RemoteService.class);
        bindService(intent, remoteServiceConnection, Context.BIND_AUTO_CREATE);
    }
}
