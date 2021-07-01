package com.srp.eways.model.bill.inquiry


import com.google.gson.annotations.SerializedName

data class BillInquiryRequest(
    @SerializedName("areaCode")
    val areaCode: String = "",
    @SerializedName("billId")
    val billId: String = "",
    @SerializedName("mobile")
    val mobile: String = "",
    @SerializedName("phone")
    val phone: String = ""
)