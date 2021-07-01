package com.srp.b2b2cEwaysPannel.ui.login

import androidx.lifecycle.MutableLiveData
import com.srp.b2b2cEwaysPannel.AppConfig
import com.srp.b2b2cEwaysPannel.di.DI
import com.srp.b2b2cEwaysPannel.model.login.authenticate.AuthenticateRequest
import com.srp.b2b2cEwaysPannel.model.login.authenticate.AuthenticateResponse
import com.srp.b2b2cEwaysPannel.model.login.registerwithotp.RegisterWithOTPRequest
import com.srp.b2b2cEwaysPannel.model.login.registerwithotp.RegisterWithOTPResponse
import com.srp.b2b2cEwaysPannel.model.login.verifyotp.VerifyOTPRequest
import com.srp.b2b2cEwaysPannel.model.login.verifyotp.VerifyOTPResponse
import com.srp.eways.base.BaseViewModel
import com.srp.eways.model.login.LoginRequest
import com.srp.eways.model.login.LoginResponse
import com.srp.eways.model.login.UserInfo
import com.srp.eways.network.CallBackWrapper
import com.srp.eways.network.NetworkResponseCodes
import com.srp.eways.util.Constants

/**
 * Created by ErfanG on 3/3/2020.
 */
object LoginViewModel : BaseViewModel() {

    private val PASSWORD_ERROR_CODE: Int = -10
    private val VERIFY_ERROR_CODE: Int = -21

    private val mLoginRepository = DI.getLoginRepository()

    private val mError = MutableLiveData<String>()

    private val mAuthenticationResponse = MutableLiveData<AuthenticateResponse>()
    private val mAuthenticationLoading = MutableLiveData<Boolean>()

    private val mVerifyResponse = MutableLiveData<VerifyOTPResponse>()
    private val mVerifyLoading = MutableLiveData<Boolean>()
    private val mVerifyError = MutableLiveData<String>()

    private val mLoginResponse = MutableLiveData<LoginResponse>()
    private val mLoginError = MutableLiveData<String>()
    private val mLoginLoading = MutableLiveData<Boolean>()

    private val mRegisterResponse = MutableLiveData<RegisterWithOTPResponse>()
    private val mRegisterLoading = MutableLiveData<Boolean>()

    private val mNewUser = MutableLiveData<Boolean>()
    private var mPhoneNumber = MutableLiveData<String>()
    private var mToken = ""


    fun authenticate(phoneNumber: String) {

        mPhoneNumber.value = phoneNumber

        mAuthenticationLoading.value = true
        mLoginRepository.authenticate(AuthenticateRequest(mobileNo = phoneNumber), object : CallBackWrapper<AuthenticateResponse> {
            override fun onError(errorCode: Int, errorMessage: String) {

                mAuthenticationLoading.value = false
                mError.value = errorMessage
            }

            override fun onSuccess(responseBody: AuthenticateResponse) {

                mAuthenticationLoading.value = false
                mError.value = null
                mAuthenticationResponse.value = responseBody

                mNewUser.value = responseBody.userIsNew
            }

        })
    }

    fun verify(phoneNumber: String, OTPToken: String, isNewUser: Boolean) {

        mVerifyLoading.value = true
        mLoginRepository.verifyOTP(VerifyOTPRequest(mobileNo = phoneNumber, token = OTPToken, userIsNew = isNewUser), object : CallBackWrapper<VerifyOTPResponse> {
            override fun onError(errorCode: Int, errorMessage: String?) {

                mVerifyLoading.value = false
                if (errorCode == VERIFY_ERROR_CODE) {
                    mVerifyError.value = errorMessage
                } else {
                    mError.value = errorMessage
                }
            }

            override fun onSuccess(responseBody: VerifyOTPResponse) {

                mVerifyLoading.value = false
                mError.value = null
                mVerifyError.value = null
                mVerifyResponse.value = responseBody

                if (responseBody.status == NetworkResponseCodes.SUCCESS) {
                    mToken = OTPToken

                    if (!isNewUser) {
                        DI.getPreferenceCache().put(Constants.TOKEN_KEY, responseBody.token)

                        saveUserInfo(responseBody.userInfo)
                    }
                }
            }

        })
    }

