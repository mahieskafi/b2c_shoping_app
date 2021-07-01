package com.srp.eways.repository.deposit;

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
public interface DepositRepository {

    void getBankList(CallBackWrapper<BankListResponse> callBack);

    void increaseDeposit(IncreaseDepositRequest request, CallBackWrapper<IncreaseDepositResponse> callBack);

    void getStatus(String uId, String uUId, CallBackWrapper<IncreaseDepositStatusResponse> callBack);

    void getMplToken(Long amount, CallBackWrapper<MPLTokenResponse> callBack);

    void confirmPayment(ConfirmPaymentRequest request, CallBackWrapper<ConfirmPaymentResponse> callBack);
}


