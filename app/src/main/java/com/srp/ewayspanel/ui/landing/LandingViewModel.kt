package com.srp.ewayspanel.ui.landing

import androidx.lifecycle.MutableLiveData
import com.srp.eways.base.BaseViewModel
import com.srp.eways.model.banner.BannerResponse
import com.srp.eways.model.charge.result.topinquiry.TopInquiriesResult
import com.srp.eways.network.BaseCallBackWrapper
import com.srp.eways.network.CallBackWrapper
import com.srp.eways.network.NetworkResponseCodes
import com.srp.eways.ui.charge.model.ChargeData
import com.srp.ewayspanel.di.DI
import com.srp.ewayspanel.model.appservice.AppService
import com.srp.ewayspanel.model.appservice.AppServiceResponse
import com.srp.ewayspanel.model.appservice.AppVersionChangesResponse
import com.srp.ewayspanel.model.appservice.AppVersionResponse
import com.srp.ewayspanel.repository.appservice.AppServiceRepository
import com.srp.ewayspanel.ui.view.landing.LandingUtil

class LandingViewModel : BaseViewModel() {

    companion object {
        const val MINIMUM_SPLASH_VISIBILITY_INTERVAL = 2 * 1000
        const val MAXIMUM_SPLASH_VISIBILITY_INTERVAL = 10 * 1000
    }

    private val mBannerRepo = DI.getBannerRepo()
    private val mAppServiceRepo: AppServiceRepository = DI.getAppServiceRepo()

    private val mBannerResponseLive = MutableLiveData<BannerResponse>()
    private val mBannerLoadingLive = MutableLiveData<Boolean>()
    private val mBannerError = MutableLiveData<String>()

    private var mAppServiceListLiveData = MutableLiveData<AppServiceResponse>()
    private var mAppVersionLiveData = MutableLiveData<AppVersionResponse>()
    private var mAppVersionChangesLiveData = MutableLiveData<AppVersionChangesResponse>()

    private val mChargeDataLive = MutableLiveData<ChargeData>()
    private val mTopInquiries = MutableLiveData<TopInquiriesResult>()

    private var mIsChargeDataReceived = false
    private var mIsTopInquiriesReceived = false
    private var mIsBannerListReceived = false
    private var mIsServiceListReceived = false
    private val mIsDataCompleted = MutableLiveData<Boolean>()

    private var mLastSplashUpdateTime: Long = 0L

    fun getBannerFromServer(bannerType: Int) {

        mBannerLoadingLive.value = true

        mBannerRepo.getBannerList(bannerType, object : BaseCallBackWrapper<BannerResponse>(this) {
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

    fun getAppServiceList() {
        mAppServiceRepo.getAppServices(object : BaseCallBackWrapper<AppServiceResponse>(this) {
            override fun onError(errorCode: Int, errorMessage: String) {
                val response = AppServiceResponse(0, errorCode, errorMessage, null)
                mAppServiceListLiveData.value = response
            }

            override fun onSuccess(responseBody: AppServiceResponse?) {
                if (responseBody?.status == NetworkResponseCodes.SUCCESS && responseBody.data!!.isNotEmpty()) {
                    var list: MutableList<AppService> = responseBody.data!!
                    val finaList: MutableList<AppService> = arrayListOf()

//                    list.add(AppService(LandingUtil.SERVICE_MOBILE_LIST_CODE, 0, "لیست موبایل", 5, true, true, false))
//                    list.add(AppService(LandingUtil.SERVICE_INSURANCE_CODE, 0, "بیمه", 7, false, true, false))

                    list.sortedWith(compareBy { it.priority }).toMutableList()

                    for (appService in list) {
                        if (appService.id != LandingUtil.SERVICE_BILL_INQUIRY_CODE) {
                            finaList.add(appService)
                        }
                    }

                    responseBody.data = finaList.sortedWith(compareBy { it.priority }).toMutableList()

                    mAppServiceListLiveData.value = responseBody
                }
            }
        })
    }

    fun getLastVersion() {
        mAppServiceRepo.getLastAppVersion(object : CallBackWrapper<AppVersionResponse> {
            override fun onError(errorCode: Int, errorMessage: String) {
                mAppVersionLiveData.value = AppVersionResponse(errorCode, errorMessage, null)
            }

            override fun onSuccess(responseBody: AppVersionResponse?) {
                mAppVersionLiveData.value = responseBody
            }

        })
    }

    fun getAppVersionChanges(oldVersion: Int) {
        mAppServiceRepo.getAppVersionChanges(oldVersion,object : CallBackWrapper<AppVersionChangesResponse> {
            override fun onError(errorCode: Int, errorMessage: String) {
                mAppVersionChangesLiveData.value = AppVersionChangesResponse(errorCode, errorMessage, null)
            }

            override fun onSuccess(responseBody: AppVersionChangesResponse?) {
                mAppVersionChangesLiveData.value = responseBody
            }

        })
    }

    fun getAppServiceListLiveData(): MutableLiveData<AppServiceResponse> {
        return mAppServiceListLiveData
    }

    fun consumeAppServiceListLiveData() {
        mAppServiceListLiveData.value = null
    }

    fun getAppVersionLiveData(): MutableLiveData<AppVersionResponse> {
        return mAppVersionLiveData
    }

    fun getAppVersionChangesLiveData(): MutableLiveData<AppVersionChangesResponse> {
        return mAppVersionChangesLiveData
    }

    fun setChargeData(chargeData: ChargeData) {
        mChargeDataLive.value = chargeData
    }

    fun getChargeData(): ChargeData? {
        return mChargeDataLive.value
    }

    fun setTopInquiresResult(topInquiriesResult: TopInquiriesResult) {
        mTopInquiries.value = topInquiriesResult
    }

    fun getTopInquiresResult(): TopInquiriesResult? {
        return mTopInquiries.value
    }

    fun checkIsDataCompleted() {
        val time = System.currentTimeMillis() - getLastSplashUpdateTime()

        if (mIsDataCompleted.value != null && (mIsDataCompleted.value as Boolean)) {
            return
        }

        mIsDataCompleted.value = mIsServiceListReceived
                && (time > MINIMUM_SPLASH_VISIBILITY_INTERVAL)

        if (time >= MAXIMUM_SPLASH_VISIBILITY_INTERVAL) {
            if (mAppVersionLiveData.value == null) {
                mAppVersionLiveData.value = AppVersionResponse(-1, "", null)
            } else {
                val response = AppServiceResponse(0, -1, "", null)
                mAppServiceListLiveData.value = response
            }
        }
    }

    fun getIsDataCompleted(): MutableLiveData<Boolean> {
        return mIsDataCompleted
    }

    fun consumeIsDataCompleted() {
        mIsDataCompleted.value = null
    }

    fun setIsTopInquiriesReceived(isInquiriesReceived: Boolean) {
        mIsTopInquiriesReceived = isInquiriesReceived
    }

    fun setIsBannerListReceived(isBannerListReceived: Boolean) {
        mIsBannerListReceived = isBannerListReceived
    }

    fun setIsChargeDataReceived(isChargeDataReceived: Boolean) {
        mIsChargeDataReceived = isChargeDataReceived
    }

    fun setIsServiceListReceived(isServiceListReceived: Boolean) {
        mIsServiceListReceived = isServiceListReceived
    }

    fun getLastSplashUpdateTime(): Long {
        return mLastSplashUpdateTime
    }

    fun setLastSplashUpdateTime() {
        mLastSplashUpdateTime = System.currentTimeMillis()
    }
}