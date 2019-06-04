package com.example.contentproviderdemo.provider;

import android.content.BroadcastReceiver;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.example.contentproviderdemo.db.DatabaseHelper;

public class FreezeProvider extends ContentProvider {

    public final static String TAG = FreezeProvider.class.getSimpleName();
    private SQLiteDatabase database;
    private static String AUTHORITY = "com.itel.myprovider";
    private static final String FREEZE_TABLE = "tb_freeze";
    private static final int FREEZE_CODE = 1;
    private static final String OTHER_TABLE = "tb_other";
    private static final int OTHER_CODE = 2;

    private static final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        matcher.addURI(AUTHORITY, FREEZE_TABLE, FREEZE_CODE);
        matcher.addURI(AUTHORITY, OTHER_TABLE, OTHER_CODE);
    }

    @Override
    public boolean onCreate() {
        //获取数据库连接
        DatabaseHelper databaseHelper = new DatabaseHelper(getContext(), "freeze.db");
        database = databaseHelper.getReadableDatabase();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        int code = matcher.match(uri);
        Cursor cursor = null;
        switch (code) {
            case FREEZE_CODE :
                cursor = database.query(getTableName(uri), projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case OTHER_CODE:
                long id = ContentUris.parseId(uri);
                break;
        }
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }


    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int code = matcher.match(uri);
        switch (code) {
            case FREEZE_CODE:
                String tableName = getTableName(uri);
                database.insert(tableName, null, values);
                break;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return uri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

     
    @Override
    public Bundle call(String method, String arg, Bundle extras) {
        if ("outerFreeze".equalsIgnoreCase(method)) {
            Log.d(TAG, "传入的参数为 : " + arg);
            String pkgName = arg;
            outerFreeze(pkgName);
        }
        Bundle bundle = new Bundle();
        bundle.putString("responseKey", "ni hao");
        return bundle;
    }

    public void outerFreeze(String pkgName) {
        //冻结应用
    }

    public String getTableName(Uri uri) {
        switch (matcher.match(uri)) {
            case FREEZE_CODE :
                return DatabaseHelper.TABLE_NAME;
        }
        return null;
    }
}
