package com.srp.ewayspanel.ui.view.store.product;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.srp.ewayspanel.R;
import com.srp.ewayspanel.di.DI;

import ir.abmyapp.androidsdk.IABResources;

public class ProductItemSliderView extends CardView implements View.OnClickListener {

    public interface ProductItemSliderListener {

        void onProductImageClicked(String imageUrl);

    }

    private int mPaddingTop;
    private int mImageHeight;
    private int mPaddingBottom;
    private int mEmptySpace;

    private ImageView mImageProduct;

    private String mUrl = null;
    private ProductItemSliderListener mListener;

    public ProductItemSliderView(Context context) {
        super(context);

        initialize(context, null, 0);
    }

    public ProductItemSliderView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initialize(context, attrs, 0);
    }

    public ProductItemSliderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initialize(context, attrs, defStyleAttr);
    }

    private void initialize(Context context, AttributeSet attrs, int defStyleAttr) {
        LayoutInflater.from(context).inflate(R.layout.item_productslider, this, true);

        IABResources abResources = DI.getABResources();

        setRadius(abResources.getDimen(R.dimen.productitemslider_raduis));
        setBackground(abResources.getDrawable(R.drawable.address_detail_card_backgound));
        setCardElevation(abResources.getDimen(R.dimen.productitemslider_elevation));


        mPaddingTop = abResources.getDimenPixelSize(R.dimen.productitemslider_padding_top);
        mImageHeight = abResources.getDimenPixelSize(R.dimen.productitemslider_image_height);
        mPaddingBottom = abResources.getDimenPixelSize(R.dimen.productitemslider_padding_bottom);
        mEmptySpace=abResources.getDimenPixelSize(R.dimen.productitemslider_empty_space);

        mImageProduct = findViewById(R.id.image_product);
        mImageProduct.setOnClickListener(this);
    }

    public void setListener(ProductItemSliderListener listener) {
        mListener = listener;
    }

    public void setImageUrl(String imageUrl) {
        mUrl = imageUrl;

        Glide.with(getContext())
                .load(imageUrl)
                .into(mImageProduct);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = mPaddingTop + mImageHeight + mPaddingBottom + mEmptySpace;

        setMeasuredDimension(widthMeasureSpec, MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int width = getWidth();
        int height = getHeight();

        int imageLeft = (width - mImageHeight) / 2;
        int imageRight = imageLeft + mImageHeight;

        mImageProduct.measure(MeasureSpec.makeMeasureSpec(mImageHeight, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(mImageHeight, MeasureSpec.EXACTLY));
        mImageProduct.layout(imageLeft, mPaddingTop, imageRight, height - mPaddingBottom - mEmptySpace);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_product:
                if (mListener != null) {
                    mListener.onProductImageClicked(mUrl);
                }
                break;
        }
    }

}
