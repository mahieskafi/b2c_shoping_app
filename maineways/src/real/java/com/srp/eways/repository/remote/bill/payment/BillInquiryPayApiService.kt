package com.srp.eways.repository.remote.bill.payment

import com.srp.eways.model.bill.payment.BillInquiryPayResponse
import com.srp.eways.network.CallBackWrapper

interface BillInquiryPayApiService {

    fun getBillDetail(
            billId: String,
            payId: String,
            callBack: CallBackWrapper<BillInquiryPayResponse>
    )
}