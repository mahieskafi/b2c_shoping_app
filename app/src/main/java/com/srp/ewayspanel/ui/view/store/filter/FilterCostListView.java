package com.srp.ewayspanel.ui.view.store.filter;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.res.ResourcesCompat;

import com.srp.ewayspanel.R;
import com.srp.ewayspanel.di.DI;
import com.srp.eways.util.Utils;

import java.util.ArrayList;
import java.util.List;

import ir.abmyapp.androidsdk.IABResources;


/**
 * Created by Eskafi on 11/11/2019.
 */
public class FilterCostListView extends ViewGroup {

    public interface ItemClickListener {
        void onItemClicked(CostItem costItem);
    }

    private List<CostItem> costItems = new ArrayList<>();

    private LinearLayout mVerticalLinear;

    private int mTextSidePadding;
    private int mTextTopPadding;

    private int mTextBottomMargin;

    private ItemClickListener mListener;

    public FilterCostListView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public FilterCostListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public FilterCostListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }


    private void init(Context context, AttributeSet attrs, int defStyleAttr) {

        IABResources abResources = DI.getABResources();

        mVerticalLinear = new LinearLayout(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        mVerticalLinear.setLayoutParams(params);
        mVerticalLinear.setOrientation(LinearLayout.VERTICAL);

        mTextSidePadding = abResources.getDimenPixelSize(R.dimen.filter_header_title_cost_items_padding_right_left);
        mTextTopPadding = abResources.getDimenPixelSize(R.dimen.filter_header_title_cost_items_padding_top_bottom);

        mTextBottomMargin = abResources.getDimenPixelSize(R.dimen.filter_header_title_cost_item_margin_bottom);

        addView(mVerticalLinear);
    }

    public void setData(List<CostItem> items) {
        costItems = new ArrayList<>();
        costItems.addAll(items);

        bindData();
    }

    private void bindData() {
        mVerticalLinear.removeAllViews();

        for (int i = 0; i < 2; i++) {
            LinearLayout horizontalLinear = createLinear();

            for (int j = i * 3; j < (i * 3) + 3; j++) {

                CostItem costItem = costItems.get(j);

                horizontalLinear.addView(getItemView(costItem, j));
            }
            mVerticalLinear.addView(horizontalLinear);
        }
    }


    private AppCompatTextView getItemView(final CostItem costItem, final int position) {
        AppCompatTextView textView = new AppCompatTextView(getContext());

        IABResources abResources = DI.getABResources();

        int sideMargin = abResources.getDimenPixelSize(R.dimen.filter_header_title_cost_item_margin_sides);

        textView.setTypeface(ResourcesCompat.getFont(getContext(), R.font.iran_sans_regular));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, abResources.getDimenPixelSize(R.dimen.filter_header_title_cost_items_text_size));
        textView.setText(costItem.getTitle());
        textView.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT);
        layoutParams.weight = 1;
        layoutParams.setMargins(sideMargin, 0, sideMargin, 0);
        textView.setLayoutParams(layoutParams);

        textView.setPadding(mTextSidePadding, mTextTopPadding, mTextSidePadding, mTextTopPadding);

        if (costItem.isSelected) {
            textView.setTextColor(abResources.getColor(R.color.filter_header_title_cost_items_selected_text_color));
            textView.setBackground(abResources.getDrawable(R.drawable.button_filter_header_cost_item_selected_background));
        } else {
            textView.setTextColor(abResources.getColor(R.color.filter_header_title_cost_items_default_text_color));
            textView.setBackground(abResources.getDrawable(R.drawable.button_filter_header_cost_item_default_background));
        }

        textView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelectedItem(position);
            }
        });

        return textView;
    }

    public void setSelectedItem(int position) {
        for (int i = 0; i < costItems.size(); i++) {
            if (i == position) {
                if (costItems.get(i).isSelected) {
                    costItems.get(i).setSelected(false);
                } else {
                    costItems.get(i).setSelected(true);
                }
            } else {
                costItems.get(i).setSelected(false);
            }
        }

//        costItems.get(position).setSelected(true);
        bindData();
        mListener.onItemClicked(costItems.get(position));

    }

    private LinearLayout createLinear() {
        LinearLayout horizontalLinear;
        horizontalLinear = new LinearLayout(getContext());

        LinearLayout.LayoutParams paramsHorizontalLinear = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        paramsHorizontalLinear.gravity = Gravity.CENTER_VERTICAL;
        paramsHorizontalLinear.setMargins(0, 0, 0, mTextBottomMargin);
        horizontalLinear.setLayoutParams(paramsHorizontalLinear);

        horizontalLinear.setOrientation(LinearLayout.HORIZONTAL);
        horizontalLinear.setWeightSum(3);

        return horizontalLinear;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mVerticalLinear.getChildAt(0).measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

        setMeasuredDimension(widthMeasureSpec, MeasureSpec.makeMeasureSpec(
                (mVerticalLinear.getChildAt(0).getMeasuredHeight()) * 2 + 2 * mTextBottomMargin, MeasureSpec.EXACTLY));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int width = getWidth();
        int height = getHeight();
        mVerticalLinear.measure(
                MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
        mVerticalLinear.layout(
                0,
                0,
                width,
                mVerticalLinear.getMeasuredHeight());

    }

    public void setListener(ItemClickListener listener) {
        mListener = listener;
    }

    public static class CostItem {
        private String maxText;
        private String minText;
        private long max;
        private long min;
        private boolean isSelected;

        public CostItem(long max, long min, String maxText, String minText, boolean isSelected) {
            this.maxText = maxText;
            this.minText = minText;
            this.max = max;
            this.min = min;
            this.isSelected = isSelected;
        }

        public long getMax() {
            return max;
        }

        public long getMin() {
            return min;
        }

        public String getTitle() {
            return Utils.toPersianNumber(minText) + " - " + Utils.toPersianNumber(maxText);
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }


    }
}
