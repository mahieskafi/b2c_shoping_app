package com.srp.eways.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.FloatRange;

import com.srp.eways.R;

public class ShadowLayout extends FrameLayout {

    private final static float DEFAULT_SHADOW_RADIUS = 30.0F;
    private final static float DEFAULT_SHADOW_DISTANCE = 15.0F;
    private final static float DEFAULT_SHADOW_ANGLE = 45.0F;
    private final static int DEFAULT_SHADOW_COLOR = Color.DKGRAY;

    private final static int MAX_ALPHA = 255;
    private final static float MAX_ANGLE = 360.0F;
    private final static float MIN_RADIUS = 0.1F;
    private final static float MIN_ANGLE = 0.0F;

    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG) {
        {
            setDither(true);
            setFilterBitmap(true);
        }
    };

    private Bitmap mBitmap;
    private final Canvas mCanvas = new Canvas();

    private final Rect mBounds = new Rect();
    private boolean mInvalidateShadow = true;

    private boolean mIsShadowed;

    private int mShadowColor;
    private int mShadowAlpha;
    private float mShadowRadius;
    private float mShadowDistance;
    private float mShadowAngle;
    private float mShadowDx;
    private float mShadowDy;

    public ShadowLayout(final Context context) {
        this(context, null);
    }

    public ShadowLayout(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShadowLayout(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setWillNotDraw(false);
        setLayerType(LAYER_TYPE_HARDWARE, mPaint);

        final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ShadowLayout);
        try {
            setIsShadowed(typedArray.getBoolean(R.styleable.ShadowLayout_sl_shadowed, true));
            setShadowRadius(typedArray.getDimension(R.styleable.ShadowLayout_sl_shadow_radius, DEFAULT_SHADOW_RADIUS));
            setShadowDistance(typedArray.getDimension(R.styleable.ShadowLayout_sl_shadow_distance, DEFAULT_SHADOW_DISTANCE));
            setShadowAngle(typedArray.getInteger(R.styleable.ShadowLayout_sl_shadow_angle, (int) DEFAULT_SHADOW_ANGLE));
            setShadowColor(typedArray.getColor(R.styleable.ShadowLayout_sl_shadow_color, DEFAULT_SHADOW_COLOR));
        } finally {
            typedArray.recycle();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mBitmap != null) {
            mBitmap.recycle();
            mBitmap = null;
        }
    }

    public boolean isShadowed() {
        return mIsShadowed;
    }

    public void setIsShadowed(final boolean isShadowed) {
        mIsShadowed = isShadowed;
        postInvalidate();
    }

    public float getShadowDistance() {
        return mShadowDistance;
    }

    public void setShadowDistance(final float shadowDistance) {
        mShadowDistance = shadowDistance;
        resetShadow();
    }

    public float getShadowAngle() {
        return mShadowAngle;
    }

    public void setShadowAngle(@FloatRange(from = MIN_ANGLE, to = MAX_ANGLE) final float shadowAngle) {
        mShadowAngle = Math.max(MIN_ANGLE, Math.min(shadowAngle, MAX_ANGLE));
        resetShadow();
    }

    public float getShadowRadius() {
        return mShadowRadius;
    }

    public void setShadowRadius(final float shadowRadius) {
        mShadowRadius = Math.max(MIN_RADIUS, shadowRadius);

        if (isInEditMode()) return;
        mPaint.setMaskFilter(new BlurMaskFilter(mShadowRadius, BlurMaskFilter.Blur.NORMAL));
        resetShadow();
    }

    public int getShadowColor() {
        return mShadowColor;
    }

    public void setShadowColor(final int shadowColor) {
        mShadowColor = shadowColor;
        mShadowAlpha = Color.alpha(shadowColor);

        resetShadow();
    }

    public float getShadowDx() {
        return mShadowDx;
    }

    public float getShadowDy() {
        return mShadowDy;
    }

    private void resetShadow() {
        mShadowDx = (float) ((mShadowDistance) * Math.cos(mShadowAngle / 180.0F * Math.PI));
        mShadowDy = (float) ((mShadowDistance) * Math.sin(mShadowAngle / 180.0F * Math.PI));

        final int padding = (int) (mShadowDistance + mShadowRadius);
        setPadding(padding, padding, padding, padding);
        requestLayout();
    }

    private int adjustShadowAlpha(final boolean adjust) {
        return Color.argb(
                adjust ? MAX_ALPHA : mShadowAlpha,
                Color.red(mShadowColor),
                Color.green(mShadowColor),
                Color.blue(mShadowColor)
        );
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mBounds.set(0, 0, getMeasuredWidth(), getMeasuredHeight());
    }

    @Override
    public void requestLayout() {
        mInvalidateShadow = true;
        super.requestLayout();
    }

    @Override
    protected void dispatchDraw(final Canvas canvas) {
        if (mIsShadowed) {
            if (mInvalidateShadow) {
                if (mBounds.width() != 0 && mBounds.height() != 0) {
                    mBitmap = Bitmap.createBitmap(mBounds.width(), mBounds.height(), Bitmap.Config.ARGB_8888);
                    mCanvas.setBitmap(mBitmap);

                    mInvalidateShadow = false;
                    super.dispatchDraw(mCanvas);

                    final Bitmap extractedAlpha = mBitmap.extractAlpha();
                    mCanvas.drawColor(0, PorterDuff.Mode.CLEAR);

                    mPaint.setColor(adjustShadowAlpha(false));
                    mCanvas.drawBitmap(extractedAlpha, mShadowDx, mShadowDy, mPaint);

                    extractedAlpha.recycle();
                } else {
                    mBitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.RGB_565);
                }
            }

            mPaint.setColor(adjustShadowAlpha(true));
            if (mCanvas != null && mBitmap != null && !mBitmap.isRecycled()) {
                canvas.drawBitmap(mBitmap, 0.0F, 0.0F, mPaint);
            }
        }

        super.dispatchDraw(canvas);
    }
}
