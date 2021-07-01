package com.srp.ewayspanel.model.login.verifyotp


import com.google.gson.annotations.SerializedName
import com.srp.eways.model.login.UserInfo

data class VerifyOTPResponse(
    @SerializedName("Description")
    var description: String = "",
    @SerializedName("Status")
    var status: Int = 0,
    @SerializedName("Token")
    var token: String? = "",
    @SerializedName("UserInfo")
    var userInfo: UserInfo? = UserInfo()
)