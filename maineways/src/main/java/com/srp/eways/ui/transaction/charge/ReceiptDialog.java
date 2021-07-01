package com.srp.eways.ui.transaction.charge;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.ViewGroup;
import androidx.annotation.NonNull;

import com.srp.eways.ui.view.receipt.Receipt;
import com.srp.eways.R;
import com.srp.eways.di.DIMain;
import com.srp.eways.ui.view.receipt.ReceiptItem;
import com.srp.eways.ui.view.receipt.ReceiptView;

import ir.abmyapp.androidsdk.IABResources;

/**
 * Created by Eskafi on 2/22/2020.
 */
public class ReceiptDialog extends Dialog {

    private Receipt mReceipt;

    public ReceiptDialog(@NonNull Context context, Receipt receipt) {
        super(context);
        mReceipt = receipt;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IABResources abResources = DIMain.getABResources();

        if (mReceipt != null) {
            ReceiptView receiptView = new ReceiptView(getContext());

            receiptView.setTitleSize(abResources.getDimenPixelSize(R.dimen.item_receipt_title_text_size));
            receiptView.setTitleColor(abResources.getColor(R.color.receipt_transaction_failed_color));
            receiptView.setTitle(abResources.getString(R.string.receipt_transaction_title_fragment));

            if (mReceipt.getStatusCode() == 0) {
                receiptView.setIcon(abResources.getDrawable(R.drawable.ic_transaction_success));
            } else {
                receiptView.setIcon(abResources.getDrawable(R.drawable.ic_transaction_faild));
            }

            if (mReceipt.getValueDeposit() != null) {
                receiptView.setDepositTextSize(abResources.getDimenPixelSize(R.dimen.receipt_deposit_text_size));
                receiptView.setDepositTitleColor(abResources.getColor(R.color.receipt_transaction_deposit_title_color));
                receiptView.setDepositValueColor(abResources.getColor(R.color.receipt_transaction__deposit_value_color));
                receiptView.setDeposit( new ReceiptItem(mReceipt.getTitleDeposit(), mReceipt.getValueDeposit(), mReceipt.getIcon()));
            }

            receiptView.setReceiptItem(mReceipt.getReceiptItems());
            receiptView.setBackColor(abResources.getColor(R.color.white));
            receiptView.setButtonText(abResources.getString(R.string.receipt_go_home));
            receiptView.setButtonClickListener(new ReceiptView.ButtonClickListener() {
                @Override
                public void onButtonClicked() {
                    dismiss();
                }
            });

            setContentView(receiptView);
        }

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
}
