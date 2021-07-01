package com.srp.eways.model.transaction.charge

import com.google.gson.annotations.SerializedName


data class ChargeTransactionRequest(
    @SerializedName("DeliverStatus")
    var DeliverStatus: Int = -1,
    @SerializedName("MainGroupType")
    var MainGroupType: Int = 0,
    @SerializedName("MaxRequestDate")
    var MaxRequestDate: String = "",
    @SerializedName("MinRequestDate")
    var MinRequestDate: String = "",
    @SerializedName("PageIndex")
    var PageIndex: Int = 0,
    @SerializedName("PageSize")
    var PageSize: Int = 0,
    @SerializedName("SaleChannelType")
    var SaleChannelType: Int = 1
)