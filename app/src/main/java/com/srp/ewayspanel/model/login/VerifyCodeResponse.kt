package com.srp.ewayspanel.model.login


import com.google.gson.annotations.SerializedName

data class VerifyCodeResponse(
    @SerializedName("Description")
    var description: String = "",
    @SerializedName("Guid")
    var guid: String = "",
    @SerializedName("Status")
    var status: Int = 0,
    @SerializedName("UserId")
    var userId: Int = 0
)