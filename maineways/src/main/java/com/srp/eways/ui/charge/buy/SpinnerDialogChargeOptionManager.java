package com.srp.eways.ui.charge.buy;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.srp.eways.di.DIMain;
import com.srp.eways.model.charge.result.TopupTypeItem;
import com.srp.eways.ui.charge.ChargeSelectionState;
import com.srp.eways.ui.charge.model.IChargeChoice;
import com.srp.eways.ui.charge.model.IChargeOption;
import com.srp.eways.ui.charge.model.TopupUserInputChoice;
import com.srp.eways.ui.view.charge.b.SpinnerDialog;
import com.srp.eways.util.Utils;

import java.util.ArrayList;
import java.util.List;

import ir.abmyapp.androidsdk.IABResources;

public class SpinnerDialogChargeOptionManager implements ChargeOptionManager {

    private static final int NO_ITEM_SELECTED_INDEX = -1;

    private int mViewType;

    private int mPendingSelectedIndex = NO_ITEM_SELECTED_INDEX;

    public SpinnerDialogChargeOptionManager(int viewType) {
        mViewType = viewType;
    }

    @Override
    public View createChargeOption(final Activity activity, final int level, List<ChargeSelectionState.ChargeLevelInfo> selectionState, final List options, final ChargeOptionsCallback callback) {
        final SpinnerDialog spinnerDialog = new SpinnerDialog(activity, mViewType);

        final List<RadioOptionModel> radioOptions = getRadioOptions(options);
        spinnerDialog.setRadioOptions(radioOptions);

        spinnerDialog.setListener(new SpinnerDialog.RadioGroupContentViewListener() {
            @Override
            public void onAddButtonClicked(long amount) {
                callback.onAddUserInputChoiceClicked(amount);
            }

            @Override
            public double getTax(long amount) {
                if (options.get(0) instanceof IChargeChoice) {
                    return ((IChargeChoice) options.get(0)).getTax();
                }
                return 0;
            }

            @Override
            public double getCoef() {
                if (options.get(0) instanceof IChargeChoice) {
                    return ((IChargeChoice)options.get(0)).getCoef();
                }

                return 0;
            }

            @Override
            public void onClickableSpinnerClicked() {
                //we show dialog within SpinnerLikeChargeOptionsViewWrapper class
            }

            @Override
            public void onItemSelected(int index, RadioOptionModel data) {
                spinnerDialog.setRadioOptionSelected(index);

                mPendingSelectedIndex = index;

                RadioOptionModel radioOptionModel = spinnerDialog.getOptionAt(index);

                if (radioOptionModel.option instanceof IChargeChoice) {
                    IChargeChoice chargeChoice = ((IChargeChoice) radioOptionModel.option);
                    if (chargeChoice.getDisplayPaidAmount() != null && !chargeChoice.getDisplayPaidAmount().equals(chargeChoice.getDisplaySum())) {
                        spinnerDialog.setDisplayPayAmount(radioOptionModel.option instanceof TopupUserInputChoice.Choice ? "" + chargeChoice.getDisplayPaidAmount() : "" + chargeChoice.getDisplayChargeAmount());
                    }
                }
            }

            @Override
            public void onItemRemoved(int index, RadioOptionModel removedData) {
                IChargeChoice userInputChoice = (IChargeChoice) removedData.option;
                callback.onRemoveUserInputChoiceClicked(userInputChoice.getAmount());
            }

            @Override
            public void onConfirmClicked() {
                if (mPendingSelectedIndex != NO_ITEM_SELECTED_INDEX) {
                    callback.onChargeOptionSelected(level, mPendingSelectedIndex, options.get(mPendingSelectedIndex));

                    mPendingSelectedIndex = NO_ITEM_SELECTED_INDEX;
                }
            }

            @Override
            public void onCancelClicked() {
                mPendingSelectedIndex = NO_ITEM_SELECTED_INDEX;
            }
        });

        setSpinnerDialogAttributes(spinnerDialog, options);

        return spinnerDialog;
    }

