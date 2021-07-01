package com.srp.eways.model.banner


import com.google.gson.annotations.SerializedName

data class BannerResponse(
    @SerializedName("Data")
    var bannerList: List<Data>,
    @SerializedName("Description")
    var description: String,
    @SerializedName("Status")
    var status: Int
)