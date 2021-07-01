package com.srp.ewayspanel.ui.view.bottomnavigation;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.srp.ewayspanel.R;

public class BottomNavigationBackground extends Drawable {

    private static final int SHADOW_COLOR = 0x14000000; // 8% black opacity

    private Paint mBackgroundPaint;

    private Paint mShadowPaint;

    private LinearGradient mLinearGradient;
    private RadialGradient mLeftCircleRadialGradient;
    private RadialGradient mCenterCircleRadialGradient;
    private RadialGradient mRightCircleRadialGradient;

    private int mCenterCircleRadius;
    private int mSideCircleRadius;

    private int mCenterCircleVisibleHeight;

    private float mShadow1Height;

    private int mShadowStartColor;
    private int mShadowEndColor;

    private Path mPath;

    private RectF mSideCircleRect = new RectF();
    private RectF mCenterCircleRect = new RectF();

    private RectF mCenterCircleShadow1Bounds = new RectF();

    private float mPosition = 0;

    private int mShadowColor;
    private float[] mShadowInfo = new float[6];

    private double mSideCircleXOffsetFromCenterCircle;
    private RectF mLeftSideCircleBounds = new RectF();

    private float mCenterCircleStartAngle;
    private float mCenterCircleSweepAngle;
    private float mLeftSideCircleSweepAngle;

    public BottomNavigationBackground(Context context) {
        Resources resources = context.getResources();
        mCenterCircleRadius = resources.getDimensionPixelSize(R.dimen.bottomnav_center_circle_radius);
        mSideCircleRadius = resources.getDimensionPixelSize(R.dimen.bottomnav_side_circle_radius);

        int backgroundColor = resources.getColor(R.color.bottomnav_background);
//        mShadowStartColor = resources.getColor(R.color.bottomnav_shadow_color);
//        mShadowEndColor = Color.parseColor("#00000000");

        float elevation = resources.getDimension(R.dimen.bottomnav_shadow_length);
        mShadowColor = getShadowInfo(elevation, mShadowInfo);

        mShadow1Height = mShadowInfo[5];
        mShadowStartColor = mShadowColor;
        mShadowEndColor = 0x00FFFFFF & mShadowStartColor;

        mPath = new Path();

        mBackgroundPaint = new Paint();
        mBackgroundPaint.setAntiAlias(true);
        mBackgroundPaint.setColor(backgroundColor);
        mBackgroundPaint.setStyle(Paint.Style.FILL);
        mBackgroundPaint.setStrokeWidth(resources.getDimensionPixelSize(R.dimen.bottomnav_strokewidth));

        mShadowPaint = new Paint();
        mShadowPaint.setAntiAlias(true);
        mShadowPaint.setStyle(Paint.Style.FILL);
    }

    public static int getShadowInfo(float elevation, float[] shadowInfo) {
        shadowInfo[0] = 0;
        shadowInfo[1] = elevation / 4;
        shadowInfo[2] = elevation / 2;

        shadowInfo[3] = 0;
        shadowInfo[4] = elevation / 4;
        shadowInfo[5] = elevation / 1;

        return SHADOW_COLOR;
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        mCenterCircleVisibleHeight = (int)(((float)18 / 56) * bounds.height());

        mCenterCircleVisibleHeight = (int)(((float)31/ 100) * bounds.height());
        mCenterCircleRadius = (int)(((float)90/ 100) * bounds.height());
        mSideCircleRadius = (int)(((float)100/ 100) * bounds.height());

        measure();
    }

    public void setPosition(float position) {
        mPosition = position;

        measure();

        invalidateSelf();
    }

