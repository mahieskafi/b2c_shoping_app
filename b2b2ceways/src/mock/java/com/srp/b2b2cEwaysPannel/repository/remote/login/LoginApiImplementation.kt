package com.srp.b2b2cEwaysPannel.repository.remote.login

import android.os.Handler
import com.google.gson.Gson
import com.srp.b2b2cEwaysPannel.model.login.authenticate.AuthenticateRequest
import com.srp.b2b2cEwaysPannel.model.login.authenticate.AuthenticateResponse
import com.srp.b2b2cEwaysPannel.model.login.registerwithotp.RegisterWithOTPRequest
import com.srp.b2b2cEwaysPannel.model.login.registerwithotp.RegisterWithOTPResponse
import com.srp.b2b2cEwaysPannel.model.login.verifyotp.VerifyOTPRequest
import com.srp.b2b2cEwaysPannel.model.login.verifyotp.VerifyOTPResponse
import com.srp.eways.model.login.UserInfo
import com.srp.eways.network.CallBackWrapper
import com.srp.eways.network.NetworkResponseCodes
import com.srp.eways.network.NetworkResponseCodes.SUCCESS
import com.srp.eways.repository.remote.login.MainLoginApiImplementation

/**
 * Created by ErfanG on 3/3/2020.
 */
object LoginApiImplementation : MainLoginApiImplementation(), LoginApiService {

    const val OTP_CODE = "12345678"
    const val TOKEN = "sdcshdbljs"

    const val ERROR_INVALID_OTP = -1

    private val userData : String = "{\n" +
            "    \"UserId\": 101610,\n" +
            "    \"UserName\": \"09187888582\",\n" +
            "    \"FirstName\": \"\",\n" +
            "    \"LastName\": \"\",\n" +
            "    \"FullName\": \" \",\n" +
            "    \"RegisterDate\": \"2020-03-10T16:40:08.293\",\n" +
            "    \"LastLogDate\": \"2020-03-14T19:14:00\",\n" +
            "    \"UpdateDate\": null,\n" +
            "    \"NationalId\": \"\",\n" +
            "    \"Mobile\": null,\n" +
            "    \"Address\": null,\n" +
            "    \"Phone\": null,\n" +
            "    \"PostCode\": \"\",\n" +
            "    \"BirthDate\": null,\n" +
            "    \"StateId\": 0,\n" +
            "    \"StateName\": null,\n" +
            "    \"TownId\": 0,\n" +
            "    \"TownName\": null,\n" +
            "    \"SubPartId\": null,\n" +
            "    \"SitePassword\": \"20C1)07817\",\n" +
            "    \"Email\": \"09187888582\",\n" +
            "    \"Gender\": 0,\n" +
            "    \"Credit\": 0,\n" +
            "    \"BlockedCredit\": 0,\n" +
            "    \"Revenue\": 0,\n" +
            "    \"SiteName\": \"\",\n" +
            "    \"LoyaltyScore\": 0,\n" +
            "    \"ProvincialScore\": 0,\n" +
            "    \"CrmUserName\": null,\n" +
            "    \"CrmUserId\": 0,\n" +
            "    \"NotificationCount\": 0,\n" +
            "    \"ParentId\": 0,\n" +
            "    \"RoleId\": 0,\n" +
            "    \"IsCreditUser\": false,\n" +
            "    \"IsOneTimePassword\": null,\n" +
            "    \"SaleChannelsInfo\": null,\n" +
            "    \"Status\": 0,\n" +
            "    \"Description\": \"عملیات با موفقیت انجام شد\"\n" +
            "  }"

    val USER_INFO : UserInfo = Gson().fromJson(userData, UserInfo::class.java)

    override fun authenticate(request: AuthenticateRequest, callback: CallBackWrapper<AuthenticateResponse>) {
        setMode(NetworkResponseCodes.SUCCESS)

        if(!handleCall(callback)){
            handleAuthenticteCallInternal(request, callback)
        }
    }

    fun handleAuthenticteCallInternal(body : AuthenticateRequest, callback : CallBackWrapper<AuthenticateResponse>) {

        postAuthenticteResult(callback, createAuthenticteSuccessResult(body))
    }

    fun postAuthenticteResult(callback: CallBackWrapper<AuthenticateResponse>, response: AuthenticateResponse){

        Handler().postDelayed({
            callback.onSuccess(response)
        }, getResponseDelay())
    }

    fun createAuthenticteSuccessResult(body : AuthenticateRequest): AuthenticateResponse{

        val req : AuthenticateResponse = AuthenticateResponse(userIsNew = true, description = "با موفقیت انجام شد")

        return req
    }



    override fun verifyOTP(request: VerifyOTPRequest, callback: CallBackWrapper<VerifyOTPResponse>) {
        setMode(SUCCESS)

        if(!handleCall(callback)){
            handleVerifyCallInternal(request, callback)
        }
    }

    fun handleVerifyCallInternal(body : VerifyOTPRequest, callback : CallBackWrapper<VerifyOTPResponse>) {

        if(body.token != OTP_CODE){
            setMode(ERROR_INVALID_OTP)
        }
        else{
            setMode(SUCCESS)
        }

        if(getMode() == SUCCESS) {

            postVerifyResult(callback, createVerifySuccessResult(body))
        }
        else if(getMode() == ERROR_INVALID_OTP){
            postVerifyErrorResult(callback, createVerifyFailResult(body))
        }
    }

    fun postVerifyResult(callback: CallBackWrapper<VerifyOTPResponse>, response: VerifyOTPResponse){

        Handler().postDelayed({
            callback.onSuccess(response)
        }, getResponseDelay())
    }

    fun postVerifyErrorResult(callback: CallBackWrapper<VerifyOTPResponse>, response: VerifyOTPResponse){

        Handler().postDelayed({
            callback.onError(response.status, response.description)
        }, getResponseDelay())
    }

    fun createVerifySuccessResult(body : VerifyOTPRequest): VerifyOTPResponse{

        val req = VerifyOTPResponse(token = OTP_CODE, userInfo = USER_INFO)

        return req
    }

    fun createVerifyFailResult(body : VerifyOTPRequest): VerifyOTPResponse{

        val req = VerifyOTPResponse(token = OTP_CODE, userInfo = null, description = "کد وارد شده صحیح نمی باشد.", status = -8)

        return req
    }



    override fun registerWithOTP(request: RegisterWithOTPRequest, callback: CallBackWrapper<RegisterWithOTPResponse>) {

        setMode(SUCCESS)

        if(!handleCall(callback)){
            handleRegisterCallInternal(request, callback)
        }
    }

    fun handleRegisterCallInternal(body : RegisterWithOTPRequest, callback : CallBackWrapper<RegisterWithOTPResponse>) {

        postRegisterResult(callback, createRegisterSuccessResult(body))
    }

    fun postRegisterResult(callback: CallBackWrapper<RegisterWithOTPResponse>, response: RegisterWithOTPResponse){

        Handler().postDelayed({
            callback.onSuccess(response)
        }, getResponseDelay())
    }

    fun createRegisterSuccessResult(body : RegisterWithOTPRequest): RegisterWithOTPResponse{

        val req = RegisterWithOTPResponse(token = TOKEN, userInfo = USER_INFO)

        return req
    }
}