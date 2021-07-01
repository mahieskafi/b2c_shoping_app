package com.srp.ewayspanel.ui.view.store.sort;

import android.content.Context;
import android.util.AttributeSet;

import com.srp.eways.ui.view.OneColumnRadioGroup;
import com.srp.eways.ui.view.RadioButtonView;

/**
 * Created by Eskafi on 11/12/2019.
 */
public class SortRadioGroup extends OneColumnRadioGroup {
    public SortRadioGroup(Context context) {
        super(context);

        initialize(context, null, 0);
    }

    public SortRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);

        initialize(context, attrs, 0);
    }

    public SortRadioGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initialize(context, attrs, defStyleAttr);
    }

    private void initialize(Context context, AttributeSet attrs, int defStyleAttr) {
    }

    @Override
    public void onRadioButtonSelected(RadioButtonView selectedRadioButton) {
        setSelectedRadioButton(getItemIndex(selectedRadioButton));

        super.onRadioButtonSelected(selectedRadioButton);
    }
}
