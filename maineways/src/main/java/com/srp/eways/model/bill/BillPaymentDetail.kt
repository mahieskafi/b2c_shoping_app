package com.srp.eways.model.bill.archivedList

import com.google.gson.annotations.SerializedName

data class BillPaymentDetail
(
        @SerializedName("Id")
        var id: Int,

        @SerializedName("BillId")
        var billId: String,

        @SerializedName("PayId")
        var payId: String,

        @SerializedName("InquiryNumber")
        var inquiryNumber: String = ""
) {
    constructor(billId: String, payId: String) : this(0, billId, payId, "")
    constructor(billId: String, payId: String, inquiryNumber: String) : this(0, billId, payId, inquiryNumber)
}