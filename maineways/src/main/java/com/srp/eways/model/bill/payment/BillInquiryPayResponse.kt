package com.srp.eways.model.bill.payment

import com.google.gson.annotations.SerializedName
import com.srp.eways.model.bill.archivedList.BillTemp

data class BillInquiryPayResponse(

        @SerializedName("Result")
        val result: ArrayList<BillTemp>? = arrayListOf(),

        @SerializedName("Status")
        val status: Int,

        @SerializedName("Description")
        val description: String
)