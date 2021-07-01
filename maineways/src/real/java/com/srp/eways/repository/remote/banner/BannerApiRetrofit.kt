package com.srp.eways.repository.remote.banner

import com.srp.eways.model.banner.BannerResponse
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Path

interface BannerApiRetrofit {

    @POST("service/v{version}/service/getbanners/{typeId}")
    fun getBanners(@Path("version") version: Int,
                   @Path("typeId") typeId: Int)
            : Call<BannerResponse>
}