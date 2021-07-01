package com.srp.ewayspanel.ui.view.shopcart;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import androidx.core.content.res.ResourcesCompat;

import com.srp.ewayspanel.R;
import com.srp.ewayspanel.di.DI;
import com.srp.ewayspanel.ui.view.textview.KeyValueTextView;
import com.srp.eways.util.Utils;
import com.yashoid.sequencelayout.SequenceLayout;

import ir.abmyapp.androidsdk.IABResources;

/**
 * Created by Eskafi on 12/10/2019.
 */
public class ReceiverDetailView extends SequenceLayout {

    private IABResources abResources;

    private KeyValueTextView mReceiverNameView;
    private KeyValueTextView mReceiverPhoneView;
    private KeyValueTextView mReceiverPostCodeView;
    private KeyValueTextView mReceiverMobileView;
    private KeyValueTextView mReceiverAddressView;
    private KeyValueTextView mReceiverStateView;
    private KeyValueTextView mReceiverCityView;
//    private KeyValueTextView mDeliveryTypeView;

    private int mItemMarginTop;

    public ReceiverDetailView(Context context) {
        super(context);

        initialize(context, null, 0);
    }

    public ReceiverDetailView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initialize(context, attrs, 0);
    }

    public ReceiverDetailView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initialize(context, attrs, defStyleAttr);
    }

    private void initialize(Context context, AttributeSet attrs, int defStyleAttr) {

        abResources = DI.getABResources();

        LayoutInflater.from(context).inflate(R.layout.item_receiver_cart_detail, this, true);

        mItemMarginTop = abResources.getDimenPixelSize(R.dimen.address_detail_items_margin_top);

        mReceiverNameView = findViewById(R.id.receiver_name);
        mReceiverPhoneView = findViewById(R.id.receiver_phone);
        mReceiverMobileView = findViewById(R.id.receiver_mobile);
        mReceiverPostCodeView = findViewById(R.id.receiver_post_code);
        mReceiverStateView = findViewById(R.id.receiver_state);
        mReceiverCityView = findViewById(R.id.receiver_city);
        mReceiverAddressView = findViewById(R.id.receiver_address);
//        mDeliveryTypeView = findViewById(R.id.sending_type);

        setTypeface(ResourcesCompat.getFont(getContext(), R.font.iran_yekan));
        setKeysTextColor(abResources.getColor(R.color.address_detail_view_receiver_view_key_text_color));
        setValuesTextColor(abResources.getColor(R.color.address_detail_view_receiver_view_value_text_color));


        mReceiverPhoneView.setKeyText(abResources.getString(R.string.address_detail_view_receiver_phone_text));
        mReceiverMobileView.setKeyText(abResources.getString(R.string.address_detail_view_receiver_mobile_text));
        mReceiverPostCodeView.setKeyText(abResources.getString(R.string.address_detail_view_receiver_post_code_text));
        mReceiverStateView.setKeyText(abResources.getString(R.string.address_detail_view_receiver_state_text));
        mReceiverCityView.setKeyText(abResources.getString(R.string.address_detail_view_receiver_city_text));
        mReceiverAddressView.setKeyText(abResources.getString(R.string.address_detail_view_receiver_address_text));
//        mDeliveryTypeView.setKeyText(abResources.getString(R.string.address_detail_view_delivery_type_text));

//        mDeliveryTypeView.setVisibility(GONE);
//        mReceiverPostCodeView.setVisibility(GONE);
        mReceiverPostCodeView.setValueText("-");

        addSequences(R.xml.sequences_receiver_cart_detail);
    }

    public void setTypeface(Typeface typeface) {
        mReceiverNameView.setTypeface(typeface);
        mReceiverPhoneView.setTypeface(typeface);
        mReceiverMobileView.setTypeface(typeface);
        mReceiverAddressView.setTypeface(typeface);
        mReceiverStateView.setTypeface(typeface);
        mReceiverCityView.setTypeface(typeface);
        mReceiverPostCodeView.setTypeface(typeface);
//        mDeliveryTypeView.setKeyTextTypeface(typeface);
    }

    public void setKeysTextColor(int color) {
        mReceiverNameView.setKeyTextColor(color);
        mReceiverPhoneView.setKeyTextColor(color);
        mReceiverMobileView.setKeyTextColor(color);
        mReceiverAddressView.setKeyTextColor(color);
        mReceiverStateView.setKeyTextColor(color);
        mReceiverCityView.setKeyTextColor(color);
        mReceiverPostCodeView.setKeyTextColor(color);
//        mDeliveryTypeView.setKeyTextTypeface(typeface);
    }

    public void setValuesTextColor(int color) {
        mReceiverNameView.setValueTextColor(color);
        mReceiverPhoneView.setValueTextColor(color);
        mReceiverMobileView.setValueTextColor(color);
        mReceiverAddressView.setValueTextColor(color);
        mReceiverStateView.setValueTextColor(color);
        mReceiverCityView.setValueTextColor(color);
        mReceiverPostCodeView.setValueTextColor(color);
//        mDeliveryTypeView.setKeyTextTypeface(typeface);
    }

    public void setName(String name) {
        mReceiverNameView.setVisibility(VISIBLE);

        mReceiverNameView.setValueText(name);
        mReceiverNameView.setKeyText(abResources.getString(R.string.address_detail_view_receiver_name_text));
    }

    public void setPhone(String phone) {
        mReceiverPhoneView.setVisibility(VISIBLE);

        mReceiverPhoneView.setValueText(Utils.toPersianNumber(phone));
    }

    public void setMobile(String mobile) {
        mReceiverMobileView.setVisibility(VISIBLE);

        mReceiverMobileView.setValueText(Utils.toPersianNumber(mobile));
    }

    public void setPostCode(String postCode) {
        mReceiverPostCodeView.setVisibility(VISIBLE);

        mReceiverPostCodeView.setValueText(Utils.toPersianNumber(postCode));
    }

    public void setState(String state) {
        mReceiverStateView.setVisibility(VISIBLE);

        mReceiverStateView.setValueText(state);
    }

    public void setCity(String city) {
        mReceiverCityView.setVisibility(VISIBLE);

        mReceiverCityView.setValueText(city);
    }

    public void setAddress(String address) {
        mReceiverAddressView.setVisibility(VISIBLE);

        mReceiverAddressView.setValueText(address);
    }

    public void setDeliveryType(String deliveryType) {
//        mDeliveryTypeView.setVisibility(VISIBLE);

//        mDeliveryTypeView.setValueText(deliveryType);
    }
}
