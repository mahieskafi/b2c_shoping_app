package com.srp.ewayspanel.repository.remote.storepage.mainpage

import com.srp.ewayspanel.AppConfig
import com.srp.ewayspanel.di.DI
import com.srp.ewayspanel.model.storepage.mainpage.StoreMainPageResponse
import com.srp.eways.network.CallBackWrapper
import com.srp.eways.repository.remote.DefaultRetroCallback

/**
 * Created by ErfanG on 2/18/2020.
 */
object StoreMainPageApiImplementation : StoreMainPageApiService {

    private val mRetrofit: StoreMainPageApiRetrofit = DI.provideApi(StoreMainPageApiRetrofit::class.java)

    override fun getMainData(itemCount: Int, callback: CallBackWrapper<StoreMainPageResponse>) {

        mRetrofit.getMainData(AppConfig.SERVER_VERSION, itemCount).enqueue(object : DefaultRetroCallback<StoreMainPageResponse>(callback){

        })

    }
}