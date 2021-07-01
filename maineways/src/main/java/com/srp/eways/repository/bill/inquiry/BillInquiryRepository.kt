package com.srp.eways.repository.bill.inquiry

import com.srp.eways.model.bill.inquiry.BillInquiryRequest
import com.srp.eways.model.bill.inquiry.BillInquiryResponse
import com.srp.eways.network.CallBackWrapper
import com.srp.eways.repository.bill.MainBillRepository

/**
 * Created by ErfanG on 4/28/2020.
 */
interface BillInquiryRepository : MainBillRepository{

    fun getBillDetails(
            body: BillInquiryRequest,
            callBack : CallBackWrapper<BillInquiryResponse>
    )
}