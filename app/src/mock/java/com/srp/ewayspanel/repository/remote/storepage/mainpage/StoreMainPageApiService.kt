package com.srp.ewayspanel.repository.remote.storepage.mainpage

import com.srp.eways.network.CallBackWrapper
import com.srp.ewayspanel.model.storepage.mainpage.StoreMainPageResponse

/**
 * Created by ErfanG on 2/19/2020.
 */
interface StoreMainPageApiService {

    fun getMainData(itemCount : Int, callback : CallBackWrapper<StoreMainPageResponse>)
}