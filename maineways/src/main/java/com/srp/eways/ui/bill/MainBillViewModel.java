package com.srp.eways.ui.bill;

import androidx.lifecycle.MutableLiveData;

import com.srp.eways.base.BaseViewModel;
import com.srp.eways.di.DIMain;
import com.srp.eways.model.bill.BillPaymentRequest;
import com.srp.eways.model.bill.BillPaymentResponse;
import com.srp.eways.model.bill.BillPaymentStatusResult;
import com.srp.eways.model.bill.archivedList.BillPaymentDetail;
import com.srp.eways.model.deposit.Bank;
import com.srp.eways.model.deposit.BankListResponse;
import com.srp.eways.network.BaseCallBackWrapper;
import com.srp.eways.repository.bill.MainBillRepository;
import com.srp.eways.util.BillUtil;

import java.util.ArrayList;

/**
 * Created by ErfanG on 5/3/2020.
 */
public class MainBillViewModel extends BaseViewModel {


    protected MainBillRepository mBillRepository;

    public MutableLiveData<BankListResponse> mBankListResponseLive;
    public MutableLiveData<BillPaymentStatusResult> mBillPaymentStatusResponseLive;

    public MainBillViewModel() {
        mBillRepository = getBillRepository();
        mBankListResponseLive = new MutableLiveData<>();
        mBillPaymentStatusResponseLive = new MutableLiveData<>();
    }


    public void saveTempBills(ArrayList<BillPaymentDetail> billPaymentDetails,
                              final MutableLiveData<BillPaymentResponse> liveData,
                              final MutableLiveData<Boolean> loading) {
        setLoading(true);

        mBillRepository.saveTempBills(new BillPaymentRequest(billPaymentDetails, 41), new BaseCallBackWrapper<BillPaymentResponse>(this) {
            @Override
            public void onError(int errorCode, String errorMessage) {
                loading.setValue(false);

                BillPaymentResponse billTempResponse = new BillPaymentResponse(errorCode, errorMessage);

                liveData.setValue(billTempResponse);
            }

            @Override
            public void onSuccess(BillPaymentResponse responseBody) {
                loading.setValue(false);

                liveData.setValue(responseBody);

                //TODO
//                getAllOnlineArchivedBills();
            }
        });
    }

    public void payBills(ArrayList<BillPaymentDetail> billPaymentDetails, int bankId, final MutableLiveData<BillPaymentResponse> liveData,
                         final MutableLiveData<Boolean> loading) {
        setLoading(true);

        mBillRepository.payTempBills(new BillPaymentRequest(billPaymentDetails, bankId), new BaseCallBackWrapper<BillPaymentResponse>(this) {
            @Override
            public void onError(int errorCode, String errorMessage) {
                loading.setValue(false);

                BillPaymentResponse billTempResponse = new BillPaymentResponse(errorCode, errorMessage);

                liveData.setValue(billTempResponse);
            }

            @Override
            public void onSuccess(BillPaymentResponse responseBody) {

                loading.setValue(false);

                liveData.setValue(responseBody);

            }
        });
    }

    public void getBankList() {
        setLoading(true);

        mBillRepository.getBankList(new BaseCallBackWrapper<BankListResponse>(this) {
            @Override
            public void onError(int errorCode, String errorMessage) {
                setLoading(false);

                BankListResponse bankListResponse = new BankListResponse(errorCode, errorMessage, null);

                mBankListResponseLive.setValue(bankListResponse);
            }

            @Override
            public void onSuccess(BankListResponse responseBody) {
                //TODO wanted to remove کسر از سپرده from bank list
                ArrayList<Bank> bankList = new ArrayList<>();
                BankListResponse bankListResponse = responseBody;

                if (responseBody.getItems() != null && responseBody.getItems().size() > 0) {
                    for (Bank bank : responseBody.getItems()) {
                        if (bank.getGId() != BillUtil.DEPOSIT_GID) {
                            bankList.add(bank);
                        }
                    }

                    bankListResponse = new BankListResponse(responseBody.getStatus()
                            , ""
                            , bankList);
                }


                setLoading(false);
                mBankListResponseLive.setValue(bankListResponse);
            }

        });
    }

    public void getPaymentStatus(String requestId) {
        mBillRepository.getPaymentStatus(requestId, new BaseCallBackWrapper<BillPaymentStatusResult>(this) {
            @Override
            public void onError(int errorCode, String errorMessage) {
                BillPaymentStatusResult bankListResponse = new BillPaymentStatusResult(null, errorCode, errorMessage);

                mBillPaymentStatusResponseLive.setValue(bankListResponse);
            }

            @Override
            public void onSuccess(BillPaymentStatusResult responseBody) {
                mBillPaymentStatusResponseLive.setValue(responseBody);
            }
        });
    }

    public MutableLiveData<BankListResponse> getBankListLiveData() {
        return mBankListResponseLive;
    }

    public MutableLiveData<BillPaymentStatusResult> getBillPaymentStatusResponseLiveLiveData() {
        return mBillPaymentStatusResponseLive;
    }

    public void consumeBillPaymentStatusResponseLiveLiveData() {
        mBillPaymentStatusResponseLive.setValue(null);
    }

    protected MainBillRepository getBillRepository() {
        return DIMain.getBillInquiryRepo();
    }

}
