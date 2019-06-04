package com.example.surfaceviewdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class SurfaceViewWritePanel extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    public final static String TAG = SurfaceViewWritePanel.class.getSimpleName();
    private Paint mPaint;
    private Canvas mCanvas;
    private Path mPath;
    private SurfaceHolder mSurfaceHolder;
    private Surface mSurface;
    private boolean mIsDrawing;


    public SurfaceViewWritePanel(Context context) {
        this(context, null);
    }

    public SurfaceViewWritePanel(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SurfaceViewWritePanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPath = new Path();
        mPath.moveTo(0, 100);
        initView();
    }

    private void initView() {
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
        setFocusable(true);
        setKeepScreenOn(true);
        setFocusableInTouchMode(true);
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mSurface = holder.getSurface();
        if (mSurface != null) {
            mIsDrawing = true;
            new Thread(this).start();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mIsDrawing = false;
        mSurfaceHolder.removeCallback(this);
    }

    @Override
    public void run() {
        while (mIsDrawing) {
            long start = System.currentTimeMillis();
            drawSomething();
            long end = System.currentTimeMillis();
            if (end - start < 100) {
                try {
                    Thread.sleep(100 - (end - start));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void drawSomething() {
        try {
            mCanvas = mSurfaceHolder.lockCanvas();
            mCanvas.drawColor(Color.WHITE);
            mCanvas.drawPath(mPath, mPaint);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (mCanvas != null) {
                mSurfaceHolder.unlockCanvasAndPost(mCanvas);
                Log.d(TAG, "unlockCanvasAndPost");
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "DOWN");
                mPath.moveTo(x, y);
                return false;
            case MotionEvent.ACTION_MOVE:
                Log.d(TAG, "MOVE");
                mPath.lineTo(x, y);
                break;
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "UP");
                break;
        }
        return true;
    }
}
