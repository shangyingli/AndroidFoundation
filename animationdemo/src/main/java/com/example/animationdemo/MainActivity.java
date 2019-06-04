package com.example.animationdemo;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends Activity {

    private Button button;
    private ImageView imageView;

    public final static String TAG = MainActivity.class.getSimpleName();
    private static final int MAIN_REQUEST_CODE = 0x1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setStatusBar();
        initView();
        freshView();

    }

    private void initView() {
        button = findViewById(R.id.btn);
        imageView = findViewById(R.id.fight);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                startActivityForResult(intent, MAIN_REQUEST_CODE);
//                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
    }

    private void setStatusBar() {
        Window window = getWindow();
        window.getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        window.setNavigationBarColor(Color.TRANSPARENT);
        window.setStatusBarColor(Color.TRANSPARENT);
    }

    private void patchAnimation() {
        //1. 平移动画
        //xml中设置动画
//        Animation translateAnimation = AnimationUtils.loadAnimation(this, R.anim.view_translate_animation);
//        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//                Log.d(TAG, "onAnimationStart");
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                Log.d(TAG, "onAnimationEnd");
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//                Log.d(TAG, "onAnimationRepeat");
//            }
//        });
//        button.startAnimation(translateAnimation);
        //java中设置动画
//        Animation translateAnimation = new TranslateAnimation(0, 250, 0, 250);
//        translateAnimation.setDuration(3000);
//        translateAnimation.setFillEnabled(true);
//        translateAnimation.setFillAfter(false);
//        translateAnimation.setRepeatMode(Animation.REVERSE);
//        translateAnimation.setRepeatCount(1);
//        translateAnimation.setStartOffset(1000);
//        button.startAnimation(translateAnimation);

        //2.缩放动画
//        Animation scaleAnimation = AnimationUtils.loadAnimation(this, R.anim.view_scale_animation);
//        button.startAnimation(scaleAnimation);

//        Animation scaleAnimation = new ScaleAnimation(1, 2, 1, 2,
//                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        //        scaleAnimation.setDuration(3000);
//        scaleAnimation.setFillEnabled(true);
//        scaleAnimation.setFillAfter(false);
//        scaleAnimation.setRepeatMode(Animation.REVERSE);
//        scaleAnimation.setRepeatCount(1);
//        scaleAnimation.setStartOffset(1000);
//        button.startAnimation(scaleAnimation);


        //旋转动画
//        Animation rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.view_rotate_animation);
//        button.startAnimation(rotateAnimation);

//        Animation rotateAnimation = new RotateAnimation(0, 270, Animation.RELATIVE_TO_SELF, 0.5f,
//                Animation.RELATIVE_TO_SELF, 0.5f);
//        rotateAnimation .setDuration(3000);
//        rotateAnimation .setFillEnabled(true);
//        rotateAnimation .setFillAfter(false);
//        rotateAnimation .setRepeatMode(Animation.REVERSE);
//        rotateAnimation .setRepeatCount(1);
//        rotateAnimation .setStartOffset(1000);
//        button.startAnimation(rotateAnimation );

        //组合动画
//        Animation setAnimation = AnimationUtils.loadAnimation(this, R.anim.view_set_animation);
//        button.startAnimation(setAnimation);

//        AnimationSet setAnimation = new AnimationSet(true);
//        setAnimation.setRepeatMode(Animation.REVERSE);
//        setAnimation.setRepeatCount(1);// 设置了循环一次,但无效
//
//        Animation rotate = new RotateAnimation(0,360,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
//        rotate.setDuration(1000);
//        rotate.setRepeatMode(Animation.RESTART);
//        rotate.setRepeatCount(Animation.INFINITE);
//
//        // 子动画2:平移动画
//        Animation translate = new TranslateAnimation(TranslateAnimation.RELATIVE_TO_PARENT,-0.5f,
//                TranslateAnimation.RELATIVE_TO_PARENT,0.5f,
//                TranslateAnimation.RELATIVE_TO_SELF,0
//                ,TranslateAnimation.RELATIVE_TO_SELF,0);
//        translate.setDuration(10000);
//
//        // 子动画3:透明度动画
//        Animation alpha = new AlphaAnimation(1,0);
//        alpha.setDuration(3000);
//        alpha.setStartOffset(7000);
//
//        // 子动画4:缩放动画
//        Animation scale1 = new ScaleAnimation(1,0.5f,1,0.5f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
//        scale1.setDuration(1000);
//        scale1.setStartOffset(4000);
//
//        // 步骤4:将创建的子动画添加到组合动画里
//        setAnimation.addAnimation(alpha);
//        setAnimation.addAnimation(rotate);
//        setAnimation.addAnimation(translate);
//        setAnimation.addAnimation(scale1);
//
//        button.startAnimation(setAnimation);
    }

    private void propertyAnimation() {
//        valueAnimation();
//        objectValue();
        frameAnimation();
    }

    private AnimationDrawable animationDrawable;
    private void frameAnimation() {
        imageView.setImageResource(R.drawable.knight_attack);
        animationDrawable = (AnimationDrawable) imageView.getDrawable();
        animationDrawable.start();
    }

    private void objectValue() {
        //alpha动画
//        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(button, "alpha", 1f, 0f, 1f);
////        objectAnimator.setDuration(5000);
////        objectAnimator.start();

        //rotate动画
//        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(button, "rotationX", 0f, 180f);
//        objectAnimator.setDuration(3000);
//        objectAnimator.start();

        //自定义动画
//        BallColorView view = findViewById(R.id.ball);
//        ObjectAnimator objectAnimator = ObjectAnimator.ofObject(view, "color", new ColorEvaluator(),
//                "#0000FF", "#FF0000");
//        objectAnimator.setDuration(8000);
//        objectAnimator.start();

        //自定义属性
//        ButtonWrapper buttonWrapper = new ButtonWrapper(button);
//        ObjectAnimator
//                .ofInt(buttonWrapper, "width", button.getWidth(), 250)
//                .setDuration(3000)
//                .start();

        //组合动画
//        ObjectAnimator translate = ObjectAnimator.ofFloat(button, "translationX", 0, 250, 0);
//        ObjectAnimator rotate = ObjectAnimator.ofFloat(button, "rotation", 0f, 360);
//        ObjectAnimator alpha = ObjectAnimator.ofFloat(button, "alpha", 1f, 0f, 1f);
//        AnimatorSet animatorSet = new AnimatorSet();
//        animatorSet.play(translate).with(rotate).before(alpha);
//        animatorSet.setDuration(5000);
//        animatorSet.start();

        //组合动画XML实现
        AnimatorSet animatorSet = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.set_property_anim);
        animatorSet.setTarget(button);
        animatorSet.start();
    }

    private void valueAnimation() {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(button.getWidth(), 250);
        valueAnimator.setDuration(3000);
        valueAnimator.setStartDelay(1000);
        valueAnimator.setRepeatCount(0);
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int currentValue = (int) animation.getAnimatedValue();
                System.out.println("currentValue" + currentValue);
                button.getLayoutParams().width = currentValue;
                button.requestLayout();
            }
        });
        valueAnimator.start();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    //属性动画
    private void freshView() {
        //控件完全加载完成后执行
        button.post(new Runnable() {
            @Override
            public void run() {
                propertyAnimation();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        final ViewTreeObserver observer = button.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                button.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int width = button.getWidth();
                int height = button.getHeight();
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus) {
            int width = button.getWidth();
            int height = button.getHeight();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MAIN_REQUEST_CODE && resultCode == RESULT_OK) {
            String returnData = data.getStringExtra("data");
            Log.d(TAG, " return data is : " + returnData);
        }
    }
}
