package com.srp.eways.repository.remote.deposit.transaction

import com.srp.eways.model.deposit.transaction.DepositTransactionResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by Eskafi on 9/3/2019.
 */
interface DepositTransactionApiRetrofit {

    @GET("service/v{version}/credit/Inquiry/{pageIndex}/{pageSize}")
    fun getTransactionList(
        @Path("version") version: Int,
        @Path("pageIndex") pageIndex: Int,
        @Path("pageSize") pageSize: Int): Call<DepositTransactionResponse>

}