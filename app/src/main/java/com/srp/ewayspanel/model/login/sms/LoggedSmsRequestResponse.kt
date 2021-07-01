package com.srp.ewayspanel.model.login.sms

import com.google.gson.annotations.SerializedName

data class LoggedSmsRequestResponse(
        @SerializedName("Status")
        var status: Int,

        @SerializedName("Description")
        var description: String

)
