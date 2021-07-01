package com.srp.ewayspanel.model.sale

import com.google.gson.annotations.SerializedName

/**
 * Created by Eskafi on 1/11/2020.
 */
data class SaleSummaryReportRequest
(
        @SerializedName("saleChannelType")
        var saleChannelType: Int,

        @SerializedName("mainGroupType")
        var mainGroupType: Int,

        @SerializedName("deliverStatus")
        var deliverStatus: Int,

        @SerializedName("minRequestDate")
        var mainRequestDate: String,

        @SerializedName("maxRequestDate")
        var maxRequestDate: String
)
