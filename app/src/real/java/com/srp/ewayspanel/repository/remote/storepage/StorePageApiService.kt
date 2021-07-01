package com.srp.ewayspanel.repository.remote.storepage

import com.srp.ewayspanel.model.storepage.category.CategoryListResponse
import com.srp.eways.network.CallBackWrapper
import retrofit2.Call

/**
 * Created by ErfanG on 23/10/2019.
 */
interface StorePageApiService {

    fun getCategoryList(parentId: Long, callback: CallBackWrapper<CategoryListResponse>)

    fun getCategoryRawResponse(callback: CallBackWrapper<CategoryListResponse>)

}