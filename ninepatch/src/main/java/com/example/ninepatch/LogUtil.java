package com.example.ninepatch;

import android.util.Log;

import java.util.concurrent.Callable;

public class LogUtil {

    private static boolean debuggable = true;

    private static String getTag() {
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        StackTraceElement element = elements[4];
        return "[NinePatch:]" + "(" + element.getFileName() + ":" + element.getLineNumber() + ")." + element.getMethodName() + "()";
    }

    public static void d(String msg) {
        if (debuggable) {
            Log.d(getTag(), msg);
        }
    }

    public static void d(Callable<String> callable) {
        if (debuggable) {
            try {
                Log.d(getTag(), callable.call());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
