package com.srp.eways.ui.view.deposit;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.res.ResourcesCompat;

import com.srp.eways.R;
import com.srp.eways.di.DIMain;
import com.srp.eways.model.deposit.Bank;

import ir.abmyapp.androidsdk.IABResources;

/**
 * Created by Eskafi on 12/14/2019.
 */
public class BankView extends LinearLayout {

    public interface BankClickListener {
        void bankClicked(Bank bank);
    }

    private AppCompatImageView mBankIcon;
    private AppCompatTextView mBankName;
    private AppCompatImageView mBankIconEnabledBackground;

    private BankClickListener mListener;

    private Bank mBank;

    public BankView(Context context) {
        super(context);

        initialize(context, null, 0);
    }

    public BankView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initialize(context, attrs, 0);
    }

    public BankView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initialize(context, attrs, defStyleAttr);
    }

    private void initialize(Context context, AttributeSet attrs, int defStyleAttr) {

        IABResources abResources = DIMain.getABResources();

        int iconSize = abResources.getDimenPixelSize(R.dimen.operator_dialog_items_operator_icon_background_width);
        int iconPaddingSize = abResources.getDimenPixelSize(R.dimen.operator_dialog_items_operator_name_margitn_top);

        LayoutInflater.from(context).inflate(R.layout.item_transportable_operator_b, this, true);

        mBankIcon = findViewById(R.id.iv_icon);
        mBankName = findViewById(R.id.tv_name);
        mBankIconEnabledBackground = findViewById(R.id.iv_icon_enable_background);

        mBankIcon.setScaleType(ImageView.ScaleType.FIT_CENTER);
        mBankIcon.setImageDrawable(abResources.getDrawable(R.drawable.ic_parsian));
        mBankIconEnabledBackground.setImageDrawable(abResources.getDrawable(R.drawable.item_transportation_operator_b_enable_background));
        mBankIconEnabledBackground.setVisibility(GONE);

        mBankName.setTextSize(TypedValue.COMPLEX_UNIT_PX, abResources.getDimenPixelSize(R.dimen.operator_dialog_items_name_textsize));
        mBankName.setTypeface(ResourcesCompat.getFont(getContext(), R.font.iran_yekan_medium));
        mBankName.setTextColor(abResources.getColor(R.color.phoneandoperatorsview_operatorname_textcolor));

        FrameLayout.LayoutParams backgroundLP = new FrameLayout.LayoutParams(iconSize, iconSize);
        mBankIcon.setPadding(iconPaddingSize, iconPaddingSize, iconPaddingSize, iconPaddingSize);
        mBankIcon.setLayoutParams(backgroundLP);
        mBankIconEnabledBackground.setLayoutParams(backgroundLP);

        LinearLayout.LayoutParams itemLP = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        itemLP.weight = 1;
        setLayoutParams(itemLP);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mListener != null) {
                    mListener.bankClicked(mBank);
                }
            }
        });
    }


    public void setBankName(String bankName) {
        mBankName.setText(bankName);
    }

    public void setBankIcon(Drawable bankIcon) {
        mBankIcon.setImageDrawable(bankIcon);
    }

    public void setBank(Bank bank) {
        mBank = bank;
    }

    public Bank getBank() {
        return mBank;
    }

    public void setListener(BankClickListener listener) {
        mListener = listener;
    }

    public void setEnabled(boolean enabled) {
        if (enabled) {
            mBankIconEnabledBackground.setVisibility(VISIBLE);
            mBankName.setTypeface(ResourcesCompat.getFont(getContext(), R.font.iran_yekan_bold));
        } else {
            mBankIconEnabledBackground.setVisibility(GONE);
            mBankName.setTypeface(ResourcesCompat.getFont(getContext(), R.font.iran_yekan_medium));
        }
    }
}
