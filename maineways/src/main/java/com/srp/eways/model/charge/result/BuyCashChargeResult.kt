package com.srp.eways.model.charge.result

import com.google.gson.annotations.SerializedName

data class BuyCashChargeResult(

        @SerializedName("Url")
        val url: String,

        @SerializedName("Status")
        val status: Int,

        @SerializedName("Description")
        val description: String
)