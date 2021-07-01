package com.srp.ewayspanel.model.storepage.mainpage


import com.google.gson.annotations.SerializedName

data class Data(
        @SerializedName("Id")
    var id: Long = 0,
        @SerializedName("Items")
    var items: ArrayList<Item> = arrayListOf(),
        @SerializedName("StaticUrl")
    var staticUrl: String? = "",
        @SerializedName("Title")
    var title: String = ""
)