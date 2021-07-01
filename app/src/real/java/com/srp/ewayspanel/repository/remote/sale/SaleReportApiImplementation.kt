package com.srp.ewayspanel.repository.remote.sale

import com.srp.ewayspanel.AppConfig
import com.srp.ewayspanel.di.DI
import com.srp.ewayspanel.model.sale.SaleSummaryReportRequest
import com.srp.ewayspanel.model.sale.SaleSummaryReportResponse
import com.srp.eways.network.CallBackWrapper
import com.srp.eways.repository.remote.BaseApiImplementation
import com.srp.eways.repository.remote.DefaultRetroCallback
import com.srp.ewayspanel.model.sale.detail.SaleDetailReportRequest
import com.srp.ewayspanel.model.sale.detail.SaleDetailReportResponse

/**
 * Created by Eskafi on 1/11/2020.
 */
class SaleReportApiImplementation : BaseApiImplementation(), SaleReportApiService {

    private val mRetrofit: SaleReportApiRetrofit

    companion object {

        private var sINSTANCE: SaleReportApiImplementation? = null

        fun getInstance(): SaleReportApiImplementation {

            if (sINSTANCE == null) {
                sINSTANCE = SaleReportApiImplementation()
            }
            return sINSTANCE as SaleReportApiImplementation
        }
    }

    init {
        mRetrofit = DI.provideApi(SaleReportApiRetrofit::class.java)
    }

    override fun getSalesSummaryReport(request: SaleSummaryReportRequest, callBackWrapper: CallBackWrapper<SaleSummaryReportResponse>) {
        mRetrofit.getSaleSummaryReport(AppConfig.SERVER_VERSION, request).enqueue(DefaultRetroCallback<SaleSummaryReportResponse>(callBackWrapper))
    }

    override fun getSalesDetailReport(request: SaleDetailReportRequest, callBackWrapper: CallBackWrapper<SaleDetailReportResponse>) {
        mRetrofit.getSaleDetailReport(AppConfig.SERVER_VERSION, request).enqueue(DefaultRetroCallback<SaleDetailReportResponse>(callBackWrapper))
    }
}