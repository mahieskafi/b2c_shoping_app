package com.srp.ewayspanel.model.shopcart


import com.google.gson.annotations.SerializedName

data class DeliverTypes(
    @SerializedName("CategoryId")
    var categoryId: Int = 0,
    @SerializedName("PostTypes")
    var postTypes: List<PostType> = listOf(),
    @SerializedName("Title")
    var title: String = ""
)