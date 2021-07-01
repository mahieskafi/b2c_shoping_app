package com.srp.eways.repository.remote.bill.report

import android.os.Handler
import com.srp.eways.model.bill.archivedList.BillTemp
import com.srp.eways.model.bill.archivedList.BillTempResponse
import com.srp.eways.model.bill.report.BillReportItem
import com.srp.eways.model.bill.report.BillReportRequest
import com.srp.eways.model.bill.report.BillReportResponse
import com.srp.eways.network.CallBackWrapper
import com.srp.eways.network.NetworkResponseCodes
import com.srp.eways.repository.remote.BaseApiImplementation

/**
 * Created by ErfanG on 5/23/2020.
 */
class BillReportApiImplementation : BaseApiImplementation(), BillReportApiService{

    override fun getBillReports(body: BillReportRequest, callBack: CallBackWrapper<BillReportResponse>) {

        setMode(NetworkResponseCodes.SUCCESS)

        if (!handleCall(callBack)) {
            Handler().postDelayed({
                callBack.onSuccess(getBillResponse())
            }
                    , getResponseDelay())
        }
    }
    private fun getBillResponse(): BillReportResponse {
        var billList = ArrayList<BillReportItem>()

        for (i in 0..6) {
            var billTemp = BillReportItem(id = i, billId = "425345645", billTypeId = (1..8).random(), paymentId = "425345645", logDate = "2020-04-25T07:47:27.852Z", price = 450000, description = "")

            billList.add(billTemp)
        }

        return BillReportResponse(status = 0, description = "", data = billList)
    }

}