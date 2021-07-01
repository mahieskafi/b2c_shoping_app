package com.srp.b2b2cEwaysPannel.model.user


import com.google.gson.annotations.SerializedName

data class SaleChannelsInfo(
    @SerializedName("Description")
    var description: String = "",
    @SerializedName("IsActive")
    var isActive: Boolean = false,
    @SerializedName("IsCredit")
    var isCredit: Boolean = false,
    @SerializedName("SaleChannelId")
    var saleChannelId: Int = 0,
    @SerializedName("SitePassword")
    var sitePassword: String = ""
)