package com.srp.eways.ui.charge.buy;

import android.app.Activity;
import android.view.View;

import com.srp.eways.R;
import com.srp.eways.di.DIMain;
import com.srp.eways.ui.charge.ChargeSelectionState;
import com.srp.eways.ui.charge.model.IChargeChoice;
import com.srp.eways.ui.charge.model.IChargeOption;
import com.srp.eways.ui.view.UserInputChoiceRadioGroup;
import com.srp.eways.ui.view.ViewUtils;
import com.srp.eways.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class AddableRadioOptionManager implements ChargeOptionManager {

    private int mColumnCount;

    public AddableRadioOptionManager(int columnCount) {
        mColumnCount = columnCount;
    }

    @Override
    public View createChargeOption(Activity activity, final int level, List<ChargeSelectionState.ChargeLevelInfo> selectionState, final List options, final ChargeOptionsCallback callback) {
        final UserInputChoiceRadioGroup userInputChoiceRadioGroup = new UserInputChoiceRadioGroup(activity);
        userInputChoiceRadioGroup.setColumnCount(mColumnCount);

        userInputChoiceRadioGroup.setOptions(getRadioOptions(options));

        userInputChoiceRadioGroup.setListener(new UserInputChoiceRadioGroup.UserInputChoiceRadioGroupListener() {
            @Override
            public void onItemSelected(int index, RadioOptionModel data) {
                callback.onChargeOptionSelected(level, index, data.option);
            }

            @Override
            public void onAddUserInputChoiceClicked(long amount) {
                callback.onAddUserInputChoiceClicked(amount);
            }

            @Override
            public void onChangeUserInputChoice(long amount) {
                callback.onChangeUserInputChoice(amount);
            }

            @Override
            public void onItemRemoved(int index, RadioOptionModel removedData) {
                IChargeChoice userInputChoice = (IChargeChoice) removedData.option;
                callback.onRemoveUserInputChoiceClicked(userInputChoice.getAmount());
            }
        });

        ViewUtils.setCardBackground(userInputChoiceRadioGroup);

        int itemPaddingVertical = DIMain.getABResources().getDimenPixelSize(R.dimen.n_column_radiogroup_buttonitem_paddingvertical);

        int paddingLeft = userInputChoiceRadioGroup.getPaddingLeft();
        int paddingRight = userInputChoiceRadioGroup.getPaddingRight();
        int paddingTop = userInputChoiceRadioGroup.getPaddingTop() - itemPaddingVertical;
        int paddingBottom = userInputChoiceRadioGroup.getPaddingBottom() - itemPaddingVertical;

        userInputChoiceRadioGroup.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);

        return userInputChoiceRadioGroup;
    }

    @Override
    public void setSelectedOption(View view, List options, int selectedIndex, Object... args) {
        ((UserInputChoiceRadioGroup) view).setOptions(getRadioOptions(options));

        ((UserInputChoiceRadioGroup) view).setSelectedRadioButton(selectedIndex);
    }

    public static List<RadioOptionModel> getRadioOptions(List options) {
        List<RadioOptionModel> radioOptions = new ArrayList<>();

        if (options.get(0) instanceof IChargeOption) {
            for (int i = 0; i < options.size(); ++i) {
                IChargeOption chargeOption = (IChargeOption) options.get(i);
                String title = Utils.toPersianNumber(chargeOption.getName());
                radioOptions.add(new RadioOptionModel(title, false, chargeOption));
            }
        } else {
            for (int i = 0; i < options.size(); ++i) {
                IChargeChoice chargeChoice = (IChargeChoice) options.get(i);
                String title = Utils.toPersianNumber(chargeChoice.getName());
                radioOptions.add(new RadioOptionModel(title, chargeChoice.isUserInputChoice(), chargeChoice));
            }
        }

        return radioOptions;
    }

}
