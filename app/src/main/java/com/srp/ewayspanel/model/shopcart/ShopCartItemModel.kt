package com.srp.ewayspanel.model.shopcart


import com.google.gson.annotations.SerializedName

data class ShopCartItemModel(
    @SerializedName("Count")
    var count: Int = 0,
    @SerializedName("Id")
    var id: Int = 0,
    @SerializedName("ProductId")
    var productId: Int = 0,
    @SerializedName("ProductInfo")
    var productInfo: ProductInfo? = ProductInfo()
)