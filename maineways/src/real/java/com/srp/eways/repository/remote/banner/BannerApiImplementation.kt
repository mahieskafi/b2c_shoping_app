package com.srp.eways.repository.remote.banner

import com.srp.eways.AppConfig
import com.srp.eways.di.DIMain
import com.srp.eways.model.banner.BannerResponse
import com.srp.eways.network.CallBackWrapper
import com.srp.eways.repository.remote.DefaultRetroCallback

object BannerApiImplementation : BannerApiService {

    private val mBannerApi : BannerApiRetrofit = DIMain.provideApi(BannerApiRetrofit::class.java)


    override fun getBannerList(typeId: Int, callback: CallBackWrapper<BannerResponse>) {

        mBannerApi.getBanners(AppConfig.SERVER_VERSION, typeId).enqueue(DefaultRetroCallback<BannerResponse>(callback))
    }
}