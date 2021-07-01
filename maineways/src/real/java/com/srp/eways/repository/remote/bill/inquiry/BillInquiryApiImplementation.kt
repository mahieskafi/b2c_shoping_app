package com.srp.eways.repository.remote.bill.inquiry

import com.srp.eways.AppConfig
import com.srp.eways.di.DIMain
import com.srp.eways.model.bill.inquiry.BillInquiryRequest
import com.srp.eways.model.bill.inquiry.BillInquiryResponse
import com.srp.eways.network.CallBackWrapper
import com.srp.eways.repository.remote.BaseApiImplementation
import com.srp.eways.repository.remote.DefaultRetroCallback

/**
 * Created by ErfanG on 4/28/2020.
 */
object BillInquiryApiImplementation : BaseApiImplementation(), BillInquiryApiService {

    private val mRetrofit = DIMain.provideApi(BillInquiryApiRetrofit::class.java)


    override fun getBillDetails(body: BillInquiryRequest, callBack: CallBackWrapper<BillInquiryResponse>) {

        mRetrofit.getBillDetails(AppConfig.SERVER_VERSION, body).enqueue(object : DefaultRetroCallback<BillInquiryResponse>(callBack){
            override fun checkResponseForError(response: BillInquiryResponse, errorInfo: ErrorInfo) {
                if (response.status != 2200 && response.status != 0) {

                    errorInfo.errorCode = response.status!!
                    errorInfo.errorMessage = response.description
                } else {

                    super.checkResponseForError(response, errorInfo)
                }
            }
        })

    }
}