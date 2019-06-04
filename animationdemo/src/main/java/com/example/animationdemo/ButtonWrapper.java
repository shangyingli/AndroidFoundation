package com.example.animationdemo;

import android.view.View;

public class ButtonWrapper {

    private View target;

    public ButtonWrapper(View target) {
        this.target = target;
    }

    public int getWidth() {
        return target.getLayoutParams().width;
    }

    public void setWidth(int width) {
        target.getLayoutParams().width =  width;
        target.requestLayout();
    }
}
