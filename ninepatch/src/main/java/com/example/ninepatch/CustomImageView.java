package com.example.ninepatch;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class CustomImageView extends AppCompatImageView {

    public final static String TAG = CustomImageView.class.getSimpleName();
    private boolean isSelected;

    @Override
    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public void setSelected(boolean selected) {
        isSelected = selected;
    }


    public CustomImageView(Context context) {
        this(context, null);
    }

    public CustomImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        LogUtil.d("dispatchTouchEvent");
        return super.dispatchTouchEvent(event);
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        LogUtil.d("onTouchEvent");
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                LogUtil.d("ACTION_DOWN");
//                break;
//            case MotionEvent.ACTION_MOVE:
//                LogUtil.d("ACTION_MOVE");
//                break;
//            case MotionEvent.ACTION_UP:
//                LogUtil.d("ACTION_UP");
//                break;
//        }
//        return true; //返回false, 后续事件由上层ViewGroup处理,即调用viewgroup的onTouch->onTouchEvent等
//    }

//    @Override
//    public boolean performClick() {
//        super.performClick();
//        LogUtil.d("performClick");
//        return true;
//    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Looper.prepare();
        Handler handler = new Handler(Looper.getMainLooper());
        handler.sendMessageDelayed()
        Looper.loop();

    }
}
