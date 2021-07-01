package com.srp.ewayspanel.model.sale.detail

import com.google.gson.annotations.SerializedName

data class SaleDetailReportResponse(

        @SerializedName("Status")
        var status: Int,

        @SerializedName("Description")
        var description: String,

        @SerializedName("RowCount")
        var rowCount: Int,

        @SerializedName("Sales")
        var saleList: ArrayList<SaleDetailReportItem>?
)