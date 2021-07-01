package com.srp.eways.repository.remote.bill.report

import com.srp.eways.AppConfig
import com.srp.eways.di.DIMain
import com.srp.eways.model.bill.report.BillReportRequest
import com.srp.eways.model.bill.report.BillReportResponse
import com.srp.eways.network.CallBackWrapper
import com.srp.eways.repository.remote.DefaultRetroCallback

/**
 * Created by ErfanG on 5/23/2020.
 */
class BillReportApiImplementation : BillReportApiService{

    private val mRetrofit = DIMain.provideApi(BillReportApiRetrofit::class.java)

    override fun getBillReports(body: BillReportRequest, callBack: CallBackWrapper<BillReportResponse>) {

        mRetrofit.getBillReports(AppConfig.SERVER_VERSION, body).enqueue(DefaultRetroCallback<BillReportResponse>(callBack))
    }


}