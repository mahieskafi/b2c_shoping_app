package com.srp.eways.ui.view.charge;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.res.ResourcesCompat;

import com.srp.eways.R;
import com.srp.eways.di.DIMain;
import com.srp.eways.model.phonebook.UserPhoneBook;
import com.srp.eways.ui.charge.model.IOperator;
import com.srp.eways.ui.view.charge.a.OperatorsViewA;
import com.srp.eways.ui.view.charge.b.OperatorsViewB;
import com.srp.eways.ui.view.input.InputElement;
import com.srp.eways.util.Utils;
import com.yashoid.inputformatter.InputFormatter;

import ir.abmyapp.androidsdk.IABResources;

import static android.text.InputType.TYPE_CLASS_NUMBER;

public class PhoneAndOperatorsView extends LinearLayout implements PhoneNumberView.PhoneNumberViewListener, OperatorsViewA.OperatorsViewListener {

    public interface PhoneAndOperatorsViewListener {

        void onUserPhoneBookClicked();

        void onMobilePhoneBookClicked();

        void onPhoneNumberChanged(String phoneNumber);

        void onOperatorLoadAnimationEnded();

        void onOperatorSelected(IOperator operator);

        void onRemovePhoneNumberClicked();

        void onEditTextGotFocus();

    }

    private View mInputContainer;
    private PhoneNumberView mPhoneNumberView;
    private IOperatorsView mOperatorsView;
    private InputElement mInputPhone;

    AppCompatImageView mImageSelectEwaysContact, mImageSelectContact;

    private String mPhoneNumber;
    private IOperator mOperator;

    private String mPhoneInputErrorText;

    private PhoneAndOperatorsViewListener mListener;

    private boolean mPreventNotify = false;
    private boolean mLoadedBefore = false;

    public PhoneAndOperatorsView(Context context) {
        super(context);

        initialize(context, null, 0);
    }

    public PhoneAndOperatorsView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        initialize(context, attrs, 0);
    }

    public PhoneAndOperatorsView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initialize(context, attrs, defStyleAttr);
    }

    private void initialize(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        setOrientation(VERTICAL);
        setClipChildren(false);
        setClipToPadding(false);

        mInputContainer = LayoutInflater.from(context).inflate(R.layout.charge_phonenumber_container, this, false);

        mInputPhone = mInputContainer.findViewById(R.id.input_phonenumber);
        mImageSelectEwaysContact = mInputContainer.findViewById(R.id.image_select_eways_contact);
        mImageSelectContact = mInputContainer.findViewById(R.id.image_select_contact);

        IABResources abResources = DIMain.getABResources();

        mPhoneInputErrorText = DIMain.getABResources().getString(R.string.input_phonenumber_error);

        mImageSelectEwaysContact.setImageDrawable(abResources.getDrawable(R.drawable.ic_select_eways_contacts));
        mImageSelectEwaysContact.setBackgroundDrawable(abResources.getDrawable(R.drawable.phone_and_operator_select_contact_background));

        mImageSelectContact.setImageDrawable(abResources.getDrawable(R.drawable.ic_select_phone_contacts));
        mImageSelectContact.setBackgroundDrawable(abResources.getDrawable(R.drawable.phone_and_operator_select_contact_background));

        int imageSelectContactSize = abResources.getDimenPixelSize(R.dimen.buycharge_selectcontact_image_size);
        int imageSelectContactMarginRight = abResources.getDimenPixelSize(R.dimen.buycharge_selectcontact_image_margin_right);

        FrameLayout.LayoutParams imageSelectContactLP = (FrameLayout.LayoutParams) mImageSelectEwaysContact.getLayoutParams();
        imageSelectContactLP.height = imageSelectContactSize;
        imageSelectContactLP.width = imageSelectContactSize;
//        imageSelectContactLP.rightMargin = imageSelectContactMarginRight;

        mImageSelectEwaysContact.setLayoutParams(imageSelectContactLP);

        LinearLayout inputContainer = new LinearLayout(context);
        inputContainer.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        mInputPhone.setIconDrawable(abResources.getDrawable(R.drawable.ic_mobile));
        mInputPhone.setTextSize(abResources.getDimenPixelSize(R.dimen.buycharge_input_phonenumber_text_size));
        mInputPhone.setTextColor(abResources.getColor(R.color.buycharge_input_phonenumber_text_color));
        mInputPhone.setHintColor(abResources.getColor(R.color.buycharge_input_phonenumber_hint_color));
        mInputPhone.setHint(abResources.getString(R.string.buycharge_input_phonenumber_hint));
        mInputPhone.setUnSelectedBackground(abResources.getDrawable(R.drawable.phone_and_operator_input_background_unselected));
        mInputPhone.setSelectedBackground(abResources.getDrawable(R.drawable.phone_and_operator_input_background_selected));
        mInputPhone.hasIcon(abResources.getBoolean(R.bool.buycharge_input_phonenumber_has_clear));
        mInputPhone.setCancelIcon(abResources.getDrawable(R.drawable.ic_survey_close));
        mInputPhone.setBackground(mInputPhone.getUnselectedBackground());
        mInputPhone.setInputType(TYPE_CLASS_NUMBER);
        mInputPhone.getEditText().setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});
        mInputPhone.setTypeFace(ResourcesCompat.getFont(context, R.font.iran_yekan));
        mInputPhone.setImeOption(EditorInfo.IME_ACTION_DONE);
        mInputPhone.addTextChangeListener(new InputFormatter(Utils.PersianNumberFormatter));
        mInputPhone.getEditText().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onEditTextGotFocus();
                }
            }
        });
        mInputPhone.getEditText().setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    mInputPhone.getEditText().performClick();
                }
            }
        });


        LinearLayout.LayoutParams inputPhoneViewLayoutParams = (LayoutParams) mInputPhone.getLayoutParams();
        inputPhoneViewLayoutParams.height = abResources.getDimenPixelSize(R.dimen.buycharge_input_phonenumber_height);

        mInputPhone.setLayoutParams(inputPhoneViewLayoutParams);

