package com.srp.eways.ui.bill;

import android.animation.ValueAnimator;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.srp.eways.R;
import com.srp.eways.di.DIMain;
import com.srp.eways.ui.bill.archive.MainBillArchiveListFragment;
import com.srp.eways.ui.bill.archive.MainBillArchiveViewModel;
import com.srp.eways.ui.bill.inquiry.BillInquiryFragment;
import com.srp.eways.ui.bill.navigation.BillNavigationFragment;
import com.srp.eways.ui.bill.paymenttype.BillPaymentTypeViewModel;
import com.srp.eways.ui.bill.report.BillReportFragment;
import com.srp.eways.ui.login.UserInfoViewModel;
import com.srp.eways.ui.navigation.INavigationController;
import com.srp.eways.ui.navigation.NavigationMemberFragment;
import com.srp.eways.ui.view.LoadingStateView;
import com.srp.eways.ui.view.toolbar.WeiredToolbar;
import com.srp.eways.util.Constants;

import ir.abmyapp.androidsdk.IABResources;

/**
 * Created by ErfanG on 4/14/2020.
 */
abstract public class MainBillFragment<V extends MainBillViewModel> extends NavigationMemberFragment<V> {

    public static String BILL_TAB_INDEX = "bill_tab_index";

    public interface OnPageChangeListener {

        void onPageChanged(int pageNumber);

        Fragment getFragment(int fragmentId);
    }

    protected WeiredToolbar mToolbar;
    protected TabLayout mTabBar;
    protected ViewPager mPager;
    protected LoadingStateView mLoadingStateView;
    protected FrameLayout mLoadingContainer;

    protected MainBillFragmentPagerAdapter mPagerAdapter;

    protected V mViewModel;
    protected UserInfoViewModel mUserInfoViewModel;

    private BillPaymentTypeViewModel billPaymentTypeViewModel = DIMain.getViewModel(BillPaymentTypeViewModel.Companion.getInstance().getClass());

