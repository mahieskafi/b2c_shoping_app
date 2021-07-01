package com.srp.eways.model.charge.result.topinquiry


import com.google.gson.annotations.SerializedName

data class Operator(
    @SerializedName("OperatorKey")
    var operatorKey: String = "",
    @SerializedName("PerfixList")
    var perfixList: List<String> = listOf()
)