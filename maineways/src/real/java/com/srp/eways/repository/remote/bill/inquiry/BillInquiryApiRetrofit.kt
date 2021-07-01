package com.srp.eways.repository.remote.bill.inquiry

import com.srp.eways.model.bill.inquiry.BillInquiryRequest
import com.srp.eways.model.bill.inquiry.BillInquiryResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Created by ErfanG on 4/28/2020.
 */
interface BillInquiryApiRetrofit {

    @POST("service/v{version}/bill/debtinquiry")
    fun getBillDetails(
            @Path("version")
            version: Int,
            @Body
            body: BillInquiryRequest
    ) : Call<BillInquiryResponse>
}