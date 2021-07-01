package com.srp.ewayspanel.model.login.registerwithotp


import com.google.gson.annotations.SerializedName
import com.srp.ewayspanel.AppConfig

data class RegisterWithOTPRequest(
        @SerializedName("appKey")
    var appKey: String = AppConfig.APP_KEY,
        @SerializedName("firstName")
    var firstName: String = "",
        @SerializedName("gender")
    var gender: Int = 0,
        @SerializedName("info")
    var info: String = "null",
        @SerializedName("lastName")
    var lastName: String = "",
        @SerializedName("mobileNo")
    var mobileNo: String = "",
        @SerializedName("parentId")
    var parentId: Int = 0,
        @SerializedName("password")
    var password: String = "",
        @SerializedName("token")
    var token: String = "",
        @SerializedName("type")
    var type: Int = AppConfig.TYPE
)