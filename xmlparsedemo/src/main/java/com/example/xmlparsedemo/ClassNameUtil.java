package com.example.xmlparsedemo;

import android.app.Activity;
import android.util.Log;

public class ClassNameUtil {

    public final static String TAG = ClassNameUtil.class.getSimpleName();

    public static void getClassName(Activity activity) {
        String cls1 = activity.getLocalClassName();
        String cls2 = activity.getComponentName().getClassName();
        String cls3 = activity.getClass().getSimpleName();
        String cls4 = activity.getClass().getSimpleName();
        String cls5 = MainActivity.class.getSimpleName();
        Log.d(TAG, "cls1 = " + cls1 + "cls2 = " + cls2 + "cls3 = " + cls3 + "cls4 = " + cls4);
    }

}
