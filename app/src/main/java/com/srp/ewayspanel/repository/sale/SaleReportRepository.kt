package com.srp.ewayspanel.repository.sale

import com.srp.eways.network.CallBackWrapper
import com.srp.ewayspanel.model.sale.SaleSummaryReportRequest
import com.srp.ewayspanel.model.sale.SaleSummaryReportResponse
import com.srp.ewayspanel.model.sale.detail.SaleDetailReportRequest
import com.srp.ewayspanel.model.sale.detail.SaleDetailReportResponse

/**
 * Created by Eskafi on 1/11/2020.
 */

interface SaleReportRepository {
    fun getSalesSummaryReport(request: SaleSummaryReportRequest, callback: CallBackWrapper<SaleSummaryReportResponse>)

    fun getSalesDetailReport(request: SaleDetailReportRequest, callBackWrapper: CallBackWrapper<SaleDetailReportResponse>)

    fun hasMoreDetailList(): Boolean

    fun getPageIndexDetailList(): Int

    fun clearDetailList()
}