package com.srp.eways.model.bill

import android.net.sip.SipErrorCode
import com.google.gson.annotations.SerializedName

data class BillPaymentResponse(

        @SerializedName("Result")
        val result: BillPaymentResult? = null,

        @SerializedName("Url")
        val url: String? = null,

        @SerializedName("Status")
        var status: Int,

        @SerializedName("Description")
        val description: String? = ""
) {
    constructor(errorCode: Int, errorMessage: String) : this(status = errorCode, description = errorMessage)
}