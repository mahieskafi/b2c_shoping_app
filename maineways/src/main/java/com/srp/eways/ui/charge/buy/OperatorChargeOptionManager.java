package com.srp.eways.ui.charge.buy;

import android.app.Activity;
import android.view.View;

import com.srp.eways.model.phonebook.UserPhoneBook;
import com.srp.eways.ui.charge.ChargeSelectionState;
import com.srp.eways.ui.charge.model.IOperator;
import com.srp.eways.ui.view.charge.PhoneAndOperatorsView;
import com.srp.eways.util.Utils;

import java.util.List;

public class OperatorChargeOptionManager implements ChargeOptionManager {

    @Override
    public View createChargeOption(final Activity activity, int level, List<ChargeSelectionState.ChargeLevelInfo> selectionState, List options, final ChargeOptionsCallback callback) {
        final PhoneAndOperatorsView phoneAndOperatorsView = new PhoneAndOperatorsView(activity);

        phoneAndOperatorsView.setListener(new PhoneAndOperatorsView.PhoneAndOperatorsViewListener() {

            @Override
            public void onUserPhoneBookClicked() {
                callback.onUserPhoneBookClicked();
            }

            @Override
            public void onMobilePhoneBookClicked() {
                callback.onMobilePhoneBookClicked();
            }

            @Override
            public void onPhoneNumberChanged(String phoneNumber) {
                if (phoneNumber.length() == 11) {
                    Utils.hideKeyboard(activity);

                    callback.onTopInquiriesVisibility(false);
                    callback.onPhoneNumberChanged(phoneNumber);
                }
                else{
                    callback.onPhoneNumberChanged("");
                }
            }

            @Override
            public void onOperatorLoadAnimationEnded() {
                callback.onOperatorLoadAnimationEnded();
            }

            @Override
            public void onOperatorSelected(IOperator operator) {
                callback.onOperatorSelected(operator);
            }

            @Override
            public void onRemovePhoneNumberClicked() {
                callback.onTopInquiriesVisibility(true);
                callback.onRemovePhoneNumberClicked();
            }

            @Override
            public void onEditTextGotFocus() {

            }

        });

        return phoneAndOperatorsView;
    }

    @Override
    public void setSelectedOption(View view, List options, int selectedIndex, Object... args) {
        PhoneAndOperatorsView phoneAndOperatorsView = ((PhoneAndOperatorsView) view);

        boolean animate = false;
        if(args.length > 0) {
            animate = (Boolean) args[0];
        }

        UserPhoneBook contactInfo = null;
        if(args.length > 1) {
            contactInfo = (UserPhoneBook) args[1];
        }

        if (selectedIndex != -1 && contactInfo != null) {
            phoneAndOperatorsView.setContactInfo(contactInfo);

            phoneAndOperatorsView.setOperator((IOperator) options.get(selectedIndex), animate);
        }
        else {
            phoneAndOperatorsView.setContactInfo(null);
            phoneAndOperatorsView.setOperator(null, animate);
        }
    }

}
