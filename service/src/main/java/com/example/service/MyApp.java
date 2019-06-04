package com.example.service;

import android.app.Application;
import android.content.Intent;

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Intent intent1 = new Intent(this, MyService.class);
        startForegroundService(intent1);
    }
}
