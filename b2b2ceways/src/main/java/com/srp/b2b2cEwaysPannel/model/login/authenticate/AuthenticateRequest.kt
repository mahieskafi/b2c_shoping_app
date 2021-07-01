package com.srp.b2b2cEwaysPannel.model.login.authenticate


import com.google.gson.annotations.SerializedName
import com.srp.b2b2cEwaysPannel.AppConfig

data class AuthenticateRequest(
        @SerializedName("appKey")
    var appKey: String = AppConfig.APP_KEY,
        @SerializedName("mobileNo")
    var mobileNo: String = "",
        @SerializedName("type")
    var type: Int = AppConfig.TYPE
)