package com.srp.eways.repository.remote.bill

import com.srp.eways.model.bill.BillPaymentRequest
import com.srp.eways.model.bill.BillPaymentResponse
import com.srp.eways.model.bill.BillPaymentStatusResult
import com.srp.eways.model.bill.archivedList.BillTemp
import com.srp.eways.model.deposit.BankListResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Created by ErfanG on 5/3/2020.
 */
interface BillApiRetrofit {

    @POST("service/v{version}/bill/savebills")
    fun saveTempBill(
            @Path("version")
            version: Int,
            @Body
            bill : BillPaymentRequest
    ): Call<BillPaymentResponse>

    @POST("service/v{version}/bill/pay")
    fun payBills(
            @Path("version")
            version: Int,
            @Body
            bill : BillPaymentRequest
    ): Call<BillPaymentResponse>

    @GET("service/v{version}/bill/getbanks")
    fun bankList(@Path("version") version: Int): Call<BankListResponse>

    @GET("service/v{version}/bill/rechecktransaction/{requestId}")
    fun getPaymentStatus(
            @Path("version") version: Int,
            @Path("requestId") requestId: String
    ): Call<BillPaymentStatusResult>
}