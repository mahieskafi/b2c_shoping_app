package com.srp.eways.repository.bill.payment

import com.srp.eways.model.bill.payment.BillInquiryPayResponse
import com.srp.eways.network.CallBackWrapper

interface BillInquiryPayRepository {

    fun getBillDetail(
            billId: String,
            payId: String,
            callBack: CallBackWrapper<BillInquiryPayResponse>
    )
}