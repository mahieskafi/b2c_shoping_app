package com.srp.ewayspanel.model.login


import com.google.gson.annotations.SerializedName

data class ResetPasswordRequest(
    @SerializedName("ConfirmPassword")
    var confirmPassword: String = "",
    @SerializedName("Guid")
    var guid: String = "",
    @SerializedName("Password")
    var password: String = "",
    @SerializedName("UserId")
    var userId: Int = 0
)