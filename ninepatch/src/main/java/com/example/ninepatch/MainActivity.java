package com.example.ninepatch;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import java.util.WeakHashMap;
import java.util.concurrent.Callable;

public class MainActivity extends Activity implements NineGridView.OnClickListener {

    public final static String TAG = MainActivity.class.getSimpleName();
    private NineGridView nineGridView;
    private ImageView bigView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nineGridView = findViewById(R.id.nine_patch);
        nineGridView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                LogUtil.d( "nineGridView onTouch");
                return false;
            }
        });

        nineGridView.setClickListener(this);
        bigView = findViewById(R.id.big_image);
        bigView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bigView.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onClick(View view) {
        LogUtil.d( "onClick");
        bigView.setVisibility(View.VISIBLE);
        Drawable drawable = ((ImageView)view).getDrawable();
        bigView.setImageDrawable(drawable);
        //获取这个windowd的宽高
        WindowManager wm = getWindowManager();
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);

        int wd = metrics.widthPixels;
        int wh = metrics.heightPixels;
        int imgW = view.getWidth();
        int imgH = view.getHeight();
        LogUtil.d("wd : " + wd + "wh : " + wh + "imgW : " + imgW
         + "imgH : " + imgH);
//        float xScale = wd / (float)imgW;
//        float yScale = wh / (float)imgH;
//        float startScale = xScale > yScale ? xScale : yScale;
//        ScaleAnimation scaleAnimation = new ScaleAnimation(1f, xScale, 1f, yScale);
//        scaleAnimation.setDuration(1000);
//        scaleAnimation.setFillAfter(true);
//        bigView.startAnimation(scaleAnimation);

    }

    @Override
    public void onLongClick(View view) {
        LogUtil.d("onLongClick");
        //弹出对话框
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        LogUtil.d( "dispatchTouchEvent");
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        LogUtil.d( "onTouchEvent");
        return super.onTouchEvent(event);
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        LogUtil.d(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "onUserInteraction";
            }
        });
    }
}
