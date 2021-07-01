package com.srp.eways.ui.bill.paymenttype

import com.srp.eways.model.bill.BillPaymentResponse
import com.srp.eways.ui.main.MainRootIds

interface PaymentResponseListener {

    fun onPaymentResponseListener(paymentResponse: BillPaymentResponse)

    fun onDeepLinkResponseReceived(requestId :String)
}