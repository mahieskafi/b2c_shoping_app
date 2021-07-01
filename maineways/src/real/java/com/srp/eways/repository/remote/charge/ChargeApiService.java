package com.srp.eways.repository.remote.charge;

import com.srp.eways.model.charge.BuyChargeResult;
import com.srp.eways.model.charge.ChargeResult;
import com.srp.eways.model.charge.IrancellSpecialOffersResult;
import com.srp.eways.model.charge.request.BuyChargeRequest;
import com.srp.eways.model.charge.result.BuyCashChargeResult;
import com.srp.eways.model.charge.result.topinquiry.TopInquiriesResult;
import com.srp.eways.model.deposit.BankListResponse;
import com.srp.eways.network.CallBackWrapper;

public interface ChargeApiService {

    void getChargeData(CallBackWrapper<ChargeResult> callBack);

    void getIrancellSpecialOffers(String phoneNumber, CallBackWrapper<IrancellSpecialOffersResult> callBack);

    void buyCharge(BuyChargeRequest buyChargeRequest, CallBackWrapper<BuyChargeResult> callBack);

    void buyCashCharge(BuyChargeRequest buyChargeRequest, CallBackWrapper<BuyCashChargeResult> callBack);

    void getTopInquiries(CallBackWrapper<TopInquiriesResult> callBack);

    void getBankList(CallBackWrapper<BankListResponse> callBack);
}
