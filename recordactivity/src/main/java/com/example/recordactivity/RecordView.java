package com.example.recordactivity;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class RecordView extends RelativeLayout {

    private static final String TAG = "RecordView";

    private String mTimeString = "";
    private View mRootviView = null;
    public TextView mStateView;

    private LinearLayout mTimerLayout;
    private TextView mHourView;
    private TextView mMinView;
    private TextView mSecView;

    public RecordView(Context context) {
        super(context);
        initView(context);
    }

    public RecordView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public RecordView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public void initView(Context context) {

        mRootviView = View.inflate(context, R.layout.layout_record_view, this);
        mStateView = (TextView) mRootviView.findViewById(R.id.id_stateView);
        mTimerLayout = (LinearLayout) mRootviView.findViewById(R.id.id_timerView1);
        mHourView = (TextView) mRootviView.findViewById(R.id.op_record_page_hourView);
        mMinView = (TextView) mRootviView.findViewById(R.id.op_record_page_minView);
        mSecView = (TextView) mRootviView.findViewById(R.id.op_record_page_secView);
    }

    public void updateTimer(String time, boolean is) {
        mTimeString = time;

        String[] times = time.split(":");

        if(mHourView != null){
            mHourView.setText(times[0]);
        }
        if(mMinView != null){
            mMinView.setText(times[1]);
        }
        if(mSecView != null){
            mSecView.setText(times[2]);
        }

        if(mTimerLayout != null) {
            if (is) {
                if (mAnimator != null && mAnimator.isRunning()) {
                    mAnimator.end();
                }
                mTimerLayout.setAlpha(0.87f);
            }
        }
    }

    // led show
    public void updateTimerLed() {

        if (mTimerLayout != null) {
            startLedAnimator();
        }
    }

    private ObjectAnimator mAnimator;

    private void startLedAnimator() {

        if (mAnimator != null && mAnimator.isRunning()) {
            mAnimator.end();
        }
        mAnimator = ObjectAnimator.ofFloat(mTimerLayout, "alpha", new float[] {
                0.87f, 0.2f, 0.87f });
        mAnimator.setDuration(3000);
        mAnimator.setRepeatCount(Animation.INFINITE);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.start();
    }
}

