package com.srp.ewayspanel.model.login.sms

import com.google.gson.annotations.SerializedName
import com.srp.ewayspanel.AppConfig

data class OutSmsRequest(
        @SerializedName("AppKey")
        var appKey: String = AppConfig.APP_KEY,
        @SerializedName("MobileNumber")
        var mobileNumber: String = "",
        @SerializedName("Imei")
        var imei: String = ""
)
