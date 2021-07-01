package com.srp.eways.ui.deposit;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Observer;
import androidx.viewpager.widget.ViewPager;

import com.srp.eways.ui.deposit.increasedeposit.IncreaseDepositViewModel;
import com.srp.eways.ui.navigation.NavigationMemberFragment;
import com.srp.eways.ui.view.LoadingStateView;
import com.srp.eways.ui.view.toolbar.WeiredToolbar;
import com.srp.eways.R;
import com.srp.eways.ui.charge.MainChargePagerAdapter;
import com.srp.eways.di.DIMain;
import com.srp.eways.ui.deposit.increasedeposit.IncreaseDepositFragment;
import com.srp.eways.ui.login.UserInfoViewModel;
import com.srp.eways.ui.main.MainRootIds;
import com.srp.eways.ui.transaction.deposit.DepositTransactionFragment;
import com.srp.eways.ui.transaction.deposit.DepositTransactionViewModel;
import com.srp.eways.ui.view.charge.TabOptionsView;
import com.srp.eways.ui.view.charge.a.ChargeOptionsTabViewA;
import com.srp.eways.util.Utils;

import java.util.ArrayList;
import java.util.List;

import ir.abmyapp.androidsdk.IABResources;

import static com.srp.eways.ui.deposit.increasedeposit.IncreaseDepositFragment.AMOUNT_OF_INCREASE;

/**
 * Created by Eskafi on 8/28/2019.
 */
public class DepositFragment extends NavigationMemberFragment<UserInfoViewModel> implements TabOptionsView.ChargeOptionsTabViewListener {

    public static DepositFragment sDepositFragmentInstance;

    public static DepositFragment newInstance() {
        if (sDepositFragmentInstance == null) {
            sDepositFragmentInstance = new DepositFragment();
        }
        return sDepositFragmentInstance;
    }

    public static DepositFragment newInstance(long amountOfIncrease) {
//        DepositFragment fragment = new DepositFragment();
        if (sDepositFragmentInstance == null) {
            sDepositFragmentInstance = new DepositFragment();
        }

        Bundle args = new Bundle();
        args.putSerializable(AMOUNT_OF_INCREASE, amountOfIncrease);

        sDepositFragmentInstance.setArguments(args);
        return sDepositFragmentInstance;
    }

    private UserInfoViewModel mViewModel;
    private DepositTransactionViewModel mDepositTransactionViewModel;
    private IncreaseDepositViewModel mIncreaseDepositViewModel;

    private WeiredToolbar mToolbar;

    private TextView mTxtDepositValue;
    private ViewPager mPager;

    private PagerAdapter mAdapter;

