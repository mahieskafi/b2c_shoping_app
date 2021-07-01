package com.srp.eways.ui.charge.model;

import com.srp.eways.R;
import com.srp.eways.model.charge.result.ITopupTypeItem;
import com.srp.eways.model.charge.result.TopupTypeItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChargeOption implements IChargeOption {

    private TopupTypeItem mItem;

    private long mInternetProductId;

    private int mDepth;

    private IOperator mOperator;

    private int mViewType;

    private List<IChargeOption> mOptions;

    private List<IChargeOption> mTopUpOptionsFromChildren;

    private boolean mIsIrancell = false;

    public ChargeOption(IOperator operator, long internetProductId, TopupTypeItem item, int depth, int viewType, boolean isIrancell) {
        mItem = item;

        mInternetProductId = internetProductId;
        mDepth = depth;

        mOperator = operator;

        mViewType = viewType;

        mIsIrancell = isIrancell;
    }

    @Override
    public String getName() {
        return mItem.getPName();
    }

    @Override
    public boolean hasChargeOptions() {
//        if (mItem.getChildType() == TopupTypeItem.TYPE_SPECIALOFFERS) {
//            return false;
//        }

        return true;
    }

    @Override
    public List<IChargeOption> getChargeOptions() {
        if (mOptions != null) {
            return mOptions;
        }

        mOptions = getChargeOptionsFromSubTypes();

        if (mOptions == null) {
            return getChargeOptionsFromChildren();
        }

        List<IChargeOption> childOptions = getChargeOptionsFromChildren();

        if (childOptions == null) {
            return mOptions;
        }

        mOptions.addAll(childOptions);

        return mOptions;
    }

    private List<IChargeOption> getChargeOptionsFromSubTypes() {
        List<TopupTypeItem> subItems = mItem.getSubTypes();

        if (subItems == null) {
            return null;
        }

        List<IChargeOption> chargeOptionsFromSubTypes = new ArrayList<>(subItems.size());

        for (TopupTypeItem subItem: subItems) {
            if (!subItem.isDisabled()) {
                chargeOptionsFromSubTypes.add(new ChargeOption(mOperator, mInternetProductId, subItem, mDepth + 1, getChildViewType(subItem), mIsIrancell));
            }
        }

        return chargeOptionsFromSubTypes;
    }

    private List<IChargeOption> getChargeOptionsFromChildren() {
        switch (mItem.getChildType()) {
            case TopupTypeItem.TYPE_CHARGE:
                return getTopUpOptionsFromChildren();
            case TopupTypeItem.TYPE_INTERNET:
                return getInternetOptionsFromChildren();
            case TopupTypeItem.TYPE_SPECIALOFFERS:
                return Arrays.asList((IChargeOption) new SpecialOfferOption(mOperator));
        }

        return null;
    }

    private List<IChargeOption> getTopUpOptionsFromChildren() {
        if (mTopUpOptionsFromChildren != null) {
            return mTopUpOptionsFromChildren;
        }

        mTopUpOptionsFromChildren = new ArrayList<>();

        for (ITopupTypeItem child: mItem.getChilds()) {
            TopUpOption option = new TopUpOption(mOperator, child, mIsIrancell);

            mTopUpOptionsFromChildren.add(option);
        }

        return mTopUpOptionsFromChildren;
    }

    private List<IChargeOption> getInternetOptionsFromChildren() {
        List<IChargeOption> options = new ArrayList<>();

        List<ITopupTypeItem> internetChildren = mItem.getChilds();

        for (ITopupTypeItem child: internetChildren) {
            List<IChargeOption> childOptions = getInternetOptionsFromChild(child);

            if (childOptions != null) {
                options.addAll(childOptions);
            }
        }

        return options;
    }

    private List<IChargeOption> getInternetOptionsFromChild(ITopupTypeItem item) {
        List<ITopupTypeItem.InternetPackageItem> parentNodes = item.getPackageMasterList();
        List<ITopupTypeItem.InternetPackageItem> childNodes = item.getPackageItems();

        List<IChargeOption> internetPackageOptions = new ArrayList<>(parentNodes.size());

        for (ITopupTypeItem.InternetPackageItem parentNode: parentNodes) {
            List<ITopupTypeItem.InternetPackageItem> children = new ArrayList<>();

            for (ITopupTypeItem.InternetPackageItem childNode: childNodes) {
                if (String.valueOf(childNode.getParent()).equals(parentNode.getId())) {
                    children.add(childNode);
                }
            }

            internetPackageOptions.add(new InternetPackageOption(mOperator, mInternetProductId, parentNode.getPName(), children, item.getCofficient()));
        }

        return internetPackageOptions;
    }

    public int getDepth() {
        return mDepth;
    }

    @Override
    public IOperator getOperator() {
        return mOperator;
    }

    @Override
    public List<IChargeChoice> getChargeChoices() {
        return null;
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
        return mViewType;
    }

    @Override
    public int getProductTypeIconResId() {
        // TODO A mystery lies here: YAHSAR!
        switch (mItem.getChildType()) {
            case 0:
                return R.drawable.ic_direct_charge;
            case 2:
                return R.drawable.ic_internetpackage;
            case 3:
                return R.drawable.ic_irancell_specialoffer;
        }

        return 0;
    }

    @Override
    public int getOptionTypeHintIconResId() {
        return 0;
    }

    @Override
    public int getOptionTypeTitleIconResId() {
        return 0;
    }

    @Override
    public int getOptionTypeHintResId() {
        return 0;
    }

    @Override
    public int getOptionTypeTitleResId() {
        return 0;
    }

    private int getChildViewType(TopupTypeItem childItem) {
        int viewType;

        if (mItem.getSubTypes() != null && childItem.getChildType() != TopupTypeItem.TYPE_INTERNET) {
            viewType = VIEWTYPE_TAB;
        }
        else if (childItem.getChildType() == TopupTypeItem.TYPE_CHARGE) {
            viewType = VIEWTYPE_RADIOBUTTON_ONECOLUMN;
        }
        else if (childItem.getChildType() == TopupTypeItem.TYPE_INTERNET) {
            viewType = VIEWTYPE_RADIOBUTTON_ONECOLUMN;
        }
        else if (childItem.getChildType() == TopupTypeItem.TYPE_SPECIALOFFERS) {
            viewType = VIEWTYPE_IRANCELL_SPECIALOFFER_LOADING;
        }
        else {
            viewType = VIEWTYPE_RADIOBUTTON_ONECOLUMN;
        }

        return viewType;
    }

}
