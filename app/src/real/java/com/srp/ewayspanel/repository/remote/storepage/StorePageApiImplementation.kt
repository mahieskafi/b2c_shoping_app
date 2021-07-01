package com.srp.ewayspanel.repository.remote.storepage

import com.srp.ewayspanel.AppConfig
import com.srp.ewayspanel.di.DI
import com.srp.ewayspanel.model.storepage.category.CategoryListResponse
import com.srp.eways.network.CallBackWrapper
import com.srp.eways.repository.remote.DefaultRetroCallback
import retrofit2.Call

/**
 * Created by ErfanG on 23/10/2019.
 */
class StorePageApiImplementation : StorePageApiService {

    private val mApiRetrofit = DI.provideApi(StorePageApRetrofit::class.java)

    companion object {
        val INSTANCE = StorePageApiImplementation()
    }


    override fun getCategoryList(parentId: Long, callback: CallBackWrapper<CategoryListResponse>) {
        mApiRetrofit.getCategoryList(AppConfig.SERVER_VERSION, parentId).enqueue(DefaultRetroCallback<CategoryListResponse>(callback))
    }

    override fun getCategoryRawResponse(callback: CallBackWrapper<CategoryListResponse>) {
        return mApiRetrofit.getCategoryRawList(AppConfig.SERVER_VERSION).enqueue(DefaultRetroCallback<CategoryListResponse>(callback))
    }
}