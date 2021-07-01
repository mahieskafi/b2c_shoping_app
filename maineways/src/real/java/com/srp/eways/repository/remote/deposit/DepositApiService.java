package com.srp.eways.repository.remote.deposit;

import com.srp.eways.model.deposit.BankListResponse;
import com.srp.eways.model.deposit.ConfirmPaymentRequest;
import com.srp.eways.model.deposit.ConfirmPaymentResponse;
import com.srp.eways.model.deposit.IncreaseDepositRequest;
import com.srp.eways.model.deposit.IncreaseDepositResponse;
import com.srp.eways.model.deposit.IncreaseDepositStatusResponse;
import com.srp.eways.model.deposit.MPLTokenResponse;
import com.srp.eways.network.CallBackWrapper;

/**
 * Created by Eskafi on 8/26/2019.
 */
public interface DepositApiService {

    void getBankList(CallBackWrapper<BankListResponse> callBack);

    void increaseDeposit(IncreaseDepositRequest request, CallBackWrapper<IncreaseDepositResponse> callBackWrapper);

    void getStatus(String uId, String uUId, CallBackWrapper<IncreaseDepositStatusResponse> callBackWrapper);

    void getMplToken(long amount, CallBackWrapper<MPLTokenResponse> callBack);

    void confirmPayment(ConfirmPaymentRequest request, CallBackWrapper<ConfirmPaymentResponse> callBackWrapper);
}
