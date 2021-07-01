package com.srp.eways.ui.charge.buy;

import android.app.Activity;
import android.view.View;

import com.srp.eways.ui.charge.ChargeSelectionState;
import com.srp.eways.ui.charge.model.IOperator;

import java.util.List;

public interface ChargeOptionManager {

    interface ChargeOptionsCallback {

        void onUserPhoneBookClicked();

        void onMobilePhoneBookClicked();

        void onPhoneNumberChanged(String phoneNumber);

        void onRemovePhoneNumberClicked();

        void onOperatorSelected(IOperator operator);

        void onOperatorLoadAnimationEnded();

        void onChargeOptionSelected(int level, int selectedIndex, Object option);

        void onAddUserInputChoiceClicked(long amount);

        void onChangeUserInputChoice(long amount);

        void onRemoveUserInputChoiceClicked(long amount);

        void onTopInquiriesVisibility(boolean visibility);

    }

    View createChargeOption(Activity activity, int level, List<ChargeSelectionState.ChargeLevelInfo> selectionState, List options, ChargeOptionsCallback callback);

    void setSelectedOption(View view, List options, int selectedIndex, Object... args);

}
