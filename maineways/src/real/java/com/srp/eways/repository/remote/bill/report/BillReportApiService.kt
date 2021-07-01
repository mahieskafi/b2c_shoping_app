package com.srp.eways.repository.remote.bill.report

import com.srp.eways.model.bill.report.BillReportRequest
import com.srp.eways.model.bill.report.BillReportResponse
import com.srp.eways.network.CallBackWrapper

/**
 * Created by ErfanG on 5/23/2020.
 */
interface BillReportApiService {

    fun getBillReports(body: BillReportRequest, callBack : CallBackWrapper<BillReportResponse>)
}