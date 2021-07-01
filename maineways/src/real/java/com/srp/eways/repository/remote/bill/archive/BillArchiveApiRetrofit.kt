package com.srp.eways.repository.remote.bill.archive

import com.srp.eways.model.bill.archivedList.BillTempRemoveResponse
import com.srp.eways.model.bill.archivedList.BillTempResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface BillArchiveApiRetrofit
{
    @GET("service/v{version}/bill/gettempbills/{pageIndex}/{pageSize}")
    fun getTempBills(
            @Path("version")
            version: Int,
            @Path("pageIndex")
            pageIndex: Int,
            @Path("pageSize")
            pageSize: Int
    ): Call<BillTempResponse>

    @GET("service/v{version}/bill/removebilltemp/{id}")
    fun removeTempBill(
            @Path("version")
            version: Int,
            @Path("id")
            id: Int
    ): Call<BillTempRemoveResponse>
}