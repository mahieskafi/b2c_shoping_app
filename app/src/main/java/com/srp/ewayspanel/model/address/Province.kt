package com.srp.ewayspanel.model.address

import com.google.gson.annotations.SerializedName

data class Province(
        @SerializedName("ProvinceName")
        var provinceName: String,

        @SerializedName("ProvinceId")
        var provinceId: Int,

        @SerializedName("CountryId")
        var countryId: Int
)