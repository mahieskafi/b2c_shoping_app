package com.srp.eways.ui.charge.model;

import android.content.Context;

import com.google.gson.JsonObject;
import com.srp.eways.R;
import com.srp.eways.model.charge.result.ITopupTypeItem;
import com.srp.eways.model.charge.result.TopupTypeItem;
import com.srp.eways.util.Utils;

public class InternetPackageChoice implements IChargeChoice {

    private long mInternetProductId;

    private ITopupTypeItem.InternetPackageItem mItem;

    private double mCoef;

    protected InternetPackageChoice(long internetProductId, ITopupTypeItem.InternetPackageItem item, double coef) {
        mInternetProductId = internetProductId;

        mItem = item;
        mCoef = coef;
    }

    @Override
    public String getName() {
        return mItem.getPName();
    }

    @Override
    public String getDescription(Context context) {
        return null; //ToDo
    }

    @Override
    public long getProductId() {
        return mInternetProductId;
    }

    @Override
    public String getPackageId() {
        return mItem.getId();
    }

    @Override
    public long getAmount() {
        return mItem.getPrice();
    }

    @Override
    public long getPaidAmount() {
        return mItem.getPaidPrice();
    }

    @Override
    public double getTax() {
        //TODO: should return tax
        return 0;
    }

    @Override
    public double getCoef() {
        return mCoef;
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
        int paidAmount = (int) Math.floor(mItem.getPaidPrice() * mCoef);
        return Utils.toPersianPriceNumber(paidAmount);
    }

    @Override
    public int getProductType() {
        return TopupTypeItem.TYPE_INTERNET;
    }

    @Override
    public String toJsonString() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", getName());
        jsonObject.addProperty("productId", getProductId());
        jsonObject.addProperty("packageId", getPackageId());
        jsonObject.addProperty("amount", getAmount());
        jsonObject.addProperty("paidAmount", getPaidAmount());
        jsonObject.addProperty("productType", getProductType());
        jsonObject.addProperty("isUserInputChoice", isUserInputChoice());
        jsonObject.addProperty("isUserInputChoice", isUserInputChoice());

        return jsonObject.toString();
    }

    @Override
    public boolean isUserInputChoice() {
        return false;
    }

    @Override
    public int getChoiceTypeHintIconResId() {
        return R.drawable.ic_internetpackagechoice;
    }

    @Override
    public int getChoiceTypeHintResId() {
        return R.string.internetpackage_choice_type_hint;
    }

    @Override
    public int getChoiceTypeTitleIconResId() {
        return R.drawable.ic_internetpackage;
    }

    @Override
    public int getChoiceTypeTitleResId() {
        return R.string.internetpackage_choice_type_title;
    }

    @Override
    public int getViewType() {
        return VIEWTYPE_RADIOBUTTON_ONECOLUMN;
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
