package com.srp.ewayspanel.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

import com.srp.ewayspanel.R;
import com.srp.ewayspanel.di.DI;

import ir.abmyapp.androidsdk.ABResources;
import ir.abmyapp.androidsdk.IABResources;

/**
 * Created by ErfanG on 4/7/2020.
 */
public class OffPriceTextView extends androidx.appcompat.widget.AppCompatTextView {

    private Paint mLinePaint;

    public OffPriceTextView(Context context) {
        super(context);
        initilizePaint();
    }

    public OffPriceTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initilizePaint();
    }

    public OffPriceTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initilizePaint();
    }

    private void initilizePaint(){

        IABResources resources = DI.getABResources();
        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStyle(Paint.Style.FILL);
        mLinePaint.setTextSize(resources.getDimenPixelSize(R.dimen.off_price_text_view_line_size));
        mLinePaint.setColor(resources.getColor(R.color.off_price_text_view_line_color));
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        canvas.drawLine(getWidth(), getHeight() * 0.3f, 0, getHeight() * 0.7f, mLinePaint);
    }
}
