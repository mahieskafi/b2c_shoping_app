package com.srp.ewayspanel.repository.remote.appservice

import com.srp.eways.network.CallBackWrapper
import com.srp.ewayspanel.model.appservice.AppServiceResponse
import com.srp.ewayspanel.model.appservice.AppVersionChangesResponse
import com.srp.ewayspanel.model.appservice.AppVersionResponse

interface AppServiceApiService {

    fun getAppServices(callback: CallBackWrapper<AppServiceResponse>)

    fun getLastAppVersion(callback: CallBackWrapper<AppVersionResponse>)

    fun getAppVersionChanges(oldVersion: Int, callback: CallBackWrapper<AppVersionChangesResponse>)
}