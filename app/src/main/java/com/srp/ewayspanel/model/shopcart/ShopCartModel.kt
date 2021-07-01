package com.srp.ewayspanel.model.shopcart


import com.google.gson.annotations.SerializedName

data class ShopCartModel(
        @SerializedName("Basket")
    var basket: ArrayList<ShopCartItemModel> = arrayListOf(),
        @SerializedName("DeliverTypes")
    var deliverTypes: DeliverTypes = DeliverTypes(),
        @SerializedName("Description")
    var description: String = "",
        @SerializedName("LackList")
    var lackList: ArrayList<Lack> = arrayListOf(),
        @SerializedName("OrderPrice")
    var orderPrice: Long = 0,
        @SerializedName("PayingPrice")
    var payingPrice: Long = 0,
        @SerializedName("DiscountPrice")
    var discountPrice: Long = 0,
        @SerializedName("ShippingPrice")
        var shippingPrice: Long = 0,
        @SerializedName("ShippingType")
    var shippingType: Int = 0,
        @SerializedName("Status")
    var status: Int = 0
)