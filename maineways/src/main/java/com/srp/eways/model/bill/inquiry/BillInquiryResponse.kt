package com.srp.eways.model.bill.inquiry


import com.google.gson.annotations.SerializedName

data class BillInquiryResponse(
        @SerializedName("BillId")
    val billId: String? = null,
        @SerializedName("BillType")
    var billType: String? = null,
        @SerializedName("Description")
    val description: String? = "",
        @SerializedName("LastTermBill")
    val lastTermBill: TermBill? = null,
        @SerializedName("MidTermBill")
    val midTermBill: TermBill? = null,
        @SerializedName("Number")
    val number: String? = "",
        @SerializedName("Status")
    val status: Int? = 0
)