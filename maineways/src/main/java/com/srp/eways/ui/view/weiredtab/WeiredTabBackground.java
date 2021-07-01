package com.srp.eways.ui.view.weiredtab;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;


public class WeiredTabBackground extends Drawable {

    private Paint mBackgroundPaint;

    private int mCenterCircleRadius;
    private int mSideCircleRadius;

    private int mCenterCircleVisibleHeight;

    private int mPaddingTop;

    private double mSideCircleXOffsetFromCenterCircle;

    private Path mPath;

    private RectF mSideCircleRect = new RectF();
    private RectF mCenterCircleRect = new RectF();

    private float mPosition;

    public WeiredTabBackground(Context context, int centerCircleRadius, int sideCircleRadius, int centerCircleVisibleHeight, int paddingTop, int fillColor) {
        Resources resources = context.getResources();

        mCenterCircleRadius = centerCircleRadius;
        mSideCircleRadius = sideCircleRadius;

        mCenterCircleVisibleHeight = centerCircleVisibleHeight;

        mPaddingTop = paddingTop;

        mBackgroundPaint = new Paint();
        mBackgroundPaint.setAntiAlias(true);
        mBackgroundPaint.setColor(fillColor);
        mBackgroundPaint.setStyle(Paint.Style.FILL);

        mPath = new Path();
    }

    public void setFillColor(int fillColor) {
        mBackgroundPaint.setColor(fillColor);

        invalidateSelf();
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        measure();
    }

    public void setPosition(float position) {
        mPosition = position;

        measure();

        invalidateSelf();
    }

    private void measure() {
        Rect bounds = getBounds();

        double tetha2 = Math.acos((double)(mCenterCircleRadius + mSideCircleRadius - mCenterCircleVisibleHeight) / (mCenterCircleRadius + mSideCircleRadius));
        double tetha1 = (Math.PI / 2) - tetha2;

        double centerX = mPosition;

        mSideCircleXOffsetFromCenterCircle = Math.sin(tetha2) * (mSideCircleRadius + mCenterCircleRadius);

        mPath.reset();
        mPath.moveTo(bounds.left, mPaddingTop);
        mPath.lineTo((float) (centerX - mSideCircleXOffsetFromCenterCircle), mPaddingTop);

        mSideCircleRect.left = (float)(centerX - mSideCircleXOffsetFromCenterCircle - mSideCircleRadius);
        mSideCircleRect.top = mPaddingTop;
        mSideCircleRect.right = mSideCircleRect.left + 2 * mSideCircleRadius;
        mSideCircleRect.bottom = mSideCircleRect.top + 2 * mSideCircleRadius;

        mPath.arcTo(mSideCircleRect, -90, getDegreeFromRadian(tetha2));

        mCenterCircleRect.left = (float)(centerX - mCenterCircleRadius);
        mCenterCircleRect.bottom = mPaddingTop + mCenterCircleVisibleHeight;
        mCenterCircleRect.top = mCenterCircleRect.bottom - 2 * mCenterCircleRadius;
        mCenterCircleRect.right = mCenterCircleRect.left + 2 * mCenterCircleRadius;

        float mCenterCircleStartAngle = 90 + getDegreeFromRadian(tetha2);
        float mCenterCircleSweepAngle = -2 * getDegreeFromRadian(tetha2);
        mPath.arcTo(mCenterCircleRect, mCenterCircleStartAngle, mCenterCircleSweepAngle);

        mSideCircleRect.offset((float) (2 * mSideCircleXOffsetFromCenterCircle), 0);
        mPath.arcTo(mSideCircleRect, 180 + getDegreeFromRadian(tetha1), getDegreeFromRadian(tetha2));

        mPath.lineTo(bounds.right, mPaddingTop);
        mPath.lineTo(bounds.right, bounds.top);
        mPath.lineTo(bounds.left, bounds.top);
        mPath.lineTo(bounds.left, bounds.bottom);
        mPath.close();
    }

    private float getDegreeFromRadian(double angelInRadian) {
        return (float)(angelInRadian * 180 / Math.PI);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawPath(mPath, mBackgroundPaint);
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }

}