    @Override
    public void setSelectedOption(View view, List options, int selectedIndex, Object... args) {
        SpinnerDialog spinnerDialog = (SpinnerDialog) view;

        final List<RadioOptionModel> radioOptions = getRadioOptions(options);
        spinnerDialog.setRadioOptions(radioOptions);
        spinnerDialog.setRadioOptionSelected(selectedIndex);

//        setSpinnerDialogAttributes(spinnerDialog, options);

        mPendingSelectedIndex = -1;

        if (selectedIndex >= 0) {
            Object selectedOption = options.get(selectedIndex);

            String selectedOptionName = selectedOption instanceof IChargeChoice ? ((IChargeChoice) selectedOption).getName() : ((IChargeOption) selectedOption).getName();
//        String selectionText = String.format(MainDI.getABResources().getString(R.string.rial_amount), selectedOptionName);

            spinnerDialog.getSpinnerLikeOptionsView().setText(selectedOptionName);
        } else {
            spinnerDialog.getSpinnerLikeOptionsView().setHint(getHintForChargeLevelView(options));
        }
    }

    private void setSpinnerDialogAttributes(SpinnerDialog spinnerDialog, List options) {
        Drawable hintIcon = getHintIconForChargeLevelView(options);
        String hint = getHintForChargeLevelView(options);

        Drawable titleIcon = getTitleIconForChargeLevelView(options);
        String title = getTitleForChargeLevelView(options);

        //set levelView properties
        spinnerDialog.getSpinnerLikeOptionsView().setIcon(hintIcon);
        spinnerDialog.getSpinnerLikeOptionsView().setHint(hint);

        //set dialog properties
        spinnerDialog.setDialogIcon(titleIcon);
        spinnerDialog.setDialogTitle(title);

        if (options.get(0) instanceof IChargeChoice && ((IChargeChoice) options.get(0)).getProductType() == TopupTypeItem.TYPE_CHARGE) {
            spinnerDialog.showRial(true);
        } else {
            spinnerDialog.showRial(false);
        }
    }

    private List<RadioOptionModel> getRadioOptions(List options) {
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

    private String getHintForChargeLevelView(List options) {
        IABResources abResources = DIMain.getABResources();

        Object firstOption = options.get(0);

        int hintResId;

        if (firstOption instanceof IChargeChoice) {
            hintResId = ((IChargeChoice) firstOption).getChoiceTypeHintResId();
        } else {
            hintResId = ((IChargeOption) firstOption).getOptionTypeHintResId();
        }

        return abResources.getString(hintResId);
    }

    private String getTitleForChargeLevelView(List options) {
        IABResources abResources = DIMain.getABResources();

        Object firstOption = options.get(0);

        int hintResId;

        if (firstOption instanceof IChargeChoice) {
            hintResId = ((IChargeChoice) firstOption).getChoiceTypeTitleResId();
        } else {
            hintResId = ((IChargeOption) firstOption).getOptionTypeTitleResId();
        }

        return abResources.getString(hintResId);
    }

    private Drawable getHintIconForChargeLevelView(List options) {
        IABResources abResources = DIMain.getABResources();

        Object firstOption = options.get(0);

        int iconResId;

        if (firstOption instanceof IChargeChoice) {
            iconResId = ((IChargeChoice) firstOption).getChoiceTypeHintIconResId();
        } else {
            iconResId = ((IChargeOption) firstOption).getOptionTypeHintIconResId();
        }

        return abResources.getDrawable(iconResId);
    }

    private Drawable getTitleIconForChargeLevelView(List options) {
        IABResources abResources = DIMain.getABResources();

        Object firstOption = options.get(0);

        int iconResId;

        if (firstOption instanceof IChargeChoice) {
            iconResId = ((IChargeChoice) firstOption).getChoiceTypeTitleIconResId();
        } else {
            iconResId = ((IChargeOption) firstOption).getOptionTypeTitleIconResId();
        }

        return abResources.getDrawable(iconResId);
    }


}
