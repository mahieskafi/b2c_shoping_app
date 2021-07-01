package com.srp.eways.repository.remote.bill.payment

import android.os.Handler
import com.srp.eways.model.bill.archivedList.BillTemp
import com.srp.eways.model.bill.payment.BillInquiryPayResponse
import com.srp.eways.network.CallBackWrapper
import com.srp.eways.network.NetworkResponseCodes
import com.srp.eways.repository.remote.BaseApiImplementation

object BillInquiryPayApiImplementation : BaseApiImplementation(), BillInquiryPayApiService {


    override fun getBillDetail(billId: String, payId: String, callBack: CallBackWrapper<BillInquiryPayResponse>) {
        setMode(NetworkResponseCodes.SUCCESS)

        if (!handleCall(callBack)) {
            Handler().postDelayed({
                callBack.onSuccess(getBillResponse(billId, payId))
            }
                    , getResponseDelay())
        }
    }


    private fun getBillResponse(billId: String, payId: String): BillInquiryPayResponse {
        var billList = ArrayList<BillTemp>()


        var billTemp = BillTemp(id = 0, billId = billId, billTypeId = (1..8).random(), paymentId = payId, logDate = "2020-04-25T07:47:27.852Z", price = 2000, description = "")

        billList.add(billTemp)


        return BillInquiryPayResponse(status = 0, description = "", result = billList)
    }
}