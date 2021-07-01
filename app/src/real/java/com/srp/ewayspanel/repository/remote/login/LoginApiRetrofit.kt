package com.srp.ewayspanel.repository.remote.login

import com.srp.ewayspanel.model.address.ProvinceResponse
import com.srp.ewayspanel.model.login.*
import com.srp.ewayspanel.model.login.authenticate.AuthenticateRequest
import com.srp.ewayspanel.model.login.authenticate.AuthenticateResponse
import com.srp.ewayspanel.model.login.registerwithotp.RegisterWithOTPRequest
import com.srp.ewayspanel.model.login.registerwithotp.RegisterWithOTPResponse
import com.srp.ewayspanel.model.login.sms.*
import com.srp.ewayspanel.model.login.verifyotp.VerifyOTPRequest
import com.srp.ewayspanel.model.login.verifyotp.VerifyOTPResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Created by ErfanG on 08/08/2019.
 */
interface LoginApiRetrofit  {

    @POST("service/v{version}/user/forgetpassword")
    fun sendVerifyCode(
            @Path("version")
            version: Int,
            @Body
            body: ForgetPasswordRequest
    ): Call<ForgetPasswordResponse>


    @POST("service/v{version}/user/verifyforget")
    fun verifyCode(
            @Path("version")
            version: Int,
            @Body
            body: VerifyCodeRequest
    ): Call<VerifyCodeResponse>


    @POST("service/v{version}/user/resetpassword")
    fun resetPassword(
            @Path("version")
            version: Int,
            @Body
            body: ResetPasswordRequest
    ): Call<ResetPasswordResponse>

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


    @POST("service/v{version}/user/verifymobilenumber")
    fun verifyMobileNumber(
            @Path("version")
            version: Int,
            @Body
            body: OutSmsRequest
    ): Call<OutSmsResponse>

    @POST("service/v{version}/user/verifymobilenumberotp")
    fun verifyOtpNumber(
            @Path("version")
            version: Int,
            @Body
            body: OutSmsVerifiRequest
    ): Call<OutSmsVerifyResponse>



    @GET("service/v{version}/user/sendmobileconfirmrequest/{mobileNumber}")
    fun sendMobileConfirmRequest(
            @Path("version")
            version: Int,
            @Path("mobileNumber")
            mobileNumber:String
    ): Call<LoggedSmsRequestResponse>


    @GET("service/v{version}/user/confirmmobilenumber/{mobileNumber}/{token}")
    fun confirmMobileNumber(
            @Path("version")
            version: Int,
            @Path("mobileNumber")
            mobileNumber:String,
            @Path("token")
            token:String
    ): Call<LoggedSmsConfirmResponse>
}