package com.example.installer;

import android.app.Application;
import android.content.Context;

public class InstallApplication extends Application {

    private InstallQueue installQueue;
    @Override
    public void onCreate() {
        super.onCreate();
        installQueue = new InstallQueue(this);
    }

    public InstallQueue getQueue() {
        return installQueue;
    }
}
