package com.srp.eways.ui.phonebook.ewayscontact;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Observer;
import androidx.viewpager.widget.ViewPager;

import com.srp.eways.R;
import com.srp.eways.base.BaseActivity;
import com.srp.eways.di.DIMain;
import com.srp.eways.model.phonebook.UserPhoneBook;
import com.srp.eways.ui.charge.MainChargePagerAdapter;
import com.srp.eways.ui.navigation.INavigationController;
import com.srp.eways.ui.phonebook.ewayscontact.contactlist.EwaysContactFragment;
import com.srp.eways.ui.phonebook.PhoneBookViewModel;
import com.srp.eways.ui.phonebook.ewayscontact.addcontact.AddContactFragment;
import com.srp.eways.ui.phonebook.ewayscontact.editcontact.EditEwaysContactActivity;
import com.srp.eways.ui.view.charge.TabOptionsView;
import com.srp.eways.ui.view.charge.a.ChargeOptionsTabViewA;
import com.srp.eways.ui.view.toolbar.WeiredToolbar;
import com.srp.eways.util.Utils;

import java.util.ArrayList;
import java.util.List;

import ir.abmyapp.androidsdk.IABResources;

public class EwaysContactActivity extends BaseActivity<PhoneBookViewModel> implements TabOptionsView.ChargeOptionsTabViewListener {

    public static EwaysContactActivity newInstance() {
        return new EwaysContactActivity();
    }

    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, EwaysContactActivity.class);
        return intent;
    }

    private WeiredToolbar mToolbar;
    private ViewPager mPager;
    private PhoneBookPagerAdapter mAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mToolbar = findViewById(R.id.contact_toolbar);
        mPager = findViewById(R.id.pager);

        setupToolbar();

        final ChargeOptionsTabViewA chargeOptionsTabViewA = findViewById(R.id.tab_view);
        String[] tabTitles = new String[]{getString(R.string.eways_contact_tab), getString(R.string.add_eways_contact_tab)};
        int[] tabIcons = new int[]{R.drawable.ic_eways_contactlist, R.drawable.add_eways_contact_list};

        List<TabOptionsView.TabItem> tabItems = new ArrayList<>();
        for (int i = 0; i < tabTitles.length; ++i) {
            tabItems.add(new TabOptionsView.TabItem(tabTitles[i], tabIcons[i], tabTitles[i]));
        }
        chargeOptionsTabViewA.setTabItems(tabItems);

        chargeOptionsTabViewA.setUnselectedTabColor(getResources().getColor(R.color.deposit_fragmnet_charge_option_tab_unselected_color));
        chargeOptionsTabViewA.setTabBackgroundColor(getResources().getColor(R.color.deposit_fragmnet_charge_option_tab_background_color));
        chargeOptionsTabViewA.setOnChargeTabClickListener(this);
        chargeOptionsTabViewA.setSelectedTab(0);


        mAdapter = new PhoneBookPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mAdapter);

        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
//                Utils.hideKeyboard();
                chargeOptionsTabViewA.setSelectedTab(1 - position);
                refreshChargeTransactionList(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }

        });

        mPager.setCurrentItem(1);

        getViewModel().getEditableUserPhoneBookLiveData().observe(this, new Observer<UserPhoneBook>() {
            @Override
            public void onChanged(UserPhoneBook userPhoneBook) {
                if (userPhoneBook != null) {
                    startActivityForResult(EditEwaysContactActivity.getIntent(getBaseContext()), 3000);
                }
            }
        });
    }


    private void refreshChargeTransactionList(int position) {
        Fragment selectedFragment = mAdapter.getItem(position);
        if (selectedFragment instanceof MainChargePagerAdapter.PagerFragmentLifeCycle) {
            MainChargePagerAdapter.PagerFragmentLifeCycle fragmentToShow = (MainChargePagerAdapter.PagerFragmentLifeCycle) selectedFragment;
            fragmentToShow.onResumePagerFragment();
        }
    }


    private class PhoneBookPagerAdapter extends FragmentPagerAdapter {
        long mAmountOfIncrease = 0;

        public PhoneBookPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return AddContactFragment.newInstance();
                case 1:
                    return EwaysContactFragment.getInstance();
            }

            throw new RuntimeException("Pager position out of bounds.");
        }

        @Override
        public int getCount() {
            return 2;
        }

    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_ewayscontact;
    }

    @Override
    public PhoneBookViewModel getViewModel() {
        return DIMain.getViewModel(PhoneBookViewModel.class);
    }

    @Override
    public void onChargeTabClicked(int tabIndex, TabOptionsView.TabItem tabItem) {
        mPager.setCurrentItem(1 - tabIndex);
    }

    private void setupToolbar() {
        IABResources resources = DIMain.getABResources();

        mToolbar.setBackgroundToolbarColor(resources.getColor(R.color.deposit_toolbar_background));
        mToolbar.setBackgroundColor(resources.getColor(R.color.deposit_toolbar_background));
//        mToolbar.showBelowArc(true);
        mToolbar.setTitle(resources.getString(R.string.eways_contact_title_activity));
        mToolbar.setShowTitle(true);
        mToolbar.setShowShop(false);
        mToolbar.showNavigationDrawer(false);
//        mToolbar.setTitleIcon(resources.getDrawable(R.drawable.ic_deposite));
        mToolbar.setTitleTextColor(resources.getColor(R.color.white));
        mToolbar.setTitleTextSize(resources.getDimen(R.dimen.deposit_fragment_title_toolbar_size));
        mToolbar.setShowDeposit(resources.getBoolean(R.bool.credit_toolbar_has_deposit));
        mToolbar.setShowNavigationUp(true);
        mToolbar.setOnBackClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideKeyboard(EwaysContactActivity.this);
                onBackPressed();
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 1) {
            INavigationController member = (INavigationController) mAdapter.instantiateItem(mPager, mPager.getCurrentItem());
            if (!member.onBackPress()) {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }
}
