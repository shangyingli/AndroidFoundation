package com.example.testinstaller;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.IPackageInstallObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.example.installer.InstallHost;

import java.io.File;

public class MainActivity extends Activity {

    private Button btn;
    private InstallHost installHost;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = findViewById(R.id.install);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (installHost == null) {
                    Intent intent = new Intent();
                    intent.setPackage("com.example.installer");
                    intent.setAction("com.example.installer.installer");
                    bindService(intent, connection, Context.BIND_AUTO_CREATE);
                } else {
                    tryInstall();
                }
            }
        });
    }

    private void tryInstall() {
        File dir = Environment.getExternalStorageDirectory();
        File file = new File(dir, "apks");
        if (!file.exists() && !file.mkdir()) {
            AdLogUtil.d("创建目录失败");
        }
        File sourceFile = new File(file, "haoKan.apk");
        AdLogUtil.d("url : " + sourceFile);
        Uri uri = Uri.fromFile(sourceFile);
        try {
            installHost.install(uri, observer);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private IPackageInstallObserver.Stub observer = new IPackageInstallObserver.Stub() {
        @Override
        public void packageInstalled(String pkgName, int returnCode) throws RemoteException {
            AdLogUtil.d(" pkgName : " + pkgName + " returnCode : " + returnCode);
        }
    };

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            AdLogUtil.d("onServiceConnected");
            installHost = InstallHost.Stub.asInterface(service);
            tryInstall();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            AdLogUtil.d("onServiceDisconnected");
        }
    };
}
