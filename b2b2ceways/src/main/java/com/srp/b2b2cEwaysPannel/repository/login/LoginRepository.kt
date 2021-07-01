package com.srp.b2b2cEwaysPannel.repository.login

import com.srp.b2b2cEwaysPannel.model.login.authenticate.AuthenticateRequest
import com.srp.b2b2cEwaysPannel.model.login.authenticate.AuthenticateResponse
import com.srp.b2b2cEwaysPannel.model.login.registerwithotp.RegisterWithOTPRequest
import com.srp.b2b2cEwaysPannel.model.login.registerwithotp.RegisterWithOTPResponse
import com.srp.b2b2cEwaysPannel.model.login.verifyotp.VerifyOTPRequest
import com.srp.b2b2cEwaysPannel.model.login.verifyotp.VerifyOTPResponse
import com.srp.eways.model.login.LoginRequest
import com.srp.eways.model.login.LoginResponse
import com.srp.eways.network.CallBackWrapper

/**
 * Created by ErfanG on 3/3/2020.
 */
interface LoginRepository {

    fun authenticate(request : AuthenticateRequest, callback : CallBackWrapper<AuthenticateResponse>)

    fun verifyOTP(request : VerifyOTPRequest, callback : CallBackWrapper<VerifyOTPResponse>)

    fun registerWithOTP(request : RegisterWithOTPRequest, callback : CallBackWrapper<RegisterWithOTPResponse>)

    fun login(body: LoginRequest, callBack: CallBackWrapper<LoginResponse>)
}