package com.srp.eways.ui.contact;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;

import com.srp.eways.ui.view.toolbar.WeiredToolbar;
import com.srp.eways.R;
import com.srp.eways.di.DIMain;
import com.srp.eways.model.user.ContactSaleExpertResponse;
import com.srp.eways.ui.navigation.DrawerFragment;
import com.srp.eways.util.Utils;

import java.util.List;

import ir.abmyapp.androidsdk.IABResources;

/**
 * Created by Eskafi on 8/29/2019.
 */
public abstract class MainContactFragment extends DrawerFragment<ContactViewModel> {

    protected static int EMAIL_TYPE = 5;
    protected static int TELEGRAM_TYPE = 4;
    protected static int PHONE_TYPE = 2;
    protected static String PHONE_COMPANY = "02164004";

    private TextView mPhoneNumberTextView;
    private TextView mPhoneNumberCompanyTextView;
    private TextView mEmailTextView;
    private TextView mTelegramTextView;
    private RelativeLayout mPhoneLayout;
    private RelativeLayout mEmailLayout;
    private RelativeLayout mTelegramLayout;
    private RelativeLayout mPhoneCompanyContainer;

    private WeiredToolbar mToolbar;
    private IABResources AB;

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AB = DIMain.getABResources();

        mToolbar = view.findViewById(R.id.toolbar);
        mPhoneLayout = view.findViewById(R.id.rl_phone);
        mEmailLayout = view.findViewById(R.id.rl_email);
        mTelegramLayout = view.findViewById(R.id.rl_telegram);

        mPhoneNumberTextView = view.findViewById(R.id.txt_number);
        mEmailTextView = view.findViewById(R.id.email);
        mTelegramTextView = view.findViewById(R.id.telegram);
        mPhoneNumberCompanyTextView = view.findViewById(R.id.txt_number_company);
        mPhoneCompanyContainer = view.findViewById(R.id.rl_phone_company);

        mPhoneNumberCompanyTextView.setText(Utils.toPersianNumber(AB.getString(R.string.contact_us_phone_number)));

        mPhoneLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkStartActivity(Utils.getDialIntent(String.valueOf(mPhoneNumberTextView.getText()).replace("_", "")));
            }
        });

        mPhoneCompanyContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkStartActivity(Utils.getDialIntent(String.valueOf(mPhoneNumberCompanyTextView.getText()).replace("_", "")));
            }
        });

        mEmailLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkStartActivity(Intent.createChooser(Utils.getEmailIntent(String.valueOf(mEmailTextView.getText()),
                        "Subject",
                        "Text"),
                        "Send mail..."));
            }
        });

        mTelegramLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkStartActivity(Utils.getTelegramIntent(String.valueOf(mTelegramTextView.getText()).substring(1)));
            }
        });

        setupToolbar();
    }

    private void checkStartActivity(Intent intent) {
        ResolveInfo resolveInfo = getActivity().getPackageManager().resolveActivity(intent, PackageManager.MATCH_ALL);

        if (resolveInfo != null) {
            startActivity(intent);
        }
    }

    private void setupToolbar() {
        mToolbar.setBackgroundToolbarColor(AB.getColor(R.color.deposit_toolbar_background));
        mToolbar.setBackgroundColor(AB.getColor(R.color.deposit_toolbar_background));
//        mToolbar.showBelowArc(true);
        mToolbar.setTitle(AB.getString(R.string.contact_us_title));
        mToolbar.setShowTitle(true);
        mToolbar.setShowShop(false);
        mToolbar.showNavigationDrawer(true);
//        mToolbar.setTitleIcon(AB.getDrawable(R.drawable.ic_support));
        mToolbar.setTitleTextColor(AB.getColor(R.color.white));
        mToolbar.setTitleTextSize(AB.getDimen(R.dimen.contact_us_title_toolbar_size));
        mToolbar.setShowDeposit(AB.getBoolean(R.bool.contact_toolbar_has_deposit));
        mToolbar.setOnNavigationDrawerClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleDrawer();
            }
        });
        mToolbar.setShowNavigationUp(true);
        mToolbar.setOnBackClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public ContactViewModel acquireViewModel() {

        return DIMain.getViewModel(ContactViewModel.class);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_contactus;
    }

    public void setContactSaleExpert(List<ContactSaleExpertResponse.ContactSaleExpert> info) {

        for (ContactSaleExpertResponse.ContactSaleExpert item : info) {

            if (item.getType() == PHONE_TYPE && item.getValue().equals(PHONE_COMPANY)) {
                return;
            }
            if (item.getType() == PHONE_TYPE && mPhoneLayout.getVisibility() == View.GONE) {
                mPhoneLayout.setVisibility(View.VISIBLE);
                mPhoneNumberTextView.setText(Utils.toPersianNumber(item.getValue()));
            }
            if (item.getType() == EMAIL_TYPE && mEmailLayout.getVisibility() == View.GONE) {
                mEmailLayout.setVisibility(View.VISIBLE);
                mEmailTextView.setText(item.getValue());
            }
            if (item.getType() == TELEGRAM_TYPE && mTelegramLayout.getVisibility() == View.GONE) {
                mTelegramLayout.setVisibility(View.VISIBLE);
                mTelegramTextView.setText(item.getValue());
            }

        }
    }
}
