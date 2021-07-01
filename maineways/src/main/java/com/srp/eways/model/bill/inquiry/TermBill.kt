package com.srp.eways.model.bill.inquiry


import com.google.gson.annotations.SerializedName

data class TermBill(
    @SerializedName("BillId")
    val billId: String? = "",
    @SerializedName("Description")
    val description: String? = "",
    @SerializedName("PayId")
    val payId: String? = "",
    @SerializedName("Price")
    val price: Long? = 0,
    @SerializedName("Status")
    val status: Int? = 0
)