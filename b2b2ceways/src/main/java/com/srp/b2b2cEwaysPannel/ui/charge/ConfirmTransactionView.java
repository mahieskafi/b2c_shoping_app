package com.srp.b2b2cEwaysPannel.ui.charge;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import com.srp.b2b2cEwaysPannel.R;
import com.srp.b2b2cEwaysPannel.di.DI;
import com.srp.eways.ui.confirmtransaction.ConfirmTransaction;
import com.srp.eways.ui.view.button.ButtonElement;
import com.srp.eways.ui.view.receipt.ReceiptListView;
import com.srp.eways.util.analytic.AnalyticConstant;

import ir.abmyapp.androidsdk.IABResources;

public class ConfirmTransactionView extends ScrollView {

    public interface BuyClickListener {
        void onCreditPaymentClicked();

        void onCashPaymentClicked();
    }

    private TextView mTitleText;
    private ReceiptListView mReceiptList;
    private ButtonElement mPayCreditButton;
    private ButtonElement mPayCashButton;

    private TextView mPaidPriceText;
    private TextView mPaidPriceValueText;
    private TextView mPaidPriceValueDescriptionText;

    private BuyClickListener mBuyClickListener;

    private IABResources abResources;


    public ConfirmTransactionView(Context context) {
        super(context);

        init(context, null, 0);
    }

    public ConfirmTransactionView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(context, attrs, 0);
    }

    public ConfirmTransactionView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        LayoutInflater.from(context).inflate(R.layout.fragment_confirm_transaction, this, true);

        abResources = DI.getABResources();

        mTitleText = findViewById(R.id.title);
        mReceiptList = findViewById(R.id.receipt_list_view);

        mPayCreditButton = findViewById(R.id.b_payment);
        mPayCashButton = findViewById(R.id.b_back);

        mPaidPriceText = findViewById(R.id.txt_title);
        mPaidPriceValueText = findViewById(R.id.txt_value);
        mPaidPriceValueDescriptionText = findViewById(R.id.value_description);

    }


    private void setupPaidPriceText() {
        mPaidPriceText.setTextColor(abResources.getColor(R.color.confirm_transaction_text_color));
        mPaidPriceText.setTextSize(TypedValue.COMPLEX_UNIT_PX, abResources.getDimenPixelSize(R.dimen.confirm_transaction_title_text_size));

        Typeface titleTypeface = ResourcesCompat.getFont(getContext(), R.font.iran_yekan_medium);
        if (titleTypeface != null) {
            mPaidPriceText.setTypeface(titleTypeface);
        }
        mPaidPriceText.setText(abResources.getString(R.string.confirm_transaction_paid_price_text));

    }

    private void setupPaidPriceValue(String price) {
        mPaidPriceValueText.setTextColor(abResources.getColor(R.color.confirm_transaction_paid_price_text_color));
        mPaidPriceValueText.setTextSize(TypedValue.COMPLEX_UNIT_PX, abResources.getDimenPixelSize(R.dimen.confirm_transaction_values_text_size));
        Typeface valueTypeface = ResourcesCompat.getFont(getContext(), R.font.iran_yekan_medium);
        if (valueTypeface != null) {
            mPaidPriceValueText.setTypeface(valueTypeface);
        }
        mPaidPriceValueText.setText(price);

    }

    private void setupPaidPriceValueDescription() {
        mPaidPriceValueDescriptionText.setTextColor(abResources.getColor(R.color.confirm_transaction_text_color));
        Typeface valueDescriptionTypeface = ResourcesCompat.getFont(getContext(), R.font.iran_yekan_light);
        if (valueDescriptionTypeface != null) {
            mPaidPriceValueDescriptionText.setTypeface(valueDescriptionTypeface);
        }
        mPaidPriceValueDescriptionText.setVisibility(View.VISIBLE);
        mPaidPriceValueDescriptionText.setText(abResources.getString(R.string.rial));

    }

    private void setupPaymentFromCreditButton() {
        mPayCreditButton.setTextColor(abResources.getColor(R.color.confirm_transaction_button_payment_text_color));
        mPayCreditButton.setTextSize(abResources.getDimenPixelSize(R.dimen.confirm_transaction_button_payment_text_size));
        mPayCreditButton.setEnabledBackground(abResources.getDrawable(R.drawable.login_button_background_enabled));
        mPayCreditButton.setText(abResources.getString(R.string.charge_confirm_transaction_payment_credit_button_text));

        mPayCreditButton.setLoadingColorFilter(abResources.getColor(R.color.confirm_transaction_button_payment_text_color));
        mPayCreditButton.setLoadingVisibility(View.GONE);
        Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.iran_yekan_light);
        if (typeface != null) {
            mPayCreditButton.setTextTypeFace(typeface);
        }
        mPayCreditButton.setEnable(true);
        mPayCreditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DI.getEventSender().sendAction(AnalyticConstant.CHARGE, AnalyticConstant.BUY_CHARGE_ACTION);

                if (mBuyClickListener != null) {
                    mBuyClickListener.onCreditPaymentClicked();
                }
            }
        });
    }

    private void setupPaymentFromDargahButton() {
        mPayCashButton.setTextColor(abResources.getColor(R.color.confirm_transaction_button_cancel_text_color));
        mPayCashButton.setTextSize(abResources.getDimenPixelSize(R.dimen.confirm_transaction_button_cancel_text_size));
        mPayCashButton.setEnabledBackground(abResources.getDrawable(R.drawable.confirm_transaction_payment_cash_button_background));

        mPayCashButton.setText(abResources.getString(R.string.charge_confirm_transaction_payment_cash_button_text));
        Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.iran_yekan_light);
        if (typeface != null) {
            mPayCashButton.setTextTypeFace(typeface);
        }
        mPayCashButton.setEnable(true);
        mPayCashButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBuyClickListener != null) {
                    mBuyClickListener.onCashPaymentClicked();
                }
            }
        });
    }


    public void setConfirmTransaction(ConfirmTransaction confirmTransaction) {
        if (confirmTransaction != null) {
            mTitleText.setTextColor(abResources.getColor(R.color.confirm_transaction_text_color));
            mTitleText.setText(abResources.getString(R.string.confirm_transaction_title));

            mReceiptList.setTextColor(abResources.getColor(R.color.confirm_transaction_text_color));
            mReceiptList.setTextSize(abResources.getDimenPixelSize(R.dimen.confirm_transaction_values_text_size));
            mReceiptList.setReceiptItem(confirmTransaction.getReceiptItems());

            setupPaidPriceText();

            setupPaidPriceValue(confirmTransaction.getValuePaidPrice());

            setupPaidPriceValueDescription();

            setupPaymentFromCreditButton();

            setupPaymentFromDargahButton();

        }
    }

    public void setBuyClickListener(BuyClickListener buyClickListener) {
        mBuyClickListener = buyClickListener;
    }
}
