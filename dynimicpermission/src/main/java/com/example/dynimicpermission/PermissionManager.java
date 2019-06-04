package com.example.dynimicpermission;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * 动态权限申请
 */

public class PermissionManager {

    public final static String TAG = PermissionManager.class.getSimpleName();
    private Callback callback;
    private int requestCode;
    private Activity activity;
    private String[] permits;
    private int settingRequestCode;

    interface Callback {

        void passPermission();

        void declinePermission();
    }

    public PermissionManager() {

    }

    public PermissionManager setPermits(String[] permits) {
        this.permits = permits;
        return this;
    }

    public PermissionManager setRequestCode(int requestCode) {
        this.requestCode = requestCode;
        return this;
    }

    public PermissionManager setCallback(Callback callback) {
        this.callback = callback;
        return this;
    }

    public PermissionManager setSettingRequestCode(int requestCode) {
        this.settingRequestCode = requestCode;
        return this;
    }

    public PermissionManager setContext(Activity activity) {
        this.activity = activity;
        return this;
    }

    public PermissionManager checkPermission() {
        //将未授权权限加入列表
        List<String> permitList = new ArrayList<>();
        for (String permit : permits) {
            if (activity.checkSelfPermission(permit) != PackageManager.PERMISSION_GRANTED) {
                permitList.add(permit);
            }
        }
        if (permitList.size() > 0) { //有未授权的权限
            ActivityCompat.requestPermissions(activity, permitList.toArray(new String[permitList.size()]), requestCode);
        } else { //没有未授权的权限
            Log.d(TAG, "已经全部授权");
            callback.passPermission();
        }
        return this;
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //有权限被拒绝
        boolean hasDenyPermission = false;
        //是否勾选不再提醒
        boolean isTips = false;
        if (this.requestCode == requestCode) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    hasDenyPermission = true;
                    if (activity.shouldShowRequestPermissionRationale(permissions[i])) { //没有勾选不再提醒返回true
                        isTips = false; //只要有一个权限没有勾选了不再提醒， 则不弹设置窗口
                        break;
                    } else {
                        isTips = true; //所有权限都勾选了不再提醒
                    }
                }
            }
            if (hasDenyPermission) { //有权限拒绝
                if (isTips) { //所有被拒绝的权限都勾选了不再提醒
                    shouldShowPermissionSettingDialog();
                } else { //有权限没有勾选不再提醒时，不进设置
                    callback.declinePermission();
                }
            } else { //所有权限都通过
                callback.passPermission();
            }
        }
    }


    private void shouldShowPermissionSettingDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(activity)
                .setTitle("说明")
                .setMessage("请前往设置授予相应权限")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        goSetting();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callback.declinePermission();
                    }
                })
                .create();
        alertDialog.show();
    }

    private void goSetting() {
        String pkgName = activity.getPackageName();
        Uri packageUri = Uri.parse("package:" + pkgName);
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageUri);
        activity.startActivityForResult(intent, settingRequestCode);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (settingRequestCode == requestCode) {
            reCheckPermission();
        }
    }

    private void reCheckPermission() {
        List<String> permitList = new ArrayList<>();
        for (String permit : permits) {
            if (activity.checkSelfPermission(permit) != PackageManager.PERMISSION_GRANTED) {
                permitList.add(permit);
            }
        }
        if (permitList.size() > 0) { //有未授权的权限
            callback.declinePermission();
        } else { //没有未授权的权限
            Log.d(TAG, "已经全部授权");
            callback.passPermission();
        }
    }

}
