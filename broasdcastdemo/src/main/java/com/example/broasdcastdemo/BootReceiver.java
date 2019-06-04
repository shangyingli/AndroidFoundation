package com.example.broasdcastdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {
    public final static String TAG = BootReceiver.class.getSimpleName();
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (Intent.ACTION_LOCKED_BOOT_COMPLETED.equalsIgnoreCase(action)) { //很快
            Log.d(TAG , " 我收到了开机广播 ： " + "action = " + action);
        }

        if (Intent.ACTION_BOOT_COMPLETED.equalsIgnoreCase(action)) {  //比较慢
            Log.d(TAG , " 我收到了开机广播 ： " + "action = " + action);
        }

    }
}
