package com.srp.eways.repository.remote.validatetoken

import com.srp.eways.AppConfig
import com.srp.eways.di.DIMain
import com.srp.eways.model.login.LoginResponse
import com.srp.eways.network.CallBackWrapper
import com.srp.eways.repository.remote.BaseApiImplementation
import com.srp.eways.repository.remote.DefaultRetroCallback

/**
 * Created by Eskafi on 8/25/2019.
 */
class ValidateTokenApiImplementation : BaseApiImplementation(), ValidateTokenApiService {
    
    private val mRetrofit: ValidateTokenApiRetrofit

    companion object {

        private var instance: ValidateTokenApiImplementation? = null

        fun getInstance(): ValidateTokenApiImplementation {

            if (instance == null) {
                instance = ValidateTokenApiImplementation()
            }
            return instance as ValidateTokenApiImplementation
        }
    }

    init {
        mRetrofit = DIMain.provideApi(ValidateTokenApiRetrofit::class.java)
    }


    override fun validateToken(callBack: CallBackWrapper<LoginResponse>) {
        mRetrofit.validateToken(AppConfig.SERVER_VERSION).enqueue(DefaultRetroCallback<LoginResponse>(callBack))
    }

}
