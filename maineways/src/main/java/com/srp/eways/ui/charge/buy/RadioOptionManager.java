package com.srp.eways.ui.charge.buy;

import android.app.Activity;
import android.view.View;

import com.srp.eways.R;
import com.srp.eways.di.DIMain;
import com.srp.eways.ui.charge.ChargeSelectionState;
import com.srp.eways.ui.view.NColumnRadioGroup;
import com.srp.eways.ui.view.ViewUtils;

import java.util.List;

public class RadioOptionManager implements ChargeOptionManager {

    private int mColumnCount;

    public RadioOptionManager(int columnCount) {
        mColumnCount = columnCount;
    }

    @Override
    public View createChargeOption(Activity activity, final int level, List<ChargeSelectionState.ChargeLevelInfo> selectionState, final List options, final ChargeOptionsCallback callback) {
        final NColumnRadioGroup nColumnRadioGroup = new NColumnRadioGroup(activity);
        nColumnRadioGroup.setColumnCount(mColumnCount);

         List<RadioOptionModel> radioModels = AddableRadioOptionManager.getRadioOptions(options);

        nColumnRadioGroup.setData(radioModels);

        nColumnRadioGroup.setOnItemSelectedListener(new NColumnRadioGroup.RadioGroupListener() {

            @Override
            public void onItemSelected(int index, RadioOptionModel data) {
                callback.onChargeOptionSelected(level, index, data.option);
            }

            @Override
            public void onItemRemoved(int index, RadioOptionModel removedData) {
            }

        });

        ViewUtils.setCardBackground(nColumnRadioGroup);

        int itemPaddingVertical = DIMain.getABResources().getDimenPixelSize(R.dimen.n_column_radiogroup_buttonitem_paddingvertical);

        int paddingLeft = nColumnRadioGroup.getPaddingLeft();
        int paddingRight = nColumnRadioGroup.getPaddingRight();
        int paddingTop = nColumnRadioGroup.getPaddingTop() - itemPaddingVertical;
        int paddingBottom = nColumnRadioGroup.getPaddingBottom() - itemPaddingVertical;

        nColumnRadioGroup.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);

        return nColumnRadioGroup;
    }

    @Override
    public void setSelectedOption(View view, List options, int selectedIndex, Object... args) {
        ((NColumnRadioGroup) view).setData(AddableRadioOptionManager.getRadioOptions(options));

        ((NColumnRadioGroup) view).setSelectedRadioButton(selectedIndex);
    }

}
