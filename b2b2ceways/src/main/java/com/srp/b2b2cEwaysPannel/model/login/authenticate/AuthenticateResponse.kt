package com.srp.b2b2cEwaysPannel.model.login.authenticate


import com.google.gson.annotations.SerializedName

data class AuthenticateResponse(
    @SerializedName("Description")
    var description: String = "",
    @SerializedName("Status")
    var status: Int = 0,
    @SerializedName("UserIsNew")
    var userIsNew: Boolean = false
)