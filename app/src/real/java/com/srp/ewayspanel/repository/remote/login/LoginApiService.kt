package com.srp.ewayspanel.repository.remote.login

import com.srp.eways.network.CallBackWrapper
import com.srp.eways.repository.remote.login.MainLoginApiService
import com.srp.ewayspanel.model.login.*
import com.srp.ewayspanel.model.login.authenticate.AuthenticateRequest
import com.srp.ewayspanel.model.login.authenticate.AuthenticateResponse
import com.srp.ewayspanel.model.login.registerwithotp.RegisterWithOTPRequest
import com.srp.ewayspanel.model.login.registerwithotp.RegisterWithOTPResponse
import com.srp.ewayspanel.model.login.sms.*
import com.srp.ewayspanel.model.login.verifyotp.VerifyOTPRequest
import com.srp.ewayspanel.model.login.verifyotp.VerifyOTPResponse

/**
 * Created by ErfanG on 08/08/2019.
 */
interface LoginApiService : MainLoginApiService {

    fun sendVerificationCode(requestBody: ForgetPasswordRequest, callback: CallBackWrapper<ForgetPasswordResponse>)

    fun verifyCode(requestBody: VerifyCodeRequest, callback: CallBackWrapper<VerifyCodeResponse>)

    fun resetPassword(requestBody: ResetPasswordRequest, callback: CallBackWrapper<ResetPasswordResponse>)

    fun authenticate(request: AuthenticateRequest, callback: CallBackWrapper<AuthenticateResponse>)

    fun verifyOTP(request: VerifyOTPRequest, callback: CallBackWrapper<VerifyOTPResponse>)

    fun registerWithOTP(request: RegisterWithOTPRequest, callback: CallBackWrapper<RegisterWithOTPResponse>)

    fun verifyMobileNumber(requestBody: OutSmsRequest, callback: CallBackWrapper<OutSmsResponse>)
    fun verifyOtpNumber(requestBody: OutSmsVerifiRequest, callback: CallBackWrapper<OutSmsVerifyResponse>)

    fun sendMobileConfirmRequest(inputNumber : String, callback: CallBackWrapper<LoggedSmsRequestResponse>)
    fun confirmMobileNumber(inputNumber : String, token : String, callback: CallBackWrapper<LoggedSmsConfirmResponse>)
}