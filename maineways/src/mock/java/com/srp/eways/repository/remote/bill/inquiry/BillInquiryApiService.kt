package com.srp.eways.repository.remote.bill.inquiry

import com.srp.eways.model.bill.inquiry.BillInquiryRequest
import com.srp.eways.model.bill.inquiry.BillInquiryResponse
import com.srp.eways.network.CallBackWrapper

/**
 * Created by ErfanG on 4/28/2020.
 */
interface BillInquiryApiService {
    fun getBillDetails(
            body: BillInquiryRequest,
            callBack : CallBackWrapper<BillInquiryResponse>
    )
}