package com.srp.ewayspanel.ui.charge.buy;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.srp.eways.di.DIMain;
import com.srp.eways.model.charge.BuyChargeResult;
import com.srp.eways.ui.charge.buy.a.MainBuyChargeFragmentA;
import com.srp.eways.ui.charge.model.IChargeChoice;
import com.srp.eways.ui.charge.model.IOperator;
import com.srp.eways.ui.confirmtransaction.ConfirmTransaction;
import com.srp.eways.ui.confirmtransaction.ConfirmTransactionClickListener;
import com.srp.eways.ui.confirmtransaction.ConfirmTransactionDialog;
import com.srp.eways.ui.view.ViewUtils;
import com.srp.eways.util.Utils;
import com.srp.eways.util.analytic.AnalyticConstant;
import com.srp.ewayspanel.R;

import ir.abmyapp.androidsdk.IABResources;


public class BuyChargeFragmentA extends MainBuyChargeFragmentA {

    public static BuyChargeFragmentA newInstance() {


        Bundle args = new Bundle();

        BuyChargeFragmentA fragment = new BuyChargeFragmentA();
        fragment.setArguments(args);
        return fragment;
    }

    private ConfirmTransactionDialog mConfirmTransactionDialog;

    @Override
    public int getLayoutId() {
        return com.srp.eways.R.layout.fragment_buy_charge_a;
    }

    @Override
    public void showConfirmTransaction(ConfirmTransaction confirmTransaction) {

        ConfirmTransactionClickListener clickListener = new ConfirmTransactionClickListener() {
            @Override
            public void onPayClicked() {
                DIMain.getEventSender().sendAction(AnalyticConstant.CHARGE, AnalyticConstant.BUY_CHARGE_ACTION);

                mPaymentDetail.setPaymentLoadingVisibility(View.VISIBLE);
                getViewModel().buyCharge();

            }

            @Override
            public void onCancelClicked() {

            }
        };

        mPaymentDetail.setClickListener(clickListener);
        mPaymentDetail.setupTransaction(confirmTransaction);

        mPaymentDetail.setVisibility(View.VISIBLE);

        ViewUtils.scrollToView(mPaymentDetail, ((ViewGroup) this.getView()));
    }

    @Override
    public void chargeBought(BuyChargeResult buyChargeResult) {
//        if (mConfirmTransactionDialog != null) {
//            mConfirmTransactionDialog.dismiss();
//        }
        //todo
        mPaymentDetail.setVisibility(View.GONE);
    }
}
