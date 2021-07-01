package com.srp.eways.ui.charge.model;

import java.util.List;

public interface IUserInputChoice {

    IChargeChoice addChoice(long amount);

    IChargeChoice createChoice(long amount);

    IChargeChoice removeChoice(long amount);

    List<IChargeChoice> getUserChoices();

    boolean isIrancell();

}
