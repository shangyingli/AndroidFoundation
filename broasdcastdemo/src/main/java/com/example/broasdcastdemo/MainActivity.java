package com.example.broasdcastdemo;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


public class MainActivity extends Activity {

    public final static String TAG = MainActivity.class.getSimpleName();
    private CustomReceiver customReceiver;
    private PackageChangeReceiver packageChangeReceiver;
    private StickReceiver stickReceiver;
    private LocalReceiver localReceiver;
    private static final String ACTION_STICK_CUSTOM = "action_stick_custom";
    private static final String ACTION_NORMAL_CUSTOM = "action_normal_custom";
    private static final String ACTION_ORDER_CUSTOM = "action_order_custom";

    private static final String ACTION_LOCAL_BROADCAST = "action_local_broadcast";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registerCustomReceiver();

        registerFirstOrderBroadcast();
        registerSecondOrderBroadcast();

        //1.发送黏性广播
        Intent intent = new Intent();
        intent.setAction(ACTION_STICK_CUSTOM);
        sendStickyBroadcast(intent);
        //2发送普通广播
        Intent normalIntent = new Intent();
        normalIntent.setAction(ACTION_NORMAL_CUSTOM);
        normalIntent.setDataAndType(Uri.EMPTY, "custom");
        normalIntent.setData(Uri.parse("custom://"));
        sendBroadcast(normalIntent);
        //3发送有序广播
        Intent orderIntent = new Intent(ACTION_ORDER_CUSTOM);
//        orderIntent.setData(Uri.parse("package://com.aa.aa"));
        orderIntent.setPackage(getPackageName());
        sendOrderedBroadcast(orderIntent, null);

//        registerCustomReceiver();
//        registerSystemReceiver();

        //注册本地广播
        registerLocalBroadcast();
        //4发送本地广播
        Intent localIntent = new Intent();
        intent.setAction(ACTION_LOCAL_BROADCAST);
        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
    }

    private void registerLocalBroadcast() {
        localReceiver = new LocalReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_LOCAL_BROADCAST);
        LocalBroadcastManager.getInstance(this).registerReceiver(localReceiver, intentFilter);
    }

    private void registerFirstOrderBroadcast() {
        FirstOrderReceive firstOrderReceive = new FirstOrderReceive();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_ORDER_CUSTOM);
        registerReceiver(firstOrderReceive, intentFilter);
    }
    private void registerSecondOrderBroadcast() {
        SecondOrderReceive firstOrderReceive = new SecondOrderReceive();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.setPriority(1000);
        intentFilter.addAction(ACTION_ORDER_CUSTOM);
        registerReceiver(firstOrderReceive, intentFilter);
    }


    class FirstOrderReceive extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String pkgName = intent.getPackage();
            String res = getResultData();
            Log.d(TAG, "FirstOrderReceive : " + pkgName + "res : " + res);
        }
    }

    class SecondOrderReceive extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String pkgName = intent.getPackage();
            Log.d(TAG, "SecondOrderReceive : " + pkgName);
            setResultData("modify data");
            //阻断广播发送
//            abortBroadcast();
        }
    }


    /**
     * 注册黏性广播
     * @param view
     */
    public void regsterSticky(View view) {
        stickReceiver = new StickReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_STICK_CUSTOM);
        registerReceiver(stickReceiver, intentFilter);
    }

    /**
     * 注册普通广播
     * @param view
     */
    public void registerNormal(View view) {
        registerCustomReceiver();
    }


    private class StickReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
                Log.d(TAG, " action : " + action);
        }
    }

    private class LocalReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

        }
    }


    public void start(View view) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("http://"));
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (packageChangeReceiver != null) {
            Log.d(TAG, "unregister packageChangeReceiver");
            unregisterReceiver(packageChangeReceiver);
        }
        if (customReceiver != null) {
            Log.d(TAG, "unregister customReceiver");
            unregisterReceiver(customReceiver);
        }

        if (stickReceiver != null) {
            unregisterReceiver(stickReceiver);
        }

        LocalBroadcastManager.getInstance(this).unregisterReceiver(localReceiver);
    }

    //注册几次广播就会接收到几次广播， 并且注册了几次必须销毁几次
    private void registerCustomReceiver() {
        Log.d(TAG, "register Custom broadcast");
        customReceiver = new CustomReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_NORMAL_CUSTOM);
        intentFilter.addDataScheme("custom");
        registerReceiver(customReceiver, intentFilter);
    }

    private void registerSystemReceiver() {
        Log.d(TAG, "registerSystemReceiver");
        packageChangeReceiver = new PackageChangeReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED); //新安装应用成功后，只会发送该广播
        intentFilter.addAction(Intent.ACTION_PACKAGE_REPLACED);//覆盖安装应用成功后，会发送ACTION_PACKAGE_REMOVED->PACKAGE_ADDED->PACKAGE_REPLACED
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);//删除成功后，只会发送该广播
        intentFilter.addAction(Intent.ACTION_PACKAGE_CHANGED); //应用状态发生变化，如enable/disable会发生该广播
        intentFilter.addDataScheme("package");
        registerReceiver(packageChangeReceiver, intentFilter);
    }
}
