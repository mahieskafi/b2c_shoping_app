package com.srp.eways.ui.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.res.ResourcesCompat;

import com.srp.eways.R;
import com.srp.eways.di.DIMain;
import com.srp.eways.util.NumberInputTextFilter;
import com.srp.eways.util.PersianPriceFormatter;
import com.srp.eways.util.Utils;

import ir.abmyapp.androidsdk.IABResources;
import okhttp3.internal.Util;

public class UserInputChoiceView extends LinearLayout {

    public interface UserInputChoiceListener {

        void onAddUserInputChoiceClicked(long amount);

        void onChangeUserInputChoice(long amount);

    }

    private static final long MAX_AMOUNT = 5000000;
    private static final long MIN_AMOUNT = 1000;

    private AppCompatEditText mInputUserChoice;
    private AppCompatImageView mImageAddUserChoice;

    private UserInputChoiceListener mListener;

    public UserInputChoiceView(Context context) {
        super(context);

        initialize(context, null, 0);
    }

    public UserInputChoiceView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        initialize(context, attrs, 0);
    }

    public UserInputChoiceView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initialize(context, attrs, defStyleAttr);
    }

    private void initialize(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        setOrientation(HORIZONTAL);

        final IABResources abResources = DIMain.getABResources();

        int paddingLeft = abResources.getDimenPixelSize(R.dimen.userinputchoice_padding_left);
        int paddingTop = abResources.getDimenPixelSize(R.dimen.userinputchoice_padding_top);
        int paddingRight = abResources.getDimenPixelSize(R.dimen.userinputchoice_padding_right);
        int paddingBottom = abResources.getDimenPixelSize(R.dimen.userinputchoice_padding_bottom);

        setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);

        int inputUserChoiceMarginLeft = abResources.getDimenPixelSize(R.dimen.inputuserchoice_marginleft);

        mInputUserChoice = new AppCompatEditText(context);
        mInputUserChoice.setTextColor(abResources.getColor(R.color.userinputchoice_text_color));
        mInputUserChoice.setGravity(Gravity.RIGHT);
        mInputUserChoice.setMaxLines(1);
        mInputUserChoice.setLines(1);
        mInputUserChoice.setTextSize(TypedValue.COMPLEX_UNIT_PX, abResources.getDimenPixelSize(R.dimen.inputuserchoice_textsize));
        mInputUserChoice.setTypeface(ResourcesCompat.getFont(context, R.font.iran_yekan_light));
        mInputUserChoice.setHint(abResources.getString(R.string.userinputchoice_hint));
        mInputUserChoice.setInputType(InputType.TYPE_CLASS_NUMBER);
        mInputUserChoice.setBackgroundDrawable(new ColorDrawable(abResources.getColor(R.color.userinputchoice_background_color)));
        mInputUserChoice.addTextChangedListener(new PersianPriceFormatter(","));
        mInputUserChoice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!String.valueOf(mInputUserChoice.getText()).isEmpty()) {

                    long userChargeChoiceAmount = Long.parseLong(mInputUserChoice.getText().toString().replaceAll(",",""));

                    if (mListener != null) {
                        mListener.onChangeUserInputChoice(userChargeChoiceAmount);
                    }
                } else {
                    if (mListener != null) {
                        mListener.onChangeUserInputChoice(0);
                    }
                }
            }
        });

        InputFilter[] filterArray = new InputFilter[1];
        filterArray[0] = new InputFilter.LengthFilter(7);
//        filterArray[0] = new NumberInputTextFilter("1000", String.valueOf(MAX_AMOUNT));
        mInputUserChoice.setFilters(filterArray);

        GradientDrawable userInputChoiceBackground = new GradientDrawable();
        userInputChoiceBackground.setColor(abResources.getColor(R.color.userinputchoice_background_color));
        userInputChoiceBackground.setCornerRadius(abResources.getDimen(R.dimen.userinputchoice_background_radius));
        userInputChoiceBackground.setStroke(abResources.getDimenPixelSize(R.dimen.userinputchoice_background_strokewidth), abResources.getColor(R.color.userinputchoice_stroke_color));

        setBackground(userInputChoiceBackground);

        LinearLayout.LayoutParams inputChoiceLp = new LayoutParams(0,
                MeasureSpec.makeMeasureSpec(abResources.getDimenPixelSize(R.dimen.userinputchoice_edittext_height), MeasureSpec.AT_MOST), 1);
        inputChoiceLp.gravity = Gravity.CENTER_VERTICAL | Gravity.RIGHT;
        inputChoiceLp.leftMargin = inputUserChoiceMarginLeft;
        mInputUserChoice.setLayoutParams(inputChoiceLp);

        mImageAddUserChoice = new AppCompatImageView(context);
        mImageAddUserChoice.setImageDrawable(abResources.getDrawable(R.drawable.ic_add));

        int imageAddWidth = abResources.getDimenPixelSize(R.dimen.inputuserchoice_image_add_width);
        LinearLayout.LayoutParams imageAddLp = new LayoutParams(imageAddWidth, imageAddWidth);
        imageAddLp.gravity = Gravity.CENTER_VERTICAL | Gravity.LEFT;

        mImageAddUserChoice.setLayoutParams(imageAddLp);

        int imageAddPadding = abResources.getDimenPixelSize(R.dimen.userinputchoice_image_add_padding);
        mImageAddUserChoice.setPadding(imageAddPadding, imageAddPadding, imageAddPadding, imageAddPadding);

        mImageAddUserChoice.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(mInputUserChoice.getText())) {
                    Context context = getContext();

                    if (context instanceof Activity) {
                        Utils.hideKeyboard((Activity) context);
                    }

                    long userChargeChoiceAmount = Long.parseLong(mInputUserChoice.getText().toString().replaceAll(",",""));

                    if (mListener != null) {

                        if(userChargeChoiceAmount <= MAX_AMOUNT && userChargeChoiceAmount >= MIN_AMOUNT){

                            mInputUserChoice.setText("");
                            mListener.onAddUserInputChoiceClicked(userChargeChoiceAmount);
                        }
                        else if(userChargeChoiceAmount > MAX_AMOUNT){
                            Toast.makeText(context, Utils.toPersianNumber(abResources.getString(R.string.userinputchoice_custom_amount_max_error)), Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(context, Utils.toPersianNumber(abResources.getString(R.string.userinputchoice_custom_amount_min_error)), Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });

        addView(mImageAddUserChoice);
        addView(mInputUserChoice);
    }

    public void setListener(UserInputChoiceListener listener) {
        mListener = listener;
    }

    public void setTextToUserInputChoice(String amount)
    {
        mInputUserChoice.setText(amount);
    }
}
