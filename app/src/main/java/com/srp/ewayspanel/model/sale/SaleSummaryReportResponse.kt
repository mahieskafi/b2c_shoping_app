package com.srp.ewayspanel.model.sale

import com.google.gson.annotations.SerializedName

/**
 * Created by Eskafi on 1/11/2020.
 */

data class SaleSummaryReportResponse
(
        @SerializedName("Title")
        var title: String,

        @SerializedName("BuySummeries")
        var buySummaryItem: List<BuySummaryItem>? = null,

        @SerializedName("ReportStatus")
        var reportStatus: Int,

        @SerializedName("Status")
        var status: Int,

        @SerializedName("Description")
        var description: String
)