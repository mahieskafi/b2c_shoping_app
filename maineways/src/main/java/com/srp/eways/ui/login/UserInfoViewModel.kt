package com.srp.eways.ui.login

import androidx.lifecycle.MutableLiveData
import com.srp.eways.base.BaseViewModel
import com.srp.eways.di.DIMain
import com.srp.eways.network.CallBackWrapper
import com.srp.eways.model.login.LoginResponse
import com.srp.eways.model.login.UserInfo
import com.srp.eways.repository.validatetoken.ValidateTokenRepository
import com.srp.eways.util.Constants

/**
 * Created by Eskafi on 8/27/2019.
 */
open class UserInfoViewModel : BaseViewModel() {

    companion object {
        const val CREDIT_REFRESH_INTERVAL = 2 * 60 * 1000

        private var instance: UserInfoViewModel? = null
        fun getInstance(): UserInfoViewModel {

            if (instance == null) {
                instance = UserInfoViewModel()
            }
            return instance as UserInfoViewModel
        }
    }

    private val mValidateTokenRepository: ValidateTokenRepository

    private var mCreditLive: MutableLiveData<Long>
    private var mUserInfoLive: MutableLiveData<UserInfo>
    private var mCreditIncreased: MutableLiveData<Boolean>
    private var mValidTokenError: MutableLiveData<String>

    private var mCreditInvalidated: Boolean
    private var mLastCreditUpdateTime: Long

    init {
        mValidateTokenRepository = DIMain.getValidTokenRepo()

        mCreditLive = MutableLiveData()
        mUserInfoLive = MutableLiveData()
        mCreditIncreased = MutableLiveData()
        mValidTokenError = MutableLiveData()

        mCreditInvalidated = true
        mLastCreditUpdateTime = 0L

        getCredit()
    }

    fun getCredit() {
        if (shouldGetCredit()) {

            val userString = DIMain.getPreferenceCache().getString(Constants.USER_INFO)

            mUserInfoLive.value = DIMain.getGson().fromJson(userString, UserInfo::class.java)
            mCreditLive.value = mUserInfoLive.value?.credit

            mValidateTokenRepository.validateToken(object : CallBackWrapper<LoginResponse> {

                override fun onSuccess(responseBody: LoginResponse?) {
                    mCreditInvalidated = false
                    mLastCreditUpdateTime = System.currentTimeMillis()


                    val userInfo = responseBody?.userInfo
                    var sUserInfo: String? = null

                    if (userInfo != null) {
                        sUserInfo = DIMain.getGson().toJson(userInfo, UserInfo::class.java)
                    }
                    DIMain.getPreferenceCache().put(Constants.USER_INFO, sUserInfo)

                    if (responseBody?.token != null && responseBody.token.isNotEmpty()) {
                        DIMain.getPreferenceCache().put(Constants.TOKEN_KEY, responseBody.token)
                    }

                    mCreditLive.value = responseBody?.userInfo?.credit
                    mUserInfoLive.value = responseBody?.userInfo
                }

                override fun onError(errorCode: Int, errorMessage: String) {
                    mCreditLive.value = 0
                    mUserInfoLive.value = null

                   mValidTokenError.value = errorMessage
                }
            })
        }
    }


    fun shouldGetCredit(): Boolean {
        return getCreditInvalidated() || (System.currentTimeMillis() - getLastCreditUpdateTime() > CREDIT_REFRESH_INTERVAL)
    }

    fun invalidateCredit() {
        mCreditInvalidated = true
    }

    fun onCreditIncreased(isChanged: Boolean) {
        mCreditIncreased.value = isChanged
    }

    fun isCreditIncreased(): MutableLiveData<Boolean> {
        return mCreditIncreased
    }

    fun getCreditLiveData(): MutableLiveData<Long> {
        return mCreditLive
    }

    fun getValidTokenErrorLiveData(): MutableLiveData<String> {
        return mValidTokenError
    }

    fun consumeValidTokenErrorLiveData() {
         mValidTokenError.value = null
    }

    fun getUserInfoLiveData(): MutableLiveData<UserInfo> {
        return mUserInfoLive
    }

    fun setUserInfo(userInfo: UserInfo) {
        mUserInfoLive.value = userInfo
    }

    fun consumeUserInfoLiveData() {
        mUserInfoLive.value = null
    }

    fun consumeCreditLiveData() {
        mCreditLive.value = null
    }

    fun getCreditInvalidated(): Boolean {
        return mCreditInvalidated
    }

    fun getLastCreditUpdateTime(): Long {
        return mLastCreditUpdateTime
    }

    fun onCreditIncreasedConsumed() {
        mCreditIncreased = MutableLiveData()
    }
}