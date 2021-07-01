package com.srp.eways.ui.charge.model;

import android.content.Context;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.srp.eways.R;

public class DefaultChargeChoice implements IChargeChoice {

    @Expose
    private String name;

    @Expose
    private long productId;

    @Expose
    private String packageId;

    @Expose
    private long amount;

    @Expose
    private long paidAmount;

    @Expose
    private int productType;

    @Expose
    private boolean isUserInputChoice;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription(Context context) {
        return null;
    }

    @Override
    public long getProductId() {
        return productId;
    }

    @Override
    public String getPackageId() {
        return packageId;
    }

    @Override
    public long getAmount() {
        return amount;
    }

    @Override
    public long getPaidAmount() {
        return paidAmount;
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
        return productType;
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
//        return "{" +
//                "\"name:\"" + getName() + ", " +
//                "\"productId:\"" + getProductId() + ", " +
//                "\"packageId:\"" + getPackageId() + ", " +
//                "\"amount:\"" + getAmount() + ", " +
//                "\"paidAmount:\"" + getTax() + ", " +
//                "\"productType:\"" + getProductType() + ", " +
//                "\"isUserInputChoice\"" + isUserInputChoice() +
//                "}";

        return jsonObject.toString();
    }

    @Override
    public boolean isUserInputChoice() {
        return isUserInputChoice;
    }

    @Override
    public int getChoiceTypeHintIconResId() {
        return R.drawable.ic_topupoption;
    }

    @Override
    public int getChoiceTypeHintResId() {
        return R.string.topupchoice_type_hint;
    }

    @Override
    public int getChoiceTypeTitleIconResId() {
        return R.drawable.ic_topupoption;
    }

    @Override
    public int getChoiceTypeTitleResId() {
        return R.string.topupchoice_type_title;
    }

    @Override
    public int getViewType() {
        return 0;
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
