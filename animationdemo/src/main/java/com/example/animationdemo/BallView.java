package com.example.animationdemo;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class BallView extends View {

    public final static String TAG = BallView.class.getSimpleName();
    private float radius;
    private Paint paint;
    private Point currentPoint;

    public BallView(Context context) {
        this(context, null);
    }

    public BallView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BallView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLUE);
        TypedArray arrays = context.obtainStyledAttributes(attrs, R.styleable.BallView);
        radius = arrays.getFloat(R.styleable.BallView_radius, 0);
        Log.d(TAG, "RADIUS : " + radius);
        arrays.recycle();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (currentPoint == null) {
            currentPoint = new Point(radius, radius);
            float x = currentPoint.getX();
            float y = currentPoint.getY();
            canvas.drawCircle(x, y, radius, paint);

            Point startPoint = new Point(radius, radius);
            Point endPoint = new Point(250, 250);
            PointEvaluator pointEvaluator = new PointEvaluator(); //估值器
            ValueAnimator valueAnimator = ValueAnimator.ofObject(pointEvaluator, startPoint, endPoint);
            valueAnimator.setDuration(3000);
            valueAnimator.setStartDelay(1000);
            valueAnimator.setRepeatCount(0);
            valueAnimator.setRepeatMode(ValueAnimator.RESTART);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    currentPoint = (Point) animation.getAnimatedValue();
                    Log.d(TAG, "X : " + currentPoint.getX() + " Y : " + currentPoint.getY());
                    invalidate(); //仅会调用onDraw
//                    requestLayout();//会调用onMeasure, onLayout, onDraw
                }
            });
            valueAnimator.start();
        } else {
            float x = currentPoint.getX();
            float y = currentPoint.getY();
            canvas.drawCircle(x, y, radius, paint);
        }
    }
}
