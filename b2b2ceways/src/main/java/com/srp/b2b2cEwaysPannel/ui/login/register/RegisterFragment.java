package com.srp.b2b2cEwaysPannel.ui.login.register;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.Observer;

import com.srp.b2b2cEwaysPannel.R;
import com.srp.b2b2cEwaysPannel.di.DI;
import com.srp.b2b2cEwaysPannel.model.login.registerwithotp.RegisterWithOTPResponse;
import com.srp.b2b2cEwaysPannel.ui.login.LoginViewModel;
import com.srp.b2b2cEwaysPannel.ui.main.MainActivity;
import com.srp.eways.di.DIMain;
import com.srp.eways.network.NetworkResponseCodes;
import com.srp.eways.ui.charge.buy.RadioOptionModel;
import com.srp.eways.ui.login.UserInfoViewModel;
import com.srp.eways.ui.navigation.NavigationMemberFragment;
import com.srp.eways.ui.view.NColumnRadioGroup;
import com.srp.eways.ui.view.button.ButtonElement;
import com.srp.eways.ui.view.dialog.ConfirmationDialog;
import com.srp.eways.ui.view.input.InputElement;
import com.srp.eways.util.CustomHintTypeFaceTextWatcher;
import com.srp.eways.util.PersianNumberFormatter;
import com.srp.eways.util.Utils;
import com.srp.eways.ui.charge.dialog.DialogContentView;

import ir.abmyapp.androidsdk.IABResources;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.srp.b2b2cEwaysPannel.ui.login.LoginRootIds.ROOT_OTP;


public class RegisterFragment extends NavigationMemberFragment<LoginViewModel> {

    private static final int PASSWORD_MIN_LENGTH = 8;
    private static final int NAME_MIN_LENGTH = 1;

    private UserInfoViewModel mUserInfoViewModel;

    private InputElement mNameInput;
    private InputElement mFamilyInput;
    private InputElement mGenderInput;
    private InputElement mPasswordInput;
    private InputElement mRePasswordInput;
    private InputElement mPresenterCodeInput;
    private AppCompatTextView mPasswordAlertText;
    private AppCompatTextView mNameErrorText;
    private AppCompatTextView mFamilyErrorText;

    private ButtonElement mRegisterButton;

    private IABResources abResources;

    private Typeface mNormalTypeface;
    private Typeface mHintTypeface;

    private ConfirmationDialog mGenderDialog;
    private GenderRadioGroup mGenderRadioGroup;
    private GenderItem.Item mGender = null;
    private GenderItem mGenderItem;

    private boolean mHasNameError = false;
    private boolean mHasFamilyError = false;
    private boolean mHasPasswordError = false;

