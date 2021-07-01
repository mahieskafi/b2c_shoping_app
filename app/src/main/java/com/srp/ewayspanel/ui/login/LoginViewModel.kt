package com.srp.ewayspanel.ui.login

import androidx.lifecycle.MutableLiveData
import com.srp.eways.base.BaseViewModel
import com.srp.eways.model.login.LoginRequest
import com.srp.eways.model.login.LoginResponse
import com.srp.eways.model.login.UserInfo
import com.srp.eways.network.CallBackWrapper
import com.srp.eways.util.Constants
import com.srp.ewayspanel.AppConfig
import com.srp.ewayspanel.AppConfig.APP_KEY
import com.srp.ewayspanel.di.DI
import com.srp.ewayspanel.model.login.*
import com.srp.ewayspanel.model.login.sms.*
import com.srp.ewayspanel.model.login.verifyotp.VerifyOTPResponse
import com.srp.ewayspanel.repository.login.LoginRepository

/**
 * Created by ErfanG on 10/08/2019.
 */
open class LoginViewModel : BaseViewModel() {

    private val TAG = "LoginViewModel"
    private val PASSWORD_ERROR_CODE: Int = -10
    private val VERIFY_ERROR_CODE: Int = -21

    private var mLoginResponse = MutableLiveData<LoginResponse>()
    private var mForgetPasswordResponse = MutableLiveData<ForgetPasswordResponse>()
    private var mVerifyCodeResponse = MutableLiveData<VerifyCodeResponse>()
    private var mResetPasswordResponse = MutableLiveData<ResetPasswordResponse>()

    private val mAuthenticationLoading = MutableLiveData<Boolean>()
    private val mVerifyError = MutableLiveData<String>()
    private val mVerifyResponse = MutableLiveData<VerifyOTPResponse>()

    private val mVerifyMobileNumberResponseLiveData = MutableLiveData<OutSmsResponse>()
    private val mVerifyOtpNumberResponseLiveData = MutableLiveData<OutSmsVerifyResponse>()

    private val mSendMobileConfirmResponseLiveData = MutableLiveData<LoggedSmsRequestResponse>()
    private val mConfirmMobileNumberResponseLiveData = MutableLiveData<LoggedSmsConfirmResponse>()

    private val mErrorVerification = MutableLiveData<String>()
    private val mSendMobileNumberError = MutableLiveData<String>()

    private var mError: MutableLiveData<Pair<Int, String>> = MutableLiveData<Pair<Int, String>>()

    private var mUsername: MutableLiveData<String> = MutableLiveData<String>()

    private var mPassword: MutableLiveData<String> = MutableLiveData<String>()


    private var mConfirmUsername = MutableLiveData<String>()
    private var mVerificationCode = MutableLiveData<String>()
    private var mResetPasswordFirst = MutableLiveData<String>()
    private var mResetPasswordSecond = MutableLiveData<String>()

    private var mRememberMe: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

    private var mCanLogin = MutableLiveData<Boolean>()
    private var mCanConfirmUsername = MutableLiveData<Boolean>()
    private var mCanVerifyCode = MutableLiveData<Boolean>()
    private var mCanResetPassword = MutableLiveData<Boolean>()

    private var verifyOtpError = MutableLiveData<Int>()
    private var confirmMobileError = MutableLiveData<Int>()

    val mLoginRepo: LoginRepository

    companion object {
        private var instance: LoginViewModel? = null
        fun getInstance(): LoginViewModel {

            if (instance == null) {
                instance = LoginViewModel()
            }
            return instance as LoginViewModel
        }
    }

    init {
        mLoginRepo = DI.getLoginRepo()
    }

    fun login() {
        setLoading(true)
        mLoginRepo.login(LoginRequest(APP_KEY, "null", mPassword.value, mRememberMe.value, 8, mUsername.value,
                DI.getPreferenceCache().getString(Constants.TRACE_CODE)),

                object : CallBackWrapper<LoginResponse> {
                    override fun onError(errorCode: Int, errorMessage: String) {
                        setLoading(false)
                        mLoginResponse.value = null

                        mError.value = Pair(errorCode, errorMessage)

                    }

                    override fun onSuccess(responseBody: LoginResponse) {
                        setLoading(false)

                        DI.getPreferenceCache().put(Constants.TOKEN_KEY, responseBody.token)

                        val userInfo = responseBody.userInfo

                        var sUserInfo: String? = null

                        if (userInfo != null) {
                            sUserInfo = DI.getGson().toJson(userInfo, UserInfo::class.java)
                        }

                        DI.getPreferenceCache().put(Constants.USER_INFO, sUserInfo)

                        mLoginResponse.value = responseBody

                    }
                })
    }

