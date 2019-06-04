package com.example.imagecompress;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {

    public final static String TAG = MainActivity.class.getSimpleName();
    private ImageView origin;
    private ImageView compressed;
    private File imageFile;
    private File cacheDir;
    private static final int REQUEST_CODE = 0x1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        origin = findViewById(R.id.origin_image);
        compressed = findViewById(R.id.compressed_image);
        String permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{permission}, REQUEST_CODE);
            return;
        }
        initView();
    }

    private void initView() {
        cacheDir = new File(Environment.getExternalStorageDirectory(), "images");
        imageFile = new File(cacheDir, "banner_3.jpg");
        Log.d(TAG, "imageFile : " + imageFile);
        Bitmap originBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath(), null);
        origin.setImageBitmap(originBitmap);
    }

    /**
     * 质量压缩
     *
     * @param view
     */
    public void compressQuailty(View view) {
        File compressedFile = new File(cacheDir, "compressed.jpg");
        bitmapQualityCompress(imageFile, compressedFile);
    }

    /**
     * 尺寸压缩
     *
     * @param view
     */
    public void compressScale(View view) {
        Bitmap bitmap = null;
        try {
            bitmap = bitmapScaleCompress(imageFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        compressed.setImageBitmap(bitmap);
    }

    private Bitmap bitmapScaleCompress(File imageFile) throws Exception{
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(new FileInputStream(imageFile.getAbsolutePath()), null, options);
        options.inSampleSize = calculateSamplesize(options, 500, 150);
        Log.d(TAG, "SAMPLE SIZE : " +options.inSampleSize);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeStream(new FileInputStream(imageFile.getAbsolutePath()), null, options);
    }

    private int calculateSamplesize(BitmapFactory.Options options, int requestWidth, int requestHeight) {
        int originWidth = options.outWidth;
        int originHeight = options.outHeight;
        Log.d(TAG, "originWidth : " + originWidth + "originHeight  : " + originHeight );
        int sampleRatio = 1;
        if (originWidth > requestWidth || originHeight > requestHeight) {
            int halfWidth = originWidth / 2;
            int halfHeight = originHeight / 2;
            while (halfWidth / sampleRatio >= requestWidth || halfHeight / sampleRatio >= requestHeight) {
                sampleRatio *= 2;
            }
        }
        return sampleRatio;
    }

    /**
     * 质量压缩，改变图片文件在磁盘中大小
     *
     * @param imageFile
     */
    private void bitmapQualityCompress(File imageFile, File compressFile) {
        try {
            FileInputStream in = new FileInputStream(imageFile);
            FileOutputStream outputStream = new FileOutputStream(compressFile);
            Bitmap originBitmap = BitmapFactory.decodeStream(in);
            originBitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream); //质量压缩
            Bitmap bitmap = BitmapFactory.decodeFile(compressFile.getAbsolutePath());
            compressed.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initView();
            }
        }
    }


}
