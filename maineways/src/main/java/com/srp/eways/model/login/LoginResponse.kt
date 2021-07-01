package com.srp.eways.model.login


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by ErfanG on 8/7/2019.
 */
data class LoginResponse(
    @SerializedName("Description")
    val description: String?,
    @SerializedName("Status")
    val status: Int?,
    @SerializedName("Token")
    val token: String?,
    @SerializedName("MobileIsConfirmed")
    val mobileIsConfirmed: Boolean? = false,

    @SerializedName("UserInfo")
    val userInfo: UserInfo?
)