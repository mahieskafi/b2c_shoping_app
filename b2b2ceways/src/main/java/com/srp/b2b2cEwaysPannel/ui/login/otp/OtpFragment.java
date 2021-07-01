package com.srp.b2b2cEwaysPannel.ui.login.otp;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.Observer;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.srp.b2b2cEwaysPannel.R;
import com.srp.b2b2cEwaysPannel.di.DI;
import com.srp.b2b2cEwaysPannel.model.login.verifyotp.VerifyOTPResponse;
import com.srp.b2b2cEwaysPannel.ui.login.LoginViewModel;
import com.srp.b2b2cEwaysPannel.ui.main.MainActivity;
import com.srp.b2b2cEwaysPannel.ui.view.OtpEditText;
import com.srp.b2b2cEwaysPannel.ui.view.TimerButton;
import com.srp.eways.di.DIMain;
import com.srp.eways.model.login.LoginResponse;
import com.srp.eways.model.login.UserInfo;
import com.srp.eways.network.NetworkResponseCodes;
import com.srp.eways.ui.login.UserInfoViewModel;
import com.srp.eways.ui.navigation.NavigationMemberFragment;
import com.srp.eways.ui.view.button.ButtonElement;
import com.srp.eways.ui.view.input.InputElement;
import com.srp.eways.util.CustomHintTypeFaceTextWatcher;
import com.srp.eways.util.Utils;

import ir.abmyapp.androidsdk.IABResources;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.srp.b2b2cEwaysPannel.ui.login.LoginRootIds.ROOT_PHONENUMBER;
import static com.srp.b2b2cEwaysPannel.ui.login.LoginRootIds.ROOT_REGISTER;

public class OtpFragment extends NavigationMemberFragment<LoginViewModel> {

    public static String OTP_INTENT_KEY = "code";
    public static String OTP_INTENT_FILTER = "otp_intent_filter";
    private static int OTP_NUMBER_LENGTH = 4;
    private static final int TIMER_BUTTON_TIME = 60;

    private UserInfoViewModel mUserInfoViewModel;

    private AppCompatTextView mTitle;
    private AppCompatTextView mSubTitle;
    private OtpEditText mOtpEditText;
    private AppCompatTextView mOtpErrorText;
    private TimerButton mTimerButton;
    private LinearLayout mPasswordLinear;
    private AppCompatTextView mOrText;
    private InputElement mPasswordInput;
    private AppCompatTextView mPasswordErrorText;
    private ButtonElement mLoginButton;

    private IABResources abResources;

    private boolean mOtpHasError = false;

