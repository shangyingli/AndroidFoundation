package com.example.installer;

import android.content.pm.IPackageInstallObserver;
import android.net.Uri;

public class InstallTask {

    private Uri uri;
    private IPackageInstallObserver observer;

    public InstallTask(Uri uri, IPackageInstallObserver observer) {
        this.uri = uri;
        this.observer = observer;
    }

    public Uri getUri() {
        return uri;
    }

    public IPackageInstallObserver getObserver() {
        return observer;
    }

}
