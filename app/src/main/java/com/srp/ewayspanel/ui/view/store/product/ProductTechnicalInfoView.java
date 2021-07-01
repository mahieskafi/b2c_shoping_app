package com.srp.ewayspanel.ui.view.store.product;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.srp.ewayspanel.R;
import com.srp.ewayspanel.di.DI;
import com.srp.eways.ui.view.ViewUtils;

import java.util.ArrayList;
import java.util.List;

import ir.abmyapp.androidsdk.IABResources;

public class ProductTechnicalInfoView extends LinearLayout {

    private int mPaddingTop;
    private int mPaddingBottom;

    private int mItemMarginTop;

    private List<ProductTechnicalInfoItemView.ProductTechnicalItem> mData;

    public ProductTechnicalInfoView(Context context) {
        super(context);

        initialize(context, null, 0);
    }

    public ProductTechnicalInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initialize(context, attrs, 0);
    }

    public ProductTechnicalInfoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initialize(context, attrs, defStyleAttr);
    }

    private void initialize(Context context, AttributeSet attrs, int defStyleAttr) {
        setOrientation(VERTICAL);

        IABResources abResources = DI.getABResources();

//        int paddingLeft = abResources.getDimenPixelSize(R.dimen.producttechnicalinfo_padding_left);
        mPaddingTop = abResources.getDimenPixelSize(R.dimen.producttechnicalinfo_padding_top);
//        int paddingRight = abResources.getDimenPixelSize(R.dimen.producttechnicalinfo_padding_right);
        mPaddingBottom = abResources.getDimenPixelSize(R.dimen.producttechnicalinfo_padding_bottom);

        setPadding(0, mPaddingTop, 0, mPaddingBottom);

        mItemMarginTop = abResources.getDimenPixelSize(R.dimen.producttechnicalinfo_item_margintop);

//        ViewUtils.setCardBackground(this);
    }

    public void setData(List<ProductTechnicalInfoItemView.ProductTechnicalItem> data) {
        if (data == null || data.size() == 0) {
            mData = null;

            return;
        }

        mData = new ArrayList<>(data);

        sortData();

        if (getChildCount() > 0) {
            removeAllViews();
        }

        for (int i = 0; i < mData.size(); ++i) {
            ProductTechnicalInfoItemView itemView = new ProductTechnicalInfoItemView(getContext());

            if (i%2 ==1){
                itemView.setData(mData.get(i),true);
            }else {
                itemView.setData(mData.get(i),false);
            }

            addView(itemView);
        }

        requestLayout();
    }

    private void sortData() {
        for (int i = 0; i < mData.size() - 1; ++i) {
            for (int j = i + 1 ; j < mData.size(); ++j) {
                if (mData.get(i).value.length() > mData.get(j).value.length()) {
                    swap(i, j);
                }
            }
        }
    }

    private void swap(int i, int j) {
        ProductTechnicalInfoItemView.ProductTechnicalItem temp = mData.get(i);

        mData.set(i, mData.get(j));
        mData.set(j, temp);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mData == null) {
            setMeasuredDimension(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.EXACTLY));

            return;
        }

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = mPaddingTop;

        for (int i = 0; i < mData.size(); ++i) {
            View child = getChildAt(i);

            if (i != 0) {
                height += mItemMarginTop;

                MarginLayoutParams layoutParams = (MarginLayoutParams) child.getLayoutParams();
                layoutParams.topMargin = mItemMarginTop;

                child.setLayoutParams(layoutParams);
            }

            child.measure(MeasureSpec.makeMeasureSpec(width - getPaddingLeft() - getPaddingRight(), MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

            height += child.getMeasuredHeight();
        }

        height += mPaddingBottom;

        setMeasuredDimension(widthMeasureSpec, MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
    }

}
