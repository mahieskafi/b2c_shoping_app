package com.srp.eways.ui.charge.buy;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.res.ResourcesCompat;

import com.srp.eways.R;
import com.srp.eways.di.DIMain;
import com.srp.eways.model.phonebook.UserPhoneBook;
import com.srp.eways.util.Utils;

import ir.abmyapp.androidsdk.IABResources;

public class ContactItemView extends LinearLayout implements View.OnClickListener {

    public interface OnContactItemClickedListener {

        void onContactItemClicked(UserPhoneBook contactItem);

    }

    private AppCompatTextView mTextContactName;
    private AppCompatTextView mTextContactPhoneNumber;

    private OnContactItemClickedListener mListener;

    private UserPhoneBook mContact;

    public ContactItemView(Context context) {
        super(context);

        initialize(context, null, 0);
    }

    public ContactItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        initialize(context, attrs, 0);
    }

    public ContactItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initialize(context, attrs, defStyleAttr);
    }

    private void initialize(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        setOrientation(HORIZONTAL);

        IABResources abResources = DIMain.getABResources();

        Typeface nameFont = ResourcesCompat.getFont(context, R.font.iran_yekan);
        Typeface phoneNumberFont = ResourcesCompat.getFont(context, R.font.iran_yekan);

        int nameColor = abResources.getColor(R.color.chargecontact_itemview_name_color);
        int phoneNumberColor = abResources.getColor(R.color.chargecontact_itemview_phone_color);

        mTextContactName = new AppCompatTextView(context);
        mTextContactName.setTypeface(nameFont);
        mTextContactName.setTextColor(nameColor);
        mTextContactName.setTextSize(TypedValue.COMPLEX_UNIT_PX, abResources.getDimenPixelSize(R.dimen.chargecontact_itemview_name_size));
        mTextContactName.setGravity(Gravity.RIGHT);

        LinearLayout.LayoutParams textNameLp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT) ;
        textNameLp.topMargin = abResources.getDimenPixelSize(R.dimen.chargecontact_itemview_name_margin_top);
//        textNameLp.rightMargin = abResources.getDimenPixelSize(R.dimen.chargecontact_itemview_name_margin_right);
        textNameLp.bottomMargin = abResources.getDimenPixelSize(R.dimen.chargecontact_itemview_name_margin_bottom);

        mTextContactName.setLayoutParams(textNameLp);

        mTextContactPhoneNumber = new AppCompatTextView(context);
        mTextContactPhoneNumber.setTypeface(phoneNumberFont);
        mTextContactPhoneNumber.setTextColor(phoneNumberColor);
        mTextContactPhoneNumber.setTextSize(TypedValue.COMPLEX_UNIT_PX, abResources.getDimenPixelSize(R.dimen.chargecontact_itemview_phone_size));

        LinearLayout.LayoutParams textPhoneNumberLp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT) ;
//        textPhoneNumberLp.leftMargin = abResources.getDimenPixelSize(R.dimen.chargecontact_itemview_phone_margin_left);
        textPhoneNumberLp.topMargin = abResources.getDimenPixelSize(R.dimen.chargecontact_itemview_phone_margin_top);
        textPhoneNumberLp.bottomMargin = abResources.getDimenPixelSize(R.dimen.chargecontact_itemview_name_margin_bottom);

        mTextContactPhoneNumber.setLayoutParams(textPhoneNumberLp);

        addView(mTextContactPhoneNumber);
        addView(mTextContactName);

        setOnClickListener(this);
    }

    public void setOnItemClickedListener(OnContactItemClickedListener listener) {
        mListener = listener;
    }

    public void setData(UserPhoneBook contact) {
        mContact = contact;

        mTextContactName.setText(mContact.getFullName());
        mTextContactPhoneNumber.setText(Utils.toPersianNumber(mContact.getCellPhone()));
    }

    @Override
    public void onClick(View v) {
        if (mListener != null) {
            mListener.onContactItemClicked(mContact);
        }
    }

}
