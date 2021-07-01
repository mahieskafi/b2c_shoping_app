package com.srp.b2b2cEwaysPannel.model.login.registerwithotp


import com.google.gson.annotations.SerializedName
import com.srp.eways.model.login.UserInfo

data class RegisterWithOTPResponse(
    @SerializedName("Description")
    var description: String = "",
    @SerializedName("Status")
    var status: Int = 0,
    @SerializedName("Token")
    var token: String = "",
    @SerializedName("UserInfo")
    var userInfo: UserInfo = UserInfo()
)