    fun registerWithOTP(OTPToken: String, phoneNumber: String, name: String, family: String, password: String, parentId: Int, gender: Int) {

        mRegisterLoading.value = true
        mLoginRepository.registerWithOTP(RegisterWithOTPRequest(mobileNo = phoneNumber, password = password, parentId = parentId, gender = gender, token = OTPToken, firstName = name, lastName = family), object : CallBackWrapper<RegisterWithOTPResponse> {
            override fun onError(errorCode: Int, errorMessage: String?) {

                mRegisterLoading.value = false
                mError.value = errorMessage
            }

            override fun onSuccess(responseBody: RegisterWithOTPResponse) {

                mRegisterLoading.value = false
                mRegisterResponse.value = responseBody

                if (responseBody.status == NetworkResponseCodes.SUCCESS) {

                    DI.getPreferenceCache().put(Constants.TOKEN_KEY, responseBody.token)

                    saveUserInfo(responseBody.userInfo)
                }
            }

        })

    }

    fun login(password: String) {

        mLoginLoading.value = true
        var loginRequest = LoginRequest(AppConfig.APP_KEY, "null", password, true, AppConfig.TYPE, mPhoneNumber.value,null)
        mLoginRepository.login(loginRequest, object : CallBackWrapper<LoginResponse> {
            override fun onError(errorCode: Int, errorMessage: String?) {

                mLoginLoading.value = false
                if (errorCode == PASSWORD_ERROR_CODE) {
                    mLoginError.value = errorMessage
                } else {
                    mError.value = errorMessage
                }
            }

            override fun onSuccess(responseBody: LoginResponse) {
                mLoginLoading.value = false
                mLoginError.value = null
                mError.value = null
                mLoginResponse.value = responseBody

                if (responseBody.status == NetworkResponseCodes.SUCCESS) {

                    mToken = responseBody.token!!

                    DI.getPreferenceCache().put(Constants.TOKEN_KEY, mToken)

                    saveUserInfo(responseBody.userInfo)
                }
            }
        })

    }

    fun saveUserInfo(userInfo: UserInfo?) {
        var sUserInfo: String? = null

        if (userInfo != null) {
            sUserInfo = DI.getGson().toJson(userInfo, UserInfo::class.java)
        }

        DI.getPreferenceCache().put(Constants.USER_INFO, sUserInfo)
    }

    fun getError() = mError
    fun getAuthenticationResponse() = mAuthenticationResponse
    fun getVerifyResponse() = mVerifyResponse
    fun getLoginResponse() = mLoginResponse
    fun getLoginError() = mLoginError
    fun getVerifyError() = mVerifyError
    fun getRegisterResponse() = mRegisterResponse

    fun consumeAuthenticationResponse() {
        mAuthenticationResponse.value = null
    }

    fun consumeVerifyResponse() {
        mVerifyResponse.value = null
    }

    fun consumeVerifyError() {
        mVerifyError.value = null
    }

    fun consumeLoginResponse() {
        mLoginResponse.value = null
    }

    fun consumeRegisterResponse() {
        mRegisterResponse.value = null
    }

    fun getAuthenticationLoading() = mAuthenticationLoading
    fun getVerifyLoading() = mVerifyLoading
    fun getRegisterLoading() = mRegisterLoading
    fun getLoginLoading() = mLoginLoading

    fun getPhoneNumberLiveData() = mPhoneNumber
    fun getIsNewUserLiveData() = mNewUser

    fun getPhoneNumber() = mPhoneNumber.value
    fun isNewUser() = mNewUser.value
    fun getToken() = mToken

}