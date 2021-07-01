package com.srp.ewayspanel.repository.remote.login

import com.srp.ewayspanel.AppConfig
import com.srp.ewayspanel.di.DI
import com.srp.ewayspanel.model.login.*
import com.srp.eways.network.CallBackWrapper
import com.srp.eways.network.NetworkResponseCodes
import com.srp.eways.repository.remote.DefaultRetroCallback
import com.srp.eways.repository.remote.login.MainLoginApiImplementation
import com.srp.ewayspanel.model.login.authenticate.AuthenticateRequest
import com.srp.ewayspanel.model.login.authenticate.AuthenticateResponse
import com.srp.ewayspanel.model.login.registerwithotp.RegisterWithOTPRequest
import com.srp.ewayspanel.model.login.registerwithotp.RegisterWithOTPResponse
import com.srp.ewayspanel.model.login.sms.*
import com.srp.ewayspanel.model.login.verifyotp.VerifyOTPRequest
import com.srp.ewayspanel.model.login.verifyotp.VerifyOTPResponse


/**
 * Created by ErfanG on 07/08/2019.
 */
class LoginApiImplementation: MainLoginApiImplementation(), LoginApiService {

    private val mLoginApiRetrofit : LoginApiRetrofit

    companion object{
        val instance = LoginApiImplementation()
    }

    init {
        mLoginApiRetrofit = DI.provideApi(LoginApiRetrofit::class.java)
    }

    override fun sendVerificationCode(requestBody: ForgetPasswordRequest, callback: CallBackWrapper<ForgetPasswordResponse>) {
        mLoginApiRetrofit.sendVerifyCode(AppConfig.SERVER_VERSION, requestBody).enqueue(object : DefaultRetroCallback<ForgetPasswordResponse>(callback) {

            override fun checkResponseForError(response: ForgetPasswordResponse?, errorInfo: ErrorInfo?) {
                if (response?.status != 0) {
                    errorInfo?.errorCode = response?.status
                    errorInfo?.errorMessage = response?.description
                }
            }

        })
    }

    override fun verifyCode(requestBody: VerifyCodeRequest, callback: CallBackWrapper<VerifyCodeResponse>) {
        mLoginApiRetrofit.verifyCode(AppConfig.SERVER_VERSION, requestBody).enqueue(object : DefaultRetroCallback<VerifyCodeResponse>(callback) {

            override fun checkResponseForError(response: VerifyCodeResponse?, errorInfo: ErrorInfo?) {
                if (response?.status != 0) {
                    errorInfo?.errorCode = response?.status
                    errorInfo?.errorMessage = response?.description
                }
            }

        })
    }

    override fun resetPassword(requestBody: ResetPasswordRequest, callback: CallBackWrapper<ResetPasswordResponse>) {
        mLoginApiRetrofit.resetPassword(AppConfig.SERVER_VERSION, requestBody).enqueue(object : DefaultRetroCallback<ResetPasswordResponse>(callback) {

            override fun checkResponseForError(response: ResetPasswordResponse?, errorInfo: ErrorInfo?) {
                if (response?.status != 0) {
                    errorInfo?.errorCode = response?.status
                    errorInfo?.errorMessage = response?.description
                }
            }

        })
    }

    override fun authenticate(request: AuthenticateRequest, callback: CallBackWrapper<AuthenticateResponse>) {
        mLoginApiRetrofit.authenticate(AppConfig.SERVER_VERSION, request).enqueue(object : DefaultRetroCallback<AuthenticateResponse>(callback) {

            override fun checkResponseForError(response: AuthenticateResponse, errorInfo: ErrorInfo) {

                if (response.status != NetworkResponseCodes.SUCCESS) {

                    errorInfo.errorCode = response.status
                    errorInfo.errorMessage = response.description
                }
            }
        })    }

    override fun verifyOTP(request: VerifyOTPRequest, callback: CallBackWrapper<VerifyOTPResponse>) {
        mLoginApiRetrofit.verifyOTP(AppConfig.SERVER_VERSION, request).enqueue(object : DefaultRetroCallback<VerifyOTPResponse>(callback) {

            override fun checkResponseForError(response: VerifyOTPResponse, errorInfo: ErrorInfo) {

                if (response.status != NetworkResponseCodes.SUCCESS && response.status != -8) {

                    errorInfo.errorCode = response.status
                    errorInfo.errorMessage = response.description
                }
            }
        })    }

    override fun registerWithOTP(request: RegisterWithOTPRequest, callback: CallBackWrapper<RegisterWithOTPResponse>) {
        mLoginApiRetrofit.registerWithOTP(AppConfig.SERVER_VERSION, request).enqueue(DefaultRetroCallback<RegisterWithOTPResponse>(callback))
    }

    override fun verifyMobileNumber(requestBody: OutSmsRequest, callback: CallBackWrapper<OutSmsResponse>) {
        mLoginApiRetrofit.verifyMobileNumber(AppConfig.SERVER_VERSION,requestBody).enqueue(object :DefaultRetroCallback<OutSmsResponse>(callback){
            override fun checkResponseForError(response: OutSmsResponse?, errorInfo: ErrorInfo?) {

                if (response?.status != 0) {
                    errorInfo?.errorCode = response?.status
                    errorInfo?.errorMessage = response?.description
                }
            }
        })
    }

    override fun verifyOtpNumber(requestBody: OutSmsVerifiRequest, callback: CallBackWrapper<OutSmsVerifyResponse>) {
        mLoginApiRetrofit.verifyOtpNumber(AppConfig.SERVER_VERSION,requestBody).enqueue(object :DefaultRetroCallback<OutSmsVerifyResponse>(callback){
            override fun checkResponseForError(response: OutSmsVerifyResponse?, errorInfo: ErrorInfo?) {

                if (response?.status != 0) {
                    errorInfo?.errorCode = response?.status
                    errorInfo?.errorMessage = response?.description
                }
            }
        })
    }

    override fun sendMobileConfirmRequest(inputNumber: String, callback: CallBackWrapper<LoggedSmsRequestResponse>) {
        mLoginApiRetrofit.sendMobileConfirmRequest(AppConfig.SERVER_VERSION,inputNumber).enqueue(object :DefaultRetroCallback<LoggedSmsRequestResponse>(callback){
            override fun checkResponseForError(response: LoggedSmsRequestResponse?, errorInfo: ErrorInfo?) {

                if (response?.status != 0) {
                    errorInfo?.errorCode = response?.status
                    errorInfo?.errorMessage = response?.description
                }
            }
        })
    }

    override fun confirmMobileNumber(inputNumber: String, token: String, callback: CallBackWrapper<LoggedSmsConfirmResponse>) {
        mLoginApiRetrofit.confirmMobileNumber(AppConfig.SERVER_VERSION,inputNumber,token).enqueue(object :DefaultRetroCallback<LoggedSmsConfirmResponse>(callback){
            override fun checkResponseForError(response: LoggedSmsConfirmResponse?, errorInfo: ErrorInfo?) {
                if (response?.status != 0) {
                    errorInfo?.errorCode = response?.status
                    errorInfo?.errorMessage = response?.description
                }
            }
        })
    }
}