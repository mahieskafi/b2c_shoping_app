package com.srp.ewayspanel.model.storepage.filter

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by Eskafi on 10/27/2019.
 */
data class FilterProductRequest
(
        @SerializedName("CategoryId")
        var categoryId: Long = 0,

        @SerializedName("Order")
        var order: Int = 0,

        @SerializedName("Sort")
        var sort: Int = 0,

        @SerializedName("PageIndex")
        var pageIndex: Int = 0,

        @SerializedName("PageSize")
        var pageSize: Int = 0,

        @SerializedName("OnlyAvailable")
        var onlyAvailable: Boolean = false,

        @SerializedName("MinPrice")
        var minPrice: Long? = 0,

        @SerializedName("MaxPrice")
        var maxPrice: Long? = 2000000000,

        @SerializedName("Text")
        var text: String = "",

        @SerializedName("SelectedBrands")
        var selectedBrands: List<Int>? = null,

        var categoryNodeRootParent: Long? = 0
) : Serializable {
    fun FilterProductRequest() {
        pageSize = 20
    }
}

