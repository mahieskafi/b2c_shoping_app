package com.srp.ewayspanel.ui.view.store.product;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.res.ResourcesCompat;

import com.srp.ewayspanel.R;
import com.srp.ewayspanel.di.DI;

public class DiscountTextView extends AppCompatTextView {

    private Paint mLinePaint;

    public DiscountTextView(Context context) {
        super(context);

        initialize(context, null, 0);
    }

    public DiscountTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initialize(context, attrs, 0);
    }

    public DiscountTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initialize(context, attrs, defStyleAttr);
    }

    private void initialize(Context context, AttributeSet attrs, int defStyleAttr) {
        int discountColor = DI.getABResources().getColor(R.color.discount_text_default_color);
        int discountLineColor = DI.getABResources().getColor(R.color.discount_line_color);

        setTextColor(discountColor);
        setTextSize(TypedValue.COMPLEX_UNIT_PX, DI.getABResources().getDimenPixelSize(R.dimen.discount_text_default_textsize));
        setTypeface(ResourcesCompat.getFont(context, R.font.iran_yekan));

        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setColor(discountLineColor);
        mLinePaint.setStrokeWidth(DI.getABResources().getDimen(R.dimen.discount_text_strokewidth));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawLine(0, getHeight(), getWidth(), 0, mLinePaint);
    }

}
