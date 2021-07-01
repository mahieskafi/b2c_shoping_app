package com.srp.ewayspanel.model.appservice

import com.google.gson.annotations.SerializedName

data class AppVersionChangesResponse
(
        @SerializedName("Status")
        var status: Int,

        @SerializedName("Description")
        var description: String,

        @SerializedName("Data")
        var data: String?
)