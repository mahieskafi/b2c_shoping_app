package com.srp.ewayspanel.model.login


import com.google.gson.annotations.SerializedName

data class ResetPasswordResponse(
    @SerializedName("Description")
    var description: String = "",
    @SerializedName("Status")
    var status: Int = 0
)