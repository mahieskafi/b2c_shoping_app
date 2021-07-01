package com.srp.ewayspanel.model.appservice

import com.google.gson.annotations.SerializedName

data class AppVersion(

        @SerializedName("VersionNo")
        var versionNumber: Int,

        @SerializedName("Status")
        var status: Int,

        @SerializedName("StatusText")
        var statusText: String,

        @SerializedName("Url")
        var url: String

)