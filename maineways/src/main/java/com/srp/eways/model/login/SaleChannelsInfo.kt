package com.srp.eways.model.login


import com.google.gson.annotations.SerializedName

/**
 * Created by ErfanG on 8/7/2019.
 */
data class SaleChannelsInfo(
    @SerializedName("Description")
    val description: String?,
    @SerializedName("IsActive")
    val isActive: Boolean?,
    @SerializedName("IsCredit")
    val isCredit: Boolean?,
    @SerializedName("SaleChannelId")
    val saleChannelId: Int?,
    @SerializedName("SitePassword")
    val sitePassword: String?
)