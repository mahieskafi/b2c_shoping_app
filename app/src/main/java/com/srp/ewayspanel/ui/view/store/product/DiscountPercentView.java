package com.srp.ewayspanel.ui.view.store.product;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.AttributeSet;
import android.view.View;

import androidx.core.content.res.ResourcesCompat;

import com.srp.ewayspanel.R;
import com.srp.ewayspanel.di.DI;
import com.srp.eways.util.Utils;

import ir.abmyapp.androidsdk.IABResources;

public class DiscountPercentView extends View {

    private Paint mTextPaint;
    private float mDeltaY;

    private String mOffPercentString;

    private ShapeDrawable mBackground;

    private Rect mStringBoundsHolder = new Rect();

    public DiscountPercentView(Context context) {
        super(context);

        initialize(context, null, 0);
    }

    public DiscountPercentView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initialize(context, attrs, 0);
    }

    public DiscountPercentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initialize(context, attrs, defStyleAttr);
    }

    private void initialize(Context context, AttributeSet attrs, int defStyleAttr) {
        IABResources abResources = DI.getABResources();

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTypeface(ResourcesCompat.getFont(context, R.font.iran_sans_medium));
        mTextPaint.setTextSize(abResources.getDimen(R.dimen.discount_text_percentage_textsize));
        mTextPaint.setColor(abResources.getColor(R.color.discount_text_percentage_text));

        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        mDeltaY = (fontMetrics.ascent + fontMetrics.descent) / 2;

        int backgroundColor = abResources.getColor(R.color.discount_text_percentage_background);
        float backgroundRadius = abResources.getDimen(R.dimen.discount_text_percentage_background_radius);

        RoundRectShape shape = new RoundRectShape(new float[]{0, 0, backgroundRadius, backgroundRadius, backgroundRadius, backgroundRadius, 0, 0}, null, null);

        mBackground = new ShapeDrawable();
        mBackground.setColorFilter(backgroundColor, PorterDuff.Mode.SRC_IN);
        mBackground.setShape(shape);
    }

    public void setOffPercent(int offPercent) {
        mOffPercentString = String.format(DI.getABResources().getString(R.string.percent), Utils.toPersianNumber(offPercent));

        mTextPaint.getTextBounds(mOffPercentString, 0, mOffPercentString.length(), mStringBoundsHolder);

        invalidate();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int width =getWidth();
        int height = getHeight();

        mBackground.setBounds(0, 0, width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mBackground.draw(canvas);

        canvas.drawText(mOffPercentString, mStringBoundsHolder.left, mStringBoundsHolder.centerY() - mDeltaY, mTextPaint);
    }

}
