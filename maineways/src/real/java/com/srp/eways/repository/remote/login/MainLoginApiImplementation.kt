package com.srp.eways.repository.remote.login

import com.srp.eways.AppConfig
import com.srp.eways.di.DIMain
import com.srp.eways.model.login.LoginRequest
import com.srp.eways.model.login.LoginResponse
import com.srp.eways.network.CallBackWrapper
import com.srp.eways.repository.remote.BaseApiImplementation
import com.srp.eways.repository.remote.DefaultRetroCallback

open class MainLoginApiImplementation : BaseApiImplementation(), MainLoginApiService {

    private val mLoginApiRetrofit : MainLoginApiRetrofit

    companion object{
        val instance = MainLoginApiImplementation()
    }

    init {
        mLoginApiRetrofit = DIMain.provideApi(MainLoginApiRetrofit::class.java)
    }

    override fun login(body: LoginRequest, callBack: CallBackWrapper<LoginResponse>) {
        mLoginApiRetrofit.login(AppConfig.SERVER_VERSION, body).enqueue(object : DefaultRetroCallback<LoginResponse>(callBack) {

            override fun checkResponseForError(response: LoginResponse?, errorInfo: ErrorInfo?) {
                if (response?.status != 0) {
                    errorInfo?.errorCode = response?.status
                    errorInfo?.errorMessage = response?.description
                }
            }

        })
    }
}