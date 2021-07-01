package com.srp.ewayspanel.ui.login.phonenumber;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.Observer;


import com.srp.eways.base.BaseActivity;
import com.srp.eways.di.DIMain;
import com.srp.eways.ui.view.button.ButtonElement;
import com.srp.eways.ui.view.input.InputElement;
import com.srp.eways.util.Constants;
import com.srp.eways.util.CustomHintTypeFaceTextWatcher;
import com.srp.eways.util.PersianNumberFormatter;
import com.srp.eways.util.Utils;
import com.srp.ewayspanel.R;
import com.srp.ewayspanel.di.DI;
import com.srp.ewayspanel.model.login.sms.LoggedSmsRequestResponse;
import com.srp.ewayspanel.model.login.sms.OutSmsResponse;
import com.srp.ewayspanel.ui.login.LoginActivity;
import com.srp.ewayspanel.ui.login.LoginViewModel;
import com.srp.ewayspanel.ui.login.otp.OtpActivity;
import com.srp.ewayspanel.ui.main.MainActivity;

import org.jetbrains.annotations.NotNull;

import ir.abmyapp.androidsdk.IABResources;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.srp.eways.di.DIMainCommon.getContext;

public class PhoneNumberActivity extends BaseActivity<LoginViewModel> {

    public static Intent newIntent(Context context) {
        return new Intent(context, PhoneNumberActivity.class);
    }

    private static final int REQUEST_CODE = 101;
    private InputElement mPhoneNumberInput;
    private AppCompatTextView mPhoneNumberErrorText;
    private AppCompatTextView mPhonNumberTitle;

    private ButtonElement mLoginButton, mCancelButton;
    private LoginViewModel mViewModel;

    private View mHeader;
    private ImageView mLogo;
    private IABResources abResources;

    private String mDeviceCode;
    private String mNeededToConfirmNumber = "";

