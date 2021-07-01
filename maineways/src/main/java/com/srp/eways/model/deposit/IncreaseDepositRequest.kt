package com.srp.eways.model.deposit

import com.google.gson.annotations.SerializedName

/**
 * Created by Eskafi on 12/15/2019.
 */

data class IncreaseDepositRequest(

        @SerializedName("Amount")
        var amount: Long,

        @SerializedName("BankId")
        var bankId: Int,

        @SerializedName("MaskCard")
        var maskCard: String

)