    private void measure() {
        final Rect bounds = getBounds();

        float centerX = mPosition;
//        float centerX = (float)bounds.width() / 2;

        double tetha2 = Math.acos((double)(mCenterCircleRadius + mSideCircleRadius - mCenterCircleVisibleHeight) / (mCenterCircleRadius + mSideCircleRadius));
        double tetha1 = (Math.PI / 2) - tetha2;

        double x = Math.sin(tetha2) * (mSideCircleRadius + mCenterCircleRadius);
        mSideCircleXOffsetFromCenterCircle = x;

        mPath.reset();
        mPath.moveTo(0, 0);
        mPath.lineTo((float) ((double)centerX - x), 0);

        mSideCircleRect.left = (float)((double)centerX - x - mSideCircleRadius);
        mSideCircleRect.top = - 2 * mSideCircleRadius;
        mSideCircleRect.right = mSideCircleRect.left + 2 * mSideCircleRadius;
        mSideCircleRect.bottom = mSideCircleRect.top + 2 * mSideCircleRadius;

        mLeftSideCircleSweepAngle = - getDegreeFromRadian(tetha2);
        mPath.arcTo(mSideCircleRect, 90, - getDegreeFromRadian(tetha2));

        mCenterCircleRect.left = centerX - mCenterCircleRadius;
        mCenterCircleRect.top = - mCenterCircleVisibleHeight;
        mCenterCircleRect.right = mCenterCircleRect.left + 2 * mCenterCircleRadius;
        mCenterCircleRect.bottom = mCenterCircleRect.top + 2 * mCenterCircleRadius;

        mCenterCircleShadow1Bounds.left = mCenterCircleRect.left - mShadow1Height;
        mCenterCircleShadow1Bounds.top = mCenterCircleRect.top - mShadow1Height;
        mCenterCircleShadow1Bounds.right = mCenterCircleRect.right + mShadow1Height;
        mCenterCircleShadow1Bounds.bottom = mCenterCircleRect.bottom + mShadow1Height;
        float centerCircleShadow1Radius = mCenterCircleRadius + mShadow1Height;
        mCenterCircleRadialGradient = new RadialGradient(mCenterCircleShadow1Bounds.centerX(), mCenterCircleShadow1Bounds.centerY(), centerCircleShadow1Radius, new int[]{mShadowEndColor, mShadowStartColor, mShadowEndColor}, new float[]{((float)mCenterCircleRadius) / centerCircleShadow1Radius, ((float)mCenterCircleRadius) / centerCircleShadow1Radius, 1}, Shader.TileMode.CLAMP);

        mCenterCircleStartAngle = 180 + getDegreeFromRadian(tetha1);
        mCenterCircleSweepAngle = 2 * getDegreeFromRadian(tetha2);
        mPath.arcTo(mCenterCircleRect, mCenterCircleStartAngle, mCenterCircleSweepAngle);

        mLinearGradient = new LinearGradient(bounds.left, bounds.top, bounds.left, bounds.top - mShadow1Height, new int[]{mShadowStartColor, mShadowEndColor}, null, Shader.TileMode.CLAMP);
        mLeftCircleRadialGradient = new RadialGradient(mSideCircleRect.centerX(), mSideCircleRect.centerY(), mSideCircleRadius, new int[]{mShadowEndColor, mShadowStartColor}, new float[]{((float)mSideCircleRadius - mShadow1Height) / mSideCircleRadius, 1}, Shader.TileMode.CLAMP);

        mLeftSideCircleBounds.set(mSideCircleRect);
        mSideCircleRect.offset((float) (2 * x), 0);
        mRightCircleRadialGradient = new RadialGradient(mSideCircleRect.centerX(), mSideCircleRect.centerY(), mSideCircleRadius, new int[]{mShadowEndColor, mShadowStartColor}, new float[]{((float)mSideCircleRadius - mShadow1Height) / mSideCircleRadius, 1}, Shader.TileMode.CLAMP);

        mPath.arcTo(mSideCircleRect, 90 + getDegreeFromRadian(tetha2), - getDegreeFromRadian(tetha2));

        mPath.lineTo(bounds.right, 0);
        mPath.lineTo(bounds.right, bounds.bottom);
        mPath.lineTo(0, bounds.bottom);
        mPath.lineTo(0, 0);
        mPath.close();
    }

