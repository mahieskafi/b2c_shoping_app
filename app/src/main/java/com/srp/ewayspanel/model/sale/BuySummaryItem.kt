package com.srp.ewayspanel.model.sale

import com.google.gson.annotations.SerializedName

/**
 * Created by Eskafi on 1/11/2020.
 */

data class BuySummaryItem
(
        @SerializedName("Groups")
        var groups: String,

        @SerializedName("TotalRequestQuantity")
        var totalRequestQuantity: String,

        @SerializedName("TotalPaymentAmount")
        var totalPaymentAmount: Long
)