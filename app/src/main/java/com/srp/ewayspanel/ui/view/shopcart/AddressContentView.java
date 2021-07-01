package com.srp.ewayspanel.ui.view.shopcart;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.srp.ewayspanel.R;
import com.srp.ewayspanel.di.DI;
import com.srp.ewayspanel.model.login.Address;

import ir.abmyapp.androidsdk.IABResources;

import static android.view.MotionEvent.ACTION_BUTTON_RELEASE;
import static android.view.MotionEvent.ACTION_UP;

public class AddressContentView extends LinearLayout {

    public interface ItemClickListener {
        void onProvinceClicked();

        void onCityClicked();
    }

    private AddressFieldView mAddressFieldTitle;
    private AddressFieldView mAddressFieldReceiverName;
    private AddressFieldView mAddressFieldFixNumber;
    private AddressFieldView mAddressFieldMobileNumber;
    private AddressFieldView mAddressFieldPostalCode;
    private AddressFieldView mAddressFieldProvince;
    private AddressFieldView mAddressFieldCity;

    private int mAddressId = -1;

    private EditText mEditAddress;
    private String mDefaultError;

    private ItemClickListener mItemClickListener = null;

    public AddressContentView(Context context) {
        super(context);

        initialize(context, null, 0);
    }

    public AddressContentView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        initialize(context, attrs, 0);
    }

    public AddressContentView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initialize(context, attrs, defStyleAttr);
    }

    private void initialize(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        LayoutInflater.from(context).inflate(R.layout.add_address_contentview, this, true);

        Resources resources = getResources();

        IABResources abResources = DI.getABResources();

        mDefaultError = abResources.getString(R.string.addressdialog_empty_error);

        mAddressFieldTitle = findViewById(R.id.addressfield_title);
        mAddressFieldTitle.setMaxLines(1);

        mAddressFieldReceiverName = findViewById(R.id.addressfield_receivername);
        mAddressFieldReceiverName.setMaxLines(1);

        mAddressFieldFixNumber = findViewById(R.id.addressfield_fixnumber);
        mAddressFieldFixNumber.setInputType(InputType.TYPE_CLASS_NUMBER);
        mAddressFieldFixNumber.setMaxLength(11);

        mAddressFieldMobileNumber = findViewById(R.id.addressfield_mobilenumber);
        mAddressFieldMobileNumber.setInputType(InputType.TYPE_CLASS_NUMBER);
        mAddressFieldMobileNumber.setMaxLength(11);

        mAddressFieldPostalCode = findViewById(R.id.addressfield_postalcode);
        mAddressFieldPostalCode.setInputType(InputType.TYPE_CLASS_NUMBER);
        mAddressFieldPostalCode.setMaxLength(10);

        mAddressFieldProvince = findViewById(R.id.addressfield_province);
        mAddressFieldProvince.setMaxLines(1);
        mAddressFieldProvince.getEditTextValue().setInputType(InputType.TYPE_NULL);
        mAddressFieldProvince.getEditTextValue().setTextIsSelectable(true);
        mAddressFieldProvince.getEditTextValue().setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (mItemClickListener != null && motionEvent.getAction() == ACTION_UP) {
                    mItemClickListener.onProvinceClicked();
                }
                return true;
            }
        });

        mAddressFieldCity = findViewById(R.id.addressfield_city);
        mAddressFieldCity.setMaxLines(1);
        mAddressFieldCity.getEditTextValue().setInputType(InputType.TYPE_NULL);
        mAddressFieldCity.getEditTextValue().setTextIsSelectable(true);
        mAddressFieldCity.getEditTextValue().setOnTouchListener(new OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (mItemClickListener != null && motionEvent.getAction() == ACTION_UP) {
                    mItemClickListener.onCityClicked();
                }
                return true;
            }

        });

        mEditAddress = findViewById(R.id.edit_address_value);

        mAddressFieldTitle.setTitle(resources.getString(R.string.addressdialog_addressname_title));
        mAddressFieldReceiverName.setTitle(resources.getString(R.string.addressdialog_receivername_title));
        mAddressFieldFixNumber.setTitle(resources.getString(R.string.addressdialog_fixname_title));
        mAddressFieldMobileNumber.setTitle(resources.getString(R.string.addressdialog_phonenumber_title));
        mAddressFieldPostalCode.setTitle(resources.getString(R.string.addressdialog_postalcode_title));
        mAddressFieldProvince.setTitle(resources.getString(R.string.addressdialog_province_title));
        mAddressFieldCity.setTitle(resources.getString(R.string.addressdialog_city_title));
    }

    public Address getAddress() {
        Address address = new Address();

        if (isDataCompelet()) {
            address.setAddressName(mAddressFieldTitle.getValue());
            address.setAddress(String.valueOf(mEditAddress.getText()));
            address.setPhoneNumber(mAddressFieldFixNumber.getValue());
            address.setMobile(mAddressFieldMobileNumber.getValue());
            address.setPostCode(mAddressFieldPostalCode.getValue());
            address.setFullName(mAddressFieldReceiverName.getValue());
            address.setStateName(mAddressFieldProvince.getValue());
            address.setCityName(mAddressFieldCity.getValue());

            if (mAddressId != -1) {
                address.setAddressId(mAddressId);
                mAddressId = -1;
            }

            return address;
        }

        return null;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public void setAddress(Address address) {

        if (address != null) {
            mAddressFieldTitle.setValue(address.getAddressName());
            mEditAddress.setText(address.getAddress());
            mAddressFieldFixNumber.setValue(address.getPhoneNumber());
            mAddressFieldMobileNumber.setValue(address.getMobile());
            mAddressFieldPostalCode.setValue(address.getPostCode());
            mAddressFieldReceiverName.setValue(address.getFullName());
            mAddressFieldProvince.setValue(address.getStateName());
            mAddressFieldCity.setValue(address.getCityName());
            mAddressId = address.getAddressId();
        }

    }

    public void setProvince(String province) {
        mAddressFieldProvince.setValue(province);
    }

    public void setCity(String city) {
        mAddressFieldCity.setValue(city);
    }

    private boolean isDataCompelet() {
        if (mAddressFieldTitle.getValue().isEmpty()) {
            mAddressFieldTitle.setError();
            return false;
        }
        if (mAddressFieldReceiverName.getValue().isEmpty()) {
            mAddressFieldReceiverName.setError();
            return false;
        }
        if (mAddressFieldFixNumber.getValue().isEmpty() || mAddressFieldFixNumber.getValue().length() < 4) {
            mAddressFieldFixNumber.setError();
            return false;
        }
        if (mAddressFieldMobileNumber.getValue().isEmpty()) {
            mAddressFieldMobileNumber.setError();
            return false;
        }
        if (mAddressFieldMobileNumber.getValue().length() < 11 || !mAddressFieldMobileNumber.getValue().startsWith("09")) {
            mAddressFieldMobileNumber.setError(DI.getABResources().getString(R.string.input_phonenumber_error));
            return false;
        }
        if (mAddressFieldPostalCode.getValue().isEmpty()) {
            mAddressFieldPostalCode.setError();
            return false;
        }
        if (mAddressFieldPostalCode.getValue().length() < 10) {
            mAddressFieldPostalCode.setError(DI.getABResources().getString(R.string.postal_code_error));
            return false;
        }
        if (mAddressFieldProvince.getValue().isEmpty()) {
            mAddressFieldProvince.setError();
            return false;
        }
        if (mAddressFieldCity.getValue().isEmpty()) {
            mAddressFieldCity.setError();
            return false;
        }
        if (String.valueOf(mEditAddress.getText()).isEmpty()) {
            mEditAddress.setError(mDefaultError);
            return false;
        }
        return true;
    }
}
