package com.srp.ewayspanel.ui.view.store.product;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.srp.ewayspanel.R;
import com.srp.ewayspanel.di.DI;

import ir.abmyapp.androidsdk.IABResources;

public class ProductDetailAddToBasketButton extends FrameLayout {

    private View mButtonAddToBasket;
    private View mLoadingView;
    private View mImageAdd;

    private int mHeight;
    private int mImageAddSize;
    private int mLoadingSize;

    private Drawable mBackground;


    public ProductDetailAddToBasketButton(@NonNull Context context) {
        super(context);

        initialize(context, null, 0);
    }

    public ProductDetailAddToBasketButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        initialize(context, attrs, 0);
    }

    public ProductDetailAddToBasketButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initialize(context, attrs, defStyleAttr);
    }

    private void initialize(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        LayoutInflater.from(context).inflate(R.layout.productdetail_button_addtobasket, this, true);
        setWillNotDraw(false);

        IABResources abResources = DI.getABResources();

//        setBackground(abResources.getDrawable(R.drawable.productdetail_button_add_to_basket_background));

        mBackground = DI.getABResources().getDrawable(R.drawable.productdetail_button_add_to_basket_background_disabled);

        mButtonAddToBasket = findViewById(R.id.button);
        mLoadingView = findViewById(R.id.loading);
        mImageAdd = findViewById(R.id.image_add);

        mButtonAddToBasket.setVisibility(VISIBLE);
        mLoadingView.setVisibility(INVISIBLE);


        mHeight = abResources.getDimenPixelSize(R.dimen.productdetail_button_addtobasket_height);
        mImageAddSize = abResources.getDimenPixelSize(R.dimen.productdetail_button_addtobasket_icon_size);
        mLoadingSize = abResources.getDimenPixelSize(R.dimen.productdetail_button_addtobasket_loading_size);
    }


    public void setLoading(boolean showLoading) {
        mLoadingView.setVisibility(showLoading ? VISIBLE : GONE);
        mButtonAddToBasket.setVisibility(showLoading ? GONE: VISIBLE);
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mButtonAddToBasket.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(mHeight, MeasureSpec.EXACTLY));
        mLoadingView.measure(MeasureSpec.makeMeasureSpec(mLoadingSize, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(mLoadingSize, MeasureSpec.EXACTLY));
        mImageAdd.measure(MeasureSpec.makeMeasureSpec(mImageAddSize, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(mImageAddSize, MeasureSpec.EXACTLY));

        setMeasuredDimension(widthMeasureSpec, MeasureSpec.makeMeasureSpec(mHeight, MeasureSpec.EXACTLY));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        mBackground.setBounds(0, 0, getWidth(), getHeight());

        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mBackground.draw(canvas);

        super.onDraw(canvas);
    }


    public void setEnable(boolean enable){
        if(enable){
            mBackground = DI.getABResources().getDrawable(R.drawable.productdetail_button_add_to_basket_background);
        }
        else{
            mBackground = DI.getABResources().getDrawable(R.drawable.productdetail_button_add_to_basket_background_disabled);
        }
        setEnabled(enable);
        requestLayout();
    }
}
