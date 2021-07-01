package com.srp.ewayspanel.model.shopcart.buy

import com.google.gson.annotations.SerializedName
import com.srp.ewayspanel.model.shopcart.Lack

data class BuyResponse(
        @SerializedName("InventoryLackList")
        var inventoryLackList: ArrayList<Lack> = arrayListOf(),

        @SerializedName("ReqId")
        var reqId: String = "",

        @SerializedName("Header")
        var header: Header? = null,

        @SerializedName("OrderId")
        var orderId: Int = 0,

        @SerializedName("Description")
        var description: String = "",

        @SerializedName("Url")
        var url: String? = "",

        @SerializedName("Status")
        var status: Int
)