package com.srp.eways.model.bill

import com.google.gson.annotations.SerializedName
import com.srp.eways.model.bill.archivedList.BillPaymentDetail

data class BillPaymentRequest
(
        @SerializedName("Bills")
        var bills: ArrayList<BillPaymentDetail>,

        @SerializedName("BillPaymentWay")
        var billPaymentWay: Int
)