    @Override
    public int getLayoutId() {
        return R.layout.fragment_bill;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewModel = acquireViewModel();
        mUserInfoViewModel = DIMain.getViewModel(UserInfoViewModel.class);

        mToolbar = view.findViewById(R.id.toolbar);
        mTabBar = view.findViewById(R.id.tab_layout);
        mPager = view.findViewById(R.id.view_pager);

        mLoadingContainer = view.findViewById(R.id.container_loading_main);
        mLoadingStateView = view.findViewById(R.id.loadingstateview_main);

        mPagerAdapter = getPagerAdapter();
        mPager.setOffscreenPageLimit(0);
        mPager.setAdapter(mPagerAdapter);

        mTabBar.setupWithViewPager(mPager);

        setupToolbar();
        setupTabBar();

        ViewGroup.LayoutParams layoutParams = mTabBar.getLayoutParams();
        if (getArguments() != null && getArguments().getBoolean(Constants.IS_PAGE_TRANSACTION_EXECUTED_KEY)) {
            layoutParams.height = 0;
            mTabBar.setLayoutParams(layoutParams);

            animateIn();
        } else {
            layoutParams.height = DIMain.getABResources().getDimenPixelSize(R.dimen.bill_page_tabbar_height);
            mTabBar.setLayoutParams(layoutParams);
        }


        billPaymentTypeViewModel.getDeepLinkResponseReceivedLiveData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String requestId) {
                if (requestId != null && !requestId.isEmpty()) {
                    ShowLoadingStat(true);
                }
//                else {
//                    mLoadingContainer.setVisibility(View.GONE);
//                }
            }
        });

        billPaymentTypeViewModel.getCheckLoading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                ShowLoadingStat(aBoolean);
            }
        });

        if (getArguments() != null) {
            int tabIndex = getArguments().getInt(BILL_TAB_INDEX,0);

            mPager.setCurrentItem(mTabBar.getTabCount() - 1 - tabIndex);
        }
    }


    public abstract MainBillFragmentPagerAdapter getPagerAdapter();

    public void setupToolbar() {
        IABResources resources = DIMain.getABResources();

        mToolbar.setShowTitle(false);
        mToolbar.setShowDeposit(true);
        mToolbar.setBackgroundColor(resources.getColor(R.color.buycharge_toolbar_background));

        mToolbar.setOnNavigationDrawerClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                toggleDrawer();
            }

        });

    }

    private void setupTabBar() {
        final IABResources abResources = DIMain.getABResources();

        mTabBar.setSelectedTabIndicatorColor(abResources.getColor(R.color.bill_page_tabbar_indicator_color));

        final Typeface selectedTypeface = ResourcesCompat.getFont(getContext(), R.font.iran_yekan_bold);
        final Typeface unSelectedTypeface = ResourcesCompat.getFont(getContext(), R.font.iran_yekan);

        for (int i = 0; i < mTabBar.getTabCount(); i++) {

            TextView textView = new TextView(getContext());
            textView.setTextColor(abResources.getColor(R.color.bill_page_tabbar_unselected_text_color));
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, abResources.getDimenPixelSize(R.dimen.bill_page_tabbar_text_size));
            textView.setGravity(Gravity.CENTER);
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            textView.setTypeface(unSelectedTypeface);
            textView.setText(mTabBar.getTabAt(i).getText());

            mTabBar.getTabAt(i).setCustomView(textView);

        }

        mTabBar.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if ((tab.getCustomView()) != null) {
                    ((TextView) tab.getCustomView()).setTypeface(selectedTypeface);
                    ((TextView) tab.getCustomView()).setTextColor(abResources.getColor(R.color.bill_page_tabbar_selected_text_color));

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if ((tab.getCustomView()) != null) {
                    ((TextView) tab.getCustomView()).setTypeface(unSelectedTypeface);
                    ((TextView) tab.getCustomView()).setTextColor(abResources.getColor(R.color.bill_page_tabbar_unselected_text_color));
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        mPager.setCurrentItem(0);
    }

    public void goToPage(final int pageNumber) {
        if (pageNumber > mTabBar.getTabCount()) {
            throw new IllegalArgumentException();
        }

        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                mTabBar.getTabAt(pageNumber).select();
                mPager.setCurrentItem(pageNumber);

                Object fragment = mPagerAdapter.instantiateItem(mPager, mPager.getCurrentItem());
                if (fragment instanceof BillNavigationFragment) {
                    Fragment billFragment = ((BillNavigationFragment) fragment).getFragment();

                    if (billFragment instanceof MainBillArchiveListFragment) {
                        ((MainBillArchiveListFragment) billFragment).onListResumed();
                    }

                    if (billFragment instanceof BillReportFragment) {
                        ((BillReportFragment) billFragment).getNewData();
                    }
                }
            }
        });

    }

    private void animateIn() {

        int tabBarHeight = DIMain.getABResources().getDimenPixelSize(R.dimen.bill_page_tabbar_height);

        ValueAnimator anim = ValueAnimator.ofInt(0, tabBarHeight);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = mTabBar.getLayoutParams();
                layoutParams.height = val;
                mTabBar.setLayoutParams(layoutParams);
            }
        });
        anim.setDuration(300);
        anim.start();
    }

    @Override
    public boolean onBackPress() {
        INavigationController member = (INavigationController) mPagerAdapter.instantiateItem(mPager, mPager.getCurrentItem());

        return member.onBackPress();
    }

    public void ShowLoadingStat(Boolean isShow){
        if (isShow){
            mLoadingContainer.setVisibility(View.VISIBLE);
            mLoadingStateView.setTextColor(DIMain.getABResources().getColor(R.color.white));
            mLoadingStateView.setStateAndDescription(LoadingStateView.STATE_LOADING, getString(R.string.loading_message), false);

        }else {
            mLoadingContainer.setVisibility(View.GONE);
        }
    }
}
