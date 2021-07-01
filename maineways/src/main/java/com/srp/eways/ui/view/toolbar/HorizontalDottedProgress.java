package com.srp.eways.ui.view.toolbar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;

import com.srp.eways.R;
import com.srp.eways.di.DIMain;

import ir.abmyapp.androidsdk.IABResources;

public class HorizontalDottedProgress extends View {

    private int mDotRadius;
    private int mDotPosition = 0;
    private int mDotAmount = 3;
    private boolean mIsIncreased = true;

    private int mDotDistance;

    private int mFillDotColor;
    private int mShadowDotColor;

    private BounceAnimation mBounceAnimation;

    public HorizontalDottedProgress(Context context) {
        super(context);
        init();
    }

    public HorizontalDottedProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HorizontalDottedProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        IABResources abResources = DIMain.getABResources();

        mDotRadius = abResources.getDimenPixelSize(R.dimen.toolbar_shopprogressbar_dot_radius);
        mDotDistance = abResources.getDimenPixelSize(R.dimen.toolbar_shopprogressbar_dot_margins);
        mFillDotColor = abResources.getColor(R.color.toolbar_shop_count_background);
        mShadowDotColor = abResources.getColor(R.color.toolbar_shopprogress_shadow_dot_background);

        mBounceAnimation = new BounceAnimation();
        mBounceAnimation.setDuration(200);
        mBounceAnimation.setRepeatCount(Animation.INFINITE);
        mBounceAnimation.setInterpolator(new LinearInterpolator());
        mBounceAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                if (mIsIncreased) {
                    mDotPosition++;
                } else {
                    mDotPosition--;
                }

                if (mDotPosition > mDotAmount) {
                    mDotPosition = mDotAmount;
                    mIsIncreased = false;
                }
                if (mDotPosition == 0) {
                    mIsIncreased = true;
                }
                invalidate();
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        createDot(canvas);
    }

    private void createDot(Canvas canvas) {

        Paint paintFill = new Paint();
        paintFill.setColor(mFillDotColor);

        Paint paintShadow = new Paint();
        paintShadow.setColor(mShadowDotColor);

        for (int i = 0; i < mDotPosition; i++) {
            canvas.drawCircle(mDotRadius + (i * mDotDistance), mDotRadius, mDotRadius, paintFill);
            if (i < mDotAmount - 1) {
                canvas.drawCircle(mDotRadius + ((i + 1) * mDotDistance), mDotRadius, mDotRadius, paintShadow);
            }
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        setMeasuredDimension((mDotRadius * 3), (mDotRadius * 2));
    }

    public void startAnimation() {
        mBounceAnimation.reset();
        startAnimation(mBounceAnimation);
    }

    public void stopAnimation() {
        mBounceAnimation.cancel();
    }

    public void setDotsColor(int color, int shadowColor) {
        mFillDotColor = color;
        mShadowDotColor = shadowColor;

        invalidate();
    }

    private class BounceAnimation extends Animation {
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);

            invalidate();
        }
    }
}
