package com.srp.ewayspanel.model.shopcart


import com.google.gson.annotations.SerializedName

data class PostType(
    @SerializedName("Description")
    var description: String = "",
    @SerializedName("Id")
    var id: Int = 0,
    @SerializedName("Title")
    var title: String = ""
)