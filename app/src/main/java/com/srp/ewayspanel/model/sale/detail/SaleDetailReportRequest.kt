package com.srp.ewayspanel.model.sale.detail

import com.google.gson.annotations.SerializedName

data class SaleDetailReportRequest(
        @SerializedName("saleChannelType")
        var saleChannelType: Int,

        @SerializedName("mainGroupType")
        var mainGroupType: Int,

        @SerializedName("deliverStatus")
        var deliverStatus: Int,

        @SerializedName("minRequestDate")
        var mainRequestDate: String,

        @SerializedName("maxRequestDate")
        var maxRequestDate: String,

        @SerializedName("PageIndex")
        var pageIndex: Int = 0,

        @SerializedName("pageSize")
        var pageSize: Int = 0

)