package com.srp.ewayspanel.ui.view.store.filter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import com.srp.ewayspanel.R;
import com.srp.ewayspanel.di.DI;
import com.srp.eways.ui.charge.buy.RadioOptionModel;
import com.srp.eways.ui.view.NColumnRadioGroup;
import com.srp.eways.ui.view.RadioButtonView;

import java.util.ArrayList;
import java.util.List;

import ir.abmyapp.androidsdk.IABResources;

/**
 * Created by Eskafi on 11/12/2019.
 */
public class TwoColumnFilterRadioGroup extends NColumnRadioGroup {

    private int mItemTextColor;
    private Drawable mItemSelectedDrawable;
    private Drawable mItemUnselectedDrawable;

    public TwoColumnFilterRadioGroup(Context context) {
        super(context);

        initialize(context, null, 0);
    }

    public TwoColumnFilterRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);

        initialize(context, attrs, 0);
    }

    public TwoColumnFilterRadioGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initialize(context, attrs, defStyleAttr);
    }

    private void initialize(Context context, AttributeSet attrs, int defStyleAttr) {

        IABResources abResources = DI.getABResources();
        mItemTextColor = abResources.getColor(R.color.filter_header_title_cost_title_text_color);

        mItemSelectedDrawable = abResources.getDrawable(R.drawable.ic_radio_gorup_filter_selected);
        mItemUnselectedDrawable = abResources.getDrawable(R.drawable.ic_radio_group_filter_unselected);
    }

    @Override
    public void setData(List<RadioOptionModel> children) {
        if (children == null) {
            return;
        }

        //sync data state
        getData().clear();
        getData().addAll(new ArrayList<>(children));

        //sync view state
        removeAllViews();

        for (int i = 1; i <= getData().size(); ++i) {
            RadioOptionModel radioOptionModel = getData().get(i - 1);

            RadioButtonView radioButtonItem = new RadioButtonView(getContext());
            radioButtonItem.setData(radioOptionModel);
            radioButtonItem.setSelected(!(Boolean) radioOptionModel.option);
            radioButtonItem.setTextColor(mItemTextColor);
            radioButtonItem.setSelectedDrawable(mItemSelectedDrawable);
            radioButtonItem.setUnSelectedDrawable(mItemUnselectedDrawable);
            radioButtonItem.setOnRadioButtonSelectionListener(this);

            addView(radioButtonItem);
        }

        getChildHeight();

        setColumnCount(3);

        requestLayout();
    }

    @Override
    public void onRadioButtonSelected(RadioButtonView selectedRadioButton) {
        setSelectedRadioButton(getItemIndex(selectedRadioButton));

        super.onRadioButtonSelected(selectedRadioButton);
    }
}
