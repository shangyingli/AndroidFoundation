package com.example.surfaceviewdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class SurfaceViewSim extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    public final static String TAG = SurfaceViewSim.class.getSimpleName();
    private Paint mPaint;
    private Canvas mCanvas;
    private Path mPath;
    private SurfaceHolder mSurfaceHolder;
    private Surface mSurface;
    private boolean mIsDrawing;
    private int x = 0;
    private int y = 0;

    public SurfaceViewSim(Context context) {
        this(context, null);
    }

    public SurfaceViewSim(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SurfaceViewSim(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Log.d(TAG, "Constructor");
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
            Log.d(TAG, "surfaceCreated");
            mIsDrawing = true;
            new Thread(this).start();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d(TAG, "surfaceChanged");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "surfaceDestroyed");
        mIsDrawing = false;
        mSurfaceHolder.removeCallback(this);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.d(TAG, "onAttachedToWindow");
    }

    @Override
    public void run() {
        while (mIsDrawing) {
            if (x > 400) {
                break;
            }
            drawSomething();
            x += 1;
            y = (int)(100 * Math.sin(2 * x * Math.PI / 180) + 400);
            mPath.lineTo(x, y);
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
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d(TAG, "onDraw");
    }
}
