package com.srp.ewayspanel.model.shopcart


import com.google.gson.annotations.SerializedName

data class ShopCartItemListRequest(
        @SerializedName("Type")
        var type: Int = 0,
        @SerializedName("couponCode")
        var couponCode: String? = null,
        @SerializedName("State")
        var state: Int? = null,
        @SerializedName("City")
        var city: Int? = null

)