package com.srp.eways.model.deposit

import com.google.gson.annotations.SerializedName

/**
 * Created by Eskafi on 8/26/2019.
 */
data class MPLTokenResponse(
        @SerializedName("Authority")
        val authority: String? = "",
        @SerializedName("RequestId")
        val requestId: String? = "",
        @SerializedName("ReqId")
        val reqId: String? = "",
        @SerializedName("Status")
        val status: Int? = 0,
        @SerializedName("Description")
        var description: String? = "",

        var mplStatus:Boolean = false
)
