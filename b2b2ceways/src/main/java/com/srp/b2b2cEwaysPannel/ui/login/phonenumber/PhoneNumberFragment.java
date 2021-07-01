package com.srp.b2b2cEwaysPannel.ui.login.phonenumber;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.Observer;

import com.srp.b2b2cEwaysPannel.R;
import com.srp.b2b2cEwaysPannel.di.DI;
import com.srp.b2b2cEwaysPannel.model.login.authenticate.AuthenticateResponse;
import com.srp.b2b2cEwaysPannel.ui.login.LoginViewModel;
import com.srp.eways.ui.navigation.NavigationMemberFragment;
import com.srp.eways.ui.view.button.ButtonElement;
import com.srp.eways.ui.view.input.InputElement;
import com.srp.eways.util.CustomHintTypeFaceTextWatcher;
import com.srp.eways.util.PersianNumberFormatter;

import ir.abmyapp.androidsdk.IABResources;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.srp.b2b2cEwaysPannel.ui.login.LoginRootIds.ROOT_OTP;

public class PhoneNumberFragment extends NavigationMemberFragment<LoginViewModel> {

    private InputElement mPhoneNumberInput;
    private AppCompatTextView mPhoneNumberErrorText;
    private ButtonElement mLoginButton;

    public static PhoneNumberFragment getInstance() {
        return new PhoneNumberFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPhoneNumberInput = view.findViewById(R.id.phone_number_input);
        mPhoneNumberErrorText = view.findViewById(R.id.phone_error_text);
        mLoginButton = view.findViewById(R.id.login_button);

        setupPhoneNumberInput();
        setupLoginButton();

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
        getViewModel().getAuthenticationResponse().observe(this, new Observer<AuthenticateResponse>() {
            @Override
            public void onChanged(AuthenticateResponse authenticateResponse) {
                if (authenticateResponse != null) {
                    onSwitchRoot(ROOT_OTP);
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

    private void setupPhoneNumberInput() {
        final IABResources abResources = DI.getABResources();

        Typeface normalTypeface = ResourcesCompat.getFont(mPhoneNumberInput.getContext(), R.font.iran_yekan);
        Typeface hintTypeface = ResourcesCompat.getFont(mPhoneNumberInput.getContext(), R.font.iran_yekan_light);

        mPhoneNumberInput.setTextSize(abResources.getDimenPixelSize(R.dimen.login_phone_number_input_text_size));
        mPhoneNumberInput.setTextColor(abResources.getColor(R.color.login_phone_number_input_text_color));
        mPhoneNumberInput.setHintColor(abResources.getColor(R.color.login_phone_number_input_hint_color));
        mPhoneNumberInput.setHint(abResources.getString(R.string.login_phone_number_input_hint_text));
        mPhoneNumberInput.setUnSelectedBackground(abResources.getDrawable(R.drawable.input_element_background_unselected));
        mPhoneNumberInput.setSelectedBackground(abResources.getDrawable(R.drawable.input_element_background_unselected));
        mPhoneNumberInput.hasIcon(true);
        mPhoneNumberInput.setIconDrawable(abResources.getDrawable(R.drawable.ic_mobile));
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
                if (!(mPhoneNumberInput.getText().isEmpty()) && mPhoneNumberInput.getText().length() == 11) {
                    mLoginButton.setEnable(true);
                } else {
                    mLoginButton.setEnable(false);
                }

                if (mPhoneNumberErrorText.getVisibility() == VISIBLE) {
                    mPhoneNumberErrorText.setVisibility(INVISIBLE);

                    mPhoneNumberInput.setIconDrawable(abResources.getDrawable(R.drawable.ic_mobile));
                    mPhoneNumberInput.setBackground(mPhoneNumberInput.getUnselectedBackground());
                }
            }
        });

        mPhoneNumberErrorText.setTextSize(TypedValue.COMPLEX_UNIT_PX, abResources.getDimenPixelSize(R.dimen.login_error_text_size));
        mPhoneNumberErrorText.setTextColor(abResources.getColor(R.color.login_error_color));
        mPhoneNumberErrorText.setText(abResources.getString(R.string.login_phone_number_error));
        mPhoneNumberErrorText.setVisibility(View.INVISIBLE);
    }

    private void setupLoginButton() {

        final IABResources abResources = DI.getABResources();

        mLoginButton.setTextSize(abResources.getDimenPixelSize(R.dimen.login_phone_number_button_text_size));
        mLoginButton.setTextColor(abResources.getColor(R.color.login_phone_number_button_text_color));
        mLoginButton.setEnabledBackground(abResources.getDrawable(R.drawable.login_button_background_enabled));
        mLoginButton.setDisableBackground(abResources.getDrawable(R.drawable.login_button_background_disabled));
        mLoginButton.setLoadingVisibility(View.GONE);
        mLoginButton.setLoadingColorFilter(abResources.getColor(R.color.login_phone_number_button_text_color));
        mLoginButton.setEnable(false);
        mLoginButton.setText(abResources.getString(R.string.login_phone_number_button_text));
        mLoginButton.hasIcon(true);
        mLoginButton.setIconVisibility(VISIBLE);
        mLoginButton.setIcon(abResources.getDrawable(R.drawable.ic_sign_in));
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mPhoneNumberInput.getText().startsWith("09")) {
                    getViewModel().authenticate(mPhoneNumberInput.getText());
                } else {
                    mPhoneNumberErrorText.setVisibility(VISIBLE);

                    mPhoneNumberInput.setIconDrawable(abResources.getDrawable(R.drawable.ic_mobile_error));
                    mPhoneNumberInput.setBackground(abResources.getDrawable(R.drawable.login_input_error_background));
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
        return R.layout.fragment_phone_number;
    }
}
