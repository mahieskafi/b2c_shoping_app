package com.srp.eways.repository.remote

import android.os.Handler
import com.srp.eways.network.CallBackWrapper
import com.srp.eways.network.NetworkResponseCodes.*
import com.srp.eways.network.NetworkUtil

/**
 * Created by ErfanG on 07/08/2019.
 */
open abstract class BaseApiImplementation {

    private var mMode : Int = SUCCESS
    private var mErrorMessage : String = ""

    private var mHasImplementation : Boolean = false

    private var mResponseDelay = 2000L

    private var mHasAbResources : Boolean = false


    fun setMode(mode : Int){

        mMode = mode

        when (mode) {
            ERROR_NO_INTERNET->{
                mHasImplementation = true
            }
            ERROR_TIMEOUT ->{
                mHasImplementation = true
            }
            ERROR_CONNECTION ->{
                mHasImplementation = true
            }
            ERROR_UNDEFINED ->{
                mHasImplementation = true
            }
            ERROR_AUTHORIZATION_FAILED ->{
                mHasImplementation = true
            }
            ERROR_UNSUPPORTED_API ->{
                mHasImplementation = true
            }
            ERROR_SERVER_PROBLEM ->{
                mHasImplementation = true
            }
            SUCCESS ->{
                mHasImplementation = false
            }
            else ->{
                mHasImplementation = true
            }
        }
        if(mHasAbResources) {
            mErrorMessage = NetworkUtil.getErrorText(mode)
        }
        else{
            mErrorMessage = handleErrorMessage(mode)
        }
    }

    private fun handleErrorMessage(errorCode: Int): String {

        when (errorCode) {
            ERROR_NO_INTERNET -> return "اتصال به اینترنت برقرار نمیباشد."

            ERROR_AUTHORIZATION_FAILED -> return "باید لاگین دوباره انجام شود"

            ERROR_CONNECTION -> return "خطا به هنگام تبادل اطلاعات با سرور"

            ERROR_SERVER_PROBLEM -> return "خطای داخلی سرور"

            ERROR_TIMEOUT -> return "کیفیت اتصال با سرور ضعیف میباشد."

            ERROR_UNSUPPORTED_API -> return "این نسخه از اپلیکیشن به روز نمیباشد."

            ERROR_UNDEFINED -> return "خطای نامشخص رخ داده است."
            else -> return ""
        }

    }

    fun <T> handleCall(callback : CallBackWrapper<T>) : Boolean{
        if(mHasImplementation){
            Handler().postDelayed({callback.onError(mMode, mErrorMessage)}, mResponseDelay)
            setMode(SUCCESS)
            return true
        }
        return false
    }

    fun handleCall() : Boolean{
        if(mHasImplementation){

            return true
        }
        return false
    }

    fun getMode(): Int {
        return mMode
    }

    fun setResponseDelay(delay : Long){
        mResponseDelay = delay
    }
    fun getResponseDelay() : Long{
        return mResponseDelay
    }

    fun setHasAbResources(hasAB : Boolean){
        mHasAbResources = hasAB
    }
}