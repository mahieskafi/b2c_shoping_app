package com.srp.ewayspanel.ui.view.store;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import com.srp.ewayspanel.R;
import com.srp.ewayspanel.di.DI;

import ir.abmyapp.androidsdk.IABResources;


public class StoreFilterHeaderDivider extends View {

    private GradientDrawable mTitleDivider;


    public StoreFilterHeaderDivider(Context context) {
        super(context);

        initialize(context, null, 0);
    }

    public StoreFilterHeaderDivider(Context context, AttributeSet attrs) {
        super(context, attrs);

        initialize(context, attrs, 0);
    }

    public StoreFilterHeaderDivider(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initialize(context, attrs, defStyleAttr);
    }

    private void initialize(Context context, AttributeSet attrs, int defStyleAttr) {
        IABResources abResources = DI.getABResources();

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int widthPixel = displayMetrics.widthPixels;


        mTitleDivider = new GradientDrawable();
        mTitleDivider.setColor(abResources.getColor(R.color.storefilter_iconbackground));
    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int height = getHeight();

        mTitleDivider.setBounds(
                 0,
                height / 2 - 30,
                4,
                height / 2 + 30);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mTitleDivider.draw(canvas);
    }

}
