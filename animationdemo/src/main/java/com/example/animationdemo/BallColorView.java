package com.example.animationdemo;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class BallColorView extends View {

    public final static String TAG = BallView.class.getSimpleName();
    private float radius;
    private Paint paint;
    private String color;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
        paint.setColor(Color.parseColor(color));
        invalidate();
    }

    public BallColorView(Context context) {
        this(context, null);
    }

    public BallColorView(Context context,  AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BallColorView(Context context,  AttributeSet attrs, int defStyleAttr) {
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
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float currentDrawPositionX = (getWidth() - radius) / 2;
        float currentY = getHeight() / 2.0f;
        canvas.translate(currentDrawPositionX, currentY);
        canvas.drawCircle(radius, radius, radius, paint);
    }
}
