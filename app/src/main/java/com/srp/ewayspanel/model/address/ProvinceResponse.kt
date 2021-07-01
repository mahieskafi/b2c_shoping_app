package com.srp.ewayspanel.model.address

import com.google.gson.annotations.SerializedName

data class ProvinceResponse(
        @SerializedName("Status")
        var status: Int,

        @SerializedName("Description")
        var description: String,

        @SerializedName("Data")
        var data: ArrayList<Province>?
)