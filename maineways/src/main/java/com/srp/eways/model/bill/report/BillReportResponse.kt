package com.srp.eways.model.bill.report


import com.google.gson.annotations.SerializedName

data class BillReportResponse(
        @SerializedName("Data")
    val `data`: ArrayList<BillReportItem> = arrayListOf<BillReportItem>(),
        @SerializedName("Description")
    val description: String? = null,
        @SerializedName("RowCount")
    val rowCount: Int = 0,
        @SerializedName("Status")
    val status: Int = 0
)