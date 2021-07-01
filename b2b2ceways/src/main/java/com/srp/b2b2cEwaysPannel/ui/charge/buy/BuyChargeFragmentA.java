package com.srp.b2b2cEwaysPannel.ui.charge.buy;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import com.srp.b2b2cEwaysPannel.R;
import com.srp.b2b2cEwaysPannel.di.DI;
import com.srp.b2b2cEwaysPannel.ui.charge.ConfirmTransactionView;
import com.srp.eways.BuildConfig;
import com.srp.eways.di.DIMain;
import com.srp.eways.model.charge.BuyChargeResult;
import com.srp.eways.model.charge.result.BuyCashChargeResult;
import com.srp.eways.model.deposit.Bank;
import com.srp.eways.model.deposit.BankListResponse;
import com.srp.eways.ui.charge.ChargeSelectionState;
import com.srp.eways.ui.charge.buy.a.MainBuyChargeFragmentA;
import com.srp.eways.ui.confirmtransaction.ConfirmTransaction;
import com.srp.eways.ui.deposit.increasedeposit.IncreaseDepositFragment;
import com.srp.eways.ui.view.charge.PhoneAndOperatorsView;
import com.srp.eways.ui.view.dialog.BankListDialog;
import com.srp.eways.util.Utils;
import com.srp.eways.util.analytic.AnalyticConstant;

import java.util.List;

import ir.abmyapp.androidsdk.IABResources;

public class BuyChargeFragmentA extends MainBuyChargeFragmentA {

    private FrameLayout mContainer;

    Observer<BankListResponse> mBankListObserver = new Observer<BankListResponse>() {
        @Override
        public void onChanged(BankListResponse bankListResponse) {
            if (bankListResponse != null) {
                if (bankListResponse.getStatus() == 0) {
                    openBankListDialog(bankListResponse.getItems());

                } else {
                    Toast.makeText(getContext(), bankListResponse.getDescription(), Toast.LENGTH_LONG).show();
                }

                getViewModel().consumeBankListLiveData();
            }
        }
    };

    public static BuyChargeFragmentA newInstance() {

        Bundle args = new Bundle();

        BuyChargeFragmentA fragment = new BuyChargeFragmentA();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mContainer = view.findViewById(R.id.container);

        getViewModel().setGetway(0);
    }

    @Override
    public void showConfirmTransaction(final ConfirmTransaction confirmTransaction) {

        final IABResources abResources = DI.getABResources();


        final ConfirmTransactionView confirmTransactionView = new ConfirmTransactionView(getContext());

        confirmTransactionView.setConfirmTransaction(confirmTransaction);
        confirmTransactionView.setBuyClickListener(new ConfirmTransactionView.BuyClickListener() {
            @Override
            public void onCreditPaymentClicked() {
                getViewModel().buyCharge();
            }

            @Override
            public void onCashPaymentClicked() {
                Long paidPrice = Long.valueOf(Utils.removeThousandSeparator(confirmTransaction.getValuePaidPrice()));
                if (paidPrice >= 20000) {

                    getViewModel().getBankList();
                    getViewModel().getBankListLiveData().observe(BuyChargeFragmentA.this, mBankListObserver);

                } else {
                    final Toast toast = Toast.makeText(getContext(), abResources.getString(R.string.charge_confirm_transaction_error), Toast.LENGTH_LONG);
                    toast.show();

                    ViewGroup group = (ViewGroup) toast.getView();
                    TextView messageTextView = (TextView) group.getChildAt(0);
                    messageTextView.setTextSize(18);
                    toast.show();

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            toast.cancel();
                        }
                    }, 4000);

                }
            }

        });

        mContainer.addView(confirmTransactionView);

        mContainer.setVisibility(View.VISIBLE);

    }

    @Override
    public void chargeBought(BuyChargeResult buyChargeResult) {

        if (buyChargeResult.getStatus() != mChargeViewModel.NOT_ENOUGH_CREDIT) {
            mContainer.setVisibility(View.GONE);
            mContainer.removeAllViews();
        }


    }

    private void openBankListDialog(List<Bank> bankList) {

        BankListDialog bankListDialog = new BankListDialog(getContext(), new BankListDialog.BankDialogListener() {
            @Override
            public void onBankChanged(Bank bank) {
                getViewModel().setGetway(bank.getGId());
            }

            @Override
            public void onConfirmClicked() {

                getViewModel().buyCashCharge();

                getViewModel().getBuyCashChargeResultLive().observe(BuyChargeFragmentA.this, new Observer<BuyCashChargeResult>() {
                    @Override
                    public void onChanged(BuyCashChargeResult buyCashChargeResult) {
                        if (buyCashChargeResult != null) {
                            if (buyCashChargeResult.getStatus() == 0 && !(buyCashChargeResult.getUrl().isEmpty())) {

                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(BuildConfig.DARGAH_URL + buyCashChargeResult.getUrl()));
                                getBaseActivity().startActivity(browserIntent);

                            } else {
                                Toast.makeText(getContext(), buyCashChargeResult.getDescription(), Toast.LENGTH_LONG).show();
                            }
                            getViewModel().consumeBuyCashChargeResultLive();
                        }
                    }
                });

            }

            @Override
            public void onCancelClicked() {

            }
        });

        bankListDialog.openBankListDialog(bankList);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_buy_charge;
    }

    @Override
    public boolean onBackPress() {
        if (mContainer.getVisibility() == View.VISIBLE) {
            mContainer.setVisibility(View.GONE);
            mContainer.removeAllViews();
            return true;
        } else return false;
    }

    @Override
    protected View createViewForLevel(int level, List<ChargeSelectionState.ChargeLevelInfo> selectionState, List<?> options, int viewType) {
        View returnView = super.createViewForLevel(level, selectionState, options, viewType);

        if (returnView instanceof PhoneAndOperatorsView) {
            ((PhoneAndOperatorsView) returnView).setContactIconVisibility(View.GONE);
        }

        return returnView;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            Utils.hideKeyboard(getActivity());
        }
    }
}