    private BroadcastReceiver mSmsReader = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {

            mOtpEditText.setText(intent.getStringExtra(OTP_INTENT_KEY));
        }
    };

    public static OtpFragment getInstance() {
        return new OtpFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mUserInfoViewModel = DI.getViewModel(UserInfoViewModel.class);

        mTitle = view.findViewById(R.id.title_text);
        mSubTitle = view.findViewById(R.id.subtitle_text);
        mOtpEditText = view.findViewById(R.id.otp_input);
        mOtpErrorText = view.findViewById(R.id.otp_error_text);
        mTimerButton = view.findViewById(R.id.timer_button);
        mPasswordLinear = view.findViewById(R.id.password_linear);
        mOrText = view.findViewById(R.id.or_text);
        mPasswordInput = view.findViewById(R.id.password_input);
        mPasswordErrorText = view.findViewById(R.id.error_password_text);
        mLoginButton = view.findViewById(R.id.login_button);

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mSmsReader, new IntentFilter(OTP_INTENT_FILTER));

        abResources = DIMain.getABResources();

        mTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, abResources.getDimenPixelSize(R.dimen.login_otp_title_text_size));
        mTitle.setTextColor(abResources.getColor(R.color.login_otp_title_text_color));
        mTitle.setText(abResources.getString(R.string.login_otp_title));

        mSubTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, abResources.getDimenPixelSize(R.dimen.login_otp_sub_title_text_size));
        mSubTitle.setTextColor(abResources.getColor(R.color.login_otp_sub_title_text_color));


        mOrText.setTextSize(TypedValue.COMPLEX_UNIT_PX, abResources.getDimenPixelSize(R.dimen.login_otp_title_text_size));
        mOrText.setTextColor(abResources.getColor(R.color.login_otp_sub_title_text_color));
        mOrText.setText(abResources.getString(R.string.login_otp_or));

        setupTimerButton();
        setupOtpEditText();
        setupPasswordInput();
        setupRegisterButton();


        getViewModel().getIsNewUserLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isNewUser) {
                if (isNewUser != null && isNewUser) {
                    mPasswordLinear.setVisibility(View.GONE);
                } else {
                    mPasswordLinear.setVisibility(VISIBLE);
                }
            }
        });

        getViewModel().getPhoneNumberLiveData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String phoneNumber) {
                String subTitleText = Utils.toPersianNumber(abResources.getString(R.string.login_otp_sub_title_text1)) +
                        " " + Utils.toPersianNumber(getViewModel().getPhoneNumber()) + " " +
                        abResources.getString(R.string.login_otp_sub_title_text2);
                mSubTitle.setText(subTitleText);
            }
        });

        getViewModel().getVerifyLoading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                handleLoginButton(isLoading);
            }
        });


        getViewModel().getLoginLoading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                handleLoginButton(isLoading);
            }
        });


        getViewModel().getVerifyResponse().observe(this, new Observer<VerifyOTPResponse>() {
            @Override
            public void onChanged(VerifyOTPResponse verifyOTPResponse) {
                if (verifyOTPResponse != null) {
                    if (verifyOTPResponse.getStatus() == NetworkResponseCodes.SUCCESS) {
                        if (getViewModel().isNewUser() != null && getViewModel().isNewUser()) {
                            consumeAllResponses();
                            onSwitchRoot(ROOT_REGISTER);
                        } else {
                            openMainActivity(verifyOTPResponse.getUserInfo());
                        }
                    } else {
                        if (mPasswordInput.getText().length() > 0) {
                            getViewModel().login(mPasswordInput.getText());

                            mOtpHasError = true;
                        } else {
                            mOtpEditText.setError(true);
                            mOtpErrorText.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        });

        getViewModel().getLoginResponse().observe(this, new Observer<LoginResponse>() {
            @Override
            public void onChanged(LoginResponse loginResponse) {
                if (loginResponse != null) {
                    if (loginResponse.getStatus() == NetworkResponseCodes.SUCCESS) {
                        if (getViewModel().isNewUser() != null && getViewModel().isNewUser()) {
                            consumeAllResponses();
                            onSwitchRoot(ROOT_REGISTER);
                        } else {
                            openMainActivity(loginResponse.getUserInfo());
                        }
                    } else {
                        checkLoginError();
                    }
                }
            }
        });

        getViewModel().getLoginError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String errorMessage) {
                if (errorMessage != null) {
                    checkLoginError();
                }
            }
        });

        getViewModel().getVerifyError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String errorMessage) {
                if (errorMessage != null) {
                    if (mPasswordInput.getText().length() > 0) {
                        getViewModel().login(mPasswordInput.getText());

                        mOtpHasError = true;
                    } else {
                        mOtpEditText.setError(true);
                        mOtpErrorText.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        getViewModel().getError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String errorMessage) {
                if (errorMessage != null) {
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void handleLoginButton(boolean isLoading){

        if (isLoading) {
            mLoginButton.setLoadingVisibility(VISIBLE);
            mLoginButton.setIconVisibility(INVISIBLE);

            mLoginButton.setClickable(false);
            mOtpEditText.setEnabled(false);
            mPasswordInput.setEnabled(false);
        }
        else {
            mLoginButton.setLoadingVisibility(INVISIBLE);
            mLoginButton.setIconVisibility(VISIBLE);

            mLoginButton.setClickable(true);
            mOtpEditText.setEnabled(true);
            mPasswordInput.setEnabled(true);
        }

    }

    private void openMainActivity(UserInfo userInfo) {
        mUserInfoViewModel.setUserInfo(userInfo);

        consumeAllResponses();

        startActivity(MainActivity.newIntent(getContext()));
        getActivity().finish();
    }

    private void consumeAllResponses()
    {
        getViewModel().consumeAuthenticationResponse();
        getViewModel().consumeLoginResponse();
        getViewModel().consumeRegisterResponse();
        getViewModel().consumeVerifyResponse();
    }
    private void checkLoginError() {
        if (mOtpHasError) {
            mOtpEditText.setError(true);
            mOtpErrorText.setVisibility(View.VISIBLE);
        }

        mPasswordErrorText.setVisibility(VISIBLE);

        mPasswordInput.setIconDrawable(abResources.getDrawable(R.drawable.ic_error_password));
        mPasswordInput.setBackground(abResources.getDrawable(R.drawable.login_input_error_background));
    }

    private void setupTimerButton() {
        mTimerButton.setBackground(abResources.getDrawable(R.drawable.login_otp_timer_button_background));
        mTimerButton.setTitle(abResources.getString(R.string.login_otp_timer_title));
        mTimerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTimerButton.startTimer(TIMER_BUTTON_TIME);

                getViewModel().authenticate(getViewModel().getPhoneNumber());
            }
        });
        if(getViewModel().getAuthenticationResponse().getValue() == null && getViewModel().getPhoneNumber()!= null)
        {
            getViewModel().authenticate(getViewModel().getPhoneNumber());
        }

        mTimerButton.startTimer(TIMER_BUTTON_TIME);

    }

    private void setupOtpEditText() {

        mOtpEditText.setMaxLength(OTP_NUMBER_LENGTH);
        mOtpEditText.setTextColor(abResources.getColor(R.color.login_otp_text_color));

        int defaultOtpColor = abResources.getColor(R.color.login_otp_pin_color);
        int errorOtpColor = abResources.getColor(R.color.login_otp_error_pin_color);
        int[] otpColors = new int[]{defaultOtpColor, errorOtpColor, defaultOtpColor, defaultOtpColor};

        mOtpEditText.setColorForState(otpColors);

        mOtpErrorText.setTextSize(TypedValue.COMPLEX_UNIT_PX, abResources.getDimenPixelSize(R.dimen.login_otp_error_text_size));
        mOtpErrorText.setTextColor(abResources.getColor(R.color.login_otp_error_color));
        mOtpErrorText.setText(abResources.getString(R.string.login_otp_error_text));
        mOtpErrorText.setVisibility(INVISIBLE);
        mOtpEditText.setError(false);
        getViewModel().consumeVerifyError();

        mOtpEditText.setOnPinEnteredListener(new OtpEditText.OnPinEnteredListener() {
            @Override
            public void onPinEntered(CharSequence str) {
                if (str.length() < OTP_NUMBER_LENGTH) {
                    mOtpErrorText.setVisibility(View.INVISIBLE);
                    mOtpEditText.setError(false);

                    if (mPasswordInput.getText().length() == 0) {
                        mLoginButton.setEnable(false);
                    }
                } else {
                    mOtpHasError = false;
                    mLoginButton.setEnable(true);
                }
            }
        });
    }

    private void setupPasswordInput() {
        mPasswordInput.setTextSize(abResources.getDimenPixelSize(R.dimen.login_otp_input_elements_text_size));
        mPasswordInput.setTextColor(abResources.getColor(R.color.login_otp_input_elements_text_color));
        mPasswordInput.setHintColor(abResources.getColor(R.color.login_otp_input_elements_hint_color));
        mPasswordInput.setHint(abResources.getString(R.string.login_otp_password_input_hint_text));
        mPasswordInput.setSelectedBackground(abResources.getDrawable(R.drawable.input_element_background_unselected));
        mPasswordInput.hasIcon(true);
        mPasswordInput.setIconDrawable(abResources.getDrawable(R.drawable.ic_password));
        mPasswordInput.setBackground(mPasswordInput.getSelectedBackground());
        mPasswordInput.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        mPasswordInput.getEditText().setMaxLines(1);
        mPasswordInput.getEditText().setText("");

        Typeface normalTypeface = ResourcesCompat.getFont(mPasswordInput.getContext(), R.font.iran_yekan);
        Typeface hintTypeface = ResourcesCompat.getFont(mPasswordInput.getContext(), R.font.iran_yekan_light);

        mPasswordInput.addTextChangeListener(new CustomHintTypeFaceTextWatcher(mPasswordInput.getEditText(), normalTypeface, hintTypeface));
        mPasswordInput.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mPasswordErrorText.getVisibility() == VISIBLE) {
                    mPasswordErrorText.setVisibility(View.INVISIBLE);

                    mPasswordInput.setIconDrawable(abResources.getDrawable(R.drawable.ic_password));
                    mPasswordInput.setBackground(mPasswordInput.getSelectedBackground());
                }

                if (s.length() > 0) {
                    mLoginButton.setEnable(true);
                } else {
                    mLoginButton.setEnable(false);
                }

            }
        });

        mPasswordErrorText.setTextSize(TypedValue.COMPLEX_UNIT_PX, abResources.getDimenPixelSize(R.dimen.login_otp_error_text_size));
        mPasswordErrorText.setTextColor(abResources.getColor(R.color.login_otp_error_color));
        mPasswordErrorText.setText(abResources.getString(R.string.login_otp_password_error));
        mPasswordErrorText.setVisibility(View.INVISIBLE);
    }

    private void setupRegisterButton() {

        mLoginButton.setTextSize(abResources.getDimenPixelSize(R.dimen.login_otp_button_text_size));
        mLoginButton.setTextColor(abResources.getColor(R.color.login_otp_button_text_color));
        mLoginButton.setEnabledBackground(abResources.getDrawable(R.drawable.login_button_background_enabled));
        mLoginButton.setDisableBackground(abResources.getDrawable(R.drawable.login_button_background_disabled));
        mLoginButton.setLoadingVisibility(View.GONE);
        mLoginButton.setLoadingColorFilter(abResources.getColor(R.color.login_otp_button_text_color));
        mLoginButton.setEnable(false);
        mLoginButton.setText(abResources.getString(R.string.login_register_button_text));
        mLoginButton.hasIcon(true);
        mLoginButton.setIconVisibility(View.VISIBLE);
        mLoginButton.setIcon(abResources.getDrawable(R.drawable.ic_sign_in));
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOtpEditText.getText().length() == OTP_NUMBER_LENGTH) {
                    getViewModel().verify(getViewModel().getPhoneNumber(), mOtpEditText.getText().toString(), getViewModel().isNewUser());

                    return;
                }
                if (mPasswordInput.getText().length() > 0) {
                    getViewModel().login(mPasswordInput.getText());
                }
            }
        });


    }

    @Override
    public LoginViewModel acquireViewModel() {
        return DI.getViewModel(LoginViewModel.class);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_otp;
    }

    @Override
    public boolean onBackPress() {
        onSwitchRoot(ROOT_PHONENUMBER);

        return true;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            setupTimerButton();
            setupOtpEditText();
            setupPasswordInput();
            setupRegisterButton();
            mTimerButton.startTimer(TIMER_BUTTON_TIME);
        } else {
            mTimerButton.stopTimer();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mSmsReader);
    }
}
