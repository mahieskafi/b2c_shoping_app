package com.srp.ewayspanel.ui.view.store;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import androidx.core.content.res.ResourcesCompat;

import com.srp.ewayspanel.R;
import com.srp.ewayspanel.di.DI;

import ir.abmyapp.androidsdk.IABResources;

public class StoreFilterItemView extends View {

    private int mPaddingRight;
    private int mPaddingTop;
    private int mPaddingBottom;

    private int mIconWidth;
    private int mIconHeight;

    private int mTitleMarginRight;

    private int mWidth;
    private int mHeight;

    private Paint mPaintTitle;

    private Drawable mIconDrawable;

    private String mTitle;
    private String mSubTitle;

    private Rect mTitleBounds = new Rect();
    private Rect mSubTitleBounds = new Rect();

    private Rect mStringSizeHolderRect = new Rect();

    private float mTitleDeltaY;
    private float mSubTitleDeltaY;
    private float mTitleWidth;

    public StoreFilterItemView(Context context) {
        super(context);

        initialize(context, null, 0);
    }

    public StoreFilterItemView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initialize(context, attrs, 0);
    }

    public StoreFilterItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initialize(context, attrs, defStyleAttr);
    }

    private void initialize(Context context, AttributeSet attrs, int defStyleAttr) {
        IABResources abResources = DI.getABResources();

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int widthPixel = displayMetrics.widthPixels;
        float ratio = (float) widthPixel / (360 * displayMetrics.density);

        mWidth = (int) (abResources.getDimenPixelSize(R.dimen.storefilter_width) * ratio);
        mHeight = (int) (abResources.getDimen(R.dimen.storefilter_height));

        mPaddingRight = (int) (abResources.getDimenPixelSize(R.dimen.storefilter_paddingright) * ratio);
        mPaddingTop = (int) (abResources.getDimen(R.dimen.storefilter_paddingtop));
        mPaddingBottom = abResources.getDimenPixelSize(R.dimen.storefilter_paddingbottom);

        mIconWidth = (int) (abResources.getDimen(R.dimen.storefilter_iconwidth));
        mIconHeight = (int) (abResources.getDimen(R.dimen.storefilter_iconheight));

        mTitleMarginRight = (int) (abResources.getDimen(R.dimen.storefilter_title_marginright));


        mPaintTitle = new Paint();
        mPaintTitle.setAntiAlias(true);
        mPaintTitle.setStyle(Paint.Style.FILL);
        mPaintTitle.setTextSize(abResources.getDimenPixelSize(R.dimen.storefilter_title_size));
        mPaintTitle.setColor(abResources.getColor(R.color.storefilter_title_color));
        mPaintTitle.setTypeface(ResourcesCompat.getFont(context, R.font.iran_yekan));


        Paint.FontMetrics fm = mPaintTitle.getFontMetrics();
        mTitleDeltaY = (fm.descent + fm.ascent) / 2;

    }

    public void setTitle(String title) {
        mTitle = title;
        mTitleWidth=mPaintTitle.measureText(mTitle);
        invalidate();
    }

    public void setSubTitle(String subTitle) {
        mSubTitle = subTitle;

        invalidate();
        requestLayout();
    }

    public void setIcon(Drawable icon) {
        mIconDrawable = icon;
        requestLayout();
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(MeasureSpec.makeMeasureSpec((int) (mTitleWidth+mIconWidth+mTitleMarginRight), MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(mHeight, MeasureSpec.EXACTLY));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int width = getWidth();
        int height = getHeight();

        mIconDrawable.setBounds(
                width - mIconWidth,
                height / 2 - mIconHeight / 2,
                width,
                height / 2 + mIconHeight / 2);

        Rect iconBounds = mIconDrawable.getBounds();

        mPaintTitle.getTextBounds(mTitle, 0, mTitle.length(), mStringSizeHolderRect);

        mTitleBounds.set(
                iconBounds.left - mTitleMarginRight - mStringSizeHolderRect.width(),
                height / 2 - mStringSizeHolderRect.height() / 2   ,
                iconBounds.left - mTitleMarginRight,
                height / 2 + mStringSizeHolderRect.height() / 2);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        mIconDrawable.draw(canvas);

        canvas.drawText(mTitle, mTitleBounds.left, mTitleBounds.centerY() - mTitleDeltaY, mPaintTitle);

    }

}
