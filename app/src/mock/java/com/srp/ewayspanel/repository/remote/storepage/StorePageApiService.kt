package com.srp.ewayspanel.repository.remote.storepage

import com.srp.eways.network.CallBackWrapper
import com.srp.ewayspanel.model.storepage.category.CategoryListResponse

/**
 * Created by ErfanG on 23/10/2019.
 */
interface StorePageApiService {

    fun getCategoryList(callback : CallBackWrapper<CategoryListResponse>)
}