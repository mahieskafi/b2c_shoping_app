package com.srp.ewayspanel.model.appservice

import com.google.gson.annotations.SerializedName

data class AppVersionResponse
(
        @SerializedName("Status")
        var status: Int,

        @SerializedName("Description")
        var description: String,

        @SerializedName("Data")
        var data: AppVersion?
)