    fun onUsernameChanged(username: String) {
        mUsername.value = username

        mCanLogin.value = isUsernameAndPasswordValid()
    }

    private fun isUsernameAndPasswordValid(): Boolean {
        if (!mUsername.value.isNullOrBlank() && !mPassword.value.isNullOrBlank())
            return true
        return false
    }

    fun onPasswordChanged(password: String) {
        mPassword.value = password

        mCanLogin.value = isUsernameAndPasswordValid()
    }

    fun onRememberMeChanged(isChecked: Boolean) {
        mRememberMe.value = isChecked
    }

    fun onLoginResultConsumed() {
        mLoginResponse.value = null
        mError.value = null
    }

    fun getLoginResponse(): MutableLiveData<LoginResponse> {
        return mLoginResponse
    }

    fun getForgetPasswordResponse() = mForgetPasswordResponse
    fun getVerifyCodeResponse() = mVerifyCodeResponse
    fun getResetPasswordResponse() = mResetPasswordResponse

    fun getError(): MutableLiveData<Pair<Int, String>> {
        return mError
    }

    fun getUsername() = mUsername

    fun getPassword() = mPassword

    fun canLogin() = mCanLogin
    fun consumeCanLogin(){
        mCanLogin.value = false
    }

    fun canConfirmUsername() = mCanConfirmUsername
    fun canVerifyCode() = mCanVerifyCode
    fun canResetPassword() = mCanResetPassword


    fun sendVerificationCode() {
        setLoading(true)
        mLoginRepo.sendVerificationCode(ForgetPasswordRequest(mConfirmUsername.value!!, com.srp.eways.AppConfig.APP_KEY),
                object : CallBackWrapper<ForgetPasswordResponse> {
                    override fun onError(errorCode: Int, errorMessage: String) {
                        setLoading(false)

                        mForgetPasswordResponse.value = null

                        mError.value = Pair(errorCode, errorMessage)

                    }

                    override fun onSuccess(responseBody: ForgetPasswordResponse) {
                        setLoading(false)

                        mForgetPasswordResponse.value = responseBody

                    }
                })
    }

    fun verifyCode() {
        setLoading(true)
        mLoginRepo.verifyCode(VerifyCodeRequest(code = mVerificationCode.value!!,
                guid = mForgetPasswordResponse.value?.guid!!,
                userId = mForgetPasswordResponse.value?.userId!!),
                object : CallBackWrapper<VerifyCodeResponse> {
                    override fun onError(errorCode: Int, errorMessage: String) {
                        setLoading(false)

                        mVerifyCodeResponse.value = null

                        mError.value = Pair(errorCode, errorMessage)

                    }

                    override fun onSuccess(responseBody: VerifyCodeResponse) {
                        setLoading(false)

                        mVerifyCodeResponse.value = responseBody

                    }
                })
    }

    fun resetPassword() {
        setLoading(true)
        mLoginRepo.resetPassword(ResetPasswordRequest(password = mResetPasswordFirst.value!!,
                confirmPassword = mResetPasswordSecond.value!!,
                guid = mVerifyCodeResponse.value?.guid!!,
                userId = mVerifyCodeResponse.value?.userId!!),
                object : CallBackWrapper<ResetPasswordResponse> {
                    override fun onError(errorCode: Int, errorMessage: String) {
                        setLoading(false)

                        mResetPasswordResponse.value = null

                        mError.value = Pair(errorCode, errorMessage)

                    }

                    override fun onSuccess(responseBody: ResetPasswordResponse) {
                        setLoading(false)

                        mResetPasswordResponse.value = responseBody

                    }
                })
    }

    fun sendMobileConfirmRequest(phoneNumber: String) {

        mAuthenticationLoading.value = true
        mLoginRepo.sendMobileConfirmRequest(phoneNumber, object : CallBackWrapper<LoggedSmsRequestResponse> {
            override fun onError(errorCode: Int, errorMessage: String) {

                mAuthenticationLoading.value = false
                mSendMobileNumberError.value = errorMessage
            }


            override fun onSuccess(responseBody: LoggedSmsRequestResponse?) {
                mAuthenticationLoading.value = false
                mSendMobileNumberError.value = null
                mSendMobileConfirmResponseLiveData.value = responseBody
            }

        })
    }

