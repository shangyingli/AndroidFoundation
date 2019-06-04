package com.example.popupwindowdemo;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import static android.view.View.MeasureSpec.getSize;
import static android.view.View.MeasureSpec.makeMeasureSpec;

public class MainActivity extends Activity {

    public final static String TAG = MainActivity.class.getSimpleName();
    private Button btn;
    private PopupWindow popupWindow;
    private Button collectBtn;
    private Button modifyBtn;
    private LinearLayout activity_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity_main = findViewById(R.id.main);
        btn = findViewById(R.id.btn);
        popupWindow = new PopupWindow();
        popupWindow.setFocusable(true);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                showBackgroundAnimator(0.5f, 1.0f);
            }
        });
        popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout layout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.view_popup, null, false);
        collectBtn = layout.findViewById(R.id.collect);
        modifyBtn = layout.findViewById(R.id.modify);
        collectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "collect", Toast.LENGTH_SHORT).show();
            }
        });
        modifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "modify", Toast.LENGTH_SHORT).show();
            }
        });
        popupWindow.setContentView(layout);
        View contentView = popupWindow.getContentView();
        Log.d(TAG, "POP WIDTH : " + popupWindow.getWidth() + "POP HEIGHT : " + popupWindow.getHeight());
        Log.d(TAG, "WIDTH : " + makeDropDownMeasureSpec(popupWindow.getWidth()));
        //根据父布局大小确定子布局大小
        contentView.measure(makeDropDownMeasureSpec(popupWindow.getWidth()), makeDropDownMeasureSpec(popupWindow.getHeight()));
        int width = contentView.getMeasuredWidth();
        int height = contentView.getMeasuredHeight();
        //测量button宽高
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        btn.measure(w, h);
        int btnWidth = btn.getMeasuredWidth();
        int btnHeight = btn.getMeasuredHeight();
        final int offsetX = Math.abs((width - btnWidth)) / 2;
        final int offsetY = -(height + btnHeight);
        Log.d(TAG, "width : " + width + "height : " + height + "btn width : " + btnWidth + "btn height : " + btnHeight);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.showAsDropDown(v, offsetX, offsetY, Gravity.START);
                showBackgroundAnimator(1.0f, 0.5f);
            }
        });
    }

    /**
     * 测量父布局的大小
     * @param measureSpec
     * @return
     */
    private static int makeDropDownMeasureSpec(int measureSpec) {
        int mode;
        if (measureSpec == ViewGroup.LayoutParams.WRAP_CONTENT) {
            mode = View.MeasureSpec.UNSPECIFIED;
        } else {
            mode = View.MeasureSpec.EXACTLY;
        }
        return makeMeasureSpec(getSize(measureSpec), mode);
    }

    private void setWindowBackgroundAlpha(float alpha) {
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.alpha = alpha;
        window.setAttributes(layoutParams);
    }

    private void showBackgroundAnimator(float startAlpha, float endAlpha) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(startAlpha, endAlpha);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float alpha = (float) animation.getAnimatedValue();
                setWindowBackgroundAlpha(alpha);
            }
        });
        valueAnimator.setDuration(360);
        valueAnimator.start();
    }
}
