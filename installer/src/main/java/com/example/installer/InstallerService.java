package com.example.installer;

import android.app.Service;
import android.content.Intent;
import android.content.pm.IPackageInstallObserver;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;

import org.jetbrains.annotations.Nullable;

public class InstallerService extends Service {

    private InstallHost.Stub installer = new InstallHost.Stub() {

        @Override
        public void install(Uri uri, IPackageInstallObserver observer) throws RemoteException {
            if (getApplication() instanceof InstallApplication) {
                //考虑在子线程中安装
                ((InstallApplication) getApplication()).getQueue().putTask(uri, observer);
            }
        }
    };


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return installer;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
