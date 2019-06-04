package com.example.dynimicpermission;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.net.URI;
import java.util.List;

public class MainActivity extends Activity {

    public final static String TAG = MainActivity.class.getSimpleName();
    private static final int PERMISSION_REQUEST_CODE = 1000;
    private static final int SETTING_PERMISSION_REQUEST_CODE = 1;
    private PermissionManager permissionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String[] permissions = new String[]{
                Manifest.permission.CALL_PHONE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        permissionManager = new PermissionManager()
                .setCallback(callback)
                .setContext(this)
                .setPermits(permissions)
                .setRequestCode(PERMISSION_REQUEST_CODE)
                .setSettingRequestCode(SETTING_PERMISSION_REQUEST_CODE)
                .checkPermission();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    PermissionManager.Callback callback = new PermissionManager.Callback() {
        @Override
        public void passPermission() {
//            phoneCall();
            installApp();

        }

        @Override
        public void declinePermission() {
            Toast.makeText(MainActivity.this, "无权限，退出应用", Toast.LENGTH_LONG).show();
            finish();
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        permissionManager.onActivityResult(requestCode, resultCode, data);
    }

    private void phoneCall() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:10086"));
        startActivity(intent);
    }


    /**
     * 8.0手动安装
     */
    private void installApp() {
        File dir = new File(Environment.getExternalStorageDirectory(), "apks");
        if (!dir.exists() && !dir.mkdir()) {
            Log.d(TAG, "目录不存在且创建失败");
        } else {
            Log.d(TAG, "目录存在或创建目录成功");
        }
        File source = new File(dir, "haoKan.apk");
        if (!source.exists()) {
            Log.d(TAG, "文件不存在");
            return;
        }
        Log.d(TAG, "source path : " + source.getAbsolutePath());
        String mimeType = "application/vnd.android.package-archive";
        Intent installIntent = new Intent();
        installIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            Log.d(TAG, "uri : " + Uri.fromFile(source));
            installIntent.setDataAndType(Uri.fromFile(source), mimeType);
        } else {
            PackageManager pm = getPackageManager();
            boolean hasInstallPermission = false;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                hasInstallPermission = pm.canRequestPackageInstalls();
            }
            Log.d(TAG, " hasInstallPermission " + hasInstallPermission);
            Uri uri = FileProvider.getUriForFile(MainActivity.this, "com.example.dynimicpermission.fileProvider", source);
            Log.d(TAG, "URI : " + uri);
            installIntent.setDataAndType(uri, mimeType);
            List<ResolveInfo> resolveInfos = pm.queryIntentActivities(installIntent, PackageManager.MATCH_DEFAULT_ONLY);
            if (resolveInfos == null || resolveInfos.size() == 0) return;
            final String installerPackageName = "com.google.android.packageinstaller";
            for (ResolveInfo resolveInfo : resolveInfos) {
                String pkgName = resolveInfo.activityInfo.packageName;
                grantUriPermission(pkgName, uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                if (installerPackageName.equalsIgnoreCase(pkgName)) {
                    installIntent.setPackage(pkgName);
                }
            }
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, installIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        sendNotification(pendingIntent);
    }

    private void sendNotification(PendingIntent pendingIntent) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel(getPackageName(), getPackageName(), NotificationManager.IMPORTANCE_HIGH);
        channel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), null);
        channel.enableVibration(true);
        channel.enableLights(true);
        channel.setVibrationPattern(new long[]{200});
        notificationManager.createNotificationChannel(channel);
        Notification notification = new Notification.Builder(this, getPackageName())
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("点击安装应用")
                .setTicker("来了一条通知")
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pendingIntent)
                .build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.flags|= Notification.FLAG_ONGOING_EVENT;
        notificationManager.notify(getPackageName().hashCode(), notification);
    }
}
