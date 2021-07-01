package com.srp.ewayspanel.model.shopcart


import com.google.gson.annotations.SerializedName

data class ProductInfo(
    @SerializedName("Availability")
    var availability: Boolean = false,
    @SerializedName("Discount")
    var discount: Long = 0,
    @SerializedName("Id")
    var id: Int = 0,
    @SerializedName("ImageUrl")
    var imageUrl: String = "",
    @SerializedName("IsSim")
    var isSim: Boolean = false,
    @SerializedName("LawId")
    var lawId: Int = 0,
    @SerializedName("MaxOrder")
    var maxOrder: Int = 0,
    @SerializedName("MinOrder")
    var minOrder: Int = 0,
    @SerializedName("Name")
    var name: String = "",
    @SerializedName("OldPrice")
    var oldPrice: Long = 0,
    @SerializedName("OverInventoryCount")
    var overInventoryCount: Int = 0,
    @SerializedName("Point")
    var point: Int = 0,
    @SerializedName("Price")
    var price: Long = 0,
    @SerializedName("SeoName")
    var seoName: String = "",
    @SerializedName("Stock")
    var stock: Int = 0
)