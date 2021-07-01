package com.srp.ewayspanel.repository.remote.sale

import com.srp.ewayspanel.model.sale.SaleSummaryReportRequest
import com.srp.ewayspanel.model.sale.SaleSummaryReportResponse
import com.srp.eways.network.CallBackWrapper
import com.srp.ewayspanel.model.sale.detail.SaleDetailReportRequest
import com.srp.ewayspanel.model.sale.detail.SaleDetailReportResponse

/**
 * Created by Eskafi on 1/11/2020.
 */
interface SaleReportApiService {

    fun getSalesSummaryReport(request: SaleSummaryReportRequest, callBackWrapper: CallBackWrapper<SaleSummaryReportResponse>)

    fun getSalesDetailReport(request: SaleDetailReportRequest, callBackWrapper: CallBackWrapper<SaleDetailReportResponse>)
}