    public static RegisterFragment getInstance() {
        return new RegisterFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mUserInfoViewModel = DI.getViewModel(UserInfoViewModel.class);

        mNameInput = view.findViewById(R.id.name_input);
        mFamilyInput = view.findViewById(R.id.family_input);
        mGenderInput = view.findViewById(R.id.gender_input);
        mPasswordInput = view.findViewById(R.id.password_input);
        mRePasswordInput = view.findViewById(R.id.re_password_input);
        mPresenterCodeInput = view.findViewById(R.id.presenter_code_input);
        mPasswordAlertText = view.findViewById(R.id.txt_password_alert);
        mNameErrorText = view.findViewById(R.id.name_error_text);
        mFamilyErrorText = view.findViewById(R.id.family_error_text);

        mRegisterButton = view.findViewById(R.id.register_button);

        abResources = DIMain.getABResources();

        mNormalTypeface = ResourcesCompat.getFont(view.getContext(), R.font.iran_yekan);
        mHintTypeface = ResourcesCompat.getFont(view.getContext(), R.font.iran_yekan_light);

        setupNameInput();
        setupFamilyInput();
        setupGenderInput();
        setupPasswordInput();
        setupRePasswordInput();
        setupPresenterCodeInput();
        setupRegisterButton();
        setupGenderDialog();

        mPasswordAlertText.setTextColor(abResources.getColor(R.color.login_register_password_alert_text_color));
        mPasswordAlertText.setTextSize(TypedValue.COMPLEX_UNIT_PX, abResources.getDimenPixelSize(R.dimen.login_register_password_alert_text_size));
        mPasswordAlertText.setTypeface(mNormalTypeface);
        mPasswordAlertText.setText(Utils.toPersianNumber(abResources.getString(R.string.login_register_password_alert_text)));
        mPasswordAlertText.setVisibility(View.INVISIBLE);

        mNameErrorText.setTextColor(abResources.getColor(R.color.login_error_color));
        mNameErrorText.setTextSize(TypedValue.COMPLEX_UNIT_PX, abResources.getDimenPixelSize(R.dimen.login_register_password_alert_text_size));
        mNameErrorText.setTypeface(mNormalTypeface);
        mNameErrorText.setText(Utils.toPersianNumber(abResources.getString(R.string.login_register_name_error_text)));
        mNameErrorText.setVisibility(View.INVISIBLE);

        mFamilyErrorText.setTextColor(abResources.getColor(R.color.login_error_color));
        mFamilyErrorText.setTextSize(TypedValue.COMPLEX_UNIT_PX, abResources.getDimenPixelSize(R.dimen.login_register_password_alert_text_size));
        mFamilyErrorText.setTypeface(mNormalTypeface);
        mFamilyErrorText.setText(Utils.toPersianNumber(abResources.getString(R.string.login_register_name_error_text)));
        mFamilyErrorText.setVisibility(View.INVISIBLE);

        getViewModel().getRegisterLoading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                if (isLoading) {
                    mRegisterButton.setLoadingVisibility(VISIBLE);
                    mRegisterButton.setIconVisibility(INVISIBLE);

                    mRegisterButton.setClickable(false);
                    mNameInput.setEnabled(false);
                    mFamilyInput.setEnabled(false);
                    mPasswordInput.setEnabled(false);
                    mRePasswordInput.setEnabled(false);
                    mPresenterCodeInput.setEnabled(false);
                    //todo set inputs disable
                } else {
                    mRegisterButton.setLoadingVisibility(INVISIBLE);
                    mRegisterButton.setIconVisibility(VISIBLE);

                    mRegisterButton.setClickable(true);
                    mNameInput.setEnabled(true);
                    mFamilyInput.setEnabled(true);
                    mPasswordInput.setEnabled(true);
                    mRePasswordInput.setEnabled(true);
                    mPresenterCodeInput.setEnabled(true);
                    //todo set inputs enable
                }
            }
        });
        getViewModel().getRegisterResponse().observe(this, new Observer<RegisterWithOTPResponse>() {
            @Override
            public void onChanged(RegisterWithOTPResponse registerResponse) {
                if (registerResponse != null) {
                    if (registerResponse.getStatus() == NetworkResponseCodes.SUCCESS) {
                        openMainActivity(registerResponse);
                    } else {
                        Toast.makeText(getContext(), registerResponse.getDescription(), Toast.LENGTH_LONG).show();
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

    private void openMainActivity(RegisterWithOTPResponse response) {
        mUserInfoViewModel.setUserInfo(response.getUserInfo());

        getViewModel().consumeAuthenticationResponse();
        getViewModel().consumeLoginResponse();
        getViewModel().consumeRegisterResponse();
        getViewModel().consumeVerifyResponse();

        startActivity(MainActivity.newIntent(getContext()));
        getActivity().finish();
    }

    private void setupNameInput() {


        mNameInput.setTextSize(abResources.getDimenPixelSize(R.dimen.login_register_input_elements_text_size));
        mNameInput.setTextColor(abResources.getColor(R.color.login_register_input_elements_text_color));
        mNameInput.setHintColor(abResources.getColor(R.color.login_register_input_elements_hint_color));
        mNameInput.setHint(abResources.getString(R.string.login_register_name_input_hint_text));
        mNameInput.setSelectedBackground(abResources.getDrawable(R.drawable.input_element_background_unselected));
        mNameInput.hasIcon(true);
        mNameInput.setIconDrawable(abResources.getDrawable(R.drawable.ic_name));
        mNameInput.setBackground(mNameInput.getSelectedBackground());
        mNameInput.setInputType(InputType.TYPE_CLASS_TEXT);
        mNameInput.getEditText().setMaxLines(1);
        mNameInput.getEditText().setText("");
        mNameInput.addTextChangeListener(new CustomHintTypeFaceTextWatcher(mNameInput.getEditText(), mNormalTypeface, mHintTypeface));
        mHasNameError = false;

        mNameInput.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mNameErrorText.getVisibility() == VISIBLE) {
                    mNameInput.setIconDrawable(abResources.getDrawable(R.drawable.ic_name));
                    mNameInput.setBackground(mNameInput.getSelectedBackground());

                    mNameErrorText.setVisibility(INVISIBLE);
                    mHasNameError = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                checkButtonEnableState();
            }
        });

    }

    private void setupFamilyInput() {

        mFamilyInput.setTextSize(abResources.getDimenPixelSize(R.dimen.login_register_input_elements_text_size));
        mFamilyInput.setTextColor(abResources.getColor(R.color.login_register_input_elements_text_color));
        mFamilyInput.setHintColor(abResources.getColor(R.color.login_register_input_elements_hint_color));
        mFamilyInput.setHint(abResources.getString(R.string.login_register_family_input_hint_text));
        mFamilyInput.setSelectedBackground(abResources.getDrawable(R.drawable.input_element_background_unselected));
        mFamilyInput.hasIcon(true);
        mFamilyInput.setIconDrawable(abResources.getDrawable(R.drawable.ic_name));
        mFamilyInput.setBackground(mFamilyInput.getSelectedBackground());
        mFamilyInput.setInputType(InputType.TYPE_CLASS_TEXT);
        mFamilyInput.getEditText().setMaxLines(1);
        mFamilyInput.getEditText().setText("");
        mFamilyInput.addTextChangeListener(new CustomHintTypeFaceTextWatcher(mFamilyInput.getEditText(), mNormalTypeface, mHintTypeface));
        mHasFamilyError = false;

        mFamilyInput.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mFamilyErrorText.getVisibility() == VISIBLE) {
                    mFamilyInput.setIconDrawable(abResources.getDrawable(R.drawable.ic_name));
                    mFamilyInput.setBackground(mFamilyInput.getSelectedBackground());

                    mFamilyErrorText.setVisibility(INVISIBLE);
                    mHasFamilyError = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                checkButtonEnableState();
            }
        });

    }

    private void setupGenderInput() {
        mGenderInput.setTextSize(abResources.getDimenPixelSize(R.dimen.login_register_input_elements_text_size));
        mGenderInput.setTextColor(abResources.getColor(R.color.login_register_input_elements_text_color));
        mGenderInput.setHintColor(abResources.getColor(R.color.login_register_input_elements_hint_color));
        mGenderInput.setHint(abResources.getString(R.string.login_register_gender_input_hint_text));
        mGenderInput.setSelectedBackground(abResources.getDrawable(R.drawable.input_element_background_unselected));
        mGenderInput.hasIcon(true);
        mGenderInput.setIconDrawable(abResources.getDrawable(R.drawable.ic_gender));
        mGenderInput.setBackground(mGenderInput.getSelectedBackground());
        mGenderInput.setInputType(InputType.TYPE_CLASS_TEXT);
        mGenderInput.getEditText().setMaxLines(1);
        mGenderInput.getEditText().setText("");
        mGenderInput.getEditText().setFocusable(false);
        mGenderInput.getEditText().setCursorVisible(false);
        mGenderInput.getEditText().setKeyListener(null);
        mGenderInput.addTextChangeListener(new CustomHintTypeFaceTextWatcher(mGenderInput.getEditText(), mNormalTypeface, mHintTypeface));
        mGenderInput.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mGenderDialog.show();
                mGenderDialog.isMatchWidth(false);

                if (mGenderInput.getText().isEmpty()) {
                    mGender = ((GenderItem.Item) mGenderItem.getRadioList().get(0).option);
                    mGenderRadioGroup.setSelectedRadioButton(0);
                } else {
                    mGenderRadioGroup.setSelectedRadioButton(mGenderItem.getGenderId(mGenderInput.getText()));
                }

                mGenderDialog.setButtonEnable(true);
            }
        });
    }

    private void setupGenderDialog() {

        mGenderDialog = new ConfirmationDialog(mGenderInput.getContext());

        mGenderRadioGroup = new GenderRadioGroup(mGenderInput.getContext());
        mGenderItem = new GenderItem();
        mGenderRadioGroup.setData(mGenderItem.getRadioList());

        mGenderDialog.setListener(new ConfirmationDialog.ConfirmationDialogItemClickListener() {
            @Override
            public void onConfirmClicked() {
                mGenderDialog.dismiss();

                mGenderInput.setText(mGender.getTitle());
            }

            @Override
            public void onCancelClicked() {

            }
        });

        DialogContentView dialogContentView = new DialogContentView(mGenderInput.getContext());
        dialogContentView.setIcon(abResources.getDrawable(R.drawable.ic_gender));
        dialogContentView.setChoiceTitle(abResources.getString(R.string.login_register_gender_dialog_title));
        dialogContentView.setChildContentView(mGenderRadioGroup);
        mGenderDialog.setChildContentView(dialogContentView);

        mGenderRadioGroup.setOnItemSelectedListener(new NColumnRadioGroup.RadioGroupListener() {
            @Override
            public void onItemSelected(int index, RadioOptionModel data) {
                mGenderDialog.setButtonEnable(true);

                mGender = ((GenderItem.Item) data.option);
            }

            @Override
            public void onItemRemoved(int index, RadioOptionModel removedData) {
            }
        });
    }

    private void setupPasswordInput() {
        mPasswordInput.setTextSize(abResources.getDimenPixelSize(R.dimen.login_register_input_elements_text_size));
        mPasswordInput.setTextColor(abResources.getColor(R.color.login_register_input_elements_text_color));
        mPasswordInput.setHintColor(abResources.getColor(R.color.login_register_input_elements_hint_color));
        mPasswordInput.setHint(abResources.getString(R.string.login_register_password_input_hint_text));
        mPasswordInput.setSelectedBackground(abResources.getDrawable(R.drawable.input_element_background_unselected));
        mPasswordInput.hasIcon(true);
        mPasswordInput.setIconDrawable(abResources.getDrawable(R.drawable.ic_password));
        mPasswordInput.setBackground(mPasswordInput.getSelectedBackground());
        mPasswordInput.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        mPasswordInput.getEditText().setMaxLines(1);
        mPasswordInput.getEditText().setText("");

        mHasPasswordError = false;

        mPasswordInput.addTextChangeListener(new CustomHintTypeFaceTextWatcher(mPasswordInput.getEditText(), mNormalTypeface, mHintTypeface));
        mPasswordInput.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    cleanPasswordError();

                    mPasswordAlertText.setVisibility(View.VISIBLE);
                    if (s.length() >= 8) {
                        mRePasswordInput.setEnabled(true);
                    } else {
                        mRePasswordInput.setText("");
                        mRePasswordInput.setEnabled(false);
                    }
                } else {
                    mPasswordAlertText.setVisibility(View.INVISIBLE);
                    mRePasswordInput.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                checkButtonEnableState();
            }
        });

    }

    private void setupRePasswordInput() {
        mRePasswordInput.setTextSize(abResources.getDimenPixelSize(R.dimen.login_register_input_elements_text_size));
        mRePasswordInput.setTextColor(abResources.getColor(R.color.login_register_input_elements_text_color));
        mRePasswordInput.setHintColor(abResources.getColor(R.color.login_register_input_elements_hint_color));
        mRePasswordInput.setHint(abResources.getString(R.string.login_register_re_pass_input_hint_text));
        mRePasswordInput.setSelectedBackground(abResources.getDrawable(R.drawable.input_element_background_unselected));
        mRePasswordInput.hasIcon(true);
        mRePasswordInput.setIconDrawable(abResources.getDrawable(R.drawable.ic_password));
        mRePasswordInput.setBackground(mRePasswordInput.getSelectedBackground());
        mRePasswordInput.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        mRePasswordInput.getEditText().setMaxLines(1);
        mRePasswordInput.getEditText().setText("");
        mRePasswordInput.setEnabled(false);

        mRePasswordInput.addTextChangeListener(new CustomHintTypeFaceTextWatcher(mRePasswordInput.getEditText(), mNormalTypeface, mHintTypeface));
        mRePasswordInput.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    cleanPasswordError();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                checkButtonEnableState();
            }
        });

    }

    private void setupPresenterCodeInput() {
        mPresenterCodeInput.setTextSize(abResources.getDimenPixelSize(R.dimen.login_register_input_elements_text_size));
        mPresenterCodeInput.setTextColor(abResources.getColor(R.color.login_register_input_elements_text_color));
        mPresenterCodeInput.setHintColor(abResources.getColor(R.color.login_register_input_elements_hint_color));
        mPresenterCodeInput.setHint(abResources.getString(R.string.login_register_presenter_code_input_hint_text));
        mPresenterCodeInput.setSelectedBackground(abResources.getDrawable(R.drawable.input_element_background_unselected));
        mPresenterCodeInput.hasIcon(true);
        mPresenterCodeInput.setIconDrawable(abResources.getDrawable(R.drawable.ic_referral_code));
        mPresenterCodeInput.setBackground(mPresenterCodeInput.getSelectedBackground());
        mPresenterCodeInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        mPresenterCodeInput.getEditText().setMaxLines(1);
        mPresenterCodeInput.getEditText().setText("");

        mPresenterCodeInput.addTextChangeListener(new PersianNumberFormatter());
        mPresenterCodeInput.addTextChangeListener(new CustomHintTypeFaceTextWatcher(mPresenterCodeInput.getEditText(), mNormalTypeface, mHintTypeface));

    }

    private void setupRegisterButton() {

        final IABResources abResources = DIMain.getABResources();

        mRegisterButton.setTextSize(abResources.getDimenPixelSize(R.dimen.login_register_button_text_size));
        mRegisterButton.setTextColor(abResources.getColor(R.color.login_register_button_text_color));
        mRegisterButton.setEnabledBackground(abResources.getDrawable(R.drawable.login_button_background_enabled));
        mRegisterButton.setDisableBackground(abResources.getDrawable(R.drawable.login_button_background_disabled));
        mRegisterButton.setLoadingVisibility(View.GONE);
        mRegisterButton.setLoadingColorFilter(abResources.getColor(R.color.login_register_button_text_color));
        mRegisterButton.setEnable(false);
        mRegisterButton.setText(abResources.getString(R.string.login_register_button_text));
        mRegisterButton.hasIcon(true);
        mRegisterButton.setIconVisibility(View.VISIBLE);
        mRegisterButton.setIcon(abResources.getDrawable(R.drawable.ic_sign_in));
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(mPasswordInput.getText().equals(mRePasswordInput.getText()))) {
                    mPasswordInput.setBackground(abResources.getDrawable(R.drawable.login_input_error_background));
                    mRePasswordInput.setBackground(abResources.getDrawable(R.drawable.login_input_error_background));

                    mPasswordAlertText.setTextColor(abResources.getColor(R.color.login_input_error_color));
                    mPasswordAlertText.setText(abResources.getString(R.string.login_register_password_error));

                    mPasswordInput.setIconDrawable(abResources.getDrawable(R.drawable.ic_error_password));
                    mRePasswordInput.setIconDrawable(abResources.getDrawable(R.drawable.ic_error_password));

                    mHasPasswordError = true;
                }

                if (mNameInput.getText().length() < 2) {
                    mNameInput.setIconDrawable(abResources.getDrawable(R.drawable.ic_name_error));
                    mNameInput.setBackground(abResources.getDrawable(R.drawable.login_input_error_background));

                    mNameErrorText.setVisibility(VISIBLE);

                    mHasNameError = true;
                }


                if (mFamilyInput.getText().length() < 2) {
                    mFamilyInput.setIconDrawable(abResources.getDrawable(R.drawable.ic_name_error));
                    mFamilyInput.setBackground(abResources.getDrawable(R.drawable.login_input_error_background));

                    mFamilyErrorText.setVisibility(VISIBLE);

                    mHasFamilyError = true;
                }
                if (!mHasNameError && !mHasFamilyError && !mHasPasswordError) {
                    getViewModel().registerWithOTP(
                            getViewModel().getToken(),
                            getViewModel().getPhoneNumber(),
                            mNameInput.getText(),
                            mFamilyInput.getText(),
                            mPasswordInput.getText(),
                            (mPresenterCodeInput.getText().length() > 0 ? Integer.valueOf(mPresenterCodeInput.getText()) : 0),
                            mGenderItem.getGenderId(mGenderInput.getText()));
                }
            }

        });


    }

    private void cleanPasswordError() {
        mHasPasswordError = false;

        mRePasswordInput.setBackground(mRePasswordInput.getSelectedBackground());
        mRePasswordInput.setIconDrawable(abResources.getDrawable(R.drawable.ic_password));

        mPasswordInput.setBackground(mPasswordInput.getSelectedBackground());
        mPasswordInput.setIconDrawable(abResources.getDrawable(R.drawable.ic_password));

        mPasswordAlertText.setTextColor(abResources.getColor(R.color.login_register_password_alert_text_color));
        mPasswordAlertText.setText(Utils.toPersianNumber(abResources.getString(R.string.login_register_password_alert_text)));

    }

    private void checkButtonEnableState() {
        if (mRePasswordInput.getText().length() >= PASSWORD_MIN_LENGTH && mPasswordInput.getText().length() >= PASSWORD_MIN_LENGTH &&
                (mNameInput.getText().length() >= NAME_MIN_LENGTH)) {
            mRegisterButton.setEnable(true);
        } else {
            mRegisterButton.setEnable(false);
        }
    }

    @Override
    public LoginViewModel acquireViewModel() {
        return DI.getViewModel(LoginViewModel.class);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_register;
    }

    @Override
    public boolean onBackPress() {
        onSwitchRoot(ROOT_OTP);

        return true;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            setupNameInput();
            setupFamilyInput();
            setupGenderInput();
            setupPasswordInput();
            setupRePasswordInput();
            setupPresenterCodeInput();
            setupRegisterButton();
            setupGenderDialog();
        }
    }
}
