package com.srp.ewayspanel.model.shopcart.addproduct


import com.google.gson.annotations.SerializedName

data class AddOrUpdateProductRequest(
    @SerializedName("CategoryId")
    var categoryId: Int = 0,
    @SerializedName("Count")
    var count: Int = 0,
    @SerializedName("ProductId")
    var productId: Int = 0
)