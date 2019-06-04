package com.example.picasodemo.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.picasodemo.R;

public class NormalItemDecoration extends RecyclerView.ItemDecoration {

    private int mDividerHeight;
    private Paint mPaint;
    private Bitmap mIcon;

    public NormalItemDecoration(Context context) {
        mDividerHeight = 10;
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.logo_rss);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            int index = parent.getChildAdapterPosition(child);
            int top = child.getBottom();
            int bottom = top + mDividerHeight;
            c.drawRect(left, top, right, bottom, mPaint);
        }

    }

//    @Override
//    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
//        int childCount = parent.getChildCount();
//        for (int i = 0; i < childCount; i++) {
//            View view = parent.getChildAt(i);
//            int top = view.getBottom() - mIcon.getHeight();
//            int left = view.getWidth() - mIcon.getWidth();
//            c.drawBitmap(mIcon, left, top, mPaint);
//        }
//    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(0, 0, 0, 20);
    }
}
