package com.srp.eways.ui.phonebook.ewayscontact.addcontact;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.srp.eways.R;
import com.srp.eways.base.BaseViewModel;
import com.srp.eways.di.DIMain;
import com.srp.eways.model.phonebook.AddOrRemovePhoneBookResponse;
import com.srp.eways.model.phonebook.UserPhoneBook;
import com.srp.eways.network.NetworkResponseCodes;
import com.srp.eways.ui.navigation.NavigationMemberFragment;
import com.srp.eways.ui.phonebook.PhoneBookViewModel;
import com.srp.eways.ui.view.button.ButtonElement;
import com.srp.eways.ui.view.input.InputElement;
import com.srp.eways.util.Utils;
import com.yashoid.inputformatter.InputFormatter;

import ir.abmyapp.androidsdk.IABResources;

import static android.text.InputType.TYPE_CLASS_NUMBER;

public class AddContactFragment extends NavigationMemberFragment<BaseViewModel> {

    //private FormFieldView test;
    private InputElement mName;
    private InputElement mFamily;
    private InputElement mNumber;
    private InputElement mCredit;
    private EditText mDescription;

    private final boolean[] check = {false ,false, false};

    private ButtonElement mButtonConfirm;
    private ButtonElement mButtonCancel;

    private PhoneBookViewModel mUserPhoneBookViewModel;
    private Observer<AddOrRemovePhoneBookResponse> mPhoneBookAddContactLiveData;

    private UserPhoneBook mEditableUserPhoneBook = null;

    public static AddContactFragment newInstance() {
        return new AddContactFragment();
    }

    @Override
    public BaseViewModel acquireViewModel() {
        return mUserPhoneBookViewModel = DIMain.getViewModel(PhoneBookViewModel.class);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_addcontact;
    }

    @Override
    public void onViewCreated(@NonNull final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mName = view.findViewById(R.id.input_name);
        mFamily = view.findViewById(R.id.input_family);
        mNumber = view.findViewById(R.id.input_number);
        mCredit = view.findViewById(R.id.input_credit);
        mDescription = view.findViewById(R.id.edit_description_value);
        mButtonConfirm = view.findViewById(R.id.button_confirm);
        mButtonCancel = view.findViewById(R.id.button_cancel);

        mButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
                Utils.hideKeyboard(getActivity());
            }
        });


        getAttrWithAB();
        isFormCompelet();
        onConfirmClick();

        mPhoneBookAddContactLiveData = new Observer<AddOrRemovePhoneBookResponse>() {
            @Override
            public void onChanged(AddOrRemovePhoneBookResponse addPhoneBookResponse) {
                if (addPhoneBookResponse != null) {
                    mButtonConfirm.setLoadingVisibility(View.INVISIBLE);

                    if (addPhoneBookResponse.getStatus() == NetworkResponseCodes.SUCCESS) {
                        mEditableUserPhoneBook = null;
                        Toast.makeText(DIMain.getContext(), "اطلاعات با موفقیت ذخیره شد", Toast.LENGTH_SHORT).show();
                        cleanForm();

                        mUserPhoneBookViewModel.setIsListResetLiveData(true);
                    } else {
                        Toast.makeText(getContext(), addPhoneBookResponse.getDescription(), Toast.LENGTH_SHORT).show();
                    }
                    mUserPhoneBookViewModel.consumedPhoneBookAddContactLiveData();
                }

            }
        };

//        mUserPhoneBookViewModel.getEditableUserPhoneBookLiveData().observe(this, new Observer<UserPhoneBook>() {
//            @Override
//            public void onChanged(UserPhoneBook userPhoneBook) {
//                if (userPhoneBook != null) {
//
//                    mEditableUserPhoneBook = userPhoneBook;
//
//                    mName.setText(userPhoneBook.getFirstName());
//                    mFamily.setText(userPhoneBook.getLastName());
//                    mNumber.setText(Utils.toPersianNumber(userPhoneBook.getCellPhone()));
//                    mCredit.setText(Utils.toPersianNumber(userPhoneBook.getDebt()));
//                    if (userPhoneBook.getDescription() != null && !(userPhoneBook.getDescription().isEmpty())) {
//                        mDescription.setText(userPhoneBook.getDescription());
//                    }
//
//                    mUserPhoneBookViewModel.setEditableUserPhoneBookLiveData(null);
//                }
//            }
//        });
    }

    private void onConfirmClick() {

        mButtonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mButtonConfirm.setLoadingVisibility(View.VISIBLE);
                Utils.hideKeyboard(getActivity());
                mUserPhoneBookViewModel.addUserPhoneBook(getContactInfo());
                mUserPhoneBookViewModel.getPhoneBookAddContactLiveData().observe(getViewLifecycleOwner(), mPhoneBookAddContactLiveData);
            }
        });
    }

    public UserPhoneBook getContactInfo() {

        if (mEditableUserPhoneBook == null) {

            mName.setText(mName.getText());
            mFamily.setText(mFamily.getText());
            mNumber.setText(Utils.toEnglishNumber(mNumber.getText()));
            mCredit.setText(mCredit.getText());
            if (mCredit.getText().isEmpty()){
                mCredit.setText("0");
            }
            mDescription.setText(String.valueOf(mDescription.getText()));

            return new UserPhoneBook(mName.getText(), mFamily.getText(), mNumber.getText(), Long.parseLong(mCredit.getText()), mDescription.getText().toString());
        } else {
            mEditableUserPhoneBook.setFirstName(mName.getText());
            mEditableUserPhoneBook.setLastName(mFamily.getText());
            mEditableUserPhoneBook.setCellPhone(Utils.toEnglishNumber(mNumber.getText()));
            mEditableUserPhoneBook.setDebt(Long.parseLong(mCredit.getText()));
            mEditableUserPhoneBook.setDescription(mDescription.getText().toString());

            return mEditableUserPhoneBook;
        }

    }

    public void isFormCompelet() {

        mName.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().isEmpty()) {
                    check[0] = true;
                    settButtonEnable();
                } else {
                    check[0] = false;
                    settButtonEnable();
                }
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        mFamily.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().isEmpty()) {
                    check[1] = true;
                    settButtonEnable();
                } else {
                    check[1] = false;
                    settButtonEnable();
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        mNumber.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length()==11) {
                    check[2] = true;
                    settButtonEnable();
                } else {
                    check[2] = false;
                    settButtonEnable();
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

    }

    public void settButtonEnable() {
        if (check[0] && check[1] && check[2]) {
            mButtonConfirm.setEnable(true);
        } else {
            mButtonConfirm.setEnable(false);
        }
    }

    public void cleanForm(){
        mName.setText("");
        mFamily.setText("");
        mNumber.setText("");
        mCredit.setText("");
        mDescription.getText().clear();
    }


    private void getAttrWithAB() {

        IABResources AB = DIMain.getABResources();


        //name
        mName.setTextSize(AB.getDimenPixelSize(R.dimen.add_eways_contact_form_inputtext_size));
        mName.setTextColor(AB.getColor(R.color.add_eways_contact_form_inputtext_color));
        mName.setUnSelectedBackground(AB.getDrawable(R.drawable.phone_and_operator_input_background_unselected));
        mName.setSelectedBackground(AB.getDrawable(R.drawable.phone_and_operator_input_background_unselected));
        mName.setBackground(mName.getUnselectedBackground());

        //family
        mFamily.setTextSize(AB.getDimenPixelSize(R.dimen.add_eways_contact_form_inputtext_size));
        mFamily.setTextColor(AB.getColor(R.color.add_eways_contact_form_inputtext_color));
        mFamily.setUnSelectedBackground(AB.getDrawable(R.drawable.phone_and_operator_input_background_unselected));
        mFamily.setSelectedBackground(AB.getDrawable(R.drawable.phone_and_operator_input_background_unselected));
        mFamily.setBackground(mFamily.getUnselectedBackground());

        //number
        mNumber.setTextSize(AB.getDimenPixelSize(R.dimen.add_eways_contact_form_inputtext_size));
        mNumber.setTextColor(AB.getColor(R.color.add_eways_contact_form_inputtext_color));
        mNumber.setUnSelectedBackground(AB.getDrawable(R.drawable.phone_and_operator_input_background_unselected));
        mNumber.setSelectedBackground(AB.getDrawable(R.drawable.phone_and_operator_input_background_unselected));
        mNumber.setBackground(mNumber.getUnselectedBackground());
        mNumber.addTextChangeListener(new InputFormatter(Utils.PersianNumberFormatter));
        mNumber.setInputType(TYPE_CLASS_NUMBER);
        mNumber.getEditText().setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});

        //Credit
        mCredit.setTextSize(AB.getDimenPixelSize(R.dimen.add_eways_contact_form_inputtext_size));
        mCredit.setTextColor(AB.getColor(R.color.add_eways_contact_form_inputtext_color));
        mCredit.setUnSelectedBackground(AB.getDrawable(R.drawable.phone_and_operator_input_background_unselected));
        mCredit.setSelectedBackground(AB.getDrawable(R.drawable.phone_and_operator_input_background_unselected));
        mCredit.setBackground(mCredit.getUnselectedBackground());
        mCredit.addTextChangeListener(new InputFormatter(Utils.PersianNumberFormatter));
        mCredit.setInputType(TYPE_CLASS_NUMBER);


        mButtonConfirm.setText(AB.getString(R.string.add_eways_contact_form_confirm_button));
        mButtonConfirm.setTextSize(AB.getDimenPixelSize(R.dimen.add_eways_contact_form_inputtext_size));
        mButtonConfirm.setTextColor(AB.getColor(R.color.add_eways_contact_form_confirm_button));
        mButtonConfirm.setEnabledBackground(AB.getDrawable(R.drawable.button_element_default_background_enabled));
        mButtonConfirm.setDisableBackground(AB.getDrawable(R.drawable.button_element_default_background_disabled));
        mButtonConfirm.setLoadingColorFilter(AB.getColor(R.color.add_eways_contact_form_confirm_button));
        mButtonConfirm.setLoadingColorFilter(AB.getColor(R.color.add_eways_contact_form_confirm_button));
        mButtonConfirm.hasIcon(false);
        mButtonConfirm.setEnable(false);

        mButtonCancel.setText(AB.getString(R.string.add_eways_contact_form_cancel_button));
        mButtonCancel.setTextSize(AB.getDimenPixelSize(R.dimen.add_eways_contact_form_inputtext_size));
        mButtonCancel.setTextColor(AB.getColor(R.color.add_eways_contact_form_cancel_button));
        mButtonCancel.setEnabledBackground(AB.getDrawable(R.drawable.bill_inquiry_detail_save_background_enabled));
        mButtonCancel.setDisableBackground(AB.getDrawable(R.drawable.button_element_default_background_disabled));
        mButtonCancel.setLoadingColorFilter(AB.getColor(R.color.add_eways_contact_form_cancel_button));
        mButtonCancel.hasIcon(false);
        mButtonCancel.setEnable(true);

    }


}
