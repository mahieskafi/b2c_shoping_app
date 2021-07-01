package com.srp.ewayspanel.model.storepage.filter

import com.google.gson.annotations.SerializedName

/**
 * Created by Eskafi on 10/27/2019.
 */
data class FilteredProduct
(
        @SerializedName("Text")
        val text: String? = "",

        @SerializedName("Products")
        val products: ArrayList<ProductInfo>? = null,

        @SerializedName("Category")
        val category: CategoryItem? = null,

        @SerializedName("GoodsCount")
        val productCount: Int? = 0,

        @SerializedName("Status")
        val status: Int = 0,

        @SerializedName("Description")
        val description: String = "",

        @SerializedName("Order")
        var order: Int = 0,

        @SerializedName("Sort")
        var sort: Int = 0,

        @SerializedName("PageIndex")
        var pageIndex: Int = 0,

        @SerializedName("OnlyAvailable")
        var onlyAvailable: Boolean = true,

        @SerializedName("MinPrice")
        var minPrice: Int = 0,

        @SerializedName("MaxPrice")
        var maxPrice: Int? = 1000000,

        @SerializedName("CatId")
        val productCategoryId: Int? = 0,

        @SerializedName("SelectedBrands")
        var selectedBrands: List<Int>? = null,

        @SerializedName("Categories")
        val categories: List<CategoryItem>? = null,

        @SerializedName("Brands")
        val brands: String? = ""
) {
    constructor(errorCode: Int, errorMessage: String) : this(status = errorCode, description = errorMessage)
}