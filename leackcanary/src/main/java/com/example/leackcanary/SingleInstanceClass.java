package com.example.leackcanary;

import android.content.Context;

public class SingleInstanceClass {
    private static SingleInstanceClass instance;
    private Context mContext;

    private SingleInstanceClass(Context context) {
        this.mContext = context; // 传递的是Activity的context
    }

    public static SingleInstanceClass getInstance(Context context) {
        if (instance == null) {
            instance = new SingleInstanceClass(context);
        }
        return instance;
    }
}
