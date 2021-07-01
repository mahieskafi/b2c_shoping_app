package com.srp.ewayspanel.model.transaction.order

import com.google.gson.annotations.SerializedName

/**
 * Created by Eskafi on 2/5/2020.
 */

data class OrderSummaryResult
(
        @SerializedName("OrderId")
        var orderId: Int = 0,

        @SerializedName("UserId")
        var userId: Int = 0,

        @SerializedName("TotalPrice")
        var totalPrice: Long = 0,

        @SerializedName("PostPrice")
        var postPrice: Long = 0,

        @SerializedName("TotalPoint")
        var totalPoint: Int = 0,

        @SerializedName("PostBarcode")
        var postBarcode: String? = null,

        @SerializedName("DeliveryAddress")
        var deliveryAddress: String? = null,

        @SerializedName("Vat")
        var vat: Int? = 0,

        @SerializedName("Payment")
        var payment: Long = 0,

        @SerializedName("Discount")
        var discount: Long? = 0,

        @SerializedName("CellPhone")
        var cellPhone: String? = "",

        @SerializedName("StateName")
        var stateName: String? = "",

        @SerializedName("Firstname")
        var firstName: String? = "",

        @SerializedName("Lastname")
        var lastName: String? = "",

        @SerializedName("FullName")
        var fullName: String? = "",

        @SerializedName("NationalCode")
        var nationalCode: String? = "",

        @SerializedName("Address")
        var address: String? = "",

        @SerializedName("PostCode")
        var postCode: String? = "",

        @SerializedName("CityName")
        var cityName: String? = "",

        @SerializedName("SiteName")
        var siteName: String? = "",

        @SerializedName("OrderStatusDescription")
        var orderStatusDescription: String? = "",

        @SerializedName("DeliveryStatusDescription")
        var deliveryStatusDescription: String? = "",

        @SerializedName("PostMethodName")
        var postMethodName: String? = "",

        @SerializedName("OrderStatus")
        var orderStatus: Int? = 0,

        @SerializedName("DeliveryStatus")
        var deliveryStatus: Int? = 0,

        @SerializedName("OrderDate")
        var orderDate: String? = ""
)