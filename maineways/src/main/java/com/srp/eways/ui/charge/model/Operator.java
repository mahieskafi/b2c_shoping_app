package com.srp.eways.ui.charge.model;


import androidx.annotation.DrawableRes;

import com.srp.eways.R;
import com.srp.eways.di.DIMain;
import com.srp.eways.model.charge.result.TopupChargeItem;
import com.srp.eways.model.charge.result.TopupTypeItem;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class Operator implements IOperator {

    public static int OPERATOR_MCI = 0;
    public static int OPERATOR_IRANCEL = 1;
    public static int OPERATOR_RIGHTEL = 2;

    private ChargeInfo mChargeInfo;
    private TopupChargeItem mItem;

    private List<IChargeOption> mOptions;


    protected Operator(ChargeInfo chargeInfo, TopupChargeItem item) {
        mChargeInfo = chargeInfo;
        mItem = item;
    }

    @Override
    public String getName() {
        return mItem.getPName();
    }

    @Override
    public @DrawableRes
    int getIconResId() {
        int designModel = DIMain.getABResources().getInt(R.integer.abtesting_designmodel);

        int iconResId = 0;

        switch (mItem.getOperatorKey()) {
            case "MTNTD":
            case "MTN":
                iconResId = designModel == 0 ? R.drawable.ic_irancell : R.drawable.ic_irancell_b;
                break;
            case "MCI":
                iconResId = designModel == 0 ? R.drawable.ic_hamrah_aval : R.drawable.ic_hamrah_avval_b;
                break;
            case "RIGHTELPR":
                iconResId = designModel == 0 ? R.drawable.ic_rightel_etebari : R.drawable.ic_rightel_b;
                break;
            case "RIGHTELPO":
                iconResId = designModel == 0 ? R.drawable.ic_rightel_daemi : R.drawable.ic_rightel_b;
                break;
        }

        return iconResId;
    }

    @Override
    public int getNoNameIconResId() {
        switch (mItem.getOperatorKey()) {
            case "MTNTD":
            case "MTN":
                return R.drawable.ic_irancell_design_b;
            case "MCI":
                return R.drawable.ic_hamrahe_aval_design_b;
            case "RIGHTELPR":
            case "RIGHTELPO":
                return R.drawable.ic_rightel_design_b;
        }

        return 0;
    }

    @Override
    public int getTransportableIconResId() {
        switch (mItem.getOperatorKey()) {
            case "MTNTD":
            case "MTN":
                return R.drawable.ic_irancell;
            case "MCI":
                return R.drawable.ic_hamrah_aval;
            case "RIGHTELPR":
                return R.drawable.ic_rightel_etebari_with_text;
            case "RIGHTELPO":
                return R.drawable.ic_rightel_daemi_with_text;
        }

        return 0;
    }

    @Override
    public int getOperatorColor() {
        switch (mItem.getOperatorKey()) {
            case "MTNTD":
            case "MTN":
                return R.color.operator_irancell;
            case "MCI":
                return R.color.operator_hamraah_avval;
            case "RIGHTELPR":
            case "RIGHTELPO":
                return R.color.operator_rightel;
        }

        return 0;
    }

    @Override
    public int getOperatorServiceColor() {
        switch (mItem.getOperatorKey()) {
            case "MTNTD":
            case "MTN":
                return R.color.operator_irancell_services;
            case "MCI":
                return R.color.operator_hamraah_avval_services;
            case "RIGHTELPR":
            case "RIGHTELPO":
                return R.color.operator_rightel_services;
        }

        return 0;
    }

    @Override
    public int getOperatorName() {
        switch (mItem.getOperatorKey()) {
            case "MTNTD":
            case "MTN":
                return R.string.irancel;
            case "MCI":
                return R.string.hamrahe_aval;
            case "RIGHTELPR":
                return R.string.rightel_etebari;
            case "RIGHTELPO":
                return R.string.rightel_daemi;
        }

        return 0;
    }

    @Override
    public int getIrancellSpecialOfferProductId() {
        try {
            for (TopupTypeItem item : mItem.getItems()) {
                if (item.getChildType() == TopupTypeItem.TYPE_SPECIALOFFERS) {
                    return (int) item.getChilds().get(0).getProductId();
                }
            }
        } catch (Throwable t) {
            // TODO Log properly.
            return 0;
        }

        return 0;
    }

    @Override
    public int getOperatorKey() {
        switch (mItem.getOperatorKey()) {
            case "MTNTD":
            case "MTN":
                return OPERATOR_IRANCEL;
            case "MCI":
                return OPERATOR_MCI;
            case "RIGHTELPR":
            case "RIGHTELPO":
                return OPERATOR_RIGHTEL;
            default:
                return OPERATOR_MCI;
        }
    }

    public int getMatchWithNumber(String mobileNumber) {
        int longestMatch = 0;

        for (String prefix : mItem.getPerfixList()) {
            if (mobileNumber.startsWith(prefix)) {
                longestMatch = Math.max(longestMatch, prefix.length());
            }
        }

        return longestMatch;
    }

    public static String getOperatorName(String phoneNumber) {
        if (phoneNumber == null) return null;

        String operatorName = "";

        if (phoneNumber.startsWith("093") || phoneNumber.startsWith("090") || phoneNumber.startsWith("094")) {
            operatorName = "ایرانسل";
        } else if (phoneNumber.startsWith("091") || phoneNumber.startsWith("099")) {
            operatorName = "همراه اول";
        } else if (phoneNumber.startsWith("0920") || phoneNumber.startsWith("0921") || phoneNumber.startsWith("0922")) {
            operatorName = "رایتل";
        }

        return operatorName;
    }

    @Override
    public boolean isTransportable() {
        return mItem.isHaveTransport();
    }

    @Override
    public List<IOperator> getTransportableOperators() {
        List<IOperator> operators = new ArrayList<IOperator>(mChargeInfo.getOperators());

        ListIterator<IOperator> iterator = operators.listIterator();

        while (iterator.hasNext()) {
            IOperator operator = iterator.next();

            if (!operator.isTransportable() || operator.getName().equals(getName())) {
                iterator.remove();
            }
        }

        return operators;
    }

    @Override
    public List<IChargeOption> getChargeOptions() {
        if (mOptions != null) {
            return mOptions;
        }

        List<TopupTypeItem> items = mItem.getItems();

        mOptions = new ArrayList<>(items.size());

        for (TopupTypeItem item : items) {
            if (!item.isDisabled()) {
                ChargeOption option = new ChargeOption(this, mItem.getInternetProductId(), item, 0, VIEWTYPE_TAB, isIrancell());

                mOptions.add(option);
            }
        }

        return mOptions;
    }

    private boolean isIrancell() {
        switch (mItem.getOperatorKey()) {
            case "MTNTD":
            case "MTN":
                return true;
            case "MCI":
            case "RIGHTELPR":
            case "RIGHTELPO":
            default:
                return false;
        }
    }

    @Override
    public int getViewType() {
        return VIEWTYPE_OPERATOR;
    }

}
