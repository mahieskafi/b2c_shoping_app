package com.srp.eways.ui.deposit.increasedeposit

import androidx.lifecycle.MutableLiveData
import com.srp.eways.base.BaseViewModel
import com.srp.eways.di.DIMain
import com.srp.eways.model.deposit.*
import com.srp.eways.network.BaseCallBackWrapper
import com.srp.eways.repository.deposit.DepositRepository

/**
 * Created by Eskafi on 9/2/2019.
 */
open class IncreaseDepositViewModel : BaseViewModel() {

    companion object {
        private var instance: IncreaseDepositViewModel? = null
        fun getInstance(): IncreaseDepositViewModel {

            if (instance == null) {
                instance = IncreaseDepositViewModel()
            }
            return instance as IncreaseDepositViewModel
        }
    }

    val mDepositRepository: DepositRepository

    private var mBankListResponseLive: MutableLiveData<BankListResponse>
    private var mIncreaseDepositResponseLive: MutableLiveData<IncreaseDepositResponse>
    private var mIncreaseDepositStatusResponseLive: MutableLiveData<IncreaseDepositStatusResponse>

    private var mMplTokenResponseLive: MutableLiveData<MPLTokenResponse>
    private var mConfirmPaymentResponseLive: MutableLiveData<ConfirmPaymentResponse>

    private var mAmount: MutableLiveData<Long>
    private var isAmountValid: MutableLiveData<Boolean>
    private var isGoToDepositTransaction: MutableLiveData<Boolean>

    private var mAuthority: String? = null
    private var mRequestId: String? = null
    private var mConfirmPaymentRequest: ConfirmPaymentRequest? = null

    private var mBankId: Int = -1

    private var mIsClearData: MutableLiveData<Boolean> = MutableLiveData()

    private lateinit var mIncreaseDepositRequest: IncreaseDepositRequest

    init {
        mDepositRepository = DIMain.getDepositRepo()

        mAmount = MutableLiveData()
        isAmountValid = MutableLiveData()
        isGoToDepositTransaction = MutableLiveData()

        mBankListResponseLive = MutableLiveData()
        mIncreaseDepositResponseLive = MutableLiveData()
        mIncreaseDepositStatusResponseLive = MutableLiveData()

        mMplTokenResponseLive = MutableLiveData()
        mConfirmPaymentResponseLive = MutableLiveData()

        mIsClearData.value = false
    }

    fun getBankList(isLoading: Boolean) {
        setLoading(isLoading)

        mDepositRepository.getBankList(object : BaseCallBackWrapper<BankListResponse>(this) {
            override fun onError(errorCode: Int, errorMessage: String) {
                setLoading(false)

                val mBankListResponse = BankListResponse(status = errorCode, description = errorMessage)

                mBankListResponseLive.value = mBankListResponse
            }

            override fun onSuccess(responseBody: BankListResponse) {
                setLoading(false)

                mBankListResponseLive.value = responseBody
            }

        })
    }

    fun increaseDeposit() {
        setLoading(true)

        mDepositRepository.increaseDeposit(getIncreaseDepositRequest(), object : BaseCallBackWrapper<IncreaseDepositResponse>(this) {
            override fun onError(errorCode: Int, errorMessage: String) {
                setLoading(false)

                val response = IncreaseDepositResponse(status = errorCode, url = "", description = errorMessage)

                mIncreaseDepositResponseLive.value = response

            }

            override fun onSuccess(responseBody: IncreaseDepositResponse?) {
                setLoading(false)

                mIncreaseDepositResponseLive.value = responseBody
            }

        })

    }

    fun getStatus(uId: String, uuid: String) {
        setLoading(true)

        mDepositRepository.getStatus(uId, uuid, object : BaseCallBackWrapper<IncreaseDepositStatusResponse>(this) {
            override fun onError(errorCode: Int, errorMessage: String) {
                setLoading(false)

                val response = IncreaseDepositStatusResponse(status = errorCode, description = errorMessage)

                mIncreaseDepositStatusResponseLive.value = response

            }

            override fun onSuccess(responseBody: IncreaseDepositStatusResponse?) {
                setLoading(false)

                mIncreaseDepositStatusResponseLive.value = responseBody
            }

        })

    }

