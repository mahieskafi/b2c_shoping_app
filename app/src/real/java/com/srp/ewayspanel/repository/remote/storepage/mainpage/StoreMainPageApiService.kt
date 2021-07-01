package com.srp.ewayspanel.repository.remote.storepage.mainpage

import com.srp.ewayspanel.model.storepage.mainpage.StoreMainPageResponse
import com.srp.eways.network.CallBackWrapper

/**
 * Created by ErfanG on 2/18/2020.
 */
interface StoreMainPageApiService {

    fun getMainData(itemCount : Int, callback: CallBackWrapper<StoreMainPageResponse>)
}