package com.srp.eways.model.deposit

import com.google.gson.annotations.SerializedName

/**
 * Created by Eskafi on 12/15/2019.
 */

data class IncreaseDepositResponse(

        @SerializedName("Url")
        val url: String,

        @SerializedName("Status")
        val status: Int,

        @SerializedName("Description")
        val description: String
)