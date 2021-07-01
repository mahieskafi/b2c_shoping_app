package com.srp.eways.ui.confirmtransaction;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

import com.srp.eways.R;
import com.srp.eways.di.DIMain;
import com.srp.eways.ui.view.button.ButtonElement;
import com.srp.eways.ui.view.receipt.ReceiptListView;

import ir.abmyapp.androidsdk.IABResources;

/**
 * Created by Eskafi on 9/14/2019.
 */
public class ConfirmTransactionDialog extends Dialog {

    private ConfirmTransactionClickListener mClickListener;
    private TextView mTitleText;
    private ReceiptListView mReceiptList;
    private ButtonElement mPayButton;
    private ButtonElement mCancelButton;
    private TextView mPaidPriceText;
    private TextView mPaidPriceValueText;
    private TextView mPaidPriceValueDescriptionText;
    private ImageView mPaidPriceIcon;

    private IABResources AB;
    private ConfirmTransaction mConfirmTransaction;

    public ConfirmTransactionDialog(@NonNull Context context, ConfirmTransaction confirmTransaction) {
        super(context);
        setConfirmTransaction(confirmTransaction);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_confirm_transaction);

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        AB = DIMain.getABResources();
        init();
        setupView();
    }


    private void init() {
        mTitleText = findViewById(R.id.title);
        mReceiptList = findViewById(R.id.receipt_list_view);

        mPaidPriceText = findViewById(R.id.txt_title);
        mPaidPriceValueText = findViewById(R.id.txt_value);
        mPaidPriceValueDescriptionText = findViewById(R.id.value_description);
        mPaidPriceIcon = findViewById(R.id.iv_icon);

        mPayButton = findViewById(R.id.b_payment);
        mCancelButton = findViewById(R.id.b_back);
    }

    private void setupView() {
        mTitleText.setTextColor(AB.getColor(R.color.confirm_transaction_text_color));
        mTitleText.setText(mConfirmTransaction.getTitle() == null ?
                AB.getString(R.string.confirm_transaction_title) : mConfirmTransaction.getTitle());

        mReceiptList.setTextColor(AB.getColor(R.color.confirm_transaction_text_color));
        mReceiptList.setTextSize(AB.getDimenPixelSize(R.dimen.confirm_transaction_values_text_size));
        mReceiptList.setReceiptItem(mConfirmTransaction.getReceiptItems());

        if (mConfirmTransaction.getIcon() != null) {
            mPaidPriceIcon.setVisibility(View.VISIBLE);
            mPaidPriceIcon.setImageDrawable(mConfirmTransaction.getIcon());
        }

        setupPaidPriceText();

        setupPaidPriceValue();

        setupPaidPriceValueDescription();

        setupPaymentButton();

        setupCancelButton();
    }

    private void setupPaidPriceText() {
        mPaidPriceText.setTextColor(AB.getColor(R.color.confirm_transaction_text_color));
        mPaidPriceText.setTextSize(TypedValue.COMPLEX_UNIT_PX, AB.getDimenPixelSize(R.dimen.confirm_transaction_title_text_size));
        Typeface titleTypeface = ResourcesCompat.getFont(getContext(), R.font.iran_yekan_medium);
        if (titleTypeface != null) {
            mPaidPriceText.setTypeface(titleTypeface);
        }
        mPaidPriceText.setText(mConfirmTransaction.getTitlePaidPrice() == null ?
                AB.getString(R.string.confirm_transaction_paid_price_text) : mConfirmTransaction.getTitlePaidPrice());

    }

    private void setupPaidPriceValue() {
        mPaidPriceValueText.setTextColor(AB.getColor(R.color.confirm_transaction_paid_price_text_color));
        mPaidPriceValueText.setTextSize(TypedValue.COMPLEX_UNIT_PX, AB.getDimenPixelSize(R.dimen.confirm_transaction_values_text_size));
        Typeface valueTypeface = ResourcesCompat.getFont(getContext(), R.font.iran_yekan_medium);
        if (valueTypeface != null) {
            mPaidPriceValueText.setTypeface(valueTypeface);
        }
        mPaidPriceValueText.setText(mConfirmTransaction.getValuePaidPrice());

    }

    private void setupPaidPriceValueDescription() {
        mPaidPriceValueDescriptionText.setTextColor(AB.getColor(R.color.confirm_transaction_text_color));
        Typeface valueDescriptionTypeface = ResourcesCompat.getFont(getContext(), R.font.iran_yekan_light);
        if (valueDescriptionTypeface != null) {
            mPaidPriceValueDescriptionText.setTypeface(valueDescriptionTypeface);
        }
        mPaidPriceValueDescriptionText.setVisibility(View.VISIBLE);
        mPaidPriceValueDescriptionText.setText(AB.getString(R.string.rial));

    }

    private void setupPaymentButton() {
        mPayButton.setTextColor(AB.getColor(R.color.confirm_transaction_button_payment_text_color));
        mPayButton.setTextSize(AB.getDimenPixelSize(R.dimen.confirm_transaction_button_payment_text_size));
        mPayButton.setEnabledBackground(AB.getDrawable(R.drawable.login_button_background_enabled));
        mPayButton.setText(mConfirmTransaction.getPayButtonText() == null ?
                AB.getString(R.string.confirm_transaction_payment) : mConfirmTransaction.getPayButtonText());
        ;
        mPayButton.setLoadingColorFilter(AB.getColor(R.color.confirm_transaction_button_payment_text_color));
        mPayButton.setLoadingVisibility(View.GONE);
        Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.iran_yekan_light);
        if (typeface != null) {
            mPayButton.setTextTypeFace(typeface);
        }
        mPayButton.setEnable(true);
        mPayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPayButton.setLoadingVisibility(View.VISIBLE);
                mClickListener.onPayClicked();
            }
        });
    }

    private void setupCancelButton() {
        mCancelButton.setTextColor(AB.getColor(R.color.confirm_transaction_button_cancel_text_color));
        mCancelButton.setTextSize(AB.getDimenPixelSize(R.dimen.confirm_transaction_button_cancel_text_size));
        mCancelButton.setEnabledBackground(AB.getDrawable(R.drawable.confirm_transaction_button_cancel_background));
        mCancelButton.setText(mConfirmTransaction.getCancelButtonText() == null ?
                AB.getString(R.string.confirm_transaction_cancel) : mConfirmTransaction.getCancelButtonText());
        Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.iran_yekan_light);
        if (typeface != null) {
            mCancelButton.setTextTypeFace(typeface);
        }
        mCancelButton.setEnable(true);
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onCancelClicked();
            }
        });
    }

    public void setClickListener(ConfirmTransactionClickListener payClickListener) {
        mClickListener = payClickListener;
    }

    private void setConfirmTransaction(ConfirmTransaction confirmTransaction) {
        mConfirmTransaction = confirmTransaction;
    }
}
