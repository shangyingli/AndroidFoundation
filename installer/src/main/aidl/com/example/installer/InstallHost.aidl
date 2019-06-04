// InstallHost.aidl
package com.example.installer;
import android.content.pm.IPackageInstallObserver;

// Declare any non-default types here with import statements

interface InstallHost {

    void install(in Uri uri, IPackageInstallObserver observer);

}
