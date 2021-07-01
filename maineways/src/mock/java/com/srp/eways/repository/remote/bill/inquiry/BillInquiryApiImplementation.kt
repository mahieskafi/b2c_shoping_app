package com.srp.eways.repository.remote.bill.inquiry


import android.os.Handler
import com.srp.eways.AppConfig
import com.srp.eways.di.DIMain
import com.srp.eways.model.bill.inquiry.BillInquiryRequest
import com.srp.eways.model.bill.inquiry.BillInquiryResponse
import com.srp.eways.model.bill.inquiry.TermBill
import com.srp.eways.network.CallBackWrapper
import com.srp.eways.network.NetworkResponseCodes
import com.srp.eways.repository.remote.BaseApiImplementation

/**
 * Created by ErfanG on 4/28/2020.
 */
object BillInquiryApiImplementation : BaseApiImplementation(), BillInquiryApiService {


    override fun getBillDetails(body: BillInquiryRequest, callBack: CallBackWrapper<BillInquiryResponse>) {
        setMode(NetworkResponseCodes.SUCCESS)

        if (!handleCall(callBack)) {
            Handler().postDelayed({
                callBack.onSuccess(getResponse())
            }
                    , getResponseDelay())
        }
    }

    private fun getResponse(): BillInquiryResponse {
        var termBill = TermBill(status = 0, description = "اطلاعات با موفقیت ثبت گردید", billId = "458280921145", payId = "35390245", price = 353000)
        var midTermBill = TermBill(status = 0, description = "اطلاعات با موفقیت ثبت گردید", billId = "458280921145", payId = "35390245", price = 353000)

        return BillInquiryResponse(billId = "null", lastTermBill = termBill, midTermBill = midTermBill)
    }

}