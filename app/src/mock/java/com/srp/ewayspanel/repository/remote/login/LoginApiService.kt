package com.srp.ewayspanel.repository.remote.login

import com.srp.eways.network.CallBackWrapper
import com.srp.eways.repository.remote.login.MainLoginApiService
import com.srp.ewayspanel.model.login.*

/**
 * Created by ErfanG on 08/08/2019.
 */
interface LoginApiService : MainLoginApiService{

    fun sendVerificationCode(requestBody : ForgetPasswordRequest, callback : CallBackWrapper<ForgetPasswordResponse>)

    fun verifyCode(requestBody: VerifyCodeRequest, callback: CallBackWrapper<VerifyCodeResponse>)

    fun resetPassword(requestBody: ResetPasswordRequest, callback: CallBackWrapper<ResetPasswordResponse>)
}