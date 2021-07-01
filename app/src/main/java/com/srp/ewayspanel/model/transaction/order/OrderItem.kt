package com.srp.ewayspanel.model.transaction.order

import android.graphics.drawable.Drawable
import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by Eskafi on 2/2/2020.
 */

data class OrderItem
(
        @SerializedName("OrderId")
        var orderId: Long = 0,

        @SerializedName("UserId")
        var userId: Long = 0,

        @SerializedName("TotalPrice")
        var totalPrice: Long = 0,

        @SerializedName("OrderStatus")
        var orderStatus: Int = 0,

        @SerializedName("OrderStatusName")
        var orderStatusName: String = "",

        @SerializedName("PaymentDate")
        var paymentDate: String = "",

        @SerializedName("Payment")
        val paymentNumber: Long? = null
) : Serializable {
    constructor(orderId: Long) : this(orderId = orderId, userId = 0, totalPrice = 0, orderStatus = 0, orderStatusName = "", paymentDate = "", paymentNumber = 0)
}