package com.example.ninepatch;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class NineGridView extends ViewGroup {

    public final static String TAG = NineGridView.class.getSimpleName();
    private int mNineGvSpacing;
    private int mSingleImageSize;
    private int mMaxImageCount;
    private List<CustomImageView> mDataList = new ArrayList<>();
    private int rowCount;
    private int columnCount;
    //单个图片宽高
    private int mWidth;
    private int mHeight;

    private Context context;
    private boolean isLongClicked;

    public NineGridView(Context context) {
        this(context, null);
    }

    public NineGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NineGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NineGridView);
        int typeCount = typedArray.getIndexCount();
        for (int i = 0; i < typeCount; i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.NineGridView_nine_gv_spacing:
                    mNineGvSpacing = typedArray.getDimensionPixelSize(attr, mNineGvSpacing);
                    LogUtil.d( "mNineGvSpacing : " + mNineGvSpacing);
                    break;
                case R.styleable.NineGridView_single_image_size:
                    mSingleImageSize = typedArray.getDimensionPixelSize(attr, mSingleImageSize);
                    LogUtil.d( "mSingleImageSize : " + mSingleImageSize);
                    break;
                case R.styleable.NineGridView_nine_maxImageNum:
                    mMaxImageCount = typedArray.getInt(attr, mMaxImageCount);
                    LogUtil.d( "mMaxImageCount : " + mMaxImageCount);
                    break;
            }
        }
        typedArray.recycle();
        setData();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        LogUtil.d( "onMeasure");
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        LogUtil.d( " widthMode : " + widthMode + "heightMode : " + heightMode + "widthSize : " + widthSize + "heightSize : " + heightSize);
        int totalWidth = widthSize - getPaddingLeft() - getPaddingRight();
        LogUtil.d( "totalWidth : " + totalWidth);
        if (mDataList != null && mDataList.size() > 0) {
            //只有单张图片
            if (mDataList.size() == 1) {
                mWidth = mSingleImageSize > totalWidth ? (int) (totalWidth * 0.8) : mSingleImageSize;
                mHeight = mWidth;
            } else { //多张图片
                mHeight = mWidth = (totalWidth - mNineGvSpacing * (columnCount - 1)) / columnCount;//每个图片宽高
            }
            widthSize = mWidth * columnCount + mNineGvSpacing * (columnCount - 1) + getPaddingLeft() + getPaddingRight();
            heightSize = mHeight * rowCount + mNineGvSpacing * (rowCount - 1) + getPaddingTop() + getPaddingBottom();
            setMeasuredDimension(widthSize, heightSize); //重新确定viewGroup的宽高
        } else {
            heightSize = widthSize;
            setMeasuredDimension(widthSize, heightSize);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onLayout(boolean changed, final int l, int t, int r, int b) {
        if (mDataList == null) return;
        LogUtil.d( "onLayout");
        int itemCount = mDataList.size() > mMaxImageCount ? mMaxImageCount : mDataList.size();
        for (int i = 0; i < itemCount; i++) {
            final CustomImageView childView = (CustomImageView) getChildAt(i);

            childView.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) { //返回true，则事件消费结束，不会调用view的onTouchEvent及其后面的方法
                    Log.d(TAG, "onTouch");
                    return false;
                }
            });

            childView.setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    LogUtil.d("onLongClick");
                    listener.onLongClick(v);
                    childView.setSelected(true);
                    //获取childView在viewGroup的位置，在指定位置弹出PopupWindow

                    invalidate();
                    return true; //返回true则拦截事件，不会执行onClick
                }
            });

            childView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LogUtil.d( "onClick");
                    listener.onClick(v);
                }
            });
            //确定每个view的所在行列
            int columnNum = i % columnCount;
            int rowNum = i / columnCount;
            //确定每个view的位置
            int left = (mWidth + mNineGvSpacing) * columnNum + getPaddingLeft();
            int top = (mHeight + mNineGvSpacing) * rowNum + getPaddingTop();
            int right = left + mWidth;
            int bottom = top + mHeight;
            //设置子view在viewGroup中的位置
            childView.layout(left, top, right, bottom); //确定每个childView的位置
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        LogUtil.d( "onDraw");
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        LogUtil.d("dispatchDraw");
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            CustomImageView view = (CustomImageView) getChildAt(i);
            if (view.isSelected()) {
                LogUtil.d("selected : " +i);
                Toast.makeText(context, "position" + i + "被点击了！", Toast.LENGTH_LONG).show();
                int top = view.getTop();
                int left = view.getLeft() + view.getWidth() / 2;
                LogUtil.d(" top : " + top + "left : " + left);
                canvas.drawText("收藏", left,  top, paint);
                view.setSelected(false);
            }
        }
    }

    public void setData() {
        //加载图片
        Drawable image1 = getResources().getDrawable(R.drawable.banner_2);
        Drawable image2 = getResources().getDrawable(R.drawable.banner_4);
        Drawable image3 = getResources().getDrawable(R.drawable.banner_5);

        CustomImageView CustomImageView1 = new CustomImageView(context);
        CustomImageView CustomImageView2 = new CustomImageView(context);
        CustomImageView CustomImageView3 = new CustomImageView(context);
        CustomImageView CustomImageView4 = new CustomImageView(context);
        CustomImageView CustomImageView5 = new CustomImageView(context);


        CustomImageView1.setImageDrawable(image1);
        CustomImageView2.setImageDrawable(image2);
        CustomImageView3.setImageDrawable(image3);
        CustomImageView4.setImageDrawable(image1);
        CustomImageView5.setImageDrawable(image2);

        mDataList.add(CustomImageView1);
        mDataList.add(CustomImageView2);
        mDataList.add(CustomImageView3);
        mDataList.add(CustomImageView4);
        mDataList.add(CustomImageView5);

        if (mDataList == null || mDataList.isEmpty()) {
            setVisibility(GONE);
            return;
        } else {
            setVisibility(VISIBLE);
        }
        //获取图片数量
        int newImageCount = mDataList.size() > mMaxImageCount ? mMaxImageCount : mDataList.size();
        //根据图片数据计算行数和列数
        setRowAndColumnCount(newImageCount);
        for (int i = 0; i < mDataList.size(); i++) {
            addView(mDataList.get(i), generateDefaultLayoutParams());
        }
    }

    private void setRowAndColumnCount(int imageCount) {
        if (imageCount <= 3) {
            rowCount = 1;
            columnCount = imageCount;
        } else if (imageCount <= 6) {
            rowCount = 2;
            columnCount = 3;
            if (imageCount == 4) {
                columnCount = 2;
            }

        } else {
            rowCount = 3;
            columnCount = 3;
        }
    }


    private OnClickListener listener;

    public void setClickListener(OnClickListener listener) {
        this.listener = listener;
    }

    public interface OnClickListener {
        void onClick(View view);
        void onLongClick(View view);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        LogUtil.d( "dispatchTouchEvent");
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 返回true, 将触摸事件拦截，事件不会传到view， 而是调用viewgroup的onTouch->onTouchEvent->
     * 返回false, 则将不会调用viewgroup的onTouch和onTouchEvent， 而是由view处理事件
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        LogUtil.d( "onInterceptTouchEvent");
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        LogUtil.d( "onTouchEvent");
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                LogUtil.d( "ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                LogUtil.d( "ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                LogUtil.d( "ACTION_UP");
                performClick();
                break;
        }
        return true; //返回true则后续事件仍由viewGroup处理，false则将后续事件向上传递到activity的onTouchEvent处理
    }


    @Override
    public boolean performClick() {
        LogUtil.d( "performClick");
        return super.performClick();
    }

}
