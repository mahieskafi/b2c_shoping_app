package com.srp.eways.ui.charge.model;

import com.srp.eways.R;

import java.util.List;

public class SpecialOfferOption implements IChargeOption {

    private List<IChargeChoice> mChoices = null;

    private IOperator mOperator;

    public SpecialOfferOption(IOperator operator) {
        mOperator = operator;
    }

    public void setChoices(List<IChargeChoice> choices) {
        mChoices = choices;
    }

    @Override
    public String getName() {
        return null;
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
        return mChoices;
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
    public IOperator getOperator() {
        return mOperator;
    }

    @Override
    public int getProductTypeIconResId() {
        return 0;
    }

    @Override
    public int getOptionTypeHintIconResId() {
        return R.drawable.ic_irancell_specialoffer;
    }

    @Override
    public int getOptionTypeTitleIconResId() {
        return R.drawable.ic_irancell_specialoffer;
    }

    @Override
    public int getOptionTypeHintResId() {
        return R.string.specialoffers_type_hint;
    }

    @Override
    public int getOptionTypeTitleResId() {
        return R.string.specialoffers_type_title;
    }

    @Override
    public int getViewType() {
        return VIEWTYPE_IRANCELL_SPECIALOFFER_LOADING;
    }

}
