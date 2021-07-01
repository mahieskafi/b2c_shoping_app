package com.srp.eways.ui.charge.model;

import java.util.List;

public interface IChargeOption extends IChargeOptionViewType {

    String getName();

    boolean hasChargeOptions();

    List<IChargeOption> getChargeOptions();

    List<IChargeChoice> getChargeChoices();

    boolean supportsUserInput();

    IChargeChoice addChoice(long amount);

    IChargeChoice createChoice(long amount);

    IChargeChoice removeChoice(long amount);

    IUserInputChoice getUserInputChoice();

    List<IChargeChoice> getUserInputChoices();

    IOperator getOperator();

    int getProductTypeIconResId();

    int getOptionTypeHintIconResId();
    int getOptionTypeTitleIconResId();

    int getOptionTypeHintResId();
    int getOptionTypeTitleResId();

}
