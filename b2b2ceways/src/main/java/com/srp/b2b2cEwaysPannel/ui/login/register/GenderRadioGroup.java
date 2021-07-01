package com.srp.b2b2cEwaysPannel.ui.login.register;

import android.content.Context;
import android.util.AttributeSet;

import com.srp.eways.ui.view.OneColumnRadioGroup;
import com.srp.eways.ui.view.RadioButtonView;

public class GenderRadioGroup extends OneColumnRadioGroup {
    public GenderRadioGroup(Context context) {
        super(context);

        initialize(context, null, 0);
    }

    public GenderRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);

        initialize(context, attrs, 0);
    }

    public GenderRadioGroup(Context context, AttributeSet attrs, int defStyleAttr) {
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
