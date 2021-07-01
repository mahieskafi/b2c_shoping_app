package com.srp.ewayspanel.repository.remote.address

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import com.srp.ewayspanel.model.address.CityResponse
import com.srp.ewayspanel.model.address.ProvinceResponse

interface AddressApiRetrofit
{
    @GET("service/v{version}/utility/getprovinces")
    fun getProvinces(
            @Path("version")
            version: Int
    ): Call<ProvinceResponse>

    @GET("service/v{version}/utility/getcities/{parentId}")
    fun getCities(
            @Path("version")
            version: Int,
            @Path("parentId")
            provinceId: Int
    ): Call<CityResponse>
}