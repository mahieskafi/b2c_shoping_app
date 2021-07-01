package com.srp.ewayspanel.model.login


import com.google.gson.annotations.SerializedName

data class ForgetPasswordRequest(
    @SerializedName("UserName")
    var userName: String = "",

    @SerializedName("AppKey")
    var appKey: String
)