package com.srp.ewayspanel.repository.remote.login

import android.os.Handler
import com.srp.eways.network.CallBackWrapper
import com.srp.eways.network.NetworkResponseCodes.SUCCESS
import com.srp.ewayspanel.model.login.*
import com.srp.eways.repository.remote.login.MainLoginApiImplementation

/**
 * Created by ErfanG on 07/08/2019.
 */
class LoginApiImplementation : MainLoginApiImplementation(), LoginApiService {

    companion object{
        val instance = LoginApiImplementation()
        const val ERROR_WRONG_CODE = -5
        const val VERIFY_CODE = "1234"
    }

    override fun sendVerificationCode(requestBody: ForgetPasswordRequest, callback: CallBackWrapper<ForgetPasswordResponse>) {
        if(!handleCall(callback)){
            handleSendVerificationCallInternal(requestBody, callback)
        }
    }

    override fun verifyCode(requestBody: VerifyCodeRequest, callback: CallBackWrapper<VerifyCodeResponse>) {
        if(!handleCall(callback)){
            handleVerifyCodeCallInternal(requestBody, callback)
        }
    }

    override fun resetPassword(requestBody: ResetPasswordRequest, callback: CallBackWrapper<ResetPasswordResponse>) {


        if(!handleCall(callback)){
            handleResetPasswordCallInternal(requestBody, callback)
        }
    }

    private fun handleResetPasswordCallInternal(requestBody: ResetPasswordRequest, callback: CallBackWrapper<ResetPasswordResponse>) {

        var response  = ResetPasswordResponse(description = "رمز عبور با موفقیت تغییر کرد.")

        Handler().postDelayed({
            callback.onSuccess(response)
        }, getResponseDelay())
    }

    private fun handleSendVerificationCallInternal(body: ForgetPasswordRequest, callback: CallBackWrapper<ForgetPasswordResponse>) {

        if(body.userName == USERNAME){
            setMode(SUCCESS)
        }
        else{
            setMode(ERROR_WRONG_USERNAME)
        }

        if(getMode() == ERROR_WRONG_USERNAME){
            var response = ForgetPasswordResponse(status = ERROR_WRONG_USERNAME, description = "نام کاربری استباه است.")

            Handler().postDelayed({
                callback.onError(response.status, response.description)
            }, getResponseDelay())
        }
        else{

            var response : ForgetPasswordResponse = ForgetPasswordResponse(status = SUCCESS, description = "کد تایید به شماره شما ارسال شد.")
            Handler().postDelayed({
                callback.onSuccess(response)
            }, getResponseDelay())
        }
    }

    private fun handleVerifyCodeCallInternal(requestBody: VerifyCodeRequest, callback: CallBackWrapper<VerifyCodeResponse>) {

        if(requestBody.code == VERIFY_CODE){
            setMode(SUCCESS)
        }
        else{
            setMode(ERROR_WRONG_CODE)
        }

        if(getMode() == ERROR_WRONG_CODE){
            var response = VerifyCodeResponse(status = ERROR_WRONG_CODE, description = "کد تایید اشتباه است.")

            Handler().postDelayed({
                callback.onError(response.status, response.description)
            }, getResponseDelay())
        }
        else{

            var response = VerifyCodeResponse()
            Handler().postDelayed({
                callback.onSuccess(response)
            }, getResponseDelay())
        }
    }
}