package com.srp.eways.repository.remote.charge;

import com.srp.eways.AppConfig;
import com.srp.eways.di.DIMain;
import com.srp.eways.model.charge.BuyChargeResult;
import com.srp.eways.model.charge.ChargeResult;
import com.srp.eways.model.charge.IrancellSpecialOffersResult;
import com.srp.eways.model.charge.result.BuyCashChargeResult;
import com.srp.eways.model.deposit.BankListResponse;
import com.srp.eways.network.CallBackWrapper;
import com.srp.eways.repository.remote.BaseApiImplementation;
import com.srp.eways.repository.remote.DefaultRetroCallback;
import com.srp.eways.model.charge.request.BuyChargeRequest;
import com.srp.eways.model.charge.result.topinquiry.TopInquiriesResult;

public class ChargeApiImplementation extends BaseApiImplementation implements ChargeApiService {

    private static ChargeApiImplementation sInstance;

    public static ChargeApiImplementation getInstance() {

        if (sInstance == null) {
            sInstance = new ChargeApiImplementation();
        }

        return sInstance;
    }

    private ChargeApiRetrofit mChargeApi;

    private ChargeApiImplementation() {
        mChargeApi = DIMain.provideApi(ChargeApiRetrofit.class);
    }

    @Override
    public void getChargeData(final CallBackWrapper<ChargeResult> callBack) {
        mChargeApi.getChargeData().enqueue(new DefaultRetroCallback<ChargeResult>(callBack) {

            @Override
            protected void checkResponseForError(ChargeResult chargeResult, DefaultRetroCallback.ErrorInfo errorInfo) {

                if (chargeResult.getStatus() != 0) {
                    errorInfo.errorCode = chargeResult.getStatus();
                    errorInfo.errorMessage = chargeResult.getDescription();
                }
            }

        });
    }

    @Override
    public void getIrancellSpecialOffers(String phoneNumber, final CallBackWrapper<IrancellSpecialOffersResult> callBack) {
        mChargeApi.getIrancellSpecialOffers(phoneNumber).enqueue(new DefaultRetroCallback<IrancellSpecialOffersResult>(callBack) {

            @Override
            protected void checkResponseForError(IrancellSpecialOffersResult result, ErrorInfo errorInfo) {
                if (result.getStatus() != 0) {
                    errorInfo.errorCode = result.getStatus();
                    errorInfo.errorMessage = result.getDescription();
                }
            }

        });
    }

    @Override
    public void buyCharge(BuyChargeRequest buyChargeRequest, final CallBackWrapper<BuyChargeResult> callBack) {
        mChargeApi.buyCharge(buyChargeRequest).enqueue(new DefaultRetroCallback<BuyChargeResult>(callBack) {

            @Override
            protected void checkResponseForError(BuyChargeResult result, ErrorInfo errorInfo) {
                if (result.getStatus() != 0) {
                    errorInfo.errorCode = result.getStatus();
                    errorInfo.errorMessage = result.getDescription();
                }
            }

        });
    }

    @Override
    public void buyCashCharge(BuyChargeRequest buyChargeRequest, final CallBackWrapper<BuyCashChargeResult> callBack) {
        mChargeApi.buyCashCharge(AppConfig.SERVER_VERSION, buyChargeRequest).enqueue(new DefaultRetroCallback<BuyCashChargeResult>(callBack) {

            @Override
            protected void checkResponseForError(BuyCashChargeResult result, ErrorInfo errorInfo) {
                if (result.getStatus() != 0) {
                    errorInfo.errorCode = result.getStatus();
                    errorInfo.errorMessage = result.getDescription();
                }
            }

        });
    }

    @Override
    public void getTopInquiries(final CallBackWrapper<TopInquiriesResult> callBack) {
        mChargeApi.topInquiries().enqueue(new DefaultRetroCallback<TopInquiriesResult>(callBack) {
        });
    }

    @Override
    public void getBankList(CallBackWrapper<BankListResponse> callBack) {
        mChargeApi.getBankList(AppConfig.SERVER_VERSION).enqueue(new DefaultRetroCallback<BankListResponse>(callBack) {

            @Override
            protected void checkResponseForError(BankListResponse result, ErrorInfo errorInfo) {
                if (result.getStatus() != 0) {
                    errorInfo.errorCode = result.getStatus();
                    errorInfo.errorMessage = result.getDescription();
                }
            }

        });
    }

}
