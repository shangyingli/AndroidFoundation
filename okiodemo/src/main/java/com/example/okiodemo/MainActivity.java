package com.example.okiodemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;

import okio.Okio;

public class MainActivity extends AppCompatActivity {

    public final static String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    private static void writeFile(File file) {
        try {
            Okio.buffer(Okio.sink(file))
                    .writeUtf8("how are your\n")
                    .writeInt(1234)
                    .close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void readFile(File file) {
        try {
            String in = Okio.buffer(Okio.source(file))
                    .readByteString()
                    .string(Charset.forName("utf-8"));
            Log.d(TAG, "in is : " + in);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeFile(View view) {
        File outFile = new File(getFilesDir(), "data");
        writeFile(outFile);
    }

    public void readFile(View view) {
        File outFile = new File(getFilesDir(), "data");
        readFile(outFile);
    }
}
