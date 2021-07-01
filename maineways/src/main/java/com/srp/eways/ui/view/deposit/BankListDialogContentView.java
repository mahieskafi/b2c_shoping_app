package com.srp.eways.ui.view.deposit;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.res.ResourcesCompat;

import com.srp.eways.R;
import com.srp.eways.di.DIMain;
import com.srp.eways.model.deposit.Bank;
import com.srp.eways.util.BillUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ir.abmyapp.androidsdk.IABResources;

/**
 * Created by Eskafi on 12/14/2019.
 */
public class BankListDialogContentView extends LinearLayout implements BankView.BankClickListener {

    public interface BankSelectionListener {
        void bankSelected(Bank bank);
    }

    private LinearLayout mBanksView;

    private IABResources abResources;

    private List<BankView> mBankViews = new ArrayList<>();

    private BankSelectionListener mListener;
    AppCompatTextView mTitle = new AppCompatTextView(getContext());

    public BankListDialogContentView(Context context) {
        super(context);

        initialize(context, null, 0);
    }

    public BankListDialogContentView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        initialize(context, attrs, 0);
    }

    public BankListDialogContentView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initialize(context, attrs, defStyleAttr);
    }

    private void initialize(Context context, AttributeSet attrs, int defStyleAttr) {

        abResources = DIMain.getABResources();

        setGravity(Gravity.RIGHT);
        setOrientation(LinearLayout.VERTICAL);
        setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        mBanksView = new LinearLayout(getContext());
        mBanksView.setGravity(Gravity.RIGHT);
        mBanksView.setOrientation(LinearLayout.HORIZONTAL);


        mTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, abResources.getDimenPixelSize(R.dimen.operator_dialog_title_textsize));
        mTitle.setTypeface(ResourcesCompat.getFont(context, R.font.iran_yekan_medium));
        mTitle.setTextColor(abResources.getColor(R.color.phoneandoperatorsview_transportoption_title_textcolor));
        mTitle.setText(abResources.getString(R.string.increase_deposit_bank_list_dialog_title));

        mTitle.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

        addView(mTitle);
    }

    public void setBankList(List<Bank> bankList) {
        mBanksView.setWeightSum(bankList.size());

        for (int i = bankList.size() - 1; i >= 0; i--) {
            Bank bank = bankList.get(i);
            if (bank != null && BillUtil.getBankName(bank.getGId()) != 0) {

                BankView bankView = new BankView(getContext());
                bankView.setBank(bank);
                bankView.setBankName(bank.getPName());
                bankView.setBankIcon(abResources.getDrawable(BillUtil.getBankIcon(bank.getGId())));
                bankView.setEnabled(false);
                bankView.setListener(this);
                mBankViews.add(bankView);

                mBanksView.addView(bankView);
            }
        }

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.topMargin = abResources.getDimenPixelSize(R.dimen.phoneandoperatorsview_title_chooseoperator_margintop);
        mBanksView.setLayoutParams(layoutParams);

        addView(mBanksView);
    }

    public void setListener(BankSelectionListener listener) {
        mListener = listener;
    }

    @Override
    public void bankClicked(Bank bank) {
        for (int i = 0; i < mBankViews.size(); ++i) {
            BankView bankView = mBankViews.get(i);

            if (bankView.getBank().getGId() == bank.getGId()) {
                bankView.setEnabled(true);
                mListener.bankSelected(bank);

            } else {
                bankView.setEnabled(false);
            }
        }
    }

    public void haveTitle(boolean has){
        if (!has){
            mTitle.setVisibility(View.GONE);
        }
    }

    public List<BankView> getmBankViews() {
        return mBankViews;
    }

}
