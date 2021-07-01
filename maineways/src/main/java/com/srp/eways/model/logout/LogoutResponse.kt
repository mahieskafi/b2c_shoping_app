package com.srp.eways.model.logout

import com.google.gson.annotations.SerializedName

/**
 * Created by Eskafi on 9/23/2019.
 */
data class LogoutResponse
(
        @SerializedName("Description")
        val description: String?,
        @SerializedName("Status")
        val status: Int?
)