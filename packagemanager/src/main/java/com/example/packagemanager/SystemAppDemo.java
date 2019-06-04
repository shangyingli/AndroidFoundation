package com.example.packagemanager;

import android.app.job.JobInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class SystemAppDemo {

    public static void main(String[] args) {
        new SystemAppDemo();
    }

    public SystemAppDemo() {

    }

    /**
     * 获取所有launchera图标的非系统应用
     * @param context
     */
    public List<String> getNoneSystemApp(Context context) {
        ArrayList<String> list = new ArrayList();
        PackageManager pm = context.getPackageManager();
        Intent mainIntent = new Intent();
        mainIntent.setAction(Intent.ACTION_MAIN);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(mainIntent, 0);
        int size = resolveInfos == null ? 0 : resolveInfos.size();
        for (int i = 0; i < size; i++) {
            ResolveInfo resolveInfo = resolveInfos.get(i);
            ApplicationInfo appInfo = resolveInfo.activityInfo.applicationInfo;
            int flag = appInfo.flags;
            if ((flag & ApplicationInfo.FLAG_SYSTEM) == 0) { //非系统应用
                String label = appInfo.loadLabel(pm).toString();
                list.add(label);
                System.out.println("none system app : " + label);
            }
        }

        return list;
    }

}
