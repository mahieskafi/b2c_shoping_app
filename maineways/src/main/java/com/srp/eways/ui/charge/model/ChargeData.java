package com.srp.eways.ui.charge.model;

/**
 * Created by ErfanG on 3/2/2020.
 */
public class ChargeData {

    public final ChargeInfo mChargeInfo;

    public final int mStatus;

    public final String errorMessage;

    public ChargeData(ChargeInfo chargeInfo, int status, String errorMessage) {
        mChargeInfo = chargeInfo;
        mStatus = status;
        this.errorMessage = errorMessage;
    }

}