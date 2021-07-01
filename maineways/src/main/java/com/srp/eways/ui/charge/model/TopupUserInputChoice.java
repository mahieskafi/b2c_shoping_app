package com.srp.eways.ui.charge.model;

import android.content.Context;

import com.google.gson.JsonObject;
import com.srp.eways.R;
import com.srp.eways.model.charge.result.ITopupTypeItem;
import com.srp.eways.model.charge.result.TopupTypeItem;
import com.srp.eways.util.Utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TopupUserInputChoice implements IUserInputChoice {

    private ITopupTypeItem mItem;

    private List<IChargeChoice> mChargeChoices = new ArrayList<>();

    private boolean mIsIrancell = false;

    protected TopupUserInputChoice(ITopupTypeItem item, boolean isIrancell) {
        mItem = item;

        mIsIrancell = isIrancell;
    }

    @Override
    public IChargeChoice addChoice(long amount) {
        if (!containsChoice(amount)) {
            amount = (int) Math.floor(amount * (mItem.getTax() * .1 + 1));

            Choice choice = new Choice((int) amount, mItem.getProductId(), mItem.getTax(), mItem.getCofficient(), mIsIrancell, true);
            mChargeChoices.add(0, choice);

            return choice;
        }

        return null;
    }

    @Override
    public IChargeChoice createChoice(long amount) {
        amount = (int) Math.floor(amount * (mItem.getTax() * .1 + 1));

        Choice choice = new Choice((int) amount, mItem.getProductId(), mItem.getTax(), mItem.getCofficient(), mIsIrancell, false);

        return choice;
    }

    private boolean containsChoice(long amount) {
        for (int i = 0; i < mChargeChoices.size(); ++i) {
            if (mChargeChoices.get(i).getAmount() == amount) {
                return true;
            }
        }

        return false;
    }

    @Override
    public IChargeChoice removeChoice(long amount) {
        IChargeChoice chargeChoice = null;

        Iterator iterator = mChargeChoices.iterator();
        while (iterator.hasNext()) {
            IChargeChoice choice = (IChargeChoice) iterator.next();
            if (("" + amount).equals(choice.getAmount() + "")) {
                chargeChoice = choice;

                iterator.remove();
            }
        }

        return chargeChoice;
    }

    @Override
    public List<IChargeChoice> getUserChoices() {
        return mChargeChoices;
    }

    @Override
    public boolean isIrancell() {
        return mIsIrancell;
    }

    public static class Choice implements IChargeChoice {

        // chargeAmount = amount / (tax * .1 + 1)
        // chargeAmount (amount)

        private long mAmount;
        private String mName;

        private double mTax;
        private double mCoef;

        private long mProductId;

        private String mDisplayChargeAmount;
        private String mDisplayTaxAmount;
        private String mDisplaySum;
        private String mDisplayPaidAmount;

        private boolean mIsIrancell = false;

        private boolean mIsAddedToUserChoices = false;

        private Choice(int amount, long productId, double tax, double coef, boolean isIrancell, boolean isAddedToUserChoices) {
            mIsIrancell = isIrancell;
            mIsAddedToUserChoices = isAddedToUserChoices;

            mProductId = productId;

            mTax = tax;
            mCoef = coef;

            int chargeAmount = (int) Math.ceil(amount / (tax * .1 + 1));

            mAmount = chargeAmount;

            if (amount != chargeAmount) {
                mName = Utils.toPersianPriceNumber(chargeAmount) + " (" + Utils.toPersianPriceNumber(amount) + ")";
            } else {
                mName = Utils.toPersianPriceNumber(chargeAmount);
            }

            int paidAmount = (int) Math.floor(amount * coef);

            int taxAmount = amount - chargeAmount;

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
            return true;
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
        public boolean isIrancell() {
            return mIsIrancell;
        }

        @Override
        public boolean isAddedToUserChoices() {
            return mIsAddedToUserChoices;
        }

        @Override
        public int getViewType() {
            return VIEWTYPE_RADIOBUTTON_ONECOLUMN_ADDABLE;
        }
    }

}