    private float getDegreeFromRadian(double angelInRadian) {
        return (float)(angelInRadian * 180 / Math.PI);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        int backgroundColor = mBackgroundPaint.getColor();

        Rect bounds = getBounds();

        canvas.save();
        canvas.translate(mShadowInfo[3], mShadowInfo[4]);

        mBackgroundPaint.setColor(mShadowColor);
        canvas.drawPath(mPath, mBackgroundPaint);
        mBackgroundPaint.setColor(backgroundColor);

        mShadowPaint.setShader(mLinearGradient);
        canvas.drawRect(bounds.left, bounds.top - mShadow1Height, (float) (mPosition - mSideCircleXOffsetFromCenterCircle + 1), bounds.top , mShadowPaint);
//        mLinearShadow.draw(getBounds().left, getBounds().top, -90, (float) (mPosition - mSideCircleXOffsetFromCenterCircle + 1), canvas);

        mShadowPaint.setShader(mLeftCircleRadialGradient);
        canvas.drawArc(mLeftSideCircleBounds, 90, mLeftSideCircleSweepAngle, true, mShadowPaint);

        mShadowPaint.setShader(mCenterCircleRadialGradient);
        canvas.drawArc(mCenterCircleShadow1Bounds, mCenterCircleStartAngle, mCenterCircleSweepAngle, true, mShadowPaint);

        mShadowPaint.setShader(mRightCircleRadialGradient);
        mLeftSideCircleBounds.offset((float) (2 * mSideCircleXOffsetFromCenterCircle), 0);
        canvas.drawArc(mLeftSideCircleBounds, 90 - mLeftSideCircleSweepAngle, mLeftSideCircleSweepAngle, true, mShadowPaint);
        mLeftSideCircleBounds.offset((float) -(2 * mSideCircleXOffsetFromCenterCircle), 0);

        mShadowPaint.setShader(mLinearGradient);
        canvas.drawRect((float) (mPosition + mSideCircleXOffsetFromCenterCircle - 1), bounds.top - mShadow1Height, bounds.right, bounds.top , mShadowPaint);

        canvas.restore();

//        canvas.save();
//        canvas.translate(mShadowInfo[3], mShadowInfo[4]);
//
//        mBackgroundPaint.setColor(mShadowColor);
//        canvas.drawPath(mPath, mBackgroundPaint);
//        mBackgroundPaint.setColor(backgroundColor);
//
//        mShadowPaint.setShader(mLinearGradient);
//        canvas.drawRect(bounds.left, bounds.top, (float) (mPosition - mSideCircleXOffsetFromCenterCircle + 1), bounds.top - mShadow2Height, mShadowPaint);
////        mLinearShadow.draw(bounds.left, bounds.top, -90, (float) (mPosition - mSideCircleXOffsetFromCenterCircle + 1), canvas);
//
//        mShadowPaint.setShader(mLeftCircleRadialGradient);
//        canvas.drawArc(mLeftSideCircleBounds, 90, mLeftSideCircleSweepAngle, true, mShadowPaint);
//
//        mShadowPaint.setShader(mCenterCircleRadialGradient);
//        canvas.drawArc(mCenterCircleShadow2Bounds, mCenterCircleStartAngle, mCenterCircleSweepAngle, true, mShadowPaint);
//
//        mShadowPaint.setShader(mRightCircleRadialGradient2);
//        mLeftSideCircleBounds.offset((float) (2 * mSideCircleXOffsetFromCenterCircle), 0);
//        canvas.drawArc(mLeftSideCircleBounds, 90 - mLeftSideCircleSweepAngle, mLeftSideCircleSweepAngle, true, mShadowPaint);
//        mLeftSideCircleBounds.offset((float) -(2 * mSideCircleXOffsetFromCenterCircle), 0);
//
//        mShadowPaint.setShader(mLinearGradient);
//        canvas.drawRect((float) (mPosition + mSideCircleXOffsetFromCenterCircle - 1), bounds.top, bounds.right, bounds.top - mShadow2Height, mShadowPaint);
//
//        canvas.restore();

        canvas.drawPath(mPath, mBackgroundPaint);
    }

    @Override
    public void setAlpha(int alpha) { }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) { }

    @Override
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }

}