    fun confirmMobileNumber(mobileNumber: String, token: String) {
        mLoginRepo.confirmMobileNumber(mobileNumber, token, object : CallBackWrapper<LoggedSmsConfirmResponse> {
            override fun onError(errorCode: Int, errorMessage: String?) {
                confirmMobileError.value = errorCode
            }

            override fun onSuccess(responseBody: LoggedSmsConfirmResponse?) {
                mConfirmMobileNumberResponseLiveData.value = responseBody
            }
        })

    }


    fun verifyMobileNumber(phoneNumber: String, imei: String) {

        mAuthenticationLoading.value = true
        var request = OutSmsRequest(AppConfig.APP_KEY, phoneNumber, imei)

        mLoginRepo.verifyMobileNumber(request, object : CallBackWrapper<OutSmsResponse> {
            override fun onError(errorCode: Int, errorMessage: String) {

                mAuthenticationLoading.value = false
                mErrorVerification.value = errorMessage
            }


            override fun onSuccess(responseBody: OutSmsResponse?) {
                mAuthenticationLoading.value = false
                mErrorVerification.value = null
                mVerifyMobileNumberResponseLiveData.value = responseBody
            }

        })
    }

    fun verifyOtpNumber(request: OutSmsVerifiRequest) {

        mLoginRepo.verifyOtpNumber(request, object : CallBackWrapper<OutSmsVerifyResponse> {
            override fun onError(errorCode: Int, errorMessage: String?) {
                verifyOtpError.value = errorCode
            }

            override fun onSuccess(responseBody: OutSmsVerifyResponse?) {
                mVerifyOtpNumberResponseLiveData.value = responseBody
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


    fun confirmUsernameChanged(confirmUsername: String) {
        mConfirmUsername.value = confirmUsername

        mCanConfirmUsername.value = !mConfirmUsername.value.isNullOrBlank()
    }

    fun verificationCodeChanged(verificationCode: String) {

        mVerificationCode.value = verificationCode

        mCanVerifyCode.value = (!mVerificationCode.value.isNullOrBlank() && (mVerificationCode.value!!.length >= 3 || mVerificationCode.value!!.length <= 5))
    }

    fun resetPasswordFirstInputChanged(password: String) {
        mResetPasswordFirst.value = password

        if (mResetPasswordFirst.value != null && mResetPasswordSecond.value != null) {
            if (mResetPasswordFirst.value == mResetPasswordSecond.value) {
                mCanResetPassword.value = true
                return
            }
        }
        mCanResetPassword.value = false
    }

    fun resetPasswordSecondInputChanged(confirmPassword: String) {
        mResetPasswordSecond.value = confirmPassword

        if (mResetPasswordFirst.value != null && mResetPasswordSecond.value != null) {
            if (mResetPasswordFirst.value == mResetPasswordSecond.value) {
                mCanResetPassword.value = true
                return
            }
        }
        mCanResetPassword.value = false
    }

    fun getVerifyMobileNumberResponseLiveData() = mVerifyMobileNumberResponseLiveData
    fun getVerifyOtpNumberResponseLiveData() = mVerifyOtpNumberResponseLiveData

    fun consumeVerifyResponse() {
        mVerifyResponse.value = null
    }

    fun consumeVerifyError() {
        mVerifyError.value = null
    }

    fun consumeLoginResponse() {
        mLoginResponse.value = null
    }

    fun getErrorVerification() = mErrorVerification
    fun getSendMobileNumberError() = mSendMobileNumberError
    fun getAuthenticationLoading() = mAuthenticationLoading

    fun getVerifyOtpError() = verifyOtpError
    fun getConfirmMobile() = confirmMobileError

    fun vrifyOtpErrorHide(){
        verifyOtpError.value=0
    }

    fun ConfirmMobileHide(){
        confirmMobileError.value=0
    }

    fun getSendMobileConfirmResponseLiveData() = mSendMobileConfirmResponseLiveData
    fun getConfirmMobileNumberResponseLiveData() = mConfirmMobileNumberResponseLiveData

    fun consumeVerifyMobileNumberResponse() {
        mVerifyMobileNumberResponseLiveData.value = null
    }

    fun consumeVerifyOtpNumberResponse() {
        mVerifyOtpNumberResponseLiveData.value = null
    }

    fun consumeSendMobileConfirmResponse() {
        mSendMobileConfirmResponseLiveData.value = null
    }

    fun consumeConfirmMobileNumberResponse() {
        mConfirmMobileNumberResponseLiveData.value = null
    }
}