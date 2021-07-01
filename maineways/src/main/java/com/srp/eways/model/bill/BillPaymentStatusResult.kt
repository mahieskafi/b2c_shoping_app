package com.srp.eways.model.bill

import com.google.gson.annotations.SerializedName
import com.srp.eways.model.bill.archivedList.BillTemp

/**
 * Created by Eskafi on 7/11/2020.
 */
data class BillPaymentStatusResult(

        @SerializedName("Result")
        val bills: ArrayList<BillTemp>?,

        @SerializedName("Status")
        val status: Int = 0,

        @SerializedName("Description")
        val description: String = ""
)