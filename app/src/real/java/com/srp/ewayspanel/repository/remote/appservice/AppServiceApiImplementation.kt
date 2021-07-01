package com.srp.ewayspanel.repository.remote.appservice

import com.srp.eways.AppConfig
import com.srp.eways.di.DIMain
import com.srp.eways.network.CallBackWrapper
import com.srp.eways.repository.remote.BaseApiImplementation
import com.srp.eways.repository.remote.DefaultRetroCallback
import com.srp.ewayspanel.BuildConfig
import com.srp.ewayspanel.model.appservice.AppServiceResponse
import com.srp.ewayspanel.model.appservice.AppVersionChangesResponse
import com.srp.ewayspanel.model.appservice.AppVersionResponse

open class AppServiceApiImplementation : BaseApiImplementation(), AppServiceApiService {

    private val mAppServiceApiRetrofit: AppServiceApiRetrofit

    companion object {
        val instance = AppServiceApiImplementation()
    }

    init {
        mAppServiceApiRetrofit = DIMain.provideApi(AppServiceApiRetrofit::class.java)
    }

    override fun getAppServices(callback: CallBackWrapper<AppServiceResponse>) {
        mAppServiceApiRetrofit.getAppServices(AppConfig.SERVER_VERSION).enqueue(object : DefaultRetroCallback<AppServiceResponse>(callback) {

            override fun checkResponseForError(response: AppServiceResponse?, errorInfo: ErrorInfo?) {
                if (response?.status != 0) {
                    errorInfo?.errorCode = response?.status
                    errorInfo?.errorMessage = response?.description
                }
            }

        })
    }

    override fun getLastAppVersion(callback: CallBackWrapper<AppVersionResponse>) {
        mAppServiceApiRetrofit.getLastMobileVersion(AppConfig.SERVER_VERSION, BuildConfig.VERSION_CODE).enqueue(object : DefaultRetroCallback<AppVersionResponse>(callback) {

            override fun checkResponseForError(response: AppVersionResponse?, errorInfo: ErrorInfo?) {
                if (response?.status != 0) {
                    errorInfo?.errorCode = response?.status
                    errorInfo?.errorMessage = response?.description
                }
            }

        })
    }

    override fun getAppVersionChanges(oldVersion: Int, callback: CallBackWrapper<AppVersionChangesResponse>) {
        mAppServiceApiRetrofit.getMobileVersionChanges(AppConfig.SERVER_VERSION, oldVersion).enqueue(object : DefaultRetroCallback<AppVersionChangesResponse>(callback) {

            override fun checkResponseForError(response: AppVersionChangesResponse?, errorInfo: ErrorInfo?) {
                if (response?.status != 0) {
                    errorInfo?.errorCode = response?.status
                    errorInfo?.errorMessage = response?.description
                }
            }

        })
    }

}