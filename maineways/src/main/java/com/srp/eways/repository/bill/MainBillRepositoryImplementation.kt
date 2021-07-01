package com.srp.eways.repository.bill

import com.srp.eways.di.DIMain
import com.srp.eways.model.bill.BillPaymentRequest
import com.srp.eways.model.bill.BillPaymentResponse
import com.srp.eways.model.bill.BillPaymentStatusResult
import com.srp.eways.model.deposit.BankListResponse
import com.srp.eways.network.CallBackWrapper
import com.srp.eways.repository.remote.bill.BillApiService

/**
 * Created by ErfanG on 5/3/2020.
 */
open class MainBillRepositoryImplementation : MainBillRepository{

    private var mBillApiService: BillApiService = DIMain.getMainBillApi()


    override fun saveTempBills(billRequest: BillPaymentRequest, callBackWrapper: CallBackWrapper<BillPaymentResponse>) {
        mBillApiService.saveTempBills(billRequest, object : CallBackWrapper<BillPaymentResponse> {
            override fun onError(errorCode: Int, errorMessage: String) {
                callBackWrapper.onError(errorCode, errorMessage)
            }

            override fun onSuccess(responseBody: BillPaymentResponse) {
                callBackWrapper.onSuccess(responseBody)
            }
        })
    }

    override fun payTempBills(billRequest: BillPaymentRequest, callBackWrapper: CallBackWrapper<BillPaymentResponse>) {
        mBillApiService.payBills(billRequest, object : CallBackWrapper<BillPaymentResponse> {
            override fun onError(errorCode: Int, errorMessage: String) {
                callBackWrapper.onError(errorCode, errorMessage)
            }

            override fun onSuccess(responseBody: BillPaymentResponse) {
                callBackWrapper.onSuccess(responseBody)
            }
        })
    }

    override fun getBankList(callBack: CallBackWrapper<BankListResponse>) {
        mBillApiService.getBankList(callBack)
    }

    override fun getPaymentStatus(requestId: String, callBack: CallBackWrapper<BillPaymentStatusResult>) {
        mBillApiService.getPaymentStatus(requestId, object : CallBackWrapper<BillPaymentStatusResult> {
            override fun onError(errorCode: Int, errorMessage: String) {
                callBack.onError(errorCode, errorMessage)
            }

            override fun onSuccess(responseBody: BillPaymentStatusResult) {
                callBack.onSuccess(responseBody)
            }
        })
    }
}