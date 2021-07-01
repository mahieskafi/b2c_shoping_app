package com.srp.eways.repository.remote.banner

import com.srp.eways.model.banner.BannerResponse
import com.srp.eways.network.CallBackWrapper

interface BannerApiService {

    fun getBannerList(typeId : Int, callback : CallBackWrapper<BannerResponse>)
}