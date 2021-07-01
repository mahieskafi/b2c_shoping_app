package com.srp.ewayspanel.repository.remote.appservice

import com.srp.eways.network.CallBackWrapper
import com.srp.eways.network.NetworkResponseCodes
import com.srp.eways.repository.remote.BaseApiImplementation
import com.srp.ewayspanel.model.appservice.AppServiceResponse
import com.srp.ewayspanel.model.appservice.AppVersionResponse

class AppServiceApiImplementation : BaseApiImplementation(), AppServiceApiService {

    companion object{
        val instance = AppServiceApiImplementation()
    }
    override fun getAppServices(callback: CallBackWrapper<AppServiceResponse>) {
        setMode(NetworkResponseCodes.SUCCESS)

        if(!handleCall(callback)){
//            handleLoginCallInternal(callback)
        }
    }


    override fun getLastAppVersion(callback: CallBackWrapper<AppVersionResponse>) {
        setMode(NetworkResponseCodes.SUCCESS)

        if(!handleCall(callback)){
//            handleLoginCallInternal(callback)
        }
    }
}