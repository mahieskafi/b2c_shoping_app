package com.srp.eways.ui.charge.model;

import android.content.Context;

public interface IChargeChoice extends IChargeOptionViewType {

    String getName();

    String getDescription(Context context);

    long getProductId();

    String getPackageId();

    long getAmount();

    long getPaidAmount();

    double getTax();

    double getCoef();

    String getDisplayChargeAmount();
    String getDisplayTaxAmount();
    String getDisplaySum();
    String getDisplayPaidAmount();

    int getProductType();

    String toJsonString();

    boolean isUserInputChoice();

    int getChoiceTypeHintIconResId();
    int getChoiceTypeTitleIconResId();

    int getChoiceTypeHintResId();
    int getChoiceTypeTitleResId();

    boolean isIrancell();

    boolean isAddedToUserChoices();
}