    private int logoMarginTopOne;
    private int logoMarginTopTwo;
    private int titemarginTopOne;
    private int titemarginTopTwo;
    private int titemarginHorizontally;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_phone_number;
    }

    @Override
    public LoginViewModel getViewModel() {
        mViewModel = DI.getViewModel(LoginViewModel.class);
        return mViewModel;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

        if (getIntent() == null || (getIntent() != null && getIntent().getStringExtra(Constants.NEEDED_TO_CONFIRM_NUMBER) == null)) {
            boolean notFirstOpen = DIMain.getPreferenceCache().getBoolean(Constants.FIRST_OPEN);
            if (notFirstOpen) {
                String token = DI.getPreferenceCache().getString(Constants.TOKEN_KEY);
                if (token != null && !token.isEmpty()) {
                    startActivity(MainActivity.newIntent(this));
                } else {
                    startActivity(LoginActivity.Companion.newIntent(getContext()));
                }

                finish();
            }
        } else {
            mNeededToConfirmNumber = getIntent().getStringExtra(Constants.NEEDED_TO_CONFIRM_NUMBER);
        }

        super.onCreate(savedInstanceState);
        abResources = DI.getABResources();

        mPhoneNumberInput = findViewById(R.id.phone_number_input);
        mPhoneNumberErrorText = findViewById(R.id.phone_error_text);
        mLoginButton = findViewById(R.id.login_button);
        mCancelButton = findViewById(R.id.lcancle_button);
        mHeader = findViewById(R.id.login_header);
        mLogo = findViewById(R.id.logo);
        mPhonNumberTitle = findViewById(R.id.input_number_txt);


        setupPhoneNumberInput();
        setupLoginButton();
        setupCancleButton();

        if (!DIMain.getPreferenceCache().getBoolean(Constants.FIRST_OPEN)) {
            //Befor login

            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, logoMarginTopOne, 0, 0);
            layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
            mLogo.setLayoutParams(layoutParams);
            //Header
            mHeader.setBackground(abResources.getDrawable(R.drawable.verification_header_2));
            //Logo
            mLogo.setImageDrawable(abResources.getDrawable(com.srp.ewayspanel.R.drawable.verification_header_logo_2));


            RelativeLayout.LayoutParams layoutParamstitle = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParamstitle.setMargins(titemarginHorizontally, titemarginTopOne, titemarginHorizontally, 0);
            mPhonNumberTitle.setLayoutParams(layoutParamstitle);

            mPhonNumberTitle.setText(abResources.getString(R.string.phone_number_activity_title_login));
        } else {
            //after login
            //Header
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, logoMarginTopTwo, 0, 0);
            layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
            mLogo.setLayoutParams(layoutParams);

            RelativeLayout.LayoutParams layoutParamstitle = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParamstitle.setMargins(titemarginHorizontally, titemarginTopTwo, titemarginHorizontally, 0);
            mPhonNumberTitle.setLayoutParams(layoutParamstitle);

            mHeader.setBackground(abResources.getDrawable(R.drawable.login_header));
            //Logo
            mLogo.setImageDrawable(abResources.getDrawable(com.srp.ewayspanel.R.drawable.login_header_logo));

            mPhonNumberTitle.setText(abResources.getString(R.string.phone_number_activity_title_out));
        }

        if (mNeededToConfirmNumber != null && !mNeededToConfirmNumber.isEmpty()) {
            mPhoneNumberInput.setText(mNeededToConfirmNumber);
        }

        getViewModel().getAuthenticationLoading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                if (isLoading) {
                    mLoginButton.setLoadingVisibility(VISIBLE);
                    mLoginButton.setIconVisibility(INVISIBLE);

                    mLoginButton.setClickable(false);
                    mPhoneNumberInput.setEnabled(false);
                } else {
                    mLoginButton.setLoadingVisibility(INVISIBLE);
                    mLoginButton.setIconVisibility(VISIBLE);

                    mLoginButton.setClickable(true);
                    mPhoneNumberInput.setEnabled(true);
                }
            }
        });

        getViewModel().getSendMobileConfirmResponseLiveData().observe(this, new Observer<LoggedSmsRequestResponse>() {
            @Override
            public void onChanged(LoggedSmsRequestResponse loggedSmsRequestResponse) {
                if (loggedSmsRequestResponse != null) {
                    Intent i = new Intent(PhoneNumberActivity.this, OtpActivity.class);
                    i.putExtra(Constants.KEY_NUMBER, mPhoneNumberInput.getText());
                    i.putExtra(Constants.IMEI, mDeviceCode);
                    startActivity(i);
                    finish();

                    getViewModel().consumeSendMobileConfirmResponse();
                }
            }
        });


        getViewModel().getVerifyMobileNumberResponseLiveData().observe(this, new Observer<OutSmsResponse>() {
            @Override
            public void onChanged(OutSmsResponse outSmsResponse) {
                if (outSmsResponse != null) {
                    DI.getPreferenceCache().put(Constants.TRACE_CODE, outSmsResponse.getTraceCode());

                    Intent i = new Intent(PhoneNumberActivity.this, OtpActivity.class);
                    i.putExtra(Constants.KEY_NUMBER, mPhoneNumberInput.getText());
                    i.putExtra(Constants.TRASE_CODE, outSmsResponse.getTraceCode());
                    i.putExtra(Constants.IMEI, mDeviceCode);
                    startActivity(i);
                    finish();

                    getViewModel().consumeVerifyMobileNumberResponse();
                }
            }

        });

        if (!DIMain.getPreferenceCache().getBoolean(Constants.FIRST_OPEN)) {
            getViewModel().getErrorVerification().observe(this, new Observer<String>() {
                @Override
                public void onChanged(String errorMessage) {
                    if (errorMessage != null) {
                        Toast.makeText(getBaseContext(), errorMessage, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            getViewModel().getSendMobileNumberError().observe(this, new Observer<String>() {
                @Override
                public void onChanged(String errorMessage) {
                    if (errorMessage != null) {
                        Toast.makeText(getBaseContext(), errorMessage, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }


    }

    private void setupPhoneNumberInput() {
        final IABResources abResources = DI.getABResources();


        logoMarginTopOne = abResources.getDimenPixelSize(R.dimen.verification_one_logo_margin_top);
        logoMarginTopTwo = abResources.getDimenPixelSize(R.dimen.verification_two_logo_margin_top);
        titemarginTopOne = abResources.getDimenPixelSize(R.dimen.verification_one_title_margin_top);
        titemarginTopTwo = abResources.getDimenPixelSize(R.dimen.verification_two_title_margin_top);
        titemarginHorizontally = abResources.getDimenPixelSize(R.dimen.verification_title_margin_horizontay);

        mPhoneNumberErrorText.setTextSize(TypedValue.COMPLEX_UNIT_PX, abResources.getDimenPixelSize(R.dimen.login_error_text_size));
        mPhoneNumberErrorText.setTextColor(abResources.getColor(R.color.login_error_color));
        mPhoneNumberErrorText.setText(abResources.getString(R.string.login_phone_number_error));
        mPhoneNumberErrorText.setVisibility(GONE);

        Typeface normalTypeface = ResourcesCompat.getFont(mPhoneNumberInput.getContext(), R.font.iran_yekan);
        Typeface hintTypeface = ResourcesCompat.getFont(mPhoneNumberInput.getContext(), R.font.iran_yekan_light);

        mPhoneNumberInput.setTextSize(abResources.getDimenPixelSize(R.dimen.login_phone_number_input_text_size));
        mPhoneNumberInput.setTextColor(abResources.getColor(R.color.login_phone_number_input_text_color));
        mPhoneNumberInput.setHintColor(abResources.getColor(R.color.login_phone_number_input_hint_color));
        mPhoneNumberInput.setHint(abResources.getString(R.string.login_phone_number_input_hint_text));
        mPhoneNumberInput.setUnSelectedBackground(abResources.getDrawable(R.drawable.input_element_background_unselected));
        mPhoneNumberInput.setSelectedBackground(abResources.getDrawable(R.drawable.input_element_background_unselected));
        mPhoneNumberInput.hasIcon(true);
        mPhoneNumberInput.setIconDrawable(abResources.getDrawable(R.drawable.ic_mobile_2));
        mPhoneNumberInput.setBackground(mPhoneNumberInput.getUnselectedBackground());
        mPhoneNumberInput.setImeOption(EditorInfo.IME_ACTION_DONE);
        mPhoneNumberInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        mPhoneNumberInput.getEditText().setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});
        mPhoneNumberInput.getEditText().setText("");

        mPhoneNumberInput.addTextChangeListener(new CustomHintTypeFaceTextWatcher(mPhoneNumberInput.getEditText(), normalTypeface, hintTypeface));
        mPhoneNumberInput.addTextChangeListener(new PersianNumberFormatter());
        mPhoneNumberInput.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (Utils.isPhoneNumberValid(mPhoneNumberInput.getText())) {
                    mLoginButton.setEnable(true);
                    mLoginButton.setTextColor(abResources.getColor(R.color.login_otp_timer_button_background));

                    if (mPhoneNumberErrorText.getVisibility() == VISIBLE) {
                        mPhoneNumberErrorText.setVisibility(GONE);

                        mPhoneNumberInput.setIconDrawable(abResources.getDrawable(R.drawable.ic_mobile));
                        mPhoneNumberInput.setBackground(mPhoneNumberInput.getUnselectedBackground());
                    }

                    Utils.hideKeyboard(PhoneNumberActivity.this);
                } else {
                    mLoginButton.setEnable(false);
                    mLoginButton.setTextColor(abResources.getColor(R.color.verification_phone_number_button_text_color_disable));
                    if (mPhoneNumberInput.getText().length() == 11) {
                        Utils.hideKeyboard(PhoneNumberActivity.this);
                        mPhoneNumberErrorText.setVisibility(VISIBLE);
                    } else {
                        mPhoneNumberErrorText.setVisibility(GONE);
                    }
                }

            }
        });
    }

    private void setupLoginButton() {

        mLoginButton.setTextSize(abResources.getDimenPixelSize(R.dimen.login_phone_number_button_text_size));
        mLoginButton.setTextColor(abResources.getColor(R.color.verification_phone_number_button_text_color_disable));
        mLoginButton.setEnabledBackground(abResources.getDrawable(R.drawable.login_button_background_enabled));
        mLoginButton.setDisableBackground(abResources.getDrawable(R.drawable.login_button_background_disabled));
        mLoginButton.setLoadingVisibility(View.GONE);
        mLoginButton.setLoadingColorFilter(abResources.getColor(R.color.login_phone_number_button_text_color));
        mLoginButton.setEnable(false);
        mLoginButton.setText(abResources.getString(R.string.login_phone_number_button_text));
        mLoginButton.hasIcon(true);
        mLoginButton.setIconVisibility(VISIBLE);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!DIMain.getPreferenceCache().getBoolean(Constants.FIRST_OPEN)) {
                    if (Utils.isPhoneNumberValid(mPhoneNumberInput.getText())) {

                        checkDeviceCode();
                        if (mDeviceCode != null && !mDeviceCode.isEmpty()) {
                            getViewModel().verifyMobileNumber(mPhoneNumberInput.getText(), mDeviceCode);
                        }
                    } else {
                        mPhoneNumberErrorText.setVisibility(VISIBLE);
                        mPhoneNumberInput.setBackground(abResources.getDrawable(R.drawable.login_input_error_background));
                    }
                } else {
                    if (Utils.isPhoneNumberValid(mPhoneNumberInput.getText())) {
                        getViewModel().sendMobileConfirmRequest(mPhoneNumberInput.getText());
                    } else {
                        mPhoneNumberErrorText.setVisibility(VISIBLE);
                        mPhoneNumberInput.setBackground(abResources.getDrawable(R.drawable.login_input_error_background));
                    }
                }

            }

        });
    }

    @SuppressLint("HardwareIds")
    private void checkDeviceCode() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            mDeviceCode = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        } else {
            if (ActivityCompat.checkSelfPermission(getContext(),
                    Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(PhoneNumberActivity.this,
                        new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_CODE);
            } else {
                mDeviceCode = Utils.getIMEI(PhoneNumberActivity.this);
            }
        }
    }

    private void setupCancleButton() {
        mCancelButton.setTextSize(abResources.getDimenPixelSize(R.dimen.login_phone_number_button_cancle_text_size));
        mCancelButton.setTextColor(abResources.getColor(R.color.verification_phone_number_button_cancle_text_color));
        mCancelButton.setEnabledBackground(abResources.getDrawable(R.drawable.login_register_button_background));
        mCancelButton.setDisableBackground(abResources.getDrawable(R.drawable.login_button_background_disabled));
        mCancelButton.setLoadingVisibility(View.GONE);
        mCancelButton.setLoadingColorFilter(abResources.getColor(R.color.login_phone_number_button_text_color));
        mCancelButton.setEnable(true);
        mCancelButton.setText("انصراف");
        mCancelButton.hasIcon(true);
        mCancelButton.setIconVisibility(VISIBLE);
        boolean notFirstOpen = DIMain.getPreferenceCache().getBoolean(Constants.FIRST_OPEN);
        if (notFirstOpen) {
            mCancelButton.setVisibility(VISIBLE);
        } else {
            mCancelButton.setVisibility(GONE);
        }
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = LoginActivity.Companion.newIntent(PhoneNumberActivity.this);

                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        if (requestCode == REQUEST_CODE && (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            mDeviceCode = Utils.getIMEI(PhoneNumberActivity.this);
            getViewModel().verifyMobileNumber(mPhoneNumberInput.getText(), mDeviceCode);
        }
    }
}
