package com.srp.eways.model.deposit

import com.google.gson.annotations.SerializedName

/**
 * Created by Eskafi on 12/15/2019.
 */

data class IncreaseDepositStatusResponse(

        @SerializedName("Amount")
        val amount: Long? = 0,

        @SerializedName("PaymentId")
        val paymentId: String? = "",

        @SerializedName("ChannelType")
        val chanelType: Int? = 0,

        @SerializedName("Status")
        val status: Int,

        @SerializedName("Description")
        val description: String
)