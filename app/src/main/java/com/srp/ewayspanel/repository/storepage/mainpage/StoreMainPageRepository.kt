package com.srp.ewayspanel.repository.storepage.mainpage

import com.srp.eways.network.CallBackWrapper
import com.srp.ewayspanel.model.storepage.mainpage.StoreMainPageResponse

/**
 * Created by ErfanG on 2/18/2020.
 */
interface StoreMainPageRepository {

    fun getMainData(itemCount : Int, callBack : CallBackWrapper<StoreMainPageResponse>)
}