package com.srp.ewayspanel.model.storepage.mainpage


import com.google.gson.annotations.SerializedName

data class StoreMainPageResponse(
        @SerializedName("Data")
    var `data`: ArrayList<Data>? = arrayListOf(),
        @SerializedName("Description")
    var description: String? = "",
        @SerializedName("Status")
    var status: Int? = 0
)