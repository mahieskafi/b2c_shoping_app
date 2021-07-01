package com.srp.ewayspanel.repository.appservice

import com.srp.eways.network.CallBackWrapper
import com.srp.ewayspanel.model.appservice.AppServiceResponse
import com.srp.ewayspanel.model.appservice.AppVersionChangesResponse
import com.srp.ewayspanel.model.appservice.AppVersionResponse

interface AppServiceRepository {

    fun getAppServices(callBack: CallBackWrapper<AppServiceResponse>)

    fun getLastAppVersion(callback: CallBackWrapper<AppVersionResponse>)

    fun getAppVersionChanges(oldVersion: Int, callback: CallBackWrapper<AppVersionChangesResponse>)
}