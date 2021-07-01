package com.srp.ewayspanel.ui.login

import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.util.TypedValue
import android.view.View
import android.view.View.*
import android.view.WindowManager
import android.widget.*
import androidx.lifecycle.Observer
import com.srp.eways.base.BaseActivity
import com.srp.eways.ui.view.button.ButtonElement
import com.srp.eways.ui.view.input.InputElement
import com.srp.eways.util.Constants
import com.srp.eways.util.Utils
import com.srp.eways.util.analytic.AnalyticConstant
import com.srp.ewayspanel.R
import com.srp.ewayspanel.di.DI
import com.srp.ewayspanel.ui.login.phonenumber.PhoneNumberActivity
import com.srp.ewayspanel.ui.main.MainActivity
import ir.abmyapp.androidsdk.ABResources
import ir.abmyapp.androidsdk.IABResources
import java.util.*


open class LoginActivity : BaseActivity<LoginViewModel>() {

    private val STATE_LOGIN = 0
    private val STATE_ENTER_USERNAME = 1
    private val STATE_ENTER_CODE = 2
    private val STATE_RESET_PASSWORD = 3

    var mViewModel: LoginViewModel? = null

    //region Login
    lateinit var AB: IABResources

    lateinit var mHeader: View
    lateinit var mLogo: ImageView

    lateinit var mLoginContainer: RelativeLayout
    lateinit var mUsernameField: InputElement
    lateinit var mPasswordField: InputElement
    lateinit var mLogin: ButtonElement
    lateinit var mSignUpTitle: TextView
    lateinit var mSignUp: ButtonElement
    lateinit var mResetPassword: TextView
    lateinit var mRemembermeLable: TextView
    lateinit var mRememberMe: CheckBox
    lateinit var mRememberMeCheck: ImageView


    lateinit var mForgetPasswordContaintr: FrameLayout
    lateinit var mConfirmUsernameContainer: LinearLayout
    lateinit var mConfirmUsernameInput: InputElement
    lateinit var mConfirmUsernameButton: ButtonElement

    lateinit var mVerificationCodeContainer: LinearLayout
    lateinit var mVerificationCodeMessage: TextView
    lateinit var mVerificationCodeInput: InputElement
    lateinit var mVerificationCodeButton: ButtonElement
    lateinit var mResendVerificationCodeTimer: TextView
    lateinit var mResendVerificationCodeText: TextView

    lateinit var mResetPasswordContainer: LinearLayout
    lateinit var mResetPasswordInput: InputElement
    lateinit var mResetPasswordAgainInput: InputElement
    lateinit var mResetPasswordButton: ButtonElement

    private var mResendVerifyCodeTimer = Timer()

    private var mCurrentState = STATE_LOGIN
    var eyeCheckedClicked: Boolean = false

