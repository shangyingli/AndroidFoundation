package com.example.picasodemo.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;

public class CustomRecyclerView extends RecyclerView {

    public final static String TAG = CustomRecyclerView.class.getSimpleName();
    private View mCurrentView;

    public CustomRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

    }

    /**
     * 滚动时回调的接口
     */
    private OnItemScrollChangeListener mItemScrollChangeListener;

    public void setOnItemScrollChangeListener(
            OnItemScrollChangeListener mItemScrollChangeListener) {
        this.mItemScrollChangeListener = mItemScrollChangeListener;
    }

    public interface OnItemScrollChangeListener {
        void onChange(View view, int position);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        mCurrentView = getChildAt(0);
        if (mItemScrollChangeListener != null) {
            if (getChildPosition(mCurrentView) >= 0 ) {
                mItemScrollChangeListener.onChange(mCurrentView, getChildPosition(mCurrentView));
            }
        }
    }

    @Override
    public void onScrolled(int dx, int dy) {
        Log.d(TAG, "onScrolled");
        View newView = getChildAt(0);
        if (mItemScrollChangeListener != null) {
            if (newView != null && newView != mCurrentView) {
                mCurrentView = newView;
                mItemScrollChangeListener.onChange(mCurrentView, getChildPosition(mCurrentView));
            }
        }
    }

}
