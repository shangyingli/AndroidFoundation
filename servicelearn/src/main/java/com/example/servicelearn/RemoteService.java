package com.example.servicelearn;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.example.server.IRemote;

public class RemoteService extends Service {

    public final static String TAG = RemoteService.class.getSimpleName();

    public RemoteService() {

    }

    private IRemote.Stub remoteServer = new IRemote.Stub() {
        @Override
        public void shakeHand() throws RemoteException {
            Log.d(TAG, "shakeHand");
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind");
        return remoteServer;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
    }
}
