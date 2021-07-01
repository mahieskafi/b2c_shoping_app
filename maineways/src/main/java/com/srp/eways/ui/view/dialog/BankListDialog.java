package com.srp.eways.ui.view.dialog;

import android.content.Context;

import com.srp.eways.model.deposit.Bank;
import com.srp.eways.ui.view.deposit.BankListDialogContentView;
import java.util.List;

public class BankListDialog {

    public interface BankDialogListener {
        void onBankChanged(Bank bank);

        void onConfirmClicked();

        void onCancelClicked();
    }

    private Context mContext;
    private BankDialogListener mBankDialogListener;

    public BankListDialog(Context context, BankDialogListener listener) {
        mContext = context;
        mBankDialogListener = listener;
    }

    public void openBankListDialog(List<Bank> bankList) {
        final ConfirmationDialog dialog = new ConfirmationDialog(mContext);

        BankListDialogContentView contentView = new BankListDialogContentView(mContext);
        contentView.setBankList(bankList);
        contentView.setListener(new BankListDialogContentView.BankSelectionListener() {
            @Override
            public void bankSelected(Bank bank) {
                if(mBankDialogListener!= null) {
                    mBankDialogListener.onBankChanged(bank);
                }

                dialog.setButtonEnable(true);

            }
        });

        dialog.setChildContentView(contentView);
        dialog.show();
        dialog.setButtonEnable(false);
        dialog.isMatchWidth(false);

        dialog.setListener(new ConfirmationDialog.ConfirmationDialogItemClickListener() {
            @Override
            public void onConfirmClicked() {
                dialog.dismiss();

                if(mBankDialogListener!= null) {
                    mBankDialogListener.onConfirmClicked();
                }
            }

            @Override
            public void onCancelClicked() {
                if(mBankDialogListener!= null) {
                    mBankDialogListener.onCancelClicked();
                }
            }
        });
    }
}
