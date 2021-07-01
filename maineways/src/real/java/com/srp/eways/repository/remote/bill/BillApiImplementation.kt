package com.srp.eways.repository.remote.bill

import com.srp.eways.AppConfig
import com.srp.eways.di.DIMain
import com.srp.eways.model.bill.BillPaymentRequest
import com.srp.eways.model.bill.BillPaymentResponse
import com.srp.eways.model.bill.BillPaymentStatusResult
import com.srp.eways.model.deposit.BankListResponse
import com.srp.eways.network.CallBackWrapper
import com.srp.eways.repository.remote.DefaultRetroCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by ErfanG on 5/3/2020.
 */
object BillApiImplementation : BillApiService {

    private val billApiRetrofit: BillApiRetrofit = DIMain.provideApi(BillApiRetrofit::class.java)


    override fun saveTempBills(billRequest: BillPaymentRequest, callBackWrapper: CallBackWrapper<BillPaymentResponse>) {
        billApiRetrofit.saveTempBill(AppConfig.SERVER_VERSION, billRequest).enqueue(object : DefaultRetroCallback<BillPaymentResponse>(callBackWrapper) {
            override fun checkResponseForError(response: BillPaymentResponse?, errorInfo: ErrorInfo?) {
                if (response?.status != 7) {
                    errorInfo?.errorCode = response?.status
                    errorInfo?.errorMessage = response?.description
                }
            }
        })
    }

    override fun payBills(billRequest: BillPaymentRequest, callBackWrapper: CallBackWrapper<BillPaymentResponse>) {
        billApiRetrofit.payBills(AppConfig.SERVER_VERSION, billRequest)
                .enqueue(DefaultRetroCallback<BillPaymentResponse>(callBackWrapper))
    }

    override fun getBankList(callBack: CallBackWrapper<BankListResponse>) {

        billApiRetrofit.bankList(AppConfig.SERVER_VERSION).enqueue(object : DefaultRetroCallback<BankListResponse>(callBack) {

            override fun checkResponseForError(response: BankListResponse?, errorInfo: ErrorInfo?) {
                if (response?.status != 0) {
                    errorInfo?.errorCode = response?.status
                    errorInfo?.errorMessage = response?.description
                }
            }

        })
    }


    override fun getPaymentStatus(requestId: String, callBack: CallBackWrapper<BillPaymentStatusResult>) {
        billApiRetrofit.getPaymentStatus(AppConfig.SERVER_VERSION, requestId).enqueue(object : Callback<BillPaymentStatusResult> {

            override fun onFailure(call: Call<BillPaymentStatusResult>, t: Throwable) {
                val retrofitCallback = DefaultRetroCallback(callBack)
                retrofitCallback.onFailure(call, t)
            }

            override fun onResponse(call: Call<BillPaymentStatusResult>, response: Response<BillPaymentStatusResult>) {
                callBack.onSuccess(response.body())
            }

        })
    }
}