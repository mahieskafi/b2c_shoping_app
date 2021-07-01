package com.srp.eways.repository.remote.chargetransaction

import android.os.Handler
import com.srp.eways.model.transaction.charge.ChargeSale
import com.srp.eways.model.transaction.charge.ChargeTransactionRequest
import com.srp.eways.model.transaction.charge.ChargeTransactionResponse
import com.srp.eways.model.transaction.inquiry.InquiryRequest
import com.srp.eways.model.transaction.inquiry.InquiryTopup
import com.srp.eways.model.transaction.inquiry.InquiryTopupNumberResponse
import com.srp.eways.network.CallBackWrapper
import com.srp.eways.repository.remote.BaseApiImplementation

/**
 * Created by ErfanG on 01/09/2019.
 */
class ChargeTransactionApiImplementation : BaseApiImplementation(), ChargeTransactionApiService {
    private var mHalfResult = false

    companion object {
        val instance = ChargeTransactionApiImplementation()
    }

    override fun loadData(version: Int, body: ChargeTransactionRequest, callBack :CallBackWrapper<ChargeTransactionResponse>){

        if(!handleCall(callBack)){
            Handler().postDelayed({
                callBack.onSuccess(handelInternal(body))
            }, getResponseDelay())
        }
    }

    private fun handelInternal(body: ChargeTransactionRequest): ChargeTransactionResponse {

        var list: ArrayList<ChargeSale> = ArrayList()

        var start = body.PageIndex * body.PageSize
        var end: Int =
                if (!mHalfResult)
                    body.PageIndex * body.PageSize + body.PageSize
                else
                    body.PageIndex * body.PageSize + (body.PageSize / 2).toInt()

        for (i in start until end)
            list.add(ChargeSale(paymentValue = i, productName = "شارژ مستقیم", customerMobile = "09187888582",
                    paymentDate = "1398/06/12 15:15:15", deliverStatus = 2))


        var data = ChargeTransactionResponse(chargeSales = list)

        return data
    }

    public fun setHalfResult(bool: Boolean) {
        mHalfResult = bool
    }

    override fun loadData(version: Int, mobileNumber: String, body: InquiryRequest, callBack: CallBackWrapper<InquiryTopupNumberResponse>) {
        if (!handleCall()) {
            callBack.onSuccess(handelInquiryInternal())
        } else {
            callBack.onError(getMode(), "")
        }
    }

    private fun handelInquiryInternal(): InquiryTopupNumberResponse {

        var list: ArrayList<InquiryTopup> = ArrayList()

        for (i in 0 until 10)
            list.add(InquiryTopup(price = "450000", requestId = "0",
                    requestDate = "1398/06/12 15:15:15", status = "2"))


        var data = InquiryTopupNumberResponse(topupList = list)

        return data
    }
}