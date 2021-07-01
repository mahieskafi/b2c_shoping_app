package com.srp.eways.repository.remote.chargetransaction

import com.srp.eways.di.DIMain
import com.srp.eways.model.transaction.charge.ChargeTransactionRequest
import com.srp.eways.model.transaction.charge.ChargeTransactionResponse
import com.srp.eways.model.transaction.inquiry.InquiryRequest
import com.srp.eways.model.transaction.inquiry.InquiryTopupNumberResponse
import com.srp.eways.network.CallBackWrapper
import com.srp.eways.repository.remote.BaseApiImplementation
import com.srp.eways.repository.remote.DefaultRetroCallback

/**
 * Created by ErfanG on 02/09/2019.
 */
class ChargeTransactionApiImplementation : BaseApiImplementation(), ChargeTransactionApiService {


    private val mRetrofit: ChargeTransactionApiRetrofit

    init {
        mRetrofit = DIMain.provideApi(ChargeTransactionApiRetrofit::class.java)
    }

    companion object {
        val instance = ChargeTransactionApiImplementation()
    }

    override fun loadData(version: Int, body: ChargeTransactionRequest,
                          callBack :CallBackWrapper<ChargeTransactionResponse>){

        mRetrofit.loadData(version, body).enqueue(DefaultRetroCallback<ChargeTransactionResponse>(callBack))
    }

    override fun loadData(version: Int, mobileNumber: String, body: InquiryRequest, callback: CallBackWrapper<InquiryTopupNumberResponse>) {
        mRetrofit.loadData(version, mobileNumber, body).enqueue(DefaultRetroCallback(callback))
    }
}