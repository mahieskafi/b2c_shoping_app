package com.srp.ewayspanel.repository.remote.storepage.mainpage

import com.srp.ewayspanel.model.storepage.mainpage.StoreMainPageResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by ErfanG on 2/18/2020.
 */
interface StoreMainPageApiRetrofit {

    @GET("service/v{version}/store/getstoremainpage/{count}")
    fun getMainData(
            @Path("version")
            version: Int,
            @Path("count")
            count: Int
    ) : Call<StoreMainPageResponse>

}