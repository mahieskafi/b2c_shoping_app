package com.srp.eways.repository.remote.bill.payment

import com.srp.eways.AppConfig
import com.srp.eways.di.DIMain
import com.srp.eways.model.bill.payment.BillInquiryPayResponse
import com.srp.eways.network.CallBackWrapper
import com.srp.eways.repository.remote.BaseApiImplementation
import com.srp.eways.repository.remote.DefaultRetroCallback

object BillInquiryPayApiImplementation : BaseApiImplementation(), BillInquiryPayApiService {

    private val mRetrofit = DIMain.provideApi(BillInquiryPayApiRetrofit::class.java)

    override fun getBillDetail(billId: String, payId: String, callBack: CallBackWrapper<BillInquiryPayResponse>) {
        mRetrofit.getBillDetail(AppConfig.SERVER_VERSION, billId, payId).enqueue(object : DefaultRetroCallback<BillInquiryPayResponse>(callBack) {
            override fun checkResponseForError(response: BillInquiryPayResponse, errorInfo: ErrorInfo) {
                if (response.status != 0) {

                    errorInfo.errorCode = response.status!!
                    errorInfo.errorMessage = response.description
                } else {

                    super.checkResponseForError(response, errorInfo)
                }
            }
        })
    }
}