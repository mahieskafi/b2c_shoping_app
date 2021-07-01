package com.srp.eways.model.bill.report


import com.google.gson.annotations.SerializedName

data class BillReportRequest(
    @SerializedName("billId")
    val billId: String? = null,
    @SerializedName("createDateFrom")
    val createDateFrom: String = "",
    @SerializedName("createDateTo")
    val createDateTo: String = "",
    @SerializedName("pageIndex")
    val pageIndex: Int = 0,
    @SerializedName("pageSize")
    val pageSize: Int = 0,
    @SerializedName("statusTypeId")
    val statusTypeId: Int? = null,
    @SerializedName("userId")
    val userId: Long? = 0
)