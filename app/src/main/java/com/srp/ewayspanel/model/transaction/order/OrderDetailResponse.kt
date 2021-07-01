package com.srp.ewayspanel.model.transaction.order

import com.google.gson.annotations.SerializedName

/**
 * Created by Eskafi on 2/8/2020.
 */

data class OrderDetailResponse
(
        @SerializedName("Description")
        var description: String? = "",

        @SerializedName("Status")
        var status: Int = 0,

        @SerializedName("OrderDetails")
        var orderDetails: List<OrderDetail>? = null

) {
    constructor(errorCode: Int, errorMessage: String) : this(description = errorMessage, status = errorCode)
}