package com.srp.ewayspanel.model.login.sms

import com.google.gson.annotations.SerializedName

data class OutSmsResponse(
        @SerializedName("Description")
        var description: String = "",
        @SerializedName("Status")
        var status: Int = 0,
        @SerializedName("TraceCode")
        var traceCode: String? = ""
)