    private Observer<Boolean> getCreditChangedObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(Boolean aBoolean) {
            // if increase credit is successful, goto homePage
            if (aBoolean != null) {
                if (aBoolean) {
                    mViewModel.onCreditIncreasedConsumed();
                    mViewModel.invalidateCredit();
                    mViewModel.getCredit();

                    if (getArguments() != null) {
                        onBackPressed();
                    } else {
                        onSwitchRoot(MainRootIds.ROOT_HOME);
                    }
                }
                if (mDepositTransactionViewModel != null) {
                    mDepositTransactionViewModel.clearData();
                    mDepositTransactionViewModel.invalidateDepositTransaction();
                    mDepositTransactionViewModel.setIsNextPageLoadData(false);
                    mDepositTransactionViewModel.loadMore();
                }

            }
        }
    };

    @Override
    public void onViewCreated(@NonNull final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final IABResources resources = DIMain.getABResources();

        mViewModel = getViewModel();
        mDepositTransactionViewModel = DIMain.getViewModel(DepositTransactionViewModel.class);
        mIncreaseDepositViewModel = DIMain.getViewModel(IncreaseDepositViewModel.class);

        final ChargeOptionsTabViewA chargeOptionsTabViewA = view.findViewById(R.id.tab_view);
        final LoadingStateView loadingStateView = view.findViewById(R.id.loadingstateview);
        final FrameLayout loadingContainer = view.findViewById(R.id.container_loading);

        mViewModel.getCreditLiveData().observe(this, getGetCreditObserver());
        mViewModel.isCreditIncreased().observe(this, getCreditChangedObserver);
        mIncreaseDepositViewModel.isLoading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                if (isLoading != null) {
                    if (isLoading) {
                        loadingContainer.setVisibility(View.VISIBLE);

                        loadingStateView.setStateAndDescription(LoadingStateView.STATE_LOADING,
                                resources.getString(R.string.loading_message),
                                false);
                    } else {
                        loadingContainer.setVisibility(View.GONE);
                    }
                }
            }
        });

        mIncreaseDepositViewModel.getGoToDepositTransaction().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isGoToDepositTransaction) {

                if (isGoToDepositTransaction != null) {
                    if (isGoToDepositTransaction) {
                        mPager.setCurrentItem(0);
                        mIncreaseDepositViewModel.consumeGoToDepositTransaction();
                    }
                }

            }
        });


        mToolbar = view.findViewById(R.id.toolbar);
        setupToolbar();

        String[] tabTitles = new String[]{getString(R.string.deposit_payment_tab), getString(R.string.deposit_transaction_tab)};
        int[] tabIcons = new int[]{R.drawable.deposit_payment_tab_icon, R.drawable.deposit_transaction_tab_icon};

        List<TabOptionsView.TabItem> tabItems = new ArrayList<>();
        for (int i = 0; i < tabTitles.length; ++i) {
            tabItems.add(new TabOptionsView.TabItem(tabTitles[i], tabIcons[i], tabTitles[i]));
        }
        chargeOptionsTabViewA.setTabItems(tabItems);

        chargeOptionsTabViewA.setUnselectedTabColor(getResources().getColor(R.color.deposit_fragmnet_charge_option_tab_unselected_color));
        chargeOptionsTabViewA.setTabBackgroundColor(getResources().getColor(R.color.deposit_fragmnet_charge_option_tab_background_color));
        chargeOptionsTabViewA.setOnChargeTabClickListener(this);
        chargeOptionsTabViewA.setSelectedTab(0);

        mTxtDepositValue = view.findViewById(R.id.txt_value);

        mPager = view.findViewById(R.id.pager);

        if (getArguments() != null) {
            mAdapter = new PagerAdapter(getChildFragmentManager(), getArguments().getLong(AMOUNT_OF_INCREASE));
        } else {
            mAdapter = new PagerAdapter(getChildFragmentManager());
        }

        mPager.setAdapter(mAdapter);
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                Utils.hideKeyboard(getActivity());

                chargeOptionsTabViewA.setSelectedTab(1 - position);

                refreshChargeTransactionList(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }

        });

        mPager.setCurrentItem(1);
    }

    private void setupToolbar() {
        IABResources resources = DIMain.getABResources();

        mToolbar.setBackgroundToolbarColor(resources.getColor(R.color.deposit_toolbar_background));
        mToolbar.setBackgroundColor(resources.getColor(R.color.deposit_toolbar_background));
//        mToolbar.showBelowArc(true);
        mToolbar.setTitle(resources.getString(R.string.deposit_credit_title_fragment));
        mToolbar.setShowTitle(true);
        mToolbar.setShowShop(false);
        mToolbar.showNavigationDrawer(true);
//        mToolbar.setTitleIcon(resources.getDrawable(R.drawable.ic_deposite));
        mToolbar.setTitleTextColor(resources.getColor(R.color.white));
        mToolbar.setTitleTextSize(resources.getDimen(R.dimen.deposit_fragment_title_toolbar_size));
        mToolbar.setShowDeposit(resources.getBoolean(R.bool.credit_toolbar_has_deposit));

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

    private Observer<Long> getGetCreditObserver() {
        return new Observer<Long>() {
            @Override
            public void onChanged(Long deposit) {
                if (deposit != null) {
                    mTxtDepositValue.setText(Utils.toPersianPriceNumber(String.valueOf(deposit)));
                }
            }
        };
    }

    @Override
    public UserInfoViewModel acquireViewModel() {
        return DIMain.getViewModel(UserInfoViewModel.class);
    }

    @Override
    public int getActionMenu() {
        return 0;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_deposit;
    }

    @Override
    public void onChargeTabClicked(int tabIndex, TabOptionsView.TabItem tabItem) {
        mPager.setCurrentItem(1 - tabIndex);
    }

    private void refreshChargeTransactionList(int position) {
        if (getArguments() != null) {
            mAdapter = new PagerAdapter(getChildFragmentManager(), getArguments().getLong(AMOUNT_OF_INCREASE));
        } else {
            mAdapter = new PagerAdapter(getChildFragmentManager());
        }
        Fragment selectedFragment = mAdapter.getItem(position);
        if (selectedFragment instanceof MainChargePagerAdapter.PagerFragmentLifeCycle) {
            MainChargePagerAdapter.PagerFragmentLifeCycle fragmentToShow = (MainChargePagerAdapter.PagerFragmentLifeCycle) selectedFragment;
            fragmentToShow.onResumePagerFragment();
        }
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        IncreaseDepositViewModel increaseDepositViewModel = DIMain.getViewModel(IncreaseDepositViewModel.class);

        if (!hidden) {
            refreshChargeTransactionList(mPager.getCurrentItem());

            DepositTransactionViewModel vm = DIMain.getViewModel(DepositTransactionViewModel.class);


            vm.clearData();
            vm.setIsNextPageLoadData(false);
            vm.loadMore();

            if (increaseDepositViewModel != null) {
                increaseDepositViewModel.clearData(true);
            }
        } else {
            setArguments(null);
            if (increaseDepositViewModel != null) {
                mIncreaseDepositViewModel.setLoading(false);
                increaseDepositViewModel.clearData(false);
            }
        }
    }


    @Override
    protected void getDataFromServer() {
        mViewModel.getCredit();
    }

    private class PagerAdapter extends FragmentPagerAdapter {

        long mAmountOfIncrease = 0;

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public PagerAdapter(FragmentManager fm, long amountOfIncrease) {
            super(fm);
            mAmountOfIncrease = amountOfIncrease;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return DepositTransactionFragment.newInstance(false);
                case 1:
                    if (mAmountOfIncrease != 0) {
                        return IncreaseDepositFragment.newInstance(mAmountOfIncrease);
                    }
                    return IncreaseDepositFragment.newInstance();
            }

            throw new RuntimeException("Pager position out of bounds.");
        }

        @Override
        public int getCount() {
            return 2;
        }

    }

}
