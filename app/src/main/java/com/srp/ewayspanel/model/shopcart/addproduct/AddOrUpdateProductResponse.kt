package com.srp.ewayspanel.model.shopcart.addproduct


import com.google.gson.annotations.SerializedName
import com.srp.ewayspanel.model.shopcart.ShopCartItemModel

data class AddOrUpdateProductResponse(
        @SerializedName("Description")
    var description: String? = "",
        @SerializedName("Items")
    var items: ArrayList<ShopCartItemModel> = arrayListOf(),
        @SerializedName("Status")
    var status: Int = 0
)