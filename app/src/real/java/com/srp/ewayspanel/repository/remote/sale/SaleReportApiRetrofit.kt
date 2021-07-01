package com.srp.ewayspanel.repository.remote.sale

import com.srp.ewayspanel.model.sale.SaleSummaryReportRequest
import com.srp.ewayspanel.model.sale.SaleSummaryReportResponse
import com.srp.ewayspanel.model.sale.detail.SaleDetailReportRequest
import com.srp.ewayspanel.model.sale.detail.SaleDetailReportResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Created by Eskafi on 1/11/2020.
 */
interface SaleReportApiRetrofit {

    @POST("service/v{version}/credit/salessummaryreport")
    fun getSaleSummaryReport(@Path("version") version: Int, @Body request: SaleSummaryReportRequest): Call<SaleSummaryReportResponse>

    @POST("service/v{version}/credit/salesdetailreport")
    fun getSaleDetailReport(@Path("version") version: Int, @Body request: SaleDetailReportRequest): Call<SaleDetailReportResponse>
}