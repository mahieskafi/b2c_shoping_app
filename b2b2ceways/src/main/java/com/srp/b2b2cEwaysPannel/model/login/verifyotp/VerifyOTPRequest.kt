package com.srp.b2b2cEwaysPannel.model.login.verifyotp


import com.google.gson.annotations.SerializedName
import com.srp.b2b2cEwaysPannel.AppConfig

data class VerifyOTPRequest(
        @SerializedName("appKey")
    var appKey: String = AppConfig.APP_KEY,
        @SerializedName("info")
    var info: String = "null",
        @SerializedName("mobileNo")
    var mobileNo: String = "",
        @SerializedName("token")
    var token: String = "",
        @SerializedName("type")
    var type: Int = AppConfig.TYPE,
        @SerializedName("userIsNew")
    var userIsNew: Boolean = false
)