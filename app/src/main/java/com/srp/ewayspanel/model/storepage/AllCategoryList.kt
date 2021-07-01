package com.srp.ewayspanel.model.storepage

import com.google.gson.annotations.SerializedName

/**
 * Created by Eskafi on 10/23/2019.
 */
data class AllCategoryList
(
        @SerializedName("Description")
        var description: String = "",

        //TODO: needs  SerializedName
        var itemList: List<AllCategoryItem> = listOf(),

        @SerializedName("Status")
        var status: Int = 0
)
