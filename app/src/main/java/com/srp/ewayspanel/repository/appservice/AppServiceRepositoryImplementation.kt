package com.srp.ewayspanel.repository.appservice

import com.srp.eways.network.CallBackWrapper
import com.srp.ewayspanel.di.DI
import com.srp.ewayspanel.model.appservice.AppServiceResponse
import com.srp.ewayspanel.model.appservice.AppVersionChangesResponse
import com.srp.ewayspanel.model.appservice.AppVersionResponse
import com.srp.ewayspanel.repository.remote.appservice.AppServiceApiService

class AppServiceRepositoryImplementation : AppServiceRepository {

    var mAppServiceApiService: AppServiceApiService? = null

    companion object {
        var sInstance: AppServiceRepositoryImplementation? = null

        fun getInstance(): AppServiceRepositoryImplementation {

            if (sInstance == null) {
                sInstance = AppServiceRepositoryImplementation()
            }
            return sInstance as AppServiceRepositoryImplementation
        }
    }

    init {
        mAppServiceApiService = DI.getAppServiceApi()
    }

    override fun getAppServices(callBack: CallBackWrapper<AppServiceResponse>) {
        mAppServiceApiService?.getAppServices(callBack)
    }

    override fun getLastAppVersion(callback: CallBackWrapper<AppVersionResponse>) {
        mAppServiceApiService?.getLastAppVersion(callback)
    }

    override fun getAppVersionChanges(oldVersion: Int, callback: CallBackWrapper<AppVersionChangesResponse>) {
        mAppServiceApiService?.getAppVersionChanges(oldVersion, callback)
    }
}