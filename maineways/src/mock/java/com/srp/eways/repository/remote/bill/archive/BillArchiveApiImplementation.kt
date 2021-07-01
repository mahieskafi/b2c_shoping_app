package com.srp.eways.repository.remote.bill.archive

import android.os.Handler
import com.srp.eways.model.bill.BillPaymentRequest
import com.srp.eways.model.bill.BillPaymentResponse
import com.srp.eways.model.bill.BillPaymentResult
import com.srp.eways.model.bill.archivedList.BillTemp
import com.srp.eways.model.bill.archivedList.BillTempRemoveResponse
import com.srp.eways.model.bill.archivedList.BillTempResponse
import com.srp.eways.network.CallBackWrapper
import com.srp.eways.network.NetworkResponseCodes
import com.srp.eways.repository.remote.BaseApiImplementation

open class BillArchiveApiImplementation : BaseApiImplementation(), BillArchiveApiService {

    companion object {
        val instant = BillArchiveApiImplementation()
    }

    override fun getTempBills(pageIndex: Int, pageSize: Int, callBackWrapper: CallBackWrapper<BillTempResponse>) {
        setMode(NetworkResponseCodes.SUCCESS)

        if (!handleCall(callBackWrapper)) {
            Handler().postDelayed({
                callBackWrapper.onSuccess(getBillResponse(pageIndex))
            }
                    , getResponseDelay())
        }
    }

    private fun getBillResponse(pageIndex: Int): BillTempResponse {
        var billList = ArrayList<BillTemp>()

        for (i in pageIndex..pageIndex + 3) {
            var billTemp = BillTemp(id = i, billId = "425345645", billTypeId = (1..8).random(), paymentId = "425345645", logDate = "2020-04-25T07:47:27.852Z", price = 450000, description = "")

            billList.add(billTemp)
        }

        return BillTempResponse(status = 0, description = "", data = billList)
    }

    override fun removeTempBills(id: Int, callBackWrapper: CallBackWrapper<BillTempRemoveResponse>) {
        setMode(NetworkResponseCodes.SUCCESS)

        if (!handleCall(callBackWrapper)) {
            Handler().postDelayed({
                callBackWrapper.onSuccess(getRemoveResponse())
            }
                    , getResponseDelay())
        }

    }

    private fun getRemoveResponse(): BillTempRemoveResponse {
        return BillTempRemoveResponse(status = 0, description = "")
    }

}