package com.srp.ewayspanel.repository.remote.appservice

import com.srp.ewayspanel.model.appservice.AppServiceResponse
import com.srp.ewayspanel.model.appservice.AppVersionChangesResponse
import com.srp.ewayspanel.model.appservice.AppVersionResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AppServiceApiRetrofit
{
    @POST("service/v{version}/service/getmobileappservices")
    fun getAppServices(
            @Path("version")
            version: Int
    ): Call<AppServiceResponse>

    @GET("service/v{version}/utility/getlastmobileversion/{versionNo}")
    fun getLastMobileVersion(
            @Path("version")
            version: Int,
            @Path("versionNo")
            versionApp: Int
    ): Call<AppVersionResponse>

    @GET("service/v{version}/utility/getmobileversionchanges/{oldVersionNo}")
    fun getMobileVersionChanges(
            @Path("version")
            version: Int,
            @Path("oldVersionNo")
            oldVersion: Int
    ): Call<AppVersionChangesResponse>
}