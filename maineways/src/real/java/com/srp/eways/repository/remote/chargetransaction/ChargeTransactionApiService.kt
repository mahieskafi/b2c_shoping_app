package com.srp.eways.repository.remote.chargetransaction

import com.srp.eways.model.transaction.charge.ChargeTransactionRequest
import com.srp.eways.model.transaction.charge.ChargeTransactionResponse
import com.srp.eways.model.transaction.inquiry.InquiryRequest
import com.srp.eways.model.transaction.inquiry.InquiryTopupNumberResponse
import com.srp.eways.network.CallBackWrapper

/**
 * Created by ErfanG on 01/09/2019.
 */
interface ChargeTransactionApiService {

    fun loadData(
            version: Int,
            body: ChargeTransactionRequest,
            callBack :CallBackWrapper<ChargeTransactionResponse>
    )

    fun loadData(
            version: Int,
            mobileNumber: String,
            body: InquiryRequest,
            callBack: CallBackWrapper<InquiryTopupNumberResponse>
    )

}