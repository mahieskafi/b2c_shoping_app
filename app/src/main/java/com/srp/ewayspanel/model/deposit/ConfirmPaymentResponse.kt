package com.srp.ewayspanel.model.deposit

import com.google.gson.annotations.SerializedName
import com.srp.eways.model.login.UserInfo

/**
 * Created by Eskafi on 8/27/2019.
 */
data class ConfirmPaymentResponse(
        @SerializedName("UserInfo")
        val userInfo: UserInfo?= null,
        @SerializedName("RequestId")
        val requestId: String? = "",
        @SerializedName("Status")
        val status: Int? = 0,
        @SerializedName("Description")
        var description: String? = ""
)
