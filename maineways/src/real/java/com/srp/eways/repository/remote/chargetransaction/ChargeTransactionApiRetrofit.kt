package com.srp.eways.repository.remote.chargetransaction

import com.srp.eways.model.transaction.charge.ChargeTransactionRequest
import com.srp.eways.model.transaction.charge.ChargeTransactionResponse
import com.srp.eways.model.transaction.inquiry.InquiryRequest
import com.srp.eways.model.transaction.inquiry.InquiryTopupNumberResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Created by ErfanG on 02/09/2019.
 */
interface ChargeTransactionApiRetrofit {

    @POST("service/v{version}/credit/SalesDetailReport")
    fun loadData(
            @Path("version")
            version: Int,
            @Body
            body: ChargeTransactionRequest
    ): Call<ChargeTransactionResponse>

    @POST("service/v{version}/charge/InquiryTopupNumber/{mobileNumber}")
    fun loadData(
            @Path("version")
            version: Int,
            @Path("mobileNumber")
            mobileNumber: String,
            @Body
            body: InquiryRequest
    ): Call<InquiryTopupNumberResponse>
}