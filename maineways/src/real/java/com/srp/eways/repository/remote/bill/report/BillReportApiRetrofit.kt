package com.srp.eways.repository.remote.bill.report

import com.srp.eways.model.bill.report.BillReportRequest
import com.srp.eways.model.bill.report.BillReportResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Created by ErfanG on 5/23/2020.
 */
interface BillReportApiRetrofit {

    @POST("service/v{version}/bill/billreportdata")
    fun getBillReports(
            @Path("version")
            version: Int,
            @Body
            body: BillReportRequest
    ) : Call<BillReportResponse>
}