package com.example.hotfix;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.HashSet;

import dalvik.system.BaseDexClassLoader;
import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;

public class FixDexUtils {

    public final static String TAG = FixDexUtils.class.getSimpleName();
    private static HashSet<File> loadedDex = new HashSet<File>();

    public static void loadFixedDex(Context context) {
        if (context == null) {
            return;
        }
        File fileDir = context.getDir("odex", Context.MODE_PRIVATE);
        Log.d(TAG, " : " + fileDir);
        File[] files = fileDir.listFiles();
        for (File file : files) {
            if (file.getName().startsWith("class") && file.getName().endsWith(".dex")) {
                loadedDex.add(file);
            }
        }
        doDexInjected(context, fileDir, loadedDex);
    }

    private static void doDexInjected(Context context, File fileDir, HashSet<File> loadedDex) {
        String optimizeDir = fileDir.getAbsolutePath() + File.separator + "opt_dex";
        File optimizeFileDir = new File(optimizeDir);
        if (!optimizeFileDir.exists()) {
            optimizeFileDir.mkdir();
        }
        try {
            PathClassLoader pathClassLoader = (PathClassLoader) context.getClassLoader();
            DexClassLoader dexClassLoader = null;
            for (File file : loadedDex) {
                dexClassLoader = new DexClassLoader(
                        file.getAbsolutePath(),
                        optimizeFileDir.getAbsolutePath(),
                        null,
                        pathClassLoader
                );
            }
            Object dexObject = getPathList(dexClassLoader);
            Object pathObject = getPathList(pathClassLoader);
            Object dexElements = getDexElements(dexObject);
            Object pathElements = getDexElements(pathObject);
            //合并pathElements
            Object newPathElements = combineElements(dexElements, pathElements);
            //重写给PathList里面的lement[] dexElements赋值
            Object pathList = getPathList(pathClassLoader);
            setField(pathList, pathList.getClass(), "dexElements", newPathElements);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private static Object getPathList(Object baseDexClassloader) throws Exception{
        return getField(baseDexClassloader, Class.forName("dalvik.system.BaseDexClassLoader"), "pathList");
    }

    private static void setField(Object pathList, Class<?> clazz, String field, Object pathElements) throws NoSuchFieldException, IllegalAccessException {
        Field localField = clazz.getDeclaredField(field);
        localField.setAccessible(true);
        localField.set(pathList, pathElements);
    }

    private static Object getField(Object object, Class<?> clazz, String field) throws NoSuchFieldException, IllegalAccessException {
        Field localField = clazz.getDeclaredField(field);
        localField.setAccessible(true);
        return localField.get(object);
    }

    private static Object getDexElements(Object pathList) throws NoSuchFieldException, IllegalAccessException {
        return getField(pathList, pathList.getClass(), "dexElements");
    }

    private static Object combineElements(Object dexElements, Object pathElements) {
        Class<?> localClass = dexElements.getClass().getComponentType(); //Element[]
        int i = Array.getLength(dexElements);
        int j = i + Array.getLength(pathElements);
        Object newDexElements = Array.newInstance(localClass, j);
        for (int k = 0; k < j; k++) {
            if (k < i) {
                Array.set(newDexElements, k, Array.get(dexElements, k));
            } else {
                Array.set(newDexElements, k, Array.get(pathElements, k - i));
            }
        }
        return newDexElements;
    }
}
