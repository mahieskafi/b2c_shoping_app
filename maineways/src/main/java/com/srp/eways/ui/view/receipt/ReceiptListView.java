package com.srp.eways.ui.view.receipt;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.annotation.ColorInt;

import com.srp.eways.R;
import com.srp.eways.di.DIMain;
import com.srp.eways.util.Utils;

import java.util.ArrayList;
import java.util.List;

import ir.abmyapp.androidsdk.IABResources;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by Eskafi on 9/3/2019.
 */
public class ReceiptListView extends ViewGroup {

    private LinearLayout mLinearView;
    private ViewGroup mItemView;
    private int mItemsMargin;
    private int mMarginSides = 0;

    private int mTextColor;
    private int mValueTextColor;
    private int mValueDescriptionTextColor = 0;

    private int mItemViewLayout;

    private float mTextSize;
    private float mValueTextSize;
    private float mValueDescriptionTextSize;

    private Typeface mTextTypeface;
    private Typeface mValueTextTypeface;
    private Typeface mValueDescriptionTextTypeface;

    public ReceiptListView(Context context) {
        super(context);

        initialize(context, null, 0);
    }

    public ReceiptListView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initialize(context, attrs, 0);
    }

    public ReceiptListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initialize(context, attrs, defStyleAttr);
    }

    private void initialize(Context context, AttributeSet attrs, int defStyleAttr) {

        IABResources abResources = DIMain.getABResources();

        mItemsMargin = abResources.getDimenPixelSize(R.dimen.receipt_standard_items_margin);

        mLinearView = new LinearLayout(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        mLinearView.setLayoutParams(params);
        mLinearView.setOrientation(LinearLayout.VERTICAL);

    }

    public void setLayoutGravity(int gravity)
    {
        mLinearView.setGravity(gravity);
    }

    public void setTextColor(@ColorInt int textColor) {
        mTextColor = textColor;

        if (mValueTextColor == 0) {
            mValueTextColor = mTextColor;
            mValueDescriptionTextColor = mTextColor;
        }
    }

    public void setValueTextColor(@ColorInt int textColor) {
        mValueTextColor = textColor;

        mValueDescriptionTextColor = textColor;
    }

    public void setTextSize(float textSize) {
        mTextSize = textSize;

        if (mValueTextSize == 0) {
            mValueTextSize = mTextSize;
        }

        if (mValueDescriptionTextSize == 0) {
            mValueDescriptionTextSize = mTextSize;
        }
    }

    public void setValueTextSize(float valueTextSize) {
        mValueTextSize = valueTextSize;
    }

    public void setValueDescrptionTextSize(float valueTextSize) {
        mValueDescriptionTextSize = valueTextSize;
    }

    public void setTextTypeface(Typeface textTypeface) {
        mTextTypeface = textTypeface;

        if (mValueTextTypeface == null) {
            mValueTextTypeface = mTextTypeface;
        }

        if (mValueDescriptionTextTypeface == null) {
            mValueDescriptionTextTypeface = mTextTypeface;
        }
    }

    public void setValueDescriptionTextTypeface(Typeface textTypeface) {

        mValueDescriptionTextTypeface = textTypeface;
    }

    public void setValueTextTypeface(Typeface textTypeface) {
        mValueTextTypeface = textTypeface;
    }

    public void setValueTextColorAtPosition(int textColor, int position) {
        IABResources abResources = DIMain.getABResources();

        if (mLinearView.getChildCount() != 0 && mLinearView.getChildCount() >= position) {
            ViewGroup view = (ViewGroup) mLinearView.getChildAt(position);
            TextView textViewValue = view.findViewById(R.id.txt_value);

            textViewValue.setTextColor(abResources.getColor(textColor));
        }
    }

    public void setReceiptItem(ReceiptItem receiptItem)
    {
        ArrayList<ReceiptItem> receiptItems = new ArrayList<>();
        receiptItems.add(receiptItem);

        setReceiptItem(receiptItems);

    }

    public void setReceiptItem(List<ReceiptItem> receiptItem) {

        mLinearView.removeAllViews();

        for (ReceiptItem item : receiptItem) {
            mItemView = getReceiptItemView();
            TextView mTextViewTitle = mItemView.findViewById(R.id.txt_title);
            ImageView mImageViewIcon = mItemView.findViewById(R.id.iv_icon);
            TextView mTextViewValue = mItemView.findViewById(R.id.txt_value);
            TextView mTextViewValueDescription = mItemView.findViewById(R.id.value_description);

            if (mTextSize != 0) {
                mTextViewTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
                mTextViewValue.setTextSize(TypedValue.COMPLEX_UNIT_PX, mValueTextSize);
            }
            if (mTextColor != 0) {
                mTextViewTitle.setTextColor(mTextColor);
                mTextViewValue.setTextColor(mValueTextColor);
            }

            if (mTextTypeface != null) {
                mTextViewTitle.setTypeface(mTextTypeface);
                mTextViewValue.setTypeface(mValueTextTypeface);
                mTextViewValueDescription.setTypeface(mValueTextTypeface);
            }

            mTextViewTitle.setText(item.getTitle());
            mTextViewValue.setText(Utils.toPersianNumber(item.getValue()));
            if (item.getIcon() != null) {
                mImageViewIcon.setVisibility(VISIBLE);
                mImageViewIcon.setImageDrawable(item.getIcon());
            }

            if (item.getValueDescription() != null) {
                mTextViewValueDescription.setVisibility(VISIBLE);
                mTextViewValueDescription.setTextSize(TypedValue.COMPLEX_UNIT_PX, mValueDescriptionTextSize);
                mTextViewValueDescription.setTextColor(mValueDescriptionTextColor != 0 ? mValueDescriptionTextColor : mValueTextColor);
                mTextViewValueDescription.setText(item.getValueDescription());
            }

            mLinearView.addView(mItemView);
        }

        removeAllViews();
        addView(mLinearView);
        requestLayout();
    }

    public void setItemsMargin(int itemsMargin) {
        mItemsMargin = itemsMargin;
    }

    public void setMarginSides(int margin) {
        mMarginSides = margin;
    }

    public void setItemViewLayoutId(int itemViewLayout) {
        mItemViewLayout = itemViewLayout;
    }

    private ViewGroup getReceiptItemView() {

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);

        ViewGroup itemView;

        if (mItemViewLayout != 0) {
            itemView = (ViewGroup) inflater.inflate(mItemViewLayout, null);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            params.setMargins(0, mItemsMargin, 0, mItemsMargin);
            itemView.setLayoutParams(params);
        } else {
            itemView = (ViewGroup) inflater.inflate(R.layout.item_receipt, null);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            params.setMargins(0, mItemsMargin, 0, mItemsMargin);
            itemView.setLayoutParams(params);
        }

        return itemView;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

//        int itemHeight = 0;
        int totalHeight = 0;

//        if (mLinearView.getChildCount() > 0) {
//            mLinearView.getChildAt(0).measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
//            itemHeight = mLinearView.getChildAt(0).getMeasuredHeight();
//        }

        for(int i = 0; i < mLinearView.getChildCount(); i++){
            mLinearView.getChildAt(i).measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

            totalHeight += mLinearView.getChildAt(i).getMeasuredHeight();
        }
//        setMeasuredDimension(MeasureSpec.makeMeasureSpec(widthMeasureSpec, MeasureSpec.EXACTLY),
//                MeasureSpec.makeMeasureSpec(((itemHeight + (mItemsMargin * 2)) * mLinearView.getChildCount()) + (2 * mItemsMargin), MeasureSpec.EXACTLY));


        setMeasuredDimension(MeasureSpec.makeMeasureSpec(widthMeasureSpec, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(totalHeight + ((mItemsMargin * 2) * mLinearView.getChildCount()) + (2 * mItemsMargin), MeasureSpec.EXACTLY));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int width = getWidth();
        int height = getHeight();
        mLinearView.measure(
                MeasureSpec.makeMeasureSpec(width - (2 * mMarginSides), MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
        mLinearView.layout(
                mMarginSides,
                mItemsMargin,
                width - mMarginSides,
                mLinearView.getMeasuredHeight() - mItemsMargin);

    }


}
