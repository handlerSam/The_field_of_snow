package com.example.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class BloodProgress extends View {

    /**
     * 缓冲时间 700ms
     */
    private static final int DELTATIME = 700;

    /**
     * 第一血条
     */
    private RectF mFirst_rect;
    private int mFirstColor;
    private Paint mFirstPaint;
    /**
     * 第二血条
     */
    private RectF mSecond_rect;
    private int mSecondWidth;
    private int mSecondColor;
    private Paint mSecondPaint;

    private int mTotalHeight;
    private int mTotalWidth;

    private int mTotalProgress = 0;
    private int mCurrentProgress = 0;
    private boolean isSliding = false;

    public BloodProgress(Context context) {
        super(context);
        init();
    }

    public BloodProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BloodProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mTotalWidth = getMeasuredWidth();
        mTotalHeight = getMeasuredHeight();
    }

    private void init() {

        //setBackgroundColor(Color.DKGRAY);

        mFirst_rect = new RectF();
        mSecond_rect = new RectF();

        mFirstPaint = new Paint();
        mFirstPaint.setAntiAlias(true);
        mFirstPaint.setDither(true);
        mSecondPaint = new Paint();
        mSecondPaint.setAntiAlias(true);
        mSecondPaint.setColor(Color.WHITE);
        mSecondPaint.setDither(true);
    }

    public int getmTotalProgress() {
        return mTotalProgress;
    }

    public int getmCurrentProgress() {
        return mCurrentProgress;
    }

    public void setmTotal(int mTotalProgress) {
        this.mTotalProgress = mTotalProgress;
        this.mCurrentProgress = mTotalProgress;
        mSecondWidth = mTotalWidth;
    }

    public void setmCurrent(int mCurrentProgress) {

        double ratio;
        if (mTotalProgress != 0) {
            ratio = (double) mCurrentProgress / (double) mTotalProgress;
            setProgressColor(ratio);
        } else {
            ratio = 0;
        }

        /* 正确的宽度 */
        final int right = (int) (ratio * mTotalWidth);
        mFirst_rect.set(0, 0, right, mTotalHeight);

        /* 如果上一次大于这次 ，执行缓冲条渐变效果*/
        if (this.mCurrentProgress > mCurrentProgress) {
            /*这里防止在缓冲条变化期间,改变currentprogress值,导致两个缓冲条同时变化*/
            if (!isSliding) {
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        isSliding = true;
                        /*for循环，直到达到  第一血条的值*/
                        for (int i = mSecondWidth; i >= mFirst_rect.right; i--) {
                            try {
                                /*第一血条的值会随时变化,如果小于缓冲值,继续执行*/
                                if ((mSecondWidth - mFirst_rect.right) > 0) {
                                    //这里缓冲条时间，700ms
                                    Thread.sleep((long) (DELTATIME / (mSecondWidth - mFirst_rect.right)));
                                    mSecond_rect.set(0, 0, i, mTotalHeight);
                                    postInvalidate();
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        isSliding = false;
                        mSecondWidth = (int) mFirst_rect.right;
                    }
                }.start();
            }

        } else {
            /* 当现在进度大于前一进度,则直接将缓冲条值设为当前进度.并且通过这个判断防止在缓冲条减少的时候,因当前值的增加而瞬间变化 */
            if (right >= mSecondWidth) {
                mSecondWidth = right;
                mSecond_rect.set(0, 0, mSecondWidth, mTotalHeight);
            }

        }

        this.mCurrentProgress = mCurrentProgress;
        invalidate();
    }

    /**
     * 设置进度条颜色
     *
     * @param radio
     */
    private void setProgressColor(double radio) {

        mFirstColor = Color.GREEN;
        mSecondColor = Color.WHITE;

        if (radio >= 0.5) {
            mFirstColor = Color.rgb((int) (254 - 508 * (radio - 0.5)), 254, 0);
            mSecondColor = Color.rgb((int) (254 - 508 * (radio - 0.5))/2, 254/2, 0);
        } else {
            mFirstColor = Color.rgb(254, (int) (254 - 508 * (0.5 - radio)), 0);
            mSecondColor = Color.rgb(254/2, (int) (254 - 508 * (0.5 - radio))/2, 0);
        }
        mFirstPaint.setColor(mFirstColor);
        mSecondPaint.setColor(mSecondColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawRoundRect(mSecond_rect, 6, 9, mSecondPaint);
        canvas.drawRoundRect(mFirst_rect, 6, 9, mFirstPaint);

    }
}
