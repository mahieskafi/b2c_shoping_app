package com.srp.ewayspanel.model.transaction.order

import com.google.gson.annotations.SerializedName
import com.srp.ewayspanel.model.followorder.FollowOrderItem

/**
 * Created by Eskafi on 2/2/2020.
 */

data class OrderTransactionListResponse
(
        @SerializedName("Description")
        var description: String? = "",

        @SerializedName("Status")
        var status: Int = 0,

        @SerializedName("PageIndex")
        val pageIndex: Int = 0,

        @SerializedName("PageSize")
        val pageSize: Int = 0,

        @SerializedName("OrderItems")
        val orderItems: ArrayList<FollowOrderItem>? = null
) {
    constructor(errorCode: Int, errorMessage: String) : this(description = errorMessage, status = errorCode)
}