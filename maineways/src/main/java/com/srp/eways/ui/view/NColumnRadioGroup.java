package com.srp.eways.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.srp.eways.R;
import com.srp.eways.di.DIMain;
import com.srp.eways.ui.charge.buy.RadioOptionModel;

import java.util.ArrayList;
import java.util.List;

import ir.abmyapp.androidsdk.IABResources;

public class NColumnRadioGroup extends ViewGroup implements RadioButtonView.RadioButtonSelectionListener {

    public interface RadioGroupListener {

        void onItemSelected(int index, RadioOptionModel data);

        void onItemRemoved(int index, RadioOptionModel removedData);

    }

    private List<RadioOptionModel> mData = new ArrayList<>();

    protected int mRowCount;
    private int mColumnCount = 1;

    protected int[] mChildHeight;

    private RadioGroupListener mListener;

    private boolean mIsInOneColumnRadioGroup = false;

    public NColumnRadioGroup(Context context) {
        super(context);

        initialize(context, null, 0);
    }

    public NColumnRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);

        initialize(context, attrs, 0);
    }

    public NColumnRadioGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initialize(context, attrs, defStyleAttr);
    }

    private void initialize(Context context, AttributeSet attrs, int defStyleAttr) {

    }

    public void setOnItemSelectedListener(RadioGroupListener listener) {
        mListener = listener;
    }

    public void setData(List<RadioOptionModel> children) {
        if (children == null) {
            return;
        }

        //sync data state
        mData.clear();
        mData.addAll(new ArrayList<>(children));

        //sync view state
        removeAllViews();

        for (int i = 1; i <= mData.size(); ++i) {
            RadioOptionModel radioOptionModel = mData.get(i - 1);

            RadioButtonView radioButtonItem = new RadioButtonView(getContext());
            radioButtonItem.setData(radioOptionModel);
            radioButtonItem.setSelected(false);
            radioButtonItem.setOnRadioButtonSelectionListener(this);

//            radioButtonItem.setLayoutParams(getRadioButtonViewLayoutParams());

            addView(radioButtonItem);
        }

        mChildHeight = new int[mData.size()];

        setColumnCount(mColumnCount);

        requestLayout();
    }

    private LinearLayout.LayoutParams getRadioButtonViewLayoutParams() {
        if (mIsInOneColumnRadioGroup) {
            return new LinearLayout.LayoutParams(MeasureSpec.makeMeasureSpec(0, MeasureSpec.EXACTLY), LayoutParams.WRAP_CONTENT);
        } else {
            return new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        }
    }

    public void setIsInOneColumnRadioGroup(boolean isInOneColumnRadioGroup) {
        mIsInOneColumnRadioGroup = isInOneColumnRadioGroup;
    }

    public RadioOptionModel getOptionAt(int index) {
        return mData.get(index);
    }

    public void setColumnCount(int columnCount) {
        calculateNumOfRows(columnCount);

        mColumnCount = columnCount;

        requestLayout();
    }

    public void setSelectedRadioButton(int position) {
        int childCount = getChildCount();

        for (int i = 0; i < childCount; ++i) {
            RadioButtonView radioButtonView = (RadioButtonView) getChildAt(i);

            if (position == i) {
                radioButtonView.setSelected(true);
            } else {
                radioButtonView.setSelected(false);
            }
        }
    }

    private void calculateNumOfRows(int numOfCols) {
        int numOfItems = mData.size();
        int numOfRows = numOfItems / numOfCols;

        if (numOfRows * numOfCols < numOfItems) {
            numOfRows++;
        }
        mRowCount = numOfRows;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int childWidth = (width - getPaddingLeft() - getPaddingRight()) / mColumnCount;
        int height = 0;
        int finalHeight = 0;

        for (int i = 0; i < mData.size(); ++i) {
            View child = getChildAt(i);

            child.measure(MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

            mChildHeight[i] = child.getMeasuredHeight();

            height += mChildHeight[i];
        }


        if (mColumnCount > 1 && mChildHeight != null) {
            finalHeight += mChildHeight[0] * mRowCount;
        } else {
            finalHeight += height;
        }

        finalHeight += getPaddingTop() + getPaddingBottom();

        setMeasuredDimension(widthMeasureSpec, MeasureSpec.makeMeasureSpec(finalHeight, MeasureSpec.EXACTLY));

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int width = getWidth();
        int height = getHeight();

        int childWidth = (width - getPaddingLeft() - getPaddingRight()) / mColumnCount;

        int childTop = getPaddingTop();

        int childCount = getChildCount();
        int col = mColumnCount - 1;
        int row = -1;
        for (int i = 0; i < childCount; ++i) {
            row++;
            if (row > mRowCount - 1) {
                row = 0;

                col--;
                if (col < 0) {
                    col = mColumnCount - 1;
                }
            }

            int childRight = width - getPaddingRight() - col * childWidth;
            int childLeft = col * childWidth + getPaddingLeft();

            if (mColumnCount > 1) {
                childTop = row * mChildHeight[0] + getPaddingTop();
            } else {
                if (i > 0) {
                    childTop += mChildHeight[i - 1];
                }
            }

            RadioOptionModel currentChild = mData.get(i);
            RadioOptionModel nextChild = i + 1 >= mData.size() ? null : mData.get(i + 1);

            if (currentChild.isUserInputChoice) {
                if (nextChild == null) {
                    getChildAt(i).setBackground(getLastUserChoiceDrawableBackground());
                } else {
                    if (!nextChild.isUserInputChoice) {
                        getChildAt(i).setBackground(getLastUserChoiceDrawableBackground());
                    }
                }
            }

            View child = getChildAt(i);
            child.measure(MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(mChildHeight[i], MeasureSpec.EXACTLY));
//            child.layout(childRight - child.getMeasuredWidth(), childTop, childRight, childTop + child.getMeasuredHeight());
            child.layout(childLeft, childTop, childLeft + child.getMeasuredWidth(), childTop + child.getMeasuredHeight());
        }
    }

    protected Drawable getLastUserChoiceDrawableBackground() {
        LastUserInputChoiceBackgroundDrawable drawable = new LastUserInputChoiceBackgroundDrawable();

        return drawable;
    }

    @Override
    public void onRadioButtonSelected(RadioButtonView selectedRadioButton) {
        if (mListener != null) {
            int selectedIndex = getItemIndex(selectedRadioButton);

            mListener.onItemSelected(selectedIndex, mData.get(selectedIndex));
        }
    }

    @Override
    public void onRemoveRadioButtonClicked(RadioButtonView radioButtonView) {
        int index = getItemIndex(radioButtonView);
        mListener.onItemRemoved(index, mData.get(index));
    }

    protected int getItemIndex(RadioButtonView radioButton) {
        int childCount = getChildCount();

        for (int i = 0; i < childCount; ++i) {
            RadioButtonView child = (RadioButtonView) getChildAt(i);
            if (child.getData() == (radioButton.getData())) {
                return i;
            }
        }

        return -1;
    }

    public void setEnable(boolean isEnable) {

        setEnabled(isEnable);
        setClickable(isEnable);

        int childCount = getChildCount();

        IABResources abResources = DIMain.getABResources();

        if (isEnable) {
            for (int i = 0; i < childCount; ++i) {
                RadioButtonView child = (RadioButtonView) getChildAt(i);
                child.setEnable(true);
                child.setTextColor(getResources().getColor(R.color.radiobuttonview_title_normal_color));
//                child.setSelectedDrawable(abResources.getDrawable(R.drawable.ic_radioitem_selected).mutate());
//                child.setUnSelectedDrawable(abResources.getDrawable(R.drawable.ic_radioitem_unselected).mutate());
//                child.invalidate();
                //make child enabled
            }
        } else {
            for (int i = 0; i < childCount; ++i) {
                RadioButtonView child = (RadioButtonView) getChildAt(i);
                child.setEnable(false);
                child.setTextColor(getResources().getColor(R.color.radiobuttonview_title_normal_disabled_color));
//                child.setSelectedDrawable(abResources.getDrawable(R.drawable.ic_radioitem_selected_disabled).mutate());
//                child.setUnSelectedDrawable(abResources.getDrawable(R.drawable.ic_radioitem_unselected_disabled).mutate());
//                child.invalidate();
                //make child disabled
            }
        }

    }

    private static class LastUserInputChoiceBackgroundDrawable extends Drawable {

        private Paint mPaint;

        private Rect mLineBounds = new Rect();

        private int mDividerLineHeight;

        private LastUserInputChoiceBackgroundDrawable() {
            IABResources abResources = DIMain.getABResources();

            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setColor(abResources.getColor(R.color.multicolumnradiogroup_lastuserinputchoice_line_color));

            mDividerLineHeight = abResources.getDimenPixelSize(R.dimen.n_column_radiogroup_lastuserchoice_background_line_height);
        }

        @Override
        protected void onBoundsChange(Rect bounds) {
            mLineBounds.set(bounds.left, bounds.bottom - mDividerLineHeight, bounds.right, bounds.bottom);
        }

        @Override
        public void draw(@NonNull Canvas canvas) {
            canvas.drawRect(mLineBounds, mPaint);
        }

        @Override
        public void setAlpha(int alpha) {

        }

        @Override
        public void setColorFilter(@Nullable ColorFilter colorFilter) {

        }

        @Override
        public int getOpacity() {
            return PixelFormat.OPAQUE;
        }

    }

    protected List<RadioOptionModel> getData() {
        return mData;
    }

    protected int[] getChildHeight() {
        if (mChildHeight == null) {
            mChildHeight = new int[getChildCount()];
        }
        return mChildHeight;
    }

}
