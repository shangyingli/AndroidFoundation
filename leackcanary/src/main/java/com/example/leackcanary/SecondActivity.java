package com.example.leackcanary;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

public class SecondActivity extends Activity {

    private SingleInstanceClass singleInstanceClass;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new SecondActivity();
    }

    public SecondActivity() {
        //单例持有activity的引用
        singleInstanceClass = SingleInstanceClass.getInstance(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
