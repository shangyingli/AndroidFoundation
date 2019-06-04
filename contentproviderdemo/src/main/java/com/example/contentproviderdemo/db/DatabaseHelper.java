package com.example.contentproviderdemo.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    public static String TABLE_NAME = "freeze_tb";

    public DatabaseHelper(Context context, String databaseName) {
        this(context, databaseName, null, DB_VERSION);
    }

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version); //创建数据库
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建表

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
