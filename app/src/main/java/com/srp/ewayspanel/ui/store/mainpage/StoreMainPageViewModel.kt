package com.srp.ewayspanel.ui.store.mainpage

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.srp.eways.base.BaseViewModel
import com.srp.eways.model.banner.BannerResponse
import com.srp.eways.network.BaseCallBackWrapper
import com.srp.ewayspanel.di.DI
import com.srp.ewayspanel.model.storepage.mainpage.Data
import com.srp.ewayspanel.model.storepage.mainpage.StoreMainPageResponse

/**
 * Created by ErfanG on 2/18/2020.
 */
class StoreMainPageViewModel : BaseViewModel {

    private val mRepository = DI.getStoreMainPageRepository()
    private val mBannerRepo = DI.getBannerRepo()

    private var mIsLoading = MutableLiveData<Boolean>()
    private var mError = MutableLiveData<String>()
    private var mData = MutableLiveData<ArrayList<Data>>()

    private val mBannerResponseLive = MutableLiveData<BannerResponse>()
    private val mBannerLoadingLive = MutableLiveData<Boolean>()
    private val mBannerError = MutableLiveData<String>()

    constructor()

    companion object {
        private const val STORE_BANNER_TYPE = 7
    }

    fun getMainPageData(itemCount: Int) {

        mIsLoading.value = true

        mRepository.getMainData(itemCount, object : BaseCallBackWrapper<StoreMainPageResponse>(this) {
            override fun onError(errorCode: Int, errorMessage: String?) {

                Log.d("StoreMainPageViewModel", errorMessage + errorCode)

                mError.value = errorMessage

                mIsLoading.value = false
            }

            override fun onSuccess(responseBody: StoreMainPageResponse) {


                if (responseBody.status == 0) {
                    mData.value = responseBody.data
                    mError.value = null
                } else {
                    mError.value = responseBody.description
                }

                mIsLoading.value = false
            }

        })
    }


    fun getBannerData() {
        mBannerLoadingLive.value = true

        mBannerRepo.getBannerList(STORE_BANNER_TYPE, object : BaseCallBackWrapper<BannerResponse>(this) {
            override fun onError(errorCode: Int, errorMessage: String?) {

                mBannerLoadingLive.value = false
                mBannerError.value = errorMessage
            }

            override fun onSuccess(responseBody: BannerResponse?) {
                mBannerLoadingLive.value = false
                mBannerError.value = null

                mBannerResponseLive.value = responseBody
            }
        })
    }

    fun getBannerLive() = mBannerResponseLive
    fun getMainPageData() = mData
    fun getIsLoading() = mIsLoading
    fun getError() = mError
}