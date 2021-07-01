package com.srp.eways.repository.remote.bill

import com.srp.eways.model.bill.BillPaymentRequest
import com.srp.eways.model.bill.BillPaymentResponse
import com.srp.eways.model.bill.BillPaymentStatusResult
import com.srp.eways.model.deposit.BankListResponse
import com.srp.eways.network.CallBackWrapper

/**
 * Created by ErfanG on 5/3/2020.
 */
interface BillApiService {

    fun saveTempBills(billRequest: BillPaymentRequest, callBackWrapper: CallBackWrapper<BillPaymentResponse>)

    fun payBills(billRequest: BillPaymentRequest, callBackWrapper: CallBackWrapper<BillPaymentResponse>)

    fun getBankList(callBack: CallBackWrapper<BankListResponse>)

    fun getPaymentStatus(requestId: String, callBack: CallBackWrapper<BillPaymentStatusResult>)
}