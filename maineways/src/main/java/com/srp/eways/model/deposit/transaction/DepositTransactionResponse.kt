package com.srp.eways.model.deposit.transaction

import com.google.gson.annotations.SerializedName

/**
 * Created by Eskafi on 9/3/2019.
 */
data class DepositTransactionResponse(
        @SerializedName("RowCount")
        val rowCount: Int? = 0,
        @SerializedName("Status")
        val status: Int? = 0,
        @SerializedName("Description")
        val description: String? = null,
        @SerializedName("Items")
        val items: ArrayList<DepositTransactionItem>? = null
)