    fun getMPLToken() {
        setLoading(true)

        mDepositRepository.getMplToken(mAmount.value, object : BaseCallBackWrapper<MPLTokenResponse>(this) {
            override fun onError(errorCode: Int, errorMessage: String) {
                setLoading(false)
                val mMplTokenResponse = MPLTokenResponse(status = errorCode)
                mMplTokenResponse.description = errorMessage

                mMplTokenResponseLive.value = mMplTokenResponse
            }

            override fun onSuccess(responseBody: MPLTokenResponse) {
                setLoading(false)
                mAuthority = responseBody.authority
                mRequestId = responseBody.requestId
                mMplTokenResponseLive.value = responseBody
            }

        })
    }

    fun confirmPayment() {
        setLoading(true)
        mDepositRepository.confirmPayment(getConfirmPaymentRequest(), object : BaseCallBackWrapper<ConfirmPaymentResponse>(this) {
            override fun onError(errorCode: Int, errorMessage: String) {
                setLoading(false)

                val response = ConfirmPaymentResponse(status = errorCode)
                response.description = errorMessage

                mConfirmPaymentResponseLive.value = response

            }

            override fun onSuccess(responseBody: ConfirmPaymentResponse?) {
                setLoading(false)
                mConfirmPaymentResponseLive.value = responseBody
            }

        })

    }

    fun onAmountChanged(amount: Long) {
        mAmount.value = amount
        isAmountValid.value = isAmountValid()
    }


    private fun isAmountValid(): Boolean {
        if (mAmount.value!! >= 10000 && mAmount?.value!! <= 500000000)
            return true
        return false
    }

    fun getAmountValidate(): MutableLiveData<Boolean> {
        return isAmountValid
    }

    fun getIncreaseDepositLiveData(): MutableLiveData<IncreaseDepositResponse> {
        return mIncreaseDepositResponseLive
    }

    fun getBankListLiveData(): MutableLiveData<BankListResponse> {
        return mBankListResponseLive
    }

    fun consumeBankListLiveData() {
        mBankListResponseLive.value = null
    }

    fun consumeStatusLiveData() {
        mIncreaseDepositStatusResponseLive.value = null
    }

    fun consumeIncreaseDepositResponseLiveData() {
        mIncreaseDepositResponseLive.value = null
    }

    fun consumeAmountLiveData() {
        isAmountValid.value = null
    }

    fun consumeGoToDepositTransaction() {

        isGoToDepositTransaction.value = null
    }

    fun setGoToDepositTransaction(isGotoDeposit: Boolean) {

        isGoToDepositTransaction.value = isGotoDeposit
    }

    fun getGoToDepositTransaction() : MutableLiveData<Boolean>{
        return isGoToDepositTransaction
    }

    fun setBankId(bankId: Int) {
        mBankId = bankId
    }

    fun getIncreaseDepositStatusLiveData(): MutableLiveData<IncreaseDepositStatusResponse> {
        return mIncreaseDepositStatusResponseLive
    }

    fun getIncreaseDepositRequest(): IncreaseDepositRequest {
        return IncreaseDepositRequest(amount = mAmount.value!!, bankId = mBankId, maskCard = "")
    }

    fun getConfirmPaymentLiveData(): MutableLiveData<ConfirmPaymentResponse> {
        return mConfirmPaymentResponseLive
    }

    fun getMPLTokenLiveData(): MutableLiveData<MPLTokenResponse> {
        return mMplTokenResponseLive
    }

    fun setMPLResponse(mMplResponse: MPLResponse, status: Int) {
        mConfirmPaymentRequest = ConfirmPaymentRequest(
                Response = mMplResponse,
                Authority = mAuthority,
                RequestId = mRequestId,
                Status = status)
    }

    fun getAuthority(): String? {
        return mAuthority
    }

    fun getRequestId(): String? {
        return mRequestId
    }

    fun getConfirmPaymentRequest(): ConfirmPaymentRequest? {
        return mConfirmPaymentRequest
    }

    fun clearData(boolean: Boolean) {
        mIsClearData.value = boolean
    }

    fun consumeClearData() {
        mIsClearData.value = null
    }

    fun getIsClearData(): MutableLiveData<Boolean> {
        return mIsClearData
    }
}