package com.srp.eways.model.transaction.charge


import com.google.gson.annotations.SerializedName

data class ChargeTransactionResponse(
        @SerializedName("Description")
    var description: String = "",
        @SerializedName("RowCount")
    var rowCount: Int = 0,
        @SerializedName("Sales")
    var chargeSales: List<ChargeSale>? = listOf(),
        @SerializedName("Status")
    var status: Int = 0
)