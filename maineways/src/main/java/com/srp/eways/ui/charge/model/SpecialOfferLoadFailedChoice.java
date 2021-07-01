package com.srp.eways.ui.charge.model;

import android.content.Context;

import com.srp.eways.R;

public class SpecialOfferLoadFailedChoice implements IChargeChoice {

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getDescription(Context context) {
        return null;
    }

    @Override
    public long getProductId() {
        return 0;
    }

    @Override
    public String getPackageId() {
        return "0";
    }

    @Override
    public long getAmount() {
        return 0;
    }

    @Override
    public long getPaidAmount() {
        return 0;
    }

    @Override
    public double getTax() {
        return 0;
    }

    @Override
    public double getCoef() {
        return 0;
    }

    @Override
    public String getDisplayChargeAmount() {
        return null;
    }

    @Override
    public String getDisplayTaxAmount() {
        return null;
    }

    @Override
    public String getDisplaySum() {
        return null;
    }

    @Override
    public String getDisplayPaidAmount() {
        return null;
    }

    @Override
    public int getProductType() {
        return 0;
    }

    @Override
    public String toJsonString() {
        return null;
    }

    @Override
    public boolean isUserInputChoice() {
        return false;
    }

    @Override
    public int getChoiceTypeHintIconResId() {
        return R.drawable.ic_irancell_specialoffer;
    }

    @Override
    public int getChoiceTypeTitleIconResId() {
        return R.drawable.ic_irancell_specialoffer;
    }

    @Override
    public int getChoiceTypeTitleResId() {
        return R.string.specialoffers_type_title;
    }

    @Override
    public int getChoiceTypeHintResId() {
        return R.string.specialoffers_type_hint;
    }

    @Override
    public int getViewType() {
        return VIEWTYPE_IRANCELL_SPECIALOFFER_FAILED;
    }

    @Override
    public boolean isIrancell() {
        return false;
    }

    @Override
    public boolean isAddedToUserChoices() {
        return true;
    }

}
