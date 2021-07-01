package com.srp.ewayspanel.model.appservice

import com.google.gson.annotations.SerializedName

data class AppServiceResponse
(
        @SerializedName("RowCount")
        var rowCount: Int,

        @SerializedName("Status")
        var status: Int,

        @SerializedName("Description")
        var description: String,

        @SerializedName("Data")
        var data: MutableList<AppService>?
)