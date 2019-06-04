package com.example.contentproviderdemo.model;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.Handler;
import android.os.Looper;

import com.example.contentproviderdemo.bean.FreezeBean;

import java.util.ArrayList;
import java.util.List;

public class FreezeModelImp implements FreezeModel {

    private PackageManager packageManager;
    private String pkgName;
    private List<FreezeBean> freezeBeans;
    private List<FreezeBean> unFreezeBeans;
    private Handler handler = new Handler(Looper.getMainLooper());

    public FreezeModelImp(Context context) {
        packageManager = context.getPackageManager();
        pkgName = context.getPackageName();
        freezeBeans = new ArrayList<>();
        unFreezeBeans = new ArrayList<>();
    }

    @Override
    public void getFreezeInfo(final Callback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Intent launcherIntent = new Intent();
                launcherIntent.setAction(Intent.ACTION_MAIN);
                launcherIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                List<ResolveInfo> resolveInfos = packageManager.queryIntentActivities(launcherIntent, 0);
                final List<FreezeBean> freezeBeans = new ArrayList<>();
                for (ResolveInfo resolveInfo : resolveInfos) {
                    ApplicationInfo appInfo = resolveInfo.activityInfo.applicationInfo;
                    int flags = appInfo.flags;
                    if ((flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                        String packageName = appInfo.packageName;
                        if (!pkgName.equalsIgnoreCase(packageName) && !appInfo.enabled) {
                            String appName = appInfo.loadLabel(packageManager).toString();
                            Drawable appIcon = appInfo.loadIcon(packageManager);
                            FreezeBean freezeBean = new FreezeBean();
                            freezeBean.setPkgName(packageName);
                            freezeBean.setAppName(appName);
                            freezeBean.setAppIcon(appIcon);
                            freezeBeans.add(freezeBean);
                        }
                    }
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onSuccess(freezeBeans);
                    }
                });
            }
        }).start();

    }

    @Override
    public void getUnfreezeInfo(final Callback callback) {
        //在子线程调用
        // TODO: 2019/4/28 改用线程池处理任务
        new Thread(new Runnable() { //线程的控制和切换不方便，需要使用handler进行线程切换；且多线程下没有线程复用
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Intent launcherIntent = new Intent();
                launcherIntent.setAction(Intent.ACTION_MAIN);
                launcherIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                List<ResolveInfo> resolveInfos = packageManager.queryIntentActivities(launcherIntent, 0);
                final List<FreezeBean> freezeBeans = new ArrayList<>();
                for (ResolveInfo resolveInfo : resolveInfos) {
                    ApplicationInfo appInfo = resolveInfo.activityInfo.applicationInfo;
                    int flags = appInfo.flags;
                    if ((flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                        String packageName = appInfo.packageName;
                        if (!pkgName.equalsIgnoreCase(packageName) && appInfo.enabled) {
                            String appName = appInfo.loadLabel(packageManager).toString();
                            Drawable appIcon = appInfo.loadIcon(packageManager);
                            FreezeBean freezeBean = new FreezeBean();
                            freezeBean.setPkgName(packageName);
                            freezeBean.setAppName(appName);
                            freezeBean.setAppIcon(appIcon);
                            freezeBeans.add(freezeBean);
                        }
                    }
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onSuccess(freezeBeans);
                    }
                });
            }
        }).start();
    }

    @Override
    public boolean doFreeze(String freezeItem) {
        try {
            packageManager.setApplicationEnabledSetting(freezeItem, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, 0);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean doUnfreeze(String unFreezeItem) {
        return false;
    }

}
