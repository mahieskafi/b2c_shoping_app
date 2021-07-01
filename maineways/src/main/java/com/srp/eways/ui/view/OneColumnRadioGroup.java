package com.srp.eways.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.srp.eways.ui.charge.buy.RadioOptionModel;

/**
 * Created by Eskafi on 10/21/2019.
 */
public class OneColumnRadioGroup extends NColumnRadioGroup {

    public OneColumnRadioGroup(Context context) {
        super(context);

        initialize(context, null, 0);
    }

    public OneColumnRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);

        initialize(context, attrs, 0);
    }

    public OneColumnRadioGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initialize(context, attrs, defStyleAttr);
    }

    private void initialize(Context context, AttributeSet attrs, int defStyleAttr) {
        setColumnCount(1);

        setIsInOneColumnRadioGroup(false);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width;
        int height = 0;

        if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.UNSPECIFIED) {
            int maximumRequiredWidth = 0;

            for (int i = 0; i < getChildCount(); ++i) {
                RadioButtonView child = (RadioButtonView) getChildAt(i);

                child.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

                maximumRequiredWidth = Math.max(child.getMeasuredWidth(), maximumRequiredWidth);
            }

            width = maximumRequiredWidth;
        } else {
            width = MeasureSpec.getSize(widthMeasureSpec);
        }

        for (int i = 0; i < getChildCount(); ++i) {
            RadioButtonView child = (RadioButtonView) getChildAt(i);

            child.measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

            mChildHeight[i] = child.getMeasuredHeight();

            height += mChildHeight[i];
        }

//        View child0 = getChildAt(0);
//        child0.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
//        mChildHeight[] = child0.getMeasuredHeight();

        height += getPaddingTop() + getPaddingBottom();

        setMeasuredDimension(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int width = getWidth();
        int height = getHeight();

        int childWidth = width - getPaddingLeft() - getPaddingRight();

        int childRight = width - getPaddingRight();

        int childTop = getPaddingTop();

        int childCount = getChildCount();
        int row = -1;
        for (int i = 0; i < childCount; ++i) {
            row++;

            if (i > 0) {
                childTop += mChildHeight[i - 1];
            }

            RadioOptionModel currentChild = getData().get(i);
            RadioOptionModel nextChild = i + 1 >= getData().size() ? null : getData().get(i + 1);

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
            child.layout(childRight - childWidth, childTop, childRight, childTop + child.getMeasuredHeight());
        }
    }

}
