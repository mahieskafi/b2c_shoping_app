package com.srp.ewayspanel.model.login


import com.google.gson.annotations.SerializedName

data class VerifyCodeRequest(
    @SerializedName("Code")
    var code: String = "",
    @SerializedName("Guid")
    var guid: String = "",
    @SerializedName("UserId")
    var userId: Int = 0
)