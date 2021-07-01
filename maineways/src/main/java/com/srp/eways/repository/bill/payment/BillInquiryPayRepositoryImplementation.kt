package com.srp.eways.repository.bill.payment

import com.srp.eways.di.DIMain
import com.srp.eways.model.bill.payment.BillInquiryPayResponse
import com.srp.eways.network.CallBackWrapper

object BillInquiryPayRepositoryImplementation : BillInquiryPayRepository {

    private val mBillInquiryPayApiService = DIMain.getBillInquiryPayApi()

    override fun getBillDetail(billId: String, payId: String, callBack: CallBackWrapper<BillInquiryPayResponse>) {
        mBillInquiryPayApiService.getBillDetail(billId, payId, callBack)
    }
}