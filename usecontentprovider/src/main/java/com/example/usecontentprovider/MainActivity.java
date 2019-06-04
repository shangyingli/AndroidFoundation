package com.example.usecontentprovider;

import android.content.ContentResolver;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    public final static String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void freeze(View view) {
        Uri uri = Uri.parse("content://com.itel.myprovider");
        ContentResolver resolver = getContentResolver();
        Bundle bundle = resolver.call(uri, "freeze", "hello", null);
        assert bundle != null;
        Log.d(TAG, bundle.getString("responseKey"));
    }

    public void add(View view) {

    }
}
