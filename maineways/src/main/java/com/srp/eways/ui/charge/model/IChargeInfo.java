package com.srp.eways.ui.charge.model;

public interface IChargeInfo {

    IOperator getOperator(String mobileNumber);

    int getOperatorIndex(String phoneNumber);

}
