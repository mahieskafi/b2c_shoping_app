package com.srp.ewayspanel.model.shopcart.buy

import com.google.gson.annotations.SerializedName

data class Header(
        @SerializedName("Id")
        val id: Int = 0,

        @SerializedName("RequestId")
        val requestId: String = "",

        @SerializedName("UserId")
        val userId: Int = 0,

        @SerializedName("DiscountPrice")
        val discountPrice: Long = 0,

        @SerializedName("Price")
        val price: Long = 0,

        @SerializedName("PostPrice")
        val postPrice: Long = 0,

        @SerializedName("Status")
        val status: Int = 0,

        @SerializedName("PostMethod")
        val postMethod: Int = 0,

        @SerializedName("SaleChannel")
        val saleChannel: Int = 0,

        @SerializedName("Address")
        val address: String = "",

        @SerializedName("CreateDate")
        val createDate: String = "",

        @SerializedName("UpdateDate")
        val updateDate: String = "",

        @SerializedName("FirstName")
        val firstName: String = "",

        @SerializedName("LastName")
        val lastName: String = "",

        @SerializedName("StateId")
        val stateId: Int = 0,

        @SerializedName("CityId")
        val cityId: Int = 0,

        @SerializedName("ReserveId")
        val reserveId: Int = 0,

        @SerializedName("PostCode")
        val postCode: String = "",

        @SerializedName("PreSaleVoucherHeaderId")
        val preSaleVoucherHeaderId: Int = 0,

        @SerializedName("TaxPrice")
        val taxPrice: Long = 0,

        @SerializedName("PayInPlace")
        val payInPlace: Boolean = false
)
