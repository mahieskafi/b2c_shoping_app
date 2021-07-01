package com.srp.eways.repository.remote.login

import android.os.Handler
import com.srp.eways.AppConfig
import com.srp.eways.di.DIMain
import com.srp.eways.model.login.LoginRequest
import com.srp.eways.model.login.LoginResponse
import com.srp.eways.model.login.UserInfo
import com.srp.eways.network.CallBackWrapper
import com.srp.eways.network.NetworkResponseCodes
import com.srp.eways.repository.remote.BaseApiImplementation

open class MainLoginApiImplementation : BaseApiImplementation(), MainLoginApiService {

    companion object{
        val instance = MainLoginApiImplementation()

        const val ERROR_USERNAME_PASSWORD = -3
        const val ERROR_WRONG_USERNAME = -4
        const val USERNAME = "s.moradiii"
        var PASSWORD = "123456"
    }

    init {

    }

    override fun login(body: LoginRequest, callback: CallBackWrapper<LoginResponse>){

        setMode(NetworkResponseCodes.SUCCESS)

        if(!handleCall(callback)){
            handleLoginCallInternal(body, callback)
        }
    }
    fun handleLoginCallInternal(body : LoginRequest, callback : CallBackWrapper<LoginResponse>) {

        if(body.UserName == USERNAME && body.Password == PASSWORD){
            setMode(NetworkResponseCodes.SUCCESS)
        }
        else{
            setMode(ERROR_USERNAME_PASSWORD)
        }

        if (getMode() == ERROR_USERNAME_PASSWORD) {
            postResult(callback, createPasswordErrorResult())
        }
        else {
            postResult(callback, createSuccessResult())
        }
    }

    fun postResult(callback: CallBackWrapper<LoginResponse>, response: LoginResponse){

        Handler().postDelayed({
            callback.onSuccess(response)
        }, getResponseDelay())
    }

    fun createSuccessResult(): LoginResponse{

        val userInfo : UserInfo = UserInfo(firstName = "username")
        val response = LoginResponse("Successful", NetworkResponseCodes.SUCCESS, "sasdasdc", userInfo)

        return response
    }

    fun createPasswordErrorResult(): LoginResponse{

        val response = LoginResponse("wrong username or password", ERROR_USERNAME_PASSWORD, null, null)

        return response
    }
}