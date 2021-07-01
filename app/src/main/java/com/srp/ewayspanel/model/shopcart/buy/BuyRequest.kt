package com.srp.ewayspanel.model.shopcart.buy

import com.google.gson.annotations.SerializedName

data class BuyRequest(
        @SerializedName("Type")
        var type: Int = 0,

        @SerializedName("CouponCode")
        var couponCode: String? = null,

        @SerializedName("DeliveryAddress")
        var deliveryAddress: String = "",

        @SerializedName("Gateway")
        var gateway: Int = 0,

        @SerializedName("GatewayType")
        var gatewayType: Int = 0,

        @SerializedName("Description")
        var description: String = "",

        @SerializedName("StateId")
        var stateId: Int = 0,

        @SerializedName("CityId")
        var cityId: Int = 0
)