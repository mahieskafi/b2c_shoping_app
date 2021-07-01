package com.srp.b2b2cEwaysPannel.ui.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.core.content.res.ResourcesCompat;

import com.srp.b2b2cEwaysPannel.R;
import com.srp.eways.di.DIMain;
import com.srp.eways.util.Utils;

import ir.abmyapp.androidsdk.IABResources;

public class TimerButton extends View {

    private RectF mTimeTextBounds = new RectF();
    private Rect mStringBoundsHolder = new Rect();
    private RectF mButtonTitleBounds = new RectF();
    private RectF mCornerArc = new RectF();

    private Path mButtonBoxPath;
    private Path mFinishedPath;
    private PathMeasure mPathMeasure;

    private Paint mTimerPaint;
    private Paint mTitlePaint;
    private Paint mButtonBorderPaint;
    private Paint mFinishedPathPaint;

    private String mTitle;

    private float mTitlePaintDeltaY;

    private int mTitleEnableTextColor;
    private int mTitleDisableTextColor;
    private int mTimerTextColor;

    private int mBorderFinishedColor;
    private int mBorderUnFinishedColor;

    private int mTimeInSeconds;
    private int mRemainedSeconds;
    private long mMilliSecondsPast = 0;

    private float mBoxAreaLength;
    private float mFinishedRatio = 0f;

    private ValueAnimator mValueAnimator;

    private boolean mTimerIsFinished = true;

    public TimerButton(Context context) {
        this(context, null, 0);
    }

    public TimerButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimerButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        IABResources resources = DIMain.getABResources();

        mTimerTextColor = resources.getColor(R.color.login_otp_timer_button_timer_text_color);
        mTitleEnableTextColor = resources.getColor(R.color.login_otp_timer_button_text_enable_color);
        mTitleDisableTextColor = resources.getColor(R.color.login_otp_timer_button_text_disable_color);

        float strokeWidth = resources.getDimenPixelSize(R.dimen.login_otp_timer_stroke_width);

        mBorderFinishedColor = resources.getColor(R.color.login_otp_timer_button_timer_text_color);
        mBorderUnFinishedColor = resources.getColor(R.color.login_otp_timer_button_timer_stroke_color);

        mButtonBorderPaint = new Paint();
        mButtonBorderPaint.setAntiAlias(true);
        mButtonBorderPaint.setColor(mBorderFinishedColor);
        mButtonBorderPaint.setStyle(Paint.Style.STROKE);
        mButtonBorderPaint.setStrokeWidth(strokeWidth);

        mFinishedPathPaint = new Paint();
        mFinishedPathPaint.setAntiAlias(true);
        mFinishedPathPaint.setStyle(Paint.Style.STROKE);
        mFinishedPathPaint.setColor(resources.getColor(R.color.login_otp_timer_button_timer_stroke_animation_color));
        mFinishedPathPaint.setStrokeWidth(strokeWidth);

        mTimerPaint = new Paint();
        mTimerPaint.setAntiAlias(true);
        mTimerPaint.setStyle(Paint.Style.FILL);
        mTimerPaint.setTextSize(resources.getDimenPixelSize(R.dimen.login_otp_timer_text_size));
        mTimerPaint.setColor(mTimerTextColor);
        mTimerPaint.setTypeface(ResourcesCompat.getFont(context,R.font.iran_yekan_light));

        mTitlePaint = new Paint();
        mTitlePaint.setAntiAlias(true);
        mTitlePaint.setStyle(Paint.Style.FILL);
        mTitlePaint.setTextSize(resources.getDimenPixelSize(R.dimen.login_otp_timer_text_size));
        mTitlePaint.setColor(mTitleEnableTextColor);
        mTitlePaint.setTypeface(ResourcesCompat.getFont(context,R.font.iran_yekan_light));

        Paint.FontMetrics fontMetrics = mTimerPaint.getFontMetrics();
        mTitlePaintDeltaY = (fontMetrics.ascent + fontMetrics.descent) / 2;

        mButtonBoxPath = new Path();
        mFinishedPath = new Path();
        mPathMeasure = new PathMeasure(mButtonBoxPath, false);

        mValueAnimator = ValueAnimator.ofFloat(0, 1);
        mValueAnimator.addUpdateListener(mAnimatorUpdateListener);
        mValueAnimator.addListener(mAnimationListener);
        mValueAnimator.setInterpolator(new LinearInterpolator());

