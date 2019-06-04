package com.example.broasdcastdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class PackageChangeReceiver extends BroadcastReceiver {

    public final static String TAG = PackageChangeReceiver.class.getSimpleName();
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        String data = intent.getData().getSchemeSpecificPart();
        Log.d( "onReceive : " , action + " data : " + data);
    }
}
