package com.srp.b2b2cEwaysPannel.repository.remote.login

import com.srp.b2b2cEwaysPannel.AppConfig
import com.srp.b2b2cEwaysPannel.di.DI
import com.srp.b2b2cEwaysPannel.model.login.authenticate.AuthenticateRequest
import com.srp.b2b2cEwaysPannel.model.login.authenticate.AuthenticateResponse
import com.srp.b2b2cEwaysPannel.model.login.registerwithotp.RegisterWithOTPRequest
import com.srp.b2b2cEwaysPannel.model.login.registerwithotp.RegisterWithOTPResponse
import com.srp.b2b2cEwaysPannel.model.login.verifyotp.VerifyOTPRequest
import com.srp.b2b2cEwaysPannel.model.login.verifyotp.VerifyOTPResponse
import com.srp.eways.network.CallBackWrapper
import com.srp.eways.network.NetworkResponseCodes
import com.srp.eways.repository.remote.DefaultRetroCallback
import com.srp.eways.repository.remote.login.MainLoginApiImplementation

/**
 * Created by ErfanG on 3/3/2020.
 */
object LoginApiImplementation : MainLoginApiImplementation(), LoginApiService {

    private val mLoginRetro = DI.provideApi(LoginApiRetrofit::class.java)


    override fun authenticate(request: AuthenticateRequest, callback: CallBackWrapper<AuthenticateResponse>) {
        mLoginRetro.authenticate(AppConfig.SERVER_VERSION, request).enqueue(object : DefaultRetroCallback<AuthenticateResponse>(callback) {

            override fun checkResponseForError(response: AuthenticateResponse, errorInfo: ErrorInfo) {

                if (response.status != NetworkResponseCodes.SUCCESS) {

                    errorInfo.errorCode = response.status
                    errorInfo.errorMessage = response.description
                } else {
                    super.checkResponseForError(response, errorInfo)
                }
            }
        })
    }

    override fun verifyOTP(request: VerifyOTPRequest, callback: CallBackWrapper<VerifyOTPResponse>) {
        mLoginRetro.verifyOTP(AppConfig.SERVER_VERSION, request).enqueue(object : DefaultRetroCallback<VerifyOTPResponse>(callback) {

            override fun checkResponseForError(response: VerifyOTPResponse, errorInfo: ErrorInfo) {

                if (response.status != NetworkResponseCodes.SUCCESS && response.status != -8) {

                    errorInfo.errorCode = response.status
                    errorInfo.errorMessage = response.description
                } else {

                    super.checkResponseForError(response, errorInfo)
                }
            }
        })
    }

    override fun registerWithOTP(request: RegisterWithOTPRequest, callback: CallBackWrapper<RegisterWithOTPResponse>) {
        mLoginRetro.registerWithOTP(AppConfig.SERVER_VERSION, request).enqueue(DefaultRetroCallback<RegisterWithOTPResponse>(callback))
    }
}