    private var notFirstOpen = false

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, LoginActivity::class.java)
        }
    }


    override fun getLayoutId(): Int {
        return R.layout.activity_login
    }

    override fun getViewModel(): LoginViewModel {
        return mViewModel!!
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        ABResources.get(this).fetchAndActivate()

        mViewModel = DI.getViewModel(LoginViewModel::class.java)

        AB = DI.getABResources()

        val token = DI.getPreferenceCache().getString(Constants.TOKEN_KEY)
        if (!token.isNullOrBlank()) {
            startActivity(MainActivity.newIntent(this))
            finish()
        }
//        else {
//            notFirstOpen = DIMain.getPreferenceCache().getBoolean(Constants.FIRST_OPEN)
//            if (notFirstOpen) {
//                if (intent == null || (intent != null && !intent.getBooleanExtra("ISCONTINUEWLOGIN", false))) {
//                    startActivity(PhoneNumberActivity.newIntent(this))
//                    finish()
//
//                }
//            } else {
//                startActivity(PhoneNumberActivity.newIntent(this))
//                finish()
//            }
//        }

        super.onCreate(savedInstanceState)

        val w = window
        w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        val sharedPreferences = DI.getPreferenceCache()

        DI.getEventSender().sendScreen(this, AnalyticConstant.LOGIN_PAGE_NAME)

        mHeader = findViewById(com.srp.ewayspanel.R.id.login_header)
        mLogo = findViewById(com.srp.ewayspanel.R.id.logo)

        mLoginContainer = findViewById(com.srp.ewayspanel.R.id.login_container)
        mUsernameField = findViewById(com.srp.ewayspanel.R.id.username_field)
        mPasswordField = findViewById(com.srp.ewayspanel.R.id.password_field)
        mLogin = findViewById(com.srp.ewayspanel.R.id.login_button)
        mSignUpTitle = findViewById(com.srp.ewayspanel.R.id.do_not_have_account)
        mSignUp = findViewById(com.srp.ewayspanel.R.id.sign_up_account)
        mResetPassword = findViewById(com.srp.ewayspanel.R.id.login_reset_password)
        mRememberMe = findViewById(com.srp.ewayspanel.R.id.remember_me)
        mRememberMeCheck = findViewById(R.id.remember_me_check)
        mRemembermeLable = findViewById(com.srp.ewayspanel.R.id.login_remember_me_text)

        mForgetPasswordContaintr = findViewById(com.srp.ewayspanel.R.id.forget_password_container)

        mConfirmUsernameContainer = findViewById(com.srp.ewayspanel.R.id.confirm_username_container)
        mConfirmUsernameInput = findViewById(com.srp.ewayspanel.R.id.login_confirm_username_input)
        mConfirmUsernameButton = findViewById(com.srp.ewayspanel.R.id.login_confirm_username_button)

        mVerificationCodeContainer = findViewById(com.srp.ewayspanel.R.id.enter_verification_code_container)
        mVerificationCodeMessage = findViewById(com.srp.ewayspanel.R.id.verification_code_message)
        mVerificationCodeInput = findViewById(com.srp.ewayspanel.R.id.login_verification_code_input)
        mVerificationCodeButton = findViewById(com.srp.ewayspanel.R.id.login_verification_code_button)
        mResendVerificationCodeTimer = findViewById(com.srp.ewayspanel.R.id.timer)
        mResendVerificationCodeText = findViewById(com.srp.ewayspanel.R.id.resend_verification_code)

        mResetPasswordContainer = findViewById(com.srp.ewayspanel.R.id.reset_password_container)
        mResetPasswordInput = findViewById(com.srp.ewayspanel.R.id.login_reset_password_input)
        mResetPasswordAgainInput = findViewById(com.srp.ewayspanel.R.id.login_reset_password_again_input)
        mResetPasswordButton = findViewById(com.srp.ewayspanel.R.id.login_reset_password_button)

        getAttrWithAB()

//        mSignUp.movementMethod = LinkMovementMethod.getInstance()
        mSignUp.setOnClickListener { view ->
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(Constants.SIGN_UP_LINK)))
        }

        mResetPassword.movementMethod = LinkMovementMethod.getInstance()
        mResetPassword.setOnClickListener { view ->
            setState(STATE_ENTER_USERNAME)
        }

        mUsernameField.addTextChangeListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                mViewModel?.onUsernameChanged(s.toString())

                if (s.toString().isEmpty()) {
                    mUsernameField.getIconDrawable().setColorFilter(AB.getColor(R.color.login_remember_me_lable_unchecked), PorterDuff.Mode.SRC_IN)
                } else {
                    mUsernameField.getIconDrawable().setColorFilter(AB.getColor(R.color.login_remember_me_lable_checked), PorterDuff.Mode.SRC_IN)
                }
            }

        })

        mPasswordField.addTextChangeListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                mViewModel?.onPasswordChanged(s.toString())

                if (s.toString().isEmpty()) {
                    mPasswordField.setCancelIcon(AB.getDrawable(R.drawable.disactive_eye_hide_icon))
                    mPasswordField.switchInputType(false)
                    mPasswordField.getIconDrawable().setColorFilter(AB.getColor(R.color.login_remember_me_lable_unchecked), PorterDuff.Mode.SRC_IN)
                } else if (!s.toString().isEmpty() and !eyeCheckedClicked) {
                    mPasswordField.setCancelIcon(AB.getDrawable(R.drawable.active_eye_icon))
                    mPasswordField.getIconDrawable().setColorFilter(AB.getColor(R.color.login_remember_me_lable_checked), PorterDuff.Mode.SRC_IN)
                }
            }

        })

        mPasswordField.getCancelImageView().setOnClickListener {

            if (eyeCheckedClicked) {
                mPasswordField.switchInputType(false)
                eyeCheckedClicked = false
                mPasswordField.setCancelIcon(AB.getDrawable(R.drawable.active_eye_icon))
            } else {
                mPasswordField.switchInputType(true)
                eyeCheckedClicked = true
                mPasswordField.setCancelIcon(AB.getDrawable(R.drawable.active_eye_hide_icon))

            }
        }

        mRememberMe.isChecked = sharedPreferences.getBoolean(Constants.LOGIN_PEREFERENCE_REMEMBERCHECK_KEY) &&
                sharedPreferences.getString(Constants.LOGIN_PEREFERENCE_USERNAME_KEY) != null

        if (mRememberMe.isChecked) {
            mRememberMeCheck.visibility = VISIBLE
            mRemembermeLable.setTextColor(AB.getColor(R.color.login_remember_me_lable_checked))
            mUsernameField.setInputText(sharedPreferences.getString(Constants.LOGIN_PEREFERENCE_USERNAME_KEY))
            mPasswordField.setInputText(sharedPreferences.getString(Constants.LOGIN_PEREFERENCE_PASSWORD_KEY))
        } else {
            mRememberMeCheck.visibility = INVISIBLE
            mRemembermeLable.setTextColor(AB.getColor(R.color.login_remember_me_lable_unchecked))
        }


        mRememberMe.setOnCheckedChangeListener { buttonView, isChecked ->
            mViewModel?.onRememberMeChanged(isChecked)

            mRemembermeLable.setTextColor(AB.getColor(R.color.login_remember_me_lable_checked))
            sharedPreferences.put((Constants.LOGIN_PEREFERENCE_REMEMBERCHECK_KEY), true)

            if (!isChecked) {
                mRememberMeCheck.visibility = INVISIBLE
                mRemembermeLable.setTextColor(AB.getColor(R.color.login_remember_me_lable_unchecked))
                sharedPreferences.put((Constants.LOGIN_PEREFERENCE_REMEMBERCHECK_KEY), false)
            } else {
                mRememberMeCheck.visibility = VISIBLE
            }
        }



        mViewModel?.canLogin()?.observe(this, androidx.lifecycle.Observer { newValue ->
            mLogin.setEnable(newValue)
        })

        mViewModel?.getError()?.observe(this, androidx.lifecycle.Observer { newValue ->

            if (newValue != null) {
                val errorCode = newValue.first
                val errorDescription = newValue.second

                Toast.makeText(this, errorDescription, Toast.LENGTH_LONG).show()
                mViewModel?.onLoginResultConsumed()
            }
        })

        mViewModel?.getLoginResponse()?.observe(this, androidx.lifecycle.Observer { newValue ->
            if (newValue != null) {
                var userInfo = newValue.userInfo

                if (userInfo != null) {

                    DI.getEventSender().sendAction(AnalyticConstant.LOGIN_CATEGORY, AnalyticConstant.LOGIN_ACTION, AnalyticConstant.USER_NAME_VALUE, userInfo.userName)

                    sharedPreferences.put(Constants.LOGIN_PEREFERENCE_USERNAME_KEY, mUsernameField.getInputText())
                    sharedPreferences.put(Constants.LOGIN_PEREFERENCE_PASSWORD_KEY, mPasswordField.getInputText())

                    if (newValue.mobileIsConfirmed == false) {
                        val intent = PhoneNumberActivity.newIntent(this)
                        intent.putExtra(Constants.NEEDED_TO_CONFIRM_NUMBER, userInfo.mobile)
                        startActivity(intent)
                        finish()
                    } else {
                        mViewModel?.consumeCanLogin()

                        startActivity(MainActivity.newIntent(this))
                        finish()
                    }

                } else {
                    Toast.makeText(this, newValue.description, Toast.LENGTH_LONG).show()
                }
                mViewModel?.onLoginResultConsumed()
            }
        })

        mViewModel?.isLoading()?.observe(this, Observer { newValue ->
            when (mCurrentState) {
                STATE_LOGIN -> {
                    if (newValue) {
                        mLogin.setLoadingVisibility(VISIBLE)
                        mLogin.setIconVisibility(INVISIBLE)

                        mLogin.isClickable = false
                        mUsernameField.isEnabled = false
                        mPasswordField.isEnabled = false
                    } else {
                        mLogin.setLoadingVisibility(INVISIBLE)
                        mLogin.setIconVisibility(VISIBLE)

                        mLogin.isClickable = true
                        mUsernameField.isEnabled = true
                        mPasswordField.isEnabled = true
                    }
                }
                STATE_ENTER_USERNAME -> {
                    if (newValue) {
                        mConfirmUsernameButton.setLoadingVisibility(VISIBLE)
                        mConfirmUsernameButton.setIconVisibility(INVISIBLE)

                        mConfirmUsernameButton.isClickable = false
                        mConfirmUsernameInput.isEnabled = false
                    } else {
                        mConfirmUsernameButton.setLoadingVisibility(INVISIBLE)
                        mConfirmUsernameButton.setIconVisibility(VISIBLE)

                        mConfirmUsernameButton.isClickable = true
                        mConfirmUsernameInput.isEnabled = true
                    }
                }
                STATE_ENTER_CODE -> {
                    if (newValue) {
                        mVerificationCodeButton.setLoadingVisibility(VISIBLE)
                        mVerificationCodeButton.setIconVisibility(INVISIBLE)

                        mVerificationCodeButton.isClickable = false
                        mVerificationCodeInput.isEnabled = false
                    } else {
                        mVerificationCodeButton.setLoadingVisibility(INVISIBLE)
                        mVerificationCodeButton.setIconVisibility(VISIBLE)

                        mVerificationCodeButton.isClickable = true
                        mVerificationCodeInput.isEnabled = true
                    }
                }
                STATE_RESET_PASSWORD -> {
                    if (newValue) {
                        mResetPasswordButton.setLoadingVisibility(VISIBLE)
                        mResetPasswordButton.setIconVisibility(INVISIBLE)

                        mResetPasswordButton.isClickable = false
                        mResetPasswordInput.isEnabled = false
                        mResetPasswordAgainInput.isEnabled = false
                    } else {
                        mResetPasswordButton.setLoadingVisibility(INVISIBLE)
                        mResetPasswordButton.setIconVisibility(VISIBLE)

                        mResetPasswordButton.isClickable = true
                        mResetPasswordInput.isEnabled = true
                        mResetPasswordAgainInput.isEnabled = true
                    }
                }

            }
        })

        mLogin.setOnClickListener {
            mLogin.setIconVisibility(INVISIBLE)
            mLogin.setLoadingVisibility(VISIBLE)
            mViewModel?.login()
        }


        mViewModel?.canConfirmUsername()?.observe(this, Observer { newValue ->
            mConfirmUsernameButton.setEnable(newValue)
        })
        mConfirmUsernameButton.setOnClickListener {
            mConfirmUsernameButton.setIconVisibility(INVISIBLE)
            mConfirmUsernameButton.setLoadingVisibility(VISIBLE)
            DI.getEventSender().sendAction(AnalyticConstant.LOGIN_CATEGORY, AnalyticConstant.FORGET_PASSWORD_ACTION)
            mViewModel?.sendVerificationCode()
        }
        mConfirmUsernameInput.addTextChangeListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                mViewModel?.confirmUsernameChanged(s.toString())
            }

        })
        mViewModel?.getForgetPasswordResponse()?.observe(this, Observer { newValue ->
            if (newValue != null) {
                if (mCurrentState == STATE_ENTER_USERNAME) {
                    mVerificationCodeMessage.text = Utils.toPersianNumber(newValue.description)
                    setState(STATE_ENTER_CODE)
                }
            }
        })


        mViewModel?.canVerifyCode()?.observe(this, Observer { newValue ->
            mVerificationCodeButton.setEnable(newValue)
        })
        mVerificationCodeButton.setOnClickListener {
            mVerificationCodeButton.setIconVisibility(INVISIBLE)
            mVerificationCodeButton.setLoadingVisibility(VISIBLE)
            DI.getEventSender().sendAction(AnalyticConstant.LOGIN_CATEGORY, AnalyticConstant.VERIFY_CODE_ACTION)
            mViewModel?.verifyCode()
        }
        mVerificationCodeInput.addTextChangeListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                mViewModel?.verificationCodeChanged(s.toString())
            }

        })
        mViewModel?.getVerifyCodeResponse()?.observe(this, Observer { newValue ->
            if (newValue != null) {
                if (mCurrentState == STATE_ENTER_CODE) {

                    setState(STATE_RESET_PASSWORD)
                }
            }
        })
        mResendVerificationCodeText.setOnClickListener {
            mViewModel?.sendVerificationCode()
            setVerificationCodeTimer(true)
            changeResendTextSettings(false)
        }


        mViewModel?.canResetPassword()?.observe(this, Observer { newValue ->
            mResetPasswordButton.setEnable(newValue)
        })
        mResetPasswordButton.setOnClickListener {
            mResetPasswordButton.setIconVisibility(INVISIBLE)
            mResetPasswordButton.setLoadingVisibility(VISIBLE)
            DI.getEventSender().sendAction(AnalyticConstant.LOGIN_CATEGORY, AnalyticConstant.RESET_PASSWORD_ACTION)
            mViewModel?.resetPassword()
        }
        mResetPasswordInput.addTextChangeListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                mViewModel?.resetPasswordFirstInputChanged(s.toString())
            }

        })
        mResetPasswordAgainInput.addTextChangeListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s?.toString() == mResetPasswordInput.getText()) {
                    mResetPasswordAgainInput.setError(null)
                } else {
                    mResetPasswordAgainInput.setError(resources.getString(R.string.reset_password_confirm_not_match))
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                mViewModel?.resetPasswordSecondInputChanged(s.toString())
            }

        })
        mViewModel?.getResetPasswordResponse()?.observe(this, Observer { newValue ->
            if (newValue != null) {
                if (mCurrentState == STATE_RESET_PASSWORD) {
                    Toast.makeText(applicationContext, newValue.description, Toast.LENGTH_LONG).show()
                    setState(STATE_LOGIN)
                }
            }
        })
    }

    private fun setVerificationCodeTimer(onState: Boolean) {

        if (onState) {
            var timerCount = Constants.RESEND_VERIFICATION_CODE_TIMEOUT

            mResendVerifyCodeTimer = Timer()

            mResendVerifyCodeTimer.scheduleAtFixedRate(object : TimerTask() {
                override fun run() {

                    runOnUiThread {
                        timerCount--

                        mResendVerificationCodeTimer.text = Utils.toPersianNumber("00:" + String.format("%02d", timerCount))

                        if (timerCount == 0) {
                            mResendVerifyCodeTimer.cancel()
                            changeResendTextSettings(true)
                        }
                    }


                }

            }, 1000, 1000)
        } else {
            mResendVerifyCodeTimer.cancel()
        }
    }

    private fun getAttrWithAB() {


        //Header
        mHeader.background = AB.getDrawable(com.srp.ewayspanel.R.drawable.login_header)

        //Logo
        mLogo.setImageDrawable(AB.getDrawable(com.srp.ewayspanel.R.drawable.login_header_logo))

        //Username
        mUsernameField.setIconDrawable(AB.getDrawable(com.srp.ewayspanel.R.drawable.login_username_icon))
        mUsernameField.setTextSize(AB.getDimenPixelSize(com.srp.ewayspanel.R.dimen.login_input_text_size).toFloat())
        mUsernameField.setTextColor(AB.getColor(R.color.login_username_text_color))
        mUsernameField.setHintColor(AB.getColor(R.color.login_username_hint_color))
        mUsernameField.setHint(AB.getString(R.string.login_username_hint))
        mUsernameField.setUnSelectedBackground(AB.getDrawable(R.drawable.phone_and_operator_input_background_unselected))
        mUsernameField.setSelectedBackground(AB.getDrawable(R.drawable.phone_and_operator_input_background_unselected))
        mUsernameField.hasIcon(AB.getBoolean(R.bool.login_username_has_clear))
        mUsernameField.background = mUsernameField.getUnselectedBackground()
//        ViewCompat.setElevation(mUsernameField, AB.getDimen(R.dimen.login_input_elevation))

        //Password
        mPasswordField.setIconDrawable(AB.getDrawable(R.drawable.login_password_icon))
        mPasswordField.setTextSize(AB.getDimenPixelSize(R.dimen.login_input_text_size).toFloat())
        mPasswordField.setTextColor(AB.getColor(R.color.login_password_text_color))
        mPasswordField.setHintColor(AB.getColor(R.color.login_password_hint_color))
        mPasswordField.setHint(AB.getString(R.string.login_password_hint))
        mPasswordField.setUnSelectedBackground(AB.getDrawable(R.drawable.login_password_background_unselected))
        mPasswordField.setSelectedBackground(AB.getDrawable(R.drawable.login_password_background_unselected))
        mPasswordField.hasIcon(AB.getBoolean(R.bool.login_password_has_clear))
        mPasswordField.background = mPasswordField.getUnselectedBackground()
        mPasswordField.setHasCancelIcon(true)
        mPasswordField.setCancelIconPadding(AB.getDimenPixelSize(R.dimen.login_input_text_show_pass_padding))
        mPasswordField.setCancelIcon(AB.getDrawable(R.drawable.disactive_eye_hide_icon))
//        ViewCompat.setElevation(mPasswordField, AB.getDimen(R.dimen.login_input_elevation))

        //Login Button
        mLogin.setText(AB.getString(R.string.login_button_text))
        mLogin.setTextSize(AB.getDimenPixelSize(R.dimen.login_button_text_size).toFloat())
        mLogin.setTextColor(AB.getColor(R.color.login_button_text_color))
        mLogin.setEnabledBackground(AB.getDrawable(R.drawable.login_button_background_enabled))
        mLogin.setDisableBackground(AB.getDrawable(R.drawable.login_button_background_disabled))
        mLogin.setLoadingColorFilter(AB.getColor(R.color.login_button_text_color))
        mLogin.hasIcon(AB.getBoolean(R.bool.login_button_has_icon))
        mLogin.setEnable(false)

        //ForgetPassword
        mResetPassword.setTextColor(AB.getColor(R.color.login_reset_password_text_color))
        mResetPassword.text = AB.getString(R.string.login_reset_password_text)
        mResetPassword.setTextSize(TypedValue.COMPLEX_UNIT_PX, AB.getDimenPixelSize(R.dimen.login_forget_password_text_size).toFloat())

        //SignUp
        mSignUpTitle.setTextColor(AB.getColor(R.color.login_do_not_have_account))
        mSignUpTitle.text = AB.getString(R.string.login_do_not_have_account)
        mSignUpTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, AB.getDimenPixelSize(R.dimen.login_do_not_have_account_size).toFloat())


        mSignUp.setText(AB.getString(R.string.login_sign_up_text))
        mSignUp.setTextSize(AB.getDimenPixelSize(R.dimen.login_sign_up_text_size).toFloat())
        mSignUp.setTextColor(AB.getColor(com.srp.eways.R.color.add_eways_contact_form_cancel_button))
        mSignUp.setEnabledBackground(AB.getDrawable(com.srp.eways.R.drawable.bill_inquiry_detail_save_background_enabled))
        mSignUp.setDisableBackground(AB.getDrawable(com.srp.eways.R.drawable.button_element_default_background_disabled))
        mSignUp.setLoadingColorFilter(AB.getColor(com.srp.eways.R.color.add_eways_contact_form_cancel_button))
        mSignUp.hasIcon(false)
        mSignUp.setEnable(true)

        //endregion

        //region ConfirmUsername

        mConfirmUsernameInput.setIconDrawable(AB.getDrawable(R.drawable.login_username_icon))
        mConfirmUsernameInput.setTextSize(AB.getDimenPixelSize(R.dimen.login_input_text_size).toFloat())
        mConfirmUsernameInput.setTextColor(AB.getColor(R.color.login_username_text_color))
        mConfirmUsernameInput.setHintColor(AB.getColor(R.color.login_username_hint_color))
        mConfirmUsernameInput.setHint(AB.getString(R.string.login_username_hint))
        mConfirmUsernameInput.setUnSelectedBackground(AB.getDrawable(R.drawable.phone_and_operator_input_background_unselected))
        mConfirmUsernameInput.setSelectedBackground(AB.getDrawable(R.drawable.phone_and_operator_input_background_selected))
        mConfirmUsernameInput.hasIcon(AB.getBoolean(R.bool.login_username_has_clear))
//        ViewCompat.setElevation(mConfirmUsernameInput, AB.getDimen(R.dimen.login_input_elevation))

        mConfirmUsernameButton.setText(AB.getString(R.string.login_recovery_password_text))
        mConfirmUsernameButton.setTextSize(AB.getDimenPixelSize(R.dimen.login_button_text_size).toFloat())
        mConfirmUsernameButton.setTextColor(AB.getColor(R.color.login_button_text_color))
        mConfirmUsernameButton.setEnabledBackground(AB.getDrawable(R.drawable.login_button_background_enabled))
        mConfirmUsernameButton.setDisableBackground(AB.getDrawable(R.drawable.login_button_background_disabled))
        mConfirmUsernameButton.setLoadingColorFilter(AB.getColor(R.color.login_button_text_color))
        mConfirmUsernameButton.hasIcon(AB.getBoolean(R.bool.login_button_has_icon))
        mConfirmUsernameButton.setEnable(false)

        //endregion

        //region VerificationCode

//        mVerificationCodeInput.setIconDrawable(AB.getDrawable(R.drawable.login_verification_code_icon))
        mVerificationCodeInput.setTextSize(AB.getDimenPixelSize(R.dimen.login_input_text_size).toFloat())
        mVerificationCodeInput.setTextColor(AB.getColor(R.color.login_username_text_color))
        mVerificationCodeInput.setHintColor(AB.getColor(R.color.login_username_hint_color))
        mVerificationCodeInput.setHint(AB.getString(R.string.login_verification_code_hint))
        mVerificationCodeInput.setUnSelectedBackground(AB.getDrawable(R.drawable.phone_and_operator_input_background_unselected))
        mVerificationCodeInput.setSelectedBackground(AB.getDrawable(R.drawable.phone_and_operator_input_background_selected))
        mVerificationCodeInput.hasIcon(AB.getBoolean(R.bool.login_username_has_clear))
//        ViewCompat.setElevation(mVerificationCodeInput, AB.getDimen(R.dimen.login_input_elevation))

        mVerificationCodeButton.setText(AB.getString(R.string.login_confirm_button_text2))
        mVerificationCodeButton.setTextSize(AB.getDimenPixelSize(R.dimen.login_button_text_size).toFloat())
        mVerificationCodeButton.setTextColor(AB.getColor(R.color.login_button_text_color))
        mVerificationCodeButton.setEnabledBackground(AB.getDrawable(R.drawable.login_button_background_enabled))
        mVerificationCodeButton.setDisableBackground(AB.getDrawable(R.drawable.login_button_background_disabled))
        mVerificationCodeButton.setLoadingColorFilter(AB.getColor(R.color.login_button_text_color))
        mVerificationCodeButton.hasIcon(AB.getBoolean(R.bool.login_button_has_icon))
        mVerificationCodeButton.setEnable(false)

        mResendVerificationCodeTimer.setTextColor(AB.getColor(R.color.login_verification_code_timer_color))
        mResendVerificationCodeTimer.setTextSize(TypedValue.COMPLEX_UNIT_PX, AB.getDimenPixelSize(R.dimen.login_verification_code_timer_size).toFloat())

        mResendVerificationCodeText.setTextSize(TypedValue.COMPLEX_UNIT_PX, AB.getDimenPixelSize(R.dimen.login_verification_code_timer_size).toFloat())
        mResendVerificationCodeText.text = AB.getString(R.string.login_verification_code_recend)
        changeResendTextSettings(false)

        //endregion

        //region ResetPassword

        mResetPasswordInput.setIconDrawable(AB.getDrawable(R.drawable.login_reset_password_icon))
        mResetPasswordInput.setTextSize(AB.getDimenPixelSize(R.dimen.login_input_text_size).toFloat())
        mResetPasswordInput.setTextColor(AB.getColor(R.color.login_username_text_color))
        mResetPasswordInput.setHintColor(AB.getColor(R.color.login_username_hint_color))
        mResetPasswordInput.setHint(AB.getString(R.string.login_reset_password_first_input_hint))
        mResetPasswordInput.setUnSelectedBackground(AB.getDrawable(R.drawable.phone_and_operator_input_background_unselected))
        mResetPasswordInput.setSelectedBackground(AB.getDrawable(R.drawable.phone_and_operator_input_background_selected))
        mResetPasswordInput.hasIcon(AB.getBoolean(R.bool.login_username_has_clear))
//        ViewCompat.setElevation(mResetPasswordInput, AB.getDimen(R.dimen.login_input_elevation))

        mResetPasswordAgainInput.setIconDrawable(AB.getDrawable(R.drawable.login_reset_password_icon))
        mResetPasswordAgainInput.setTextSize(AB.getDimenPixelSize(R.dimen.login_input_text_size).toFloat())
        mResetPasswordAgainInput.setTextColor(AB.getColor(R.color.login_username_text_color))
        mResetPasswordAgainInput.setHintColor(AB.getColor(R.color.login_username_hint_color))
        mResetPasswordAgainInput.setHint(AB.getString(R.string.login_reset_password_second_input_hint))
        mResetPasswordAgainInput.setUnSelectedBackground(AB.getDrawable(R.drawable.phone_and_operator_input_background_unselected))
        mResetPasswordAgainInput.setSelectedBackground(AB.getDrawable(R.drawable.phone_and_operator_input_background_selected))
        mResetPasswordAgainInput.hasIcon(AB.getBoolean(R.bool.login_username_has_clear))
        mResetPasswordAgainInput.background = mResetPasswordAgainInput.getUnselectedBackground()
//        ViewCompat.setElevation(mResetPasswordAgainInput, AB.getDimen(R.dimen.login_input_elevation))

        mResetPasswordButton.setText(AB.getString(R.string.login_confirm_button_text))
        mResetPasswordButton.setTextSize(AB.getDimenPixelSize(R.dimen.login_button_text_size).toFloat())
        mResetPasswordButton.setTextColor(AB.getColor(R.color.login_button_text_color))
        mResetPasswordButton.setEnabledBackground(AB.getDrawable(R.drawable.login_button_background_enabled))
        mResetPasswordButton.setDisableBackground(AB.getDrawable(R.drawable.login_button_background_disabled))
        mResetPasswordButton.setLoadingColorFilter(AB.getColor(R.color.login_button_text_color))
        mResetPasswordButton.hasIcon(AB.getBoolean(R.bool.login_button_has_icon))
        mResetPasswordButton.setEnable(false)

        //endregion
    }

    private fun changeResendTextSettings(isEnable: Boolean) {

        mResendVerificationCodeText.isEnabled = isEnable

        val AB = DI.getABResources()


        if (isEnable) {
            mResendVerificationCodeText.setTextColor(AB.getColor(R.color.login_verification_code_resend_color_enable))
            mResendVerificationCodeText.background = AB.getDrawable(R.drawable.login_resend_verification_code_background_enabled)
        } else {
            mResendVerificationCodeText.setTextColor(AB.getColor(R.color.login_verification_code_resend_color_disable))
            mResendVerificationCodeText.background = AB.getDrawable(R.drawable.login_resend_verification_code_background_disable)
        }

    }

    override fun onBackPressed() {

        if (mCurrentState != STATE_LOGIN) {

            goPreviousState(mCurrentState)
        } else {
            super.onBackPressed()
        }
    }

    private fun goPreviousState(currentState: Int) {
        when (currentState) {
            STATE_ENTER_USERNAME -> {
                mConfirmUsernameInput.setText("")
                setState(STATE_LOGIN)
            }
            STATE_ENTER_CODE -> {
                mVerificationCodeInput.setText("")
                setState(STATE_ENTER_USERNAME)
            }
            STATE_RESET_PASSWORD -> {
                mResetPasswordInput.setText("")
                mResetPasswordAgainInput.setText("")
                setState(STATE_ENTER_USERNAME)
            }
        }
    }

    private fun setState(state: Int) {

        mCurrentState = state

        when (state) {
            STATE_LOGIN -> {
                mLoginContainer.visibility = VISIBLE
                mForgetPasswordContaintr.visibility = GONE

                mConfirmUsernameInput.setText("")
                mVerificationCodeInput.setText("")
                mResetPasswordInput.setText("")
                mResetPasswordAgainInput.setText("")
            }
            STATE_ENTER_USERNAME -> {
                mLoginContainer.visibility = GONE
                mForgetPasswordContaintr.visibility = VISIBLE
                mConfirmUsernameContainer.visibility = VISIBLE
                mVerificationCodeContainer.visibility = GONE
                mResetPasswordContainer.visibility = GONE

                setVerificationCodeTimer(false)

                mVerificationCodeInput.setText("")

            }
            STATE_ENTER_CODE -> {
                mLoginContainer.visibility = GONE
                mForgetPasswordContaintr.visibility = VISIBLE
                mConfirmUsernameContainer.visibility = GONE
                mVerificationCodeContainer.visibility = VISIBLE
                mResetPasswordContainer.visibility = GONE

                setVerificationCodeTimer(true)

            }
            STATE_RESET_PASSWORD -> {
                mLoginContainer.visibility = GONE
                mForgetPasswordContaintr.visibility = VISIBLE
                mConfirmUsernameContainer.visibility = GONE
                mVerificationCodeContainer.visibility = GONE
                mResetPasswordContainer.visibility = VISIBLE

                setVerificationCodeTimer(false)

                mConfirmUsernameInput.setText("")
                mVerificationCodeInput.setText("")
            }
        }
    }

}