        mTitle = "دریافت رمز پویا";
        mTimeInSeconds = 10;
    }

    private ValueAnimator.AnimatorUpdateListener mAnimatorUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            float animatedValue = (float) animation.getAnimatedValue();

            long timeNow = System.currentTimeMillis();
            if (timeNow - mMilliSecondsPast >= 1000) {
                mMilliSecondsPast = timeNow;
                mRemainedSeconds--;
            }

            mFinishedRatio = animatedValue;
            invalidate();
        }
    };

    private ValueAnimator.AnimatorListener mAnimationListener = new Animator.AnimatorListener() {

        @Override
        public void onAnimationStart(Animator animation) { }

        @Override
        public void onAnimationEnd(Animator animation) {
            mRemainedSeconds = 0;
            mFinishedRatio = 1;
            mTimerIsFinished = true;
            setClickable(true);
            invalidate();
        }

        @Override
        public void onAnimationCancel(Animator animation) { }

        @Override
        public void onAnimationRepeat(Animator animation) { }

    };

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        measureRelatedVars();
    }

    public boolean startTimer(int durationSeconds) {
//        if (mValueAnimator.isRunning() || mValueAnimator.isStarted() || !mTimerIsFinished) {
//            return false;
//        }

        if (mTimerIsFinished && durationSeconds == 0) {
            return true;
        }

        setClickable(false);
        mValueAnimator.cancel();

        mFinishedRatio = 0f;
        mTimerIsFinished = false;

        mTimeInSeconds = durationSeconds;
        mRemainedSeconds = mTimeInSeconds;

        mValueAnimator.setDuration(mTimeInSeconds * 1_000);

        mValueAnimator.removeUpdateListener(mAnimatorUpdateListener);
        mValueAnimator.removeListener(mAnimationListener);

        mValueAnimator.addUpdateListener(mAnimatorUpdateListener);
        mValueAnimator.addListener(mAnimationListener);

        mMilliSecondsPast = System.currentTimeMillis();

        mFinishedPath.reset();
        mValueAnimator.start();

        return true;
    }

    public void stopTimer(){

        mValueAnimator.cancel();

        mRemainedSeconds = 0;
        mFinishedRatio = 1;
        mTimerIsFinished = true;
        setClickable(true);
        invalidate();


    }

    public boolean isTimerFinished() {
        return mTimerIsFinished;
    }

    public void setTitle(String title) {
        mTitle = title;

         requestLayout();
    }

    private void measureRelatedVars() {
        int width = getWidth();
        int height = getHeight();

        int boxCornerRadius = height / 2;

        int centerX = width / 2;
        int centerY = height / 2;

        String timePresentation = getTimePresentation();
        mTimerPaint.getTextBounds(timePresentation, 0, timePresentation.length(), mStringBoundsHolder);

        float timeTextWidth = mStringBoundsHolder.width();
        float timeTextHeight = mStringBoundsHolder.height();

        mTimeTextBounds.set((int)(width * 0.87), centerY - timeTextHeight / 2, (int)(width * 0.87) + timeTextWidth, centerY + timeTextHeight / 2);

        mTimerPaint.getTextBounds(mTitle, 0, mTitle.length(), mStringBoundsHolder);
        float buttonTextWidth = mStringBoundsHolder.width();
        float buttonTextHeight = mStringBoundsHolder.height();

        mButtonTitleBounds.set(centerX - buttonTextWidth / 2, centerY - buttonTextHeight / 2, centerX + buttonTextWidth / 2, centerY + buttonTextHeight / 2);

        float boxMargin = mButtonBorderPaint.getStrokeWidth() / 2f;

        mButtonBoxPath.reset();
        mButtonBoxPath.moveTo(((float)width / 2), boxMargin);
        mButtonBoxPath.lineTo(width - (boxCornerRadius + boxMargin), boxMargin);

        mCornerArc.set(width - (2 * boxCornerRadius) + boxMargin, boxMargin, width - boxMargin, height - boxMargin);
        mButtonBoxPath.arcTo(mCornerArc, 270, 180, false);

        mButtonBoxPath.lineTo(boxCornerRadius + boxMargin, height - boxMargin);

        mCornerArc.set(boxMargin, boxMargin, 2 * boxCornerRadius - boxMargin, height - boxMargin);
        mButtonBoxPath.arcTo(mCornerArc, 90, 180, false);

        mButtonBoxPath.close();

        if (mBoxAreaLength == 0) {
            mPathMeasure.setPath(mButtonBoxPath, false);
            mBoxAreaLength = mPathMeasure.getLength();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mTimerIsFinished) {
            mButtonBorderPaint.setColor(mBorderFinishedColor);
            canvas.drawPath(mButtonBoxPath, mButtonBorderPaint);

            mTitlePaint.setColor(mTitleEnableTextColor);

            canvas.drawText(mTitle, mButtonTitleBounds.left, mButtonTitleBounds.centerY() - mTitlePaintDeltaY, mTitlePaint);
        }
        else {
            mButtonBorderPaint.setColor(mBorderUnFinishedColor);
            canvas.drawPath(mButtonBoxPath, mButtonBorderPaint);

            mTimerPaint.setColor(mTimerTextColor);
            canvas.drawText(getTimePresentation(), mTimeTextBounds.left, mButtonTitleBounds.centerY() - mTitlePaintDeltaY, mTimerPaint);

            mTitlePaint.setColor(mTitleDisableTextColor);
            canvas.drawText(mTitle, mButtonTitleBounds.left, mButtonTitleBounds.centerY() - mTitlePaintDeltaY, mTitlePaint);

            mPathMeasure.setPath(mButtonBoxPath, false);
            mFinishedPath.moveTo(((float)getWidth() / 2), mButtonBorderPaint.getStrokeWidth() / 2f);
            mPathMeasure.getSegment(0, mPathMeasure.getLength() * mFinishedRatio, mFinishedPath, false);
            canvas.drawPath(mFinishedPath, mFinishedPathPaint);
        }
    }

    private String getTimePresentation() {
//        int minuit = mRemainedSeconds / 60;
        int second = mRemainedSeconds % 60;

//        String minuitRepresentation = minuit < 10 ? ("0" + minuit) : ("" + minuit);
        String secondRepresentation = second < 10 ? ("0" + second) : ("" + second);

        return Utils.toPersianNumber(secondRepresentation);
    }

}
