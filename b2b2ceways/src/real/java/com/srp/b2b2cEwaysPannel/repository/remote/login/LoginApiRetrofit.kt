package com.srp.b2b2cEwaysPannel.repository.remote.login

import com.srp.b2b2cEwaysPannel.model.login.authenticate.AuthenticateRequest
import com.srp.b2b2cEwaysPannel.model.login.authenticate.AuthenticateResponse
import com.srp.b2b2cEwaysPannel.model.login.registerwithotp.RegisterWithOTPRequest
import com.srp.b2b2cEwaysPannel.model.login.registerwithotp.RegisterWithOTPResponse
import com.srp.b2b2cEwaysPannel.model.login.verifyotp.VerifyOTPRequest
import com.srp.b2b2cEwaysPannel.model.login.verifyotp.VerifyOTPResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Created by ErfanG on 3/3/2020.
 */
interface LoginApiRetrofit {

    @POST("service/v{version}/user/authenticate")
    fun authenticate(
            @Path("version")
            version: Int,
            @Body
            body: AuthenticateRequest
    ): Call<AuthenticateResponse>


    @POST("service/v{version}/user/verifyotp")
    fun verifyOTP(
            @Path("version")
            version: Int,
            @Body
            body: VerifyOTPRequest
    ): Call<VerifyOTPResponse>

    @POST("service/v{version}/user/registerwithotp")
    fun registerWithOTP(
            @Path("version")
            version: Int,
            @Body
            body: RegisterWithOTPRequest
    ): Call<RegisterWithOTPResponse>


}