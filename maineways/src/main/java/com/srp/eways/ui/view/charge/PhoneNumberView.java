package com.srp.eways.ui.view.charge;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;

import com.srp.eways.R;
import com.srp.eways.model.phonebook.UserPhoneBook;
import com.srp.eways.util.Utils;

import ir.abmyapp.androidsdk.ABResources;
import ir.abmyapp.androidsdk.IABResources;

public class PhoneNumberView extends CardView {

    public interface PhoneNumberViewListener {

        void onRemovePhoneNumberClicked();

    }

    private AppCompatImageView mImageContactIcon;
    private AppCompatTextView mTextContactNumber;
    private AppCompatImageView mImageRemoveContact;

    private int mContactIconMarginTop;

    private int mContactIconWidth;
    private int mContactIconHeight;

    private int mImageRemoveContactWidth;
    private int mImageRemoveContactHeight;

    private Rect mContactIconBounds = new Rect();
    private Rect mContactNumberBounds = new Rect();
    private Rect mRemoveContactIconBounds = new Rect();

    private int mContactIconMarginRight;
    private int mContactNumberMarginRight;
    private int mIconRemoveContactMarginLeft;

    private PhoneNumberViewListener mPhoneNumberViewListener;

    private float mFontDeltaY;

    private UserPhoneBook mContactInfo;

    public PhoneNumberView(@NonNull Context context) {
        super(context);

        initialize(context, null, 0);
    }

    public PhoneNumberView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        initialize(context, attrs, 0);
    }

    public PhoneNumberView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initialize(context, attrs, defStyleAttr);
    }

    private void initialize(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        IABResources abResources = ABResources.get(context);

        mImageContactIcon = new AppCompatImageView(context);
        mImageContactIcon.setImageDrawable(abResources.getDrawable(R.drawable.ic_mobile));

        Typeface typeface = ResourcesCompat.getFont(context, R.font.iran_yekan);
        float contactNumberTextSize = abResources.getDimen(R.dimen.phonenumberview_number_textsize);

        Paint helperPaint = new Paint();
        helperPaint.setTypeface(typeface);
        helperPaint.setTextSize(contactNumberTextSize);

        Paint.FontMetrics fontMetrics = helperPaint.getFontMetrics();
        mFontDeltaY = (fontMetrics.descent + fontMetrics.ascent) / 2;

        mTextContactNumber = new AppCompatTextView(context);
        mTextContactNumber.setTextSize(TypedValue.COMPLEX_UNIT_PX, contactNumberTextSize);
        mTextContactNumber.setTextColor(abResources.getColor(R.color.phonenumberview_number_number_textcolot));
        mTextContactNumber.setTypeface(typeface);

        mImageRemoveContact = new AppCompatImageView(context);
        mImageRemoveContact.setScaleType(ImageView.ScaleType.CENTER_CROP);
        mImageRemoveContact.setImageDrawable(abResources.getDrawable(R.drawable.ic_cross_cancel_contact));

        int mDesignModel = abResources.getInt(R.integer.abtesting_designmodel);
        if (mDesignModel == 0) {//A
            setRadius(abResources.getDimen(R.dimen.phonenumberview_cornerradius));
        } else {//B
            setRadius(abResources.getDimen(R.dimen.phonenumberview_cornerradius_b));
        }

        setCardElevation(abResources.getDimen(R.dimen.phonenumberview_shadowheight));

        mContactIconMarginTop = abResources.getDimenPixelSize(R.dimen.phonenumberview_contacticon_margintop);
        mContactIconWidth = abResources.getDimenPixelSize(R.dimen.phonenumberview_contacticon_width);
        mContactIconHeight = abResources.getDimenPixelSize(R.dimen.phonenumberview_contacticon_height);

        mIconRemoveContactMarginLeft = abResources.getDimenPixelSize(R.dimen.phonenumberview_removecontacticon_marginleft);

        mImageRemoveContactWidth = abResources.getDimenPixelSize(R.dimen.phonenumberview_removecontacticon_width);
        mImageRemoveContactHeight = abResources.getDimenPixelSize(R.dimen.phonenumberview_removecontacticon_height);

        mContactIconMarginRight = abResources.getDimenPixelSize(R.dimen.phonenumberview_contacticon_marginright);
        mContactNumberMarginRight = abResources.getDimenPixelSize(R.dimen.phonenumberview_contactnumber_marginright);

        addView(mImageContactIcon);
        addView(mTextContactNumber);
        addView(mImageRemoveContact);

        mImageRemoveContact.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPhoneNumberViewListener != null) {
                    mPhoneNumberViewListener.onRemovePhoneNumberClicked();
                }
            }
        });
    }

    public void setContactInfo(UserPhoneBook contactInfo) {
        mContactInfo = contactInfo;

        if (mContactInfo != null) {
            String name = mContactInfo.getFullName();

            if (!TextUtils.isEmpty(name)) {
                mTextContactNumber.setText(name);
            } else {
                mTextContactNumber.setText(Utils.toPersianNumber(mContactInfo.getCellPhone()));
            }
        }
        else {
            mTextContactNumber.setText("");
        }
    }

    public void setPhoneNumberViewListener(PhoneNumberViewListener listener) {
        mPhoneNumberViewListener = listener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = mContactIconMarginTop + mContactIconHeight + mContactIconMarginTop;

        int width = MeasureSpec.getSize(widthMeasureSpec);

        mContactIconBounds.right = width - mContactIconMarginRight;
        mContactIconBounds.left = mContactIconBounds.right - mContactIconWidth;
        mContactIconBounds.top = mContactIconMarginTop;
        mContactIconBounds.bottom = mContactIconBounds.top + mContactIconHeight;

        mTextContactNumber.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

        mContactNumberBounds.right = mContactIconBounds.left - mContactNumberMarginRight;
        mContactNumberBounds.left = mContactNumberBounds.right - mTextContactNumber.getMeasuredWidth();
        mContactNumberBounds.top = height / 2 - (mTextContactNumber.getMeasuredHeight() / 2);
        mContactNumberBounds.bottom = height / 2 + (mTextContactNumber.getMeasuredHeight() / 2);

        mRemoveContactIconBounds.left = mIconRemoveContactMarginLeft;
        mRemoveContactIconBounds.right = mRemoveContactIconBounds.left + mImageRemoveContactWidth;
        mRemoveContactIconBounds.top = height / 2 - (mImageRemoveContactHeight / 2);
        mRemoveContactIconBounds.bottom = height / 2 + (mImageRemoveContactHeight / 2);

        setMeasuredDimension(widthMeasureSpec, MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        layoutChild(mImageContactIcon, mContactIconBounds);
        layoutChild(mTextContactNumber, mContactNumberBounds);
        layoutChild(mImageRemoveContact, mRemoveContactIconBounds);
    }

    private void layoutChild(View child, Rect bounds) {
        child.measure(MeasureSpec.makeMeasureSpec(bounds.width(), MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(bounds.height(), MeasureSpec.EXACTLY));
        child.layout(bounds.left, bounds.top, bounds.right, bounds.bottom);
    }

    public String getText() {
        return mTextContactNumber.getText().toString();
    }

}
