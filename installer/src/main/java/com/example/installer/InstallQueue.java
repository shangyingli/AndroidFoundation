package com.example.installer;

import android.content.Context;
import android.content.pm.IPackageInstallObserver;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;

import java.util.concurrent.ArrayBlockingQueue;

public class InstallQueue extends IPackageInstallObserver.Stub{

    private static final String TAG = InstallQueue.class.getSimpleName();

    private Context appContext;
    private ArrayBlockingQueue<InstallTask> tasks = new ArrayBlockingQueue<>(3);
    private Handler handler = new Handler(Looper.getMainLooper());

    public InstallQueue(Context context) {
        this.appContext = context;
    }

    public void putTask(Uri uri, IPackageInstallObserver observer) {
        boolean installNow = tasks.isEmpty();
        tasks.offer(new InstallTask(uri, observer));
        if (!installNow) {
            tryInstall();
        }
    }

    private void tryInstall() {
        InstallTask installTask = tasks.peek();
        if (installTask != null) {
            PackageManager pm = appContext.getPackageManager();
            pm.installPackage(installTask.getUri(), this, PackageManager.INSTALL_REPLACE_EXISTING, "");
        }
    }

    private void postInstallDelay() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tryInstall();
            }
        }, 1000);
    }

    @Override
    public void packageInstalled(String pkgName, int returnCode) throws RemoteException {
        Log.d(TAG, "THREAD : " + Thread.currentThread().getName() + "pkgName : " + pkgName + " return code : " + returnCode);
        InstallTask installTask = tasks.poll(); //安装后移除出队列
        postInstallDelay();
        if (installTask != null) {
            //将结果回调给客户端
            if (!installTask.getObserver().asBinder().isBinderAlive()) {
                Log.d(TAG, "binder is died!");
            } else {
                installTask.getObserver().packageInstalled(pkgName, returnCode);
            }
        }
    }
}
