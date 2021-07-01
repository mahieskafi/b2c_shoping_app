package com.srp.ewayspanel.model.storepage.category


import com.google.gson.annotations.SerializedName

data class CategoryListResponse(
        @SerializedName("Description")
    var description: String? = "",
        @SerializedName("Items")
    var items: List<CategoryItem>? = listOf(),
        @SerializedName("Status")
    var status: Int = 0
)