//        ViewCompat.setElevation(mInputPhone, abResources.getDimen(R.dimen.buycharge_input_phonenumber_elevation));
//        ViewCompat.setElevation(mImageSelectContact, abResources.getDimen(R.dimen.buycharge_input_phonenumber_elevation));

        int mDesignModel = DIMain.getABResources().getInt(R.integer.abtesting_designmodel);
        if (mDesignModel == 0) {//A
            mOperatorsView = new OperatorsViewA(context);
        } else {//B
            mOperatorsView = new OperatorsViewB(context);
        }

        LinearLayout.LayoutParams operatorsViewLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        operatorsViewLayoutParams.topMargin = abResources.getDimenPixelSize(R.dimen.buycharge_chargelevelview_margin_top);

        mOperatorsView.setLayoutParams(operatorsViewLayoutParams);

        mPhoneNumberView = new PhoneNumberView(context);
        LinearLayout.LayoutParams phoneViewViewLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        mPhoneNumberView.setLayoutParams(phoneViewViewLayoutParams);

        addView(mInputContainer);
        addView(mPhoneNumberView);
        addView(mOperatorsView);

        mInputContainer.setVisibility(VISIBLE);
        mPhoneNumberView.setVisibility(GONE);
        mOperatorsView.setVisibility(GONE);

        mPhoneNumberView.setPhoneNumberViewListener(this);
        mOperatorsView.setListener(this);

        mInputPhone.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                mPhoneNumber = s.toString();

                if (mPhoneNumber.length() > 11) {
                    mInputPhone.setError(mPhoneInputErrorText);
                    mInputContainer.setVisibility(VISIBLE);
                    mOperatorsView.setVisibility(GONE);
                    mListener.onRemovePhoneNumberClicked();
                    return;
                }

                if (TextUtils.isEmpty(mPhoneNumber) && mListener != null) {
//                    mInputPhone.setError("");
                    mInputContainer.setVisibility(VISIBLE);
                    mOperatorsView.setVisibility(GONE);
                    mListener.onRemovePhoneNumberClicked();
                    return;
                }

                if (!Utils.isPhoneNumberLikeString(mPhoneNumber)) {
                    mInputPhone.setError(mPhoneInputErrorText);
                    mInputContainer.setVisibility(VISIBLE);
                    mOperatorsView.setVisibility(GONE);
                    mListener.onRemovePhoneNumberClicked();
                    return;
                }

                if (mListener != null && !mPreventNotify) {
                    if (mPhoneNumber.length() == 11) {
                        mInputPhone.getEditText().setError(null);
                        mListener.onPhoneNumberChanged(mPhoneNumber);
                    } else {

                        mInputContainer.setVisibility(VISIBLE);
                        mOperatorsView.setVisibility(GONE);

                        mListener.onPhoneNumberChanged(mPhoneNumber);
                    }
                }
            }
        });

        mImageSelectEwaysContact.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onUserPhoneBookClicked();
                }
            }
        });

        mImageSelectContact.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onMobilePhoneBookClicked();
                }
            }
        });


    }

    public boolean setContactIconVisibility(int visibility) {

        if (mImageSelectContact != null) {

            mImageSelectEwaysContact.setVisibility(visibility);

            return true;
        } else {
            return false;
        }

    }

    public void disableEditText(Drawable background) {
        mInputPhone.setBackground(background);
        mInputPhone.getEditText().setCursorVisible(false);
        mInputPhone.getEditText().setText("");
        mInputPhone.getEditText().setHint("");
        mInputPhone.getEditText().setEnabled(false);
    }

    public void enableEditText() {
        mInputPhone.setBackground(mInputPhone.getSelectedBackground());
        mInputPhone.getEditText().setCursorVisible(true);
        mInputPhone.getEditText().setText("");
        mInputPhone.getEditText().requestFocus();
        mInputPhone.getEditText().setHint(DIMain.getABResources().getString(R.string.buycharge_input_phonenumber_hint));
        mInputPhone.getEditText().setEnabled(true);
    }

    public void setContactInfo(UserPhoneBook contactInfo) {
        mPhoneNumberView.setContactInfo(contactInfo);

//        if(mLoadedBefore){
//            mLoadedBefore = false;
//        }
//        else {

        mPreventNotify = true;

        if (contactInfo == null) {
            if (mPhoneNumber != null) {
                mInputPhone.setText(mPhoneNumber);
            }
        } else {
            mInputPhone.setText(contactInfo.getCellPhone());
        }

        mPreventNotify = false;
//        }
    }

    public void setListener(PhoneAndOperatorsViewListener listener) {
        mListener = listener;
    }

    public void setOperator(IOperator operator, boolean animate) {
        mOperator = operator;

        if (mOperator == null) {
            mInputContainer.setVisibility(VISIBLE);

            mPhoneNumberView.setVisibility(GONE);
            mOperatorsView.setVisibility(GONE);
        } else {
            mInputContainer.setVisibility(VISIBLE);
            mInputContainer.requestFocus();

//            mPhoneNumberView.setNumber(mPaymentNumber);
            mOperatorsView.setData(mPhoneNumber, mOperator, animate);

            mPhoneNumberView.setVisibility(GONE);
            mOperatorsView.setVisibility(VISIBLE);
        }
    }

    public void setPhoneNumber(String phoneNumber) {
        mInputPhone.setText(phoneNumber);
    }

    public IOperator getOperator() {
        return mOperator;
    }

    public void makeFocus() {

        mInputPhone.makeFocus();
    }

    @Override
    public void onRemovePhoneNumberClicked() {
        if (mListener != null) {
            mInputContainer.setVisibility(VISIBLE);
            mOperatorsView.setVisibility(GONE);
            mListener.onRemovePhoneNumberClicked();
        }
    }

    @Override
    public void onCancelOperator() {
        if (mListener != null) {
            mInputContainer.setVisibility(VISIBLE);
            mOperatorsView.setVisibility(GONE);
            mInputPhone.setText("");
        }
    }

    @Override
    public void onOperatorLoadAnimationEnded() {
        if (mListener != null) {
            mListener.onOperatorLoadAnimationEnded();
        }
    }

    @Override
    public void onTransportableOperatorSelected(IOperator transportedOperator) {
        if (mListener != null) {
            mListener.onOperatorSelected(transportedOperator);
        }
    }

    public void setErrorVisible(Boolean isVisible){
        if (!isVisible){
            mInputPhone.setError(null);
        }
    }

}
