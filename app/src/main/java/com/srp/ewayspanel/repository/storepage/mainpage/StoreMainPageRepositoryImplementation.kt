package com.srp.ewayspanel.repository.storepage.mainpage

import com.srp.eways.network.CallBackWrapper
import com.srp.ewayspanel.di.DI
import com.srp.ewayspanel.model.storepage.mainpage.StoreMainPageResponse
import com.srp.ewayspanel.repository.remote.storepage.mainpage.StoreMainPageApiService

/**
 * Created by ErfanG on 2/18/2020.
 */
object StoreMainPageRepositoryImplementation : StoreMainPageRepository {

    private val mStoreMainPageApiService : StoreMainPageApiService = DI.getStoreMainPageApi()

    override fun getMainData(itemCount: Int, callBack: CallBackWrapper<StoreMainPageResponse>) {

        mStoreMainPageApiService.getMainData(itemCount, callBack)
    }
}