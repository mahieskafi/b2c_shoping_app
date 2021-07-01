package com.srp.ewayspanel.ui.login.otp;


import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.srp.eways.base.BaseActivity;
import com.srp.eways.di.DIMain;
import com.srp.eways.ui.view.LoadingStateView;
import com.srp.eways.ui.view.button.ButtonElement;
import com.srp.eways.util.Constants;
import com.srp.eways.util.Utils;
import com.srp.ewayspanel.AppConfig;
import com.srp.ewayspanel.R;
import com.srp.ewayspanel.di.DI;
import com.srp.ewayspanel.model.login.sms.LoggedSmsConfirmResponse;
import com.srp.ewayspanel.model.login.sms.OutSmsVerifiRequest;
import com.srp.ewayspanel.model.login.sms.OutSmsVerifyResponse;
import com.srp.ewayspanel.ui.login.LoginActivity;
import com.srp.ewayspanel.ui.login.LoginViewModel;
import com.srp.ewayspanel.ui.login.phonenumber.PhoneNumberActivity;
import com.srp.ewayspanel.ui.main.MainActivity;
import com.srp.ewayspanel.ui.view.verification.OtpEditText;
import com.srp.ewayspanel.ui.view.verification.TimerButton;

import org.jetbrains.annotations.NotNull;

import ir.abmyapp.androidsdk.IABResources;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.srp.eways.di.DIMainCommon.getContext;


public class OtpActivity extends BaseActivity<LoginViewModel> {

    public static String OTP_INTENT_KEY = "code";
    public static String OTP_INTENT_FILTER = "otp_intent_filter";
    private static int OTP_NUMBER_LENGTH = 4;
    private static final int TIMER_BUTTON_TIME = 119;
    private static final int SMS_CODE = 508;

    private LoginViewModel mViewModel;

    private AppCompatTextView mTitle;
    private AppCompatTextView mSubTitle;
    private OtpEditText mOtpEditText;
    private LoadingStateView mLoading;

    private TimerButton mTimerButton;

    private View mHeader;
    private ImageView mLogo;

    private ButtonElement mChangeNumber;

    private IABResources abResources;
    private RelativeLayout mToast;

    private String mPhoneNumber;
    private String mTraceCode;
    private String mIMEI;

    private TextView mToastText;
    private int CodeInputCounter = 0;

    private int logoMarginTopOne;
    private int logoMarginTopTwo;

    private BroadcastReceiver mSmsReader = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            mOtpEditText.setText(intent.getStringExtra(OTP_INTENT_KEY));
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (intent != null) {
            mPhoneNumber = intent.getStringExtra(Constants.KEY_NUMBER);
            mTraceCode = intent.getStringExtra(Constants.TRASE_CODE);
            mIMEI = intent.getStringExtra(Constants.IMEI);

        }


        mTitle = findViewById(R.id.title_text);
        mSubTitle = findViewById(R.id.subtitle_text);
        mOtpEditText = findViewById(R.id.otp_input);
        mTimerButton = findViewById(R.id.timer_button);
        mToast = findViewById(R.id.toast_error_verify_code);
        mToastText=findViewById(R.id.toast_error_text);
        mLoading=findViewById(R.id.loadingstateview);

        mChangeNumber = findViewById(R.id.change_number_button);

        mHeader = findViewById(R.id.login_header);
        mLogo = findViewById(R.id.logo);

        getViewModel().vrifyOtpErrorHide();
        getViewModel().ConfirmMobileHide();

        mLoading.setTextColor(DIMain.getABResources().getColor(com.srp.eways.R.color.white));
        mLoading.setStateAndDescription(LoadingStateView.STATE_LOADING, getString(com.srp.eways.R.string.loading_message), false);

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(OtpActivity.this, new String[]{Manifest.permission.RECEIVE_SMS}, SMS_CODE);
        } else {
            LocalBroadcastManager.getInstance(getContext()).registerReceiver(mSmsReader, new IntentFilter(OTP_INTENT_FILTER));
        }


        abResources = DIMain.getABResources();

        mTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, abResources.getDimenPixelSize(R.dimen.login_otp_title_text_size));
        mTitle.setTextColor(abResources.getColor(R.color.login_otp_title_text_color));
        mTitle.setText(abResources.getString(R.string.login_otp_title));

        mSubTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, abResources.getDimenPixelSize(R.dimen.login_otp_sub_title_text_size));
        mSubTitle.setTextColor(abResources.getColor(R.color.login_otp_sub_title_text_color));

        mSubTitle.setText(Utils.toPersianNumber(mPhoneNumber));

        setupTimerButton();
        setupOtpEditText();
        ChangeNumberSetup();

        logoMarginTopOne = abResources.getDimenPixelSize(R.dimen.verification_one_logo_margin_top);
        logoMarginTopTwo = abResources.getDimenPixelSize(R.dimen.verification_two_logo_margin_top);

        if (!DIMain.getPreferenceCache().getBoolean(Constants.FIRST_OPEN)) {
            //Befor login
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, logoMarginTopOne, 0, 0);
            layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
            mLogo.setLayoutParams(layoutParams);

            mHeader.setBackground(abResources.getDrawable(R.drawable.verification_header_2));
            mLogo.setImageDrawable(abResources.getDrawable(com.srp.ewayspanel.R.drawable.verification_header_logo_2));
            mTitle.setText(abResources.getString(R.string.login_otp_title));
        } else {
            //after login

            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, logoMarginTopTwo, 0, 0);
            layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
            mLogo.setLayoutParams(layoutParams);

            mHeader.setBackground(abResources.getDrawable(R.drawable.login_header));
            mLogo.setImageDrawable(abResources.getDrawable(com.srp.ewayspanel.R.drawable.login_header_logo));
            mTitle.setText(abResources.getString(R.string.login_otp_title));

        }

        getViewModel().getConfirmMobileNumberResponseLiveData().observe(this, new Observer<LoggedSmsConfirmResponse>() {
            @Override
            public void onChanged(LoggedSmsConfirmResponse loggedSmsConfirmResponse) {

                if (loggedSmsConfirmResponse != null) {
                    if (loggedSmsConfirmResponse.getStatus() == 0) {
                        mToast.setVisibility(INVISIBLE);
                        DI.getPreferenceCache().put(Constants.FIRST_OPEN, true);

                        String token = DI.getPreferenceCache().getString(Constants.TOKEN_KEY);
                        if (token != null && !token.isEmpty()) {
                            startActivity(MainActivity.newIntent(getContext()));
                        } else {
                            startActivity(LoginActivity.Companion.newIntent(getContext()));
                        }
                        finish();
                        consumeAllResponses();
                    } else {
                        mToast.setVisibility(View.VISIBLE);
                    }
                }
            }
        });


        getViewModel().getVerifyOtpNumberResponseLiveData().observe(this, new Observer<OutSmsVerifyResponse>() {
            @Override
            public void onChanged(OutSmsVerifyResponse outSmsVerifyResponse) {

                if (outSmsVerifyResponse != null) {
                    if (outSmsVerifyResponse.getStatus() == 0) {
                        mLoading.setVisibility(View.GONE);
                        mChangeNumber.setLoadingVisibility(GONE);
                        mChangeNumber.setClickable(true);
                        mToast.setVisibility(INVISIBLE);
                        DI.getPreferenceCache().put(Constants.FIRST_OPEN, true);

                        String token = DI.getPreferenceCache().getString(Constants.TOKEN_KEY);
                        if (token != null && !token.isEmpty()) {
                            DI.getPreferenceCache().put(Constants.USER_INFO, "");
                            DI.getPreferenceCache().put(Constants.TOKEN_KEY, "");
                        }

                        startActivity(LoginActivity.Companion.newIntent(getContext()));
                        consumeAllResponses();
                        finish();
                    } else {
                        mLoading.setVisibility(View.GONE);
                        mChangeNumber.setLoadingVisibility(GONE);
                        mChangeNumber.setClickable(true);
                        if (CodeInputCounter<3){
                            mToastText.setText("کد وارد شده صحیح نمیباشد.");
                        }else{
                            mToastText.setText("لطفا دوباره درخواست کد دهید.");
                        }
                        mToast.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        getViewModel().getVerifyOtpError().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (integer != 0) {
                    mLoading.setVisibility(View.GONE);
                    mChangeNumber.setLoadingVisibility(GONE);
                    mChangeNumber.setClickable(true);
                    if (CodeInputCounter<3){
                        mToastText.setText("کد وارد شده صحیح نمیباشد.");
                    }else{
                        mToastText.setText("لطفا دوباره درخواست کد دهید.");
                    }

                    mToast.setVisibility(View.VISIBLE);
                } else {
                    mToast.setVisibility(INVISIBLE);
                }
            }
        });

        getViewModel().getConfirmMobile().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (integer != 0) {
                    mLoading.setVisibility(View.GONE);
                    mChangeNumber.setLoadingVisibility(GONE);
                    mChangeNumber.setClickable(true);
                    if (CodeInputCounter<3){
                        mToastText.setText("کد وارد شده صحیح نمیباشد.");
                    }else{
                        mToastText.setText("لطفا دوباره درخواست کد دهید.");
                    }
                    mToast.setVisibility(View.VISIBLE);
                } else {
                    mToast.setVisibility(INVISIBLE);
                }
            }
        });

    }

    private void consumeAllResponses() {
        getViewModel().consumeLoginResponse();
        getViewModel().consumeVerifyResponse();
        getViewModel().consumeVerifyOtpNumberResponse();
    }

    private void setupTimerButton() {
        mTimerButton.setBackground(abResources.getDrawable(R.drawable.login_otp_timer_button_background));
        mTimerButton.setTitle(abResources.getString(R.string.login_otp_timer_title));
        mTimerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CodeInputCounter=0;
                mTimerButton.startTimer(TIMER_BUTTON_TIME);

                if (!DIMain.getPreferenceCache().getBoolean(Constants.FIRST_OPEN)) {
                    getViewModel().verifyMobileNumber(mPhoneNumber, mIMEI);
                } else {
                    getViewModel().sendMobileConfirmRequest(mPhoneNumber);
                }


            }
        });

        mTimerButton.startTimer(TIMER_BUTTON_TIME);

    }

    private void setupOtpEditText() {

        mOtpEditText.setMaxLength(OTP_NUMBER_LENGTH);
        mOtpEditText.setTextColor(abResources.getColor(R.color.login_otp_text_color));

        int defaultOtpColor = abResources.getColor(R.color.login_otp_pin_color);
        int errorOtpColor = abResources.getColor(R.color.login_otp_error_pin_color);
        int[] otpColors = new int[]{defaultOtpColor, errorOtpColor, defaultOtpColor, defaultOtpColor};

        mOtpEditText.setColorForState(otpColors);


        mToast.setVisibility(View.GONE);
        CodeInputCounter=0;
        mOtpEditText.setError(false);
        getViewModel().consumeVerifyError();

        mOtpEditText.setOnPinEnteredListener(new OtpEditText.OnPinEnteredListener() {
            @Override
            public void onPinEntered(CharSequence str) {
                if (str.length() < OTP_NUMBER_LENGTH) {
                    mToast.setVisibility(INVISIBLE);
                    mOtpEditText.setError(false);

                } else if (!DIMain.getPreferenceCache().getBoolean(Constants.FIRST_OPEN)) {
                    mLoading.setVisibility(View.VISIBLE);
                    mChangeNumber.setLoadingVisibility(VISIBLE);
                    mChangeNumber.setClickable(false);
                    getViewModel().verifyOtpNumber(new OutSmsVerifiRequest(AppConfig.APP_KEY, mPhoneNumber, mTraceCode, String.valueOf(str)));
                    CodeInputCounter++;

                } else if (DIMain.getPreferenceCache().getBoolean(Constants.FIRST_OPEN)) {
                    mLoading.setVisibility(View.VISIBLE);
                    mChangeNumber.setLoadingVisibility(VISIBLE);
                    mChangeNumber.setClickable(false);
                    getViewModel().confirmMobileNumber(mPhoneNumber, String.valueOf(str));
                    CodeInputCounter++;
                }
            }
        });
    }


    private void ChangeNumberSetup() {
        mChangeNumber.setTextSize(abResources.getDimenPixelSize(R.dimen.change_number_botton_size));
        mChangeNumber.setTextColor(abResources.getColor(R.color.login_otp_button_text_color));
        mChangeNumber.setEnabledBackground(abResources.getDrawable(R.drawable.login_button_background_enabled));
        mChangeNumber.setDisableBackground(abResources.getDrawable(R.drawable.login_button_background_disabled));
        mChangeNumber.setLoadingVisibility(View.GONE);
        mChangeNumber.setLoadingColorFilter(abResources.getColor(R.color.login_otp_button_text_color));
        mChangeNumber.setEnable(true);
        mChangeNumber.setText(abResources.getString(R.string.change_number_otf_activity));
        mChangeNumber.hasIcon(false);
        mChangeNumber.setIconVisibility(View.GONE);
        mChangeNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CodeInputCounter=0;
                startActivity(PhoneNumberActivity.newIntent(getContext()));
                finish();

            }
        });
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_otp;
    }

    @Override
    public LoginViewModel getViewModel() {
        mViewModel = DI.getViewModel(LoginViewModel.class);
        return mViewModel;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        LocalBroadcastManager.getInstance(this).unregisterReceiver(mSmsReader);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        if (requestCode == SMS_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            LocalBroadcastManager.getInstance(getContext()).registerReceiver(mSmsReader, new IntentFilter(OTP_INTENT_FILTER));
        }
    }
}
