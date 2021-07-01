package com.srp.eways.repository.banner

import com.srp.eways.di.DIMain
import com.srp.eways.model.banner.BannerResponse
import com.srp.eways.network.CallBackWrapper
import com.srp.eways.repository.remote.banner.BannerApiService

class BannerRepositoryImplementation : BannerRepository {

    private val mBannerApiService : BannerApiService = DIMain.getBannerApi()

    override fun getBannerList(typeId: Int, callback: CallBackWrapper<BannerResponse>) {
        mBannerApiService.getBannerList(typeId, callback)
    }
}