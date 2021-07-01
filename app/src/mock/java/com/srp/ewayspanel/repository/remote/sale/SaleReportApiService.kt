package com.srp.ewayspanel.repository.remote.sale

import com.srp.ewayspanel.model.sale.SaleSummaryReportRequest
import com.srp.ewayspanel.model.sale.SaleSummaryReportResponse
import com.srp.eways.network.CallBackWrapper

/**
 * Created by Eskafi on 1/11/2020.
 */
interface SaleReportApiService {

    fun getSalesSummaryReport(request: SaleSummaryReportRequest, callBackWrapper: CallBackWrapper<SaleSummaryReportResponse>)
}
