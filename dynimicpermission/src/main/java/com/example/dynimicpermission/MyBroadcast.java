package com.example.dynimicpermission;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * 普通应用也能接收到开机广播
 */

public class MyBroadcast extends BroadcastReceiver {

    public final static String TAG = MyBroadcast.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        String a = intent.getAction();
        if (Intent.ACTION_BOOT_COMPLETED.equals(a)) {
            Log.d(TAG, "I am here");
        }

        if (Intent.ACTION_LOCKED_BOOT_COMPLETED.equals(a)) {
            Log.d(TAG, "I am here too");
        }
    }
}
