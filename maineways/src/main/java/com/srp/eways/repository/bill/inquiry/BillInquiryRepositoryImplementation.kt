package com.srp.eways.repository.bill.inquiry

import com.srp.eways.di.DIMain
import com.srp.eways.model.bill.inquiry.BillInquiryRequest
import com.srp.eways.model.bill.inquiry.BillInquiryResponse
import com.srp.eways.network.CallBackWrapper
import com.srp.eways.repository.bill.MainBillRepositoryImplementation

/**
 * Created by ErfanG on 4/28/2020.
 */
object BillInquiryRepositoryImplementation : BillInquiryRepository, MainBillRepositoryImplementation() {


    private val mBillInquiryApiService = DIMain.getBillInquiryApi()

    override fun getBillDetails(body: BillInquiryRequest, callBack: CallBackWrapper<BillInquiryResponse>) {
        mBillInquiryApiService.getBillDetails(body, callBack)
    }
}