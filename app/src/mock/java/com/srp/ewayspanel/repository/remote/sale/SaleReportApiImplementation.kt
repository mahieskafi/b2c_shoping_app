package com.srp.ewayspanel.repository.remote.sale

import android.os.Handler
import com.google.gson.Gson
import com.srp.ewayspanel.model.sale.SaleSummaryReportRequest
import com.srp.ewayspanel.model.sale.SaleSummaryReportResponse
import com.srp.eways.network.CallBackWrapper
import com.srp.eways.repository.remote.BaseApiImplementation

/**
 * Created by Eskafi on 1/11/2020.
 */
class SaleReportApiImplementation : BaseApiImplementation(), SaleReportApiService {

    companion object {
        private var sINSTANCE: SaleReportApiImplementation? = null

        fun getInstance(): SaleReportApiImplementation {
            if (sINSTANCE == null) {
                sINSTANCE = SaleReportApiImplementation()
            }
            return sINSTANCE as SaleReportApiImplementation
        }
    }


    override fun getSalesSummaryReport(request: SaleSummaryReportRequest, callBackWrapper: CallBackWrapper<SaleSummaryReportResponse>) {

        var mSaleSummaryResult: SaleSummaryReportResponse = Gson().fromJson(mSaleSummaryReportMockResponce, SaleSummaryReportResponse::class.java)

        if (!handleCall(callBackWrapper)) {
            Handler().postDelayed({
                callBackWrapper.onSuccess(mSaleSummaryResult)
            }, getResponseDelay())
        }
    }

    private val mSaleSummaryReportMockResponce: String = "{\n" +
            "  \"Title\": \"خلاصه گزارش فروش از تاریخ 1397/10/10 الی 1398/10/10\",\n" +
            "  \"BuySummeries\": [\n" +
            "    {\n" +
            "      \"Groups\": \"استعلام کد پستی\",\n" +
            "      \"TotalRequestQuantity\": \"0\",\n" +
            "      \"TotalPaymentAmount\": 0\n" +
            "    },\n" +
            "    {\n" +
            "      \"Groups\": \"اعلام مفقودی یا سرقت\",\n" +
            "      \"TotalRequestQuantity\": \"0\",\n" +
            "      \"TotalPaymentAmount\": 0\n" +
            "    },\n" +
            "    {\n" +
            "      \"Groups\": \"بسته های پیامکی\",\n" +
            "      \"TotalRequestQuantity\": \"0\",\n" +
            "      \"TotalPaymentAmount\": 0\n" +
            "    },\n" +
            "    {\n" +
            "      \"Groups\": \"بلیط اتوبوس\",\n" +
            "      \"TotalRequestQuantity\": \"0(0)\",\n" +
            "      \"TotalPaymentAmount\": 0\n" +
            "    },\n" +
            "    {\n" +
            "      \"Groups\": \"بلیط قطار\",\n" +
            "      \"TotalRequestQuantity\": \"0(0)\",\n" +
            "      \"TotalPaymentAmount\": 0\n" +
            "    },\n" +
            "    {\n" +
            "      \"Groups\": \"بلیط هواپیما\",\n" +
            "      \"TotalRequestQuantity\": \"0(0)\",\n" +
            "      \"TotalPaymentAmount\": 0\n" +
            "    },\n" +
            "    {\n" +
            "      \"Groups\": \"شارژ بین المللی\",\n" +
            "      \"TotalRequestQuantity\": \"0\",\n" +
            "      \"TotalPaymentAmount\": 0\n" +
            "    },\n" +
            "    {\n" +
            "      \"Groups\": \"شارژ مستقیم\",\n" +
            "      \"TotalRequestQuantity\": \"48\",\n" +
            "      \"TotalPaymentAmount\": 685598\n" +
            "    },\n" +
            "    {\n" +
            "      \"Groups\": \"فروشگاه\",\n" +
            "      \"TotalRequestQuantity\": \"18\",\n" +
            "      \"TotalPaymentAmount\": 678370\n" +
            "    },\n" +
            "    {\n" +
            "      \"Groups\": \"قبض درگاه\",\n" +
            "      \"TotalRequestQuantity\": \"0\",\n" +
            "      \"TotalPaymentAmount\": 0\n" +
            "    },\n" +
            "    {\n" +
            "      \"Groups\": \"قبض سپرده\",\n" +
            "      \"TotalRequestQuantity\": \"2\",\n" +
            "      \"TotalPaymentAmount\": 640000\n" +
            "    },\n" +
            "    {\n" +
            "      \"Groups\": \"کارت شارژ\",\n" +
            "      \"TotalRequestQuantity\": \"0\",\n" +
            "      \"TotalPaymentAmount\": 0\n" +
            "    },\n" +
            "    {\n" +
            "      \"Groups\": \"کالای دیجیتال\",\n" +
            "      \"TotalRequestQuantity\": \"0\",\n" +
            "      \"TotalPaymentAmount\": 0\n" +
            "    },\n" +
            "    {\n" +
            "      \"Groups\": \"گیفت کارت\",\n" +
            "      \"TotalRequestQuantity\": \"0\",\n" +
            "      \"TotalPaymentAmount\": 0\n" +
            "    },\n" +
            "    {\n" +
            "      \"Groups\": \"لایسنس آنتی ویروس\",\n" +
            "      \"TotalRequestQuantity\": \"0\",\n" +
            "      \"TotalPaymentAmount\": 0\n" +
            "    },\n" +
            "    {\n" +
            "      \"Groups\": \"جمع کل بدون قبض\",\n" +
            "      \"TotalRequestQuantity\": \"66\",\n" +
            "      \"TotalPaymentAmount\": 1363968\n" +
            "    },\n" +
            "    {\n" +
            "      \"Groups\": \"جمع کل \",\n" +
            "      \"TotalRequestQuantity\": \"68\",\n" +
            "      \"TotalPaymentAmount\": 2003968\n" +
            "    }\n" +
            "  ],\n" +
            "  \"ReportStatus\": 1,\n" +
            "  \"Status\": 0,\n" +
            "  \"Description\": \"\"\n" +
            "}"
}