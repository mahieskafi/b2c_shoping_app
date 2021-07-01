package com.srp.ewayspanel.repository.remote.storepage

import com.srp.ewayspanel.model.storepage.category.CategoryListResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by ErfanG on 23/10/2019.
 */
interface StorePageApRetrofit {

    @GET("service/v{version}/store/getcategorylist/{parentId}")
    fun getCategoryList(
            @Path("version")
            version: Int,
            @Path("parentId")
            parentId: Long
    ) : Call<CategoryListResponse>

    @GET("service/v{version}/store/getcategorylist")
    fun getCategoryRawList(
            @Path("version")
            version: Int
    ) : Call<CategoryListResponse>
}