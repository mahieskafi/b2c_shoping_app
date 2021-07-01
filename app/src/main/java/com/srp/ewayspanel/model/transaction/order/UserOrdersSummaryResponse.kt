package com.srp.ewayspanel.model.transaction.order

import com.google.gson.annotations.SerializedName

/**
 * Created by Eskafi on 2/5/2020.
 */

data class UserOrdersSummaryResponse
(
        @SerializedName("Description")
        var description: String? = "",

        @SerializedName("Status")
        var status: Int = 0,

        @SerializedName("OrderSummaryResult")
        var orderSummaryResult: OrderSummaryResult? = null
) {
    constructor(errorCode: Int, errorMessage: String) : this(description = errorMessage, status = errorCode)
}