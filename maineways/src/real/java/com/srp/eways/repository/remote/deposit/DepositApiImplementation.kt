package com.srp.eways.repository.remote.deposit

import com.google.gson.JsonObject
import com.srp.eways.AppConfig
import com.srp.eways.di.DIMain
import com.srp.eways.model.deposit.*
import com.srp.eways.network.CallBackWrapper
import com.srp.eways.repository.remote.BaseApiImplementation
import com.srp.eways.repository.remote.DefaultRetroCallback

/**
 * Created by Eskafi on 8/26/2019.
 */
class DepositApiImplementation : BaseApiImplementation(), DepositApiService {

    private val mDepositRetrofit: DepositApiRetrofit

    companion object {
        private var instance: DepositApiImplementation? = null
        private val AMOUNT_KEY = "Amount"

        fun getInstance(): DepositApiImplementation {

            if (instance == null) {
                instance = DepositApiImplementation()
            }
            return instance as DepositApiImplementation
        }
    }

    init {
        mDepositRetrofit = DIMain.provideApi(DepositApiRetrofit::class.java)
    }

    override fun getBankList(callBack: CallBackWrapper<BankListResponse>) {

        mDepositRetrofit.getBankList(AppConfig.SERVER_VERSION).enqueue(object : DefaultRetroCallback<BankListResponse>(callBack) {

            override fun checkResponseForError(response: BankListResponse?, errorInfo: ErrorInfo?) {
                if (response?.status != 0) {
                    errorInfo?.errorCode = response?.status
                    errorInfo?.errorMessage = response?.description
                }
            }

        })
    }

    override fun increaseDeposit(request: IncreaseDepositRequest?, callBack: CallBackWrapper<IncreaseDepositResponse>) {
        mDepositRetrofit.increaseDeposit(AppConfig.SERVER_VERSION, request).enqueue(object : DefaultRetroCallback<IncreaseDepositResponse>(callBack) {

            override fun checkResponseForError(response: IncreaseDepositResponse?, errorInfo: ErrorInfo?) {
                if (response?.status != 0) {
                    errorInfo?.errorCode = response?.status
                    errorInfo?.errorMessage = response?.description
                }
            }

        })
    }

    override fun getStatus(uId: String, uUId: String, callBack: CallBackWrapper<IncreaseDepositStatusResponse>) {
        mDepositRetrofit.getStatus(AppConfig.SERVER_VERSION, uId, uUId).enqueue(object : DefaultRetroCallback<IncreaseDepositStatusResponse>(callBack) {

            override fun checkResponseForError(response: IncreaseDepositStatusResponse?, errorInfo: ErrorInfo?) {
                if (response?.status != 0) {
                    errorInfo?.errorCode = response?.status
                    errorInfo?.errorMessage = response?.description
                }
            }

        })
    }

    override fun getMplToken(amount: Long, callBack: CallBackWrapper<MPLTokenResponse>) {

        mDepositRetrofit.getMplToken(AppConfig.SERVER_VERSION, getBody(amount)).enqueue(object : DefaultRetroCallback<MPLTokenResponse>(callBack) {

            override fun checkResponseForError(response: MPLTokenResponse?, errorInfo: ErrorInfo?) {
                if (response?.status != 0) {
                    errorInfo?.errorCode = response?.status
                    errorInfo?.errorMessage = response?.description
                }
            }

        })
    }

    override fun confirmPayment(request: ConfirmPaymentRequest?, callBack: CallBackWrapper<ConfirmPaymentResponse>) {
        mDepositRetrofit.confirmPayment(AppConfig.SERVER_VERSION, request).enqueue(object : DefaultRetroCallback<ConfirmPaymentResponse>(callBack) {

            override fun checkResponseForError(response: ConfirmPaymentResponse?, errorInfo: ErrorInfo?) {
                if (response?.status != 0) {
                    errorInfo?.errorCode = response?.status
                    errorInfo?.errorMessage = response?.description
                }
            }

        })
    }

    private fun getBody(amount: Long): JsonObject {
        val jsonObject = JsonObject()
        jsonObject.addProperty(AMOUNT_KEY, amount)
        return jsonObject
    }


}
