package com.srp.ewayspanel.model.login.sms

import com.google.gson.annotations.SerializedName
import com.srp.ewayspanel.AppConfig

data class OutSmsVerifiRequest(
        @SerializedName("AppKey")
        var appKey: String = AppConfig.APP_KEY,
        @SerializedName("MobileNumber")
        var mobileNumber: String = "",
        @SerializedName("TraceCode")
        var traceCode: String = "",
        @SerializedName("Token")
        var token: String = ""
)
