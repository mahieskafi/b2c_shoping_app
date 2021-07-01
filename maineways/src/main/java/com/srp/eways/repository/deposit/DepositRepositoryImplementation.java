package com.srp.eways.repository.deposit;

import com.srp.eways.model.deposit.ConfirmPaymentRequest;
import com.srp.eways.model.deposit.ConfirmPaymentResponse;
import com.srp.eways.model.deposit.MPLTokenResponse;
import com.srp.eways.network.CallBackWrapper;
import com.srp.eways.di.DIMain;
import com.srp.eways.model.deposit.BankListResponse;
import com.srp.eways.model.deposit.IncreaseDepositRequest;
import com.srp.eways.model.deposit.IncreaseDepositResponse;
import com.srp.eways.model.deposit.IncreaseDepositStatusResponse;

import com.srp.eways.repository.remote.deposit.DepositApiService;

/**
 * Created by Eskafi on 8/26/2019.
 */
public class DepositRepositoryImplementation implements DepositRepository {

    private static DepositRepositoryImplementation INSTANCE;

    private DepositApiService mDepositApiService;


    public static DepositRepositoryImplementation getInstance() {

        if (INSTANCE == null) {

            INSTANCE = new DepositRepositoryImplementation();
        }

        return INSTANCE;
    }

    public DepositRepositoryImplementation() {

        mDepositApiService = DIMain.getDepositApi();
    }

    @Override
    public void getBankList(CallBackWrapper<BankListResponse> callBack) {

        mDepositApiService.getBankList(callBack);
    }

    @Override
    public void increaseDeposit(IncreaseDepositRequest request, CallBackWrapper<IncreaseDepositResponse> callBack) {

        mDepositApiService.increaseDeposit(request, callBack);
    }

    @Override
    public void getStatus(String uId, String uUId, CallBackWrapper<IncreaseDepositStatusResponse> callBack) {

        mDepositApiService.getStatus(uId, uUId, callBack);
    }

    @Override
    public void getMplToken(Long amount, CallBackWrapper<MPLTokenResponse> callBack) {

        mDepositApiService.getMplToken(amount, callBack);
    }

    @Override
    public void confirmPayment(ConfirmPaymentRequest request, CallBackWrapper<ConfirmPaymentResponse> callBack) {

        mDepositApiService.confirmPayment(request, callBack);
    }
}
