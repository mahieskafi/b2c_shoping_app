package com.srp.ewayspanel.model.storepage.product

import com.google.gson.annotations.SerializedName

data class ProductInventoryResponse(

        @SerializedName("Id")
        var productId: Int,

        @SerializedName("Stock")
        var stock: Long,

        @SerializedName("MinOrder")
        var minOrder: Int,

        @SerializedName("MaxOrder")
        var maxOrder: Long,

        @SerializedName("Status")
        var status: Int,

        @SerializedName("Description")
        var description: String
)