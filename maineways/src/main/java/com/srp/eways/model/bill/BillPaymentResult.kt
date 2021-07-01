package com.srp.eways.model.bill

import com.google.gson.annotations.SerializedName
import com.srp.eways.model.bill.archivedList.BillTemp

data class BillPaymentResult(

        @SerializedName("Bills")
        val bills: ArrayList<BillTemp>?,

        @SerializedName("BillPayStatus")
        val billPayStatus: Int = 0,

        @SerializedName("BillPaymentGetWay")
        val billPaymentGetWay: Int = 0,

        @SerializedName("BatchId")
        val batchId: Int = 0
)