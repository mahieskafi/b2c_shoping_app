package com.srp.eways.ui.charge.model;


import com.srp.eways.R;
import com.srp.eways.model.charge.result.ITopupTypeItem;

import java.util.ArrayList;
import java.util.List;

public class InternetPackageOption implements IChargeOption {

    private String mName;
    private long mInternetProductId;

    private IOperator mOperator;

    private List<ITopupTypeItem.InternetPackageItem> mChildren;
    private double mCoeff;

    protected InternetPackageOption(IOperator operator, long internetProductId, String name, List<ITopupTypeItem.InternetPackageItem> children, double coeff) {
        mName = name;
        mInternetProductId = internetProductId;

        mOperator = operator;

        mChildren = children;
        mCoeff = coeff;
    }

    @Override
    public String getName() {
        return mName;
    }

    @Override
    public IOperator getOperator() {
        return mOperator;
    }

    @Override
    public int getProductTypeIconResId() {
        return 0;
    }

    @Override
    public int getOptionTypeHintIconResId() {
        return R.drawable.ic_internetpackageoption;
    }

    @Override
    public int getOptionTypeTitleIconResId() {
        return R.drawable.ic_internetpackage;
    }

    @Override
    public int getOptionTypeHintResId() {
        return R.string.internetpackage_option_type_hint;
    }

    @Override
    public int getOptionTypeTitleResId() {
        return R.string.internetpackage_option_type_title;
    }

    @Override
    public boolean hasChargeOptions() {
        return false;
    }

    @Override
    public List<IChargeOption> getChargeOptions() {
        return null;
    }

    @Override
    public List<IChargeChoice> getChargeChoices() {
        List<IChargeChoice> internetPackageChoices = new ArrayList<>();
        for (ITopupTypeItem.InternetPackageItem child: mChildren) {
            InternetPackageChoice internetPackageChoice = new InternetPackageChoice(mInternetProductId, child, mCoeff);

            internetPackageChoices.add(internetPackageChoice);
        }

        return internetPackageChoices;
    }

    @Override
    public boolean supportsUserInput() {
        return false;
    }

    @Override
    public IChargeChoice addChoice(long amount) {
        return null;
    }

    @Override
    public IChargeChoice createChoice(long amount) {
        return null;
    }

    @Override
    public IChargeChoice removeChoice(long amount) {
        //do nothing
        return null;
    }

    @Override
    public IUserInputChoice getUserInputChoice() {
        return null;
    }

    @Override
    public List<IChargeChoice> getUserInputChoices() {
        return null;
    }

    @Override
    public int getViewType() {
        return VIEWTYPE_RADIOBUTTON_ONECOLUMN;
    }

}
