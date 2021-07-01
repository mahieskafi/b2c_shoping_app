package com.srp.eways.repository.charge;

import com.srp.eways.model.charge.BuyChargeResult;
import com.srp.eways.model.charge.IrancellSpecialOffersResult;
import com.srp.eways.model.charge.request.BuyChargeRequest;
import com.srp.eways.model.charge.result.BuyCashChargeResult;
import com.srp.eways.model.charge.result.topinquiry.TopInquiriesResult;
import com.srp.eways.model.deposit.BankListResponse;
import com.srp.eways.network.CallBackWrapper;
import com.srp.eways.ui.charge.OnChargeDataReadyListener;
import com.srp.eways.ui.charge.model.IChargeChoice;
import com.srp.eways.ui.charge.model.IChargeOption;

/**
 * Created by ErfanG on 19/08/2019.
 */
public interface ChargeRepository {

    void getChargeData(OnChargeDataReadyListener onChargeDataReadyListener);

    void addChoice(IChargeOption parentChargeOption, IChargeChoice chargeChoice);

    void removeChoice(IChargeOption parentChargeOption, IChargeChoice chargeChoice);

    void buyCharge(BuyChargeRequest buyChargeRequest, CallBackWrapper<BuyChargeResult> callBack);

    void buyCashCharge(BuyChargeRequest buyChargeRequest, CallBackWrapper<BuyCashChargeResult> callBack);

    void getIrancellSpecialOffers(String phoneNumber, CallBackWrapper<IrancellSpecialOffersResult> callBack);

    void getTopInquiries(CallBackWrapper<TopInquiriesResult> callBack);

    void getBankList(CallBackWrapper<BankListResponse> callBack);
}
