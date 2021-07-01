package com.srp.ewayspanel.model.address

import com.google.gson.annotations.SerializedName

data class City(
        @SerializedName("CityName")
        var cityName: String,

        @SerializedName("ProvinceId")
        var provinceId: Int,

        @SerializedName("CityId")
        var cityId: Int
)