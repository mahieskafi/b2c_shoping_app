package com.srp.eways.repository.remote.bill.payment

import com.srp.eways.model.bill.payment.BillInquiryPayResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface BillInquiryPayApiRetrofit {

    @GET("service/v{version}/bill/inquirypay/{billId}/{paymentId}")
    fun getBillDetail(
            @Path("version")
            version: Int,
            @Path("billId")
            billId: String,
            @Path("paymentId")
            payId: String
    ): Call<BillInquiryPayResponse>
}