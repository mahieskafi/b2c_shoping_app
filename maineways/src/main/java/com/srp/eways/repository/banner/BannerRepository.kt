package com.srp.eways.repository.banner

import com.srp.eways.model.banner.BannerResponse
import com.srp.eways.network.CallBackWrapper

interface BannerRepository {
    


    companion object{
        val BANNER_TYPE_MAIN_PAGE: Int
            get() = 5

        val BANNER_TYPE_CHARGE: Int
            get() = 6

        val BANNER_TYPE_STORE: Int
            get() = 7
    }

    fun getBannerList(typeId : Int, callback : CallBackWrapper<BannerResponse>)
}