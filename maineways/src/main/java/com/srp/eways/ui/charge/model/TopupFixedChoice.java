package com.srp.eways.ui.charge.model;

import android.content.Context;

import com.google.gson.JsonObject;
import com.srp.eways.R;
import com.srp.eways.model.charge.result.TopupTypeItem;
import com.srp.eways.util.Utils;

import static com.srp.eways.model.charge.result.ITopupTypeItem.AMAZING_CHARGE_IRANCELL_PRODUCT_ID;

public class TopupFixedChoice implements IChargeChoice {

    private long mProductId;

    private String mName;
    private long mAmount;

    private double mTax;
    private double mCoef;

    private int mViewType;

    private String mDisplayChargeAmount;
    private String mDisplayTaxAmount;
    private String mDisplaySum;
    private String mDisplayPaidAmount;

    private boolean mIsIrancell;

    public TopupFixedChoice(long productId, String name, long amount, int viewType, double tax, double coef, boolean isIrancell) {
        mProductId = productId;

        mName = name;
        mAmount = amount;

        mTax = tax;
        mCoef = coef;

        mViewType = viewType;

        mIsIrancell = isIrancell;

        calcStuff((int) amount, tax, coef);

    }

    private void calcStuff(int chargeAmount, double tax, double coef) {
        int amount = (int) Math.floor(chargeAmount * (tax * .1 + 1));
        int paidAmount = (int) Math.ceil(amount * coef);
        int taxAmount = amount - chargeAmount;

        //TODO:
        if ((isIrancell() && getProductId() == AMAZING_CHARGE_IRANCELL_PRODUCT_ID/*Amazing charge*/) || coef == 1) {
            mAmount = paidAmount;
//        mAmount = (int) Math.ceil((chargeAmount + (chargeAmount * tax / 10)) * coef);
        }

        mDisplayChargeAmount = Utils.toPersianPriceNumber(chargeAmount);
        mDisplayTaxAmount = Utils.toPersianPriceNumber(taxAmount);
        mDisplaySum = Utils.toPersianPriceNumber(amount);
        mDisplayPaidAmount = Utils.toPersianPriceNumber(paidAmount);
    }

    @Override
    public String getName() {
        return mName;
    }

    @Override
    public String getDescription(Context context) {
        return null; // TODO
    }

    @Override
    public long getProductId() {
        return mProductId;
    }

    @Override
    public String getPackageId() {
        return "0";
    }

    @Override
    public long getAmount() {
        return mAmount;
    }

    @Override
    public long getPaidAmount() {
        return mAmount;
    }

    @Override
    public double getTax() {
        return mTax;
    }

    @Override
    public double getCoef() {
        return mCoef;
    }

    @Override
    public String getDisplayChargeAmount() {
        return mDisplayChargeAmount;
    }

    @Override
    public String getDisplayTaxAmount() {
        return mDisplayTaxAmount;
    }

    @Override
    public String getDisplaySum() {
        return mDisplaySum;
    }

    @Override
    public String getDisplayPaidAmount() {
        return mDisplayPaidAmount;
    }

    @Override
    public int getProductType() {
        return TopupTypeItem.TYPE_CHARGE;
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
        return R.drawable.ic_topupoption;
    }

    @Override
    public int getChoiceTypeTitleIconResId() {
        return R.drawable.ic_topupoption;
    }

    @Override
    public int getChoiceTypeTitleResId() {
        return R.string.topupchoice_type_hint;
    }

    @Override
    public int getChoiceTypeHintResId() {
        return R.string.topupchoice_type_title;
    }

    @Override
    public int getViewType() {
        return mViewType;
    }

    @Override
    public boolean isIrancell() {
        return mIsIrancell;
    }

    @Override
    public boolean isAddedToUserChoices() {
        return true;
    }

}
