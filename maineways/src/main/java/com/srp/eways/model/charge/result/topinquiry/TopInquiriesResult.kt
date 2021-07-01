package com.srp.eways.model.charge.result.topinquiry


import com.google.gson.annotations.SerializedName

data class TopInquiriesResult(
    @SerializedName("Description")
    var description: Any = Any(),
    @SerializedName("Items")
    var items: List<Item> = listOf(),
    @SerializedName("Operators")
    var operators: List<Operator> = listOf(),
    @SerializedName("Status")
    var status: Int = 0
)