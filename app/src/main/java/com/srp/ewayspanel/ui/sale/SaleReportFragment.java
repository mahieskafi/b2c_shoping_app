package com.srp.ewayspanel.ui.sale;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.srp.eways.di.DIMain;
import com.srp.eways.ui.navigation.NavigationMemberFragment;
import com.srp.eways.ui.view.charge.TabOptionsView;
import com.srp.eways.ui.view.charge.a.ChargeOptionsTabViewA;
import com.srp.eways.ui.view.toolbar.WeiredToolbar;
import com.srp.ewayspanel.R;
import com.srp.ewayspanel.ui.sale.detail.SaleDetailFragment;
import com.srp.ewayspanel.ui.sale.summary.SaleSummaryFragment;

import java.util.ArrayList;
import java.util.List;

import ir.abmyapp.androidsdk.IABResources;


public class SaleReportFragment extends NavigationMemberFragment<SaleReportViewModel> implements TabOptionsView.ChargeOptionsTabViewListener {


    public static SaleReportFragment newInstance() {
        return new SaleReportFragment();
    }

    private WeiredToolbar mToolbar;
    private ViewPager mPager;
    private SaleReportPagerAdapter mAdapter;


    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mToolbar = view.findViewById(R.id.salereport_toolbar);
        mPager = view.findViewById(R.id.salereport_pager);
        final ChargeOptionsTabViewA chargeOptionsTabViewA = view.findViewById(R.id.salereport_tabview);

        setupToolbar();

        String[] tabTitles = new String[]{getString(R.string.salereport_detail), getString(R.string.salereport_summary)};
        int[] tabIcons = new int[]{R.drawable.ic_sale_report_tabview, R.drawable.ic_salereport_tabview_detail};

        List<TabOptionsView.TabItem> tabItems = new ArrayList<>();
        for (int i = 0; i < tabTitles.length; ++i) {
            tabItems.add(new TabOptionsView.TabItem(tabTitles[i], tabIcons[i], tabTitles[i]));
        }
        chargeOptionsTabViewA.setTabItems(tabItems);

        chargeOptionsTabViewA.setUnselectedTabColor(getResources().getColor(R.color.salereport_tab_unselected_color));
        chargeOptionsTabViewA.setTabBackgroundColor(getResources().getColor(R.color.salereport_tab_background_color));
        chargeOptionsTabViewA.setOnChargeTabClickListener(this);
        chargeOptionsTabViewA.setSelectedTab(0);

        mAdapter = new SaleReportPagerAdapter(getChildFragmentManager());
        mPager.setAdapter(mAdapter);

        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                chargeOptionsTabViewA.setSelectedTab(1 - position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }

        });
        mPager.setCurrentItem(1);
    }


    @Override
    public void onChargeTabClicked(int tabIndex, TabOptionsView.TabItem tabItem) {
        mPager.setCurrentItem(1 - tabIndex);

    }

    private static class SaleReportPagerAdapter extends FragmentPagerAdapter {

        public SaleReportPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public Fragment getItem(int position) {
            switch(position){

                case 0 :
                    return SaleDetailFragment.Companion.newInstance();
                case 1 :
                    return SaleSummaryFragment.newInstance();
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
        return R.layout.sale_report;
    }

    @Override
    public SaleReportViewModel acquireViewModel() {
        return DIMain.getViewModel(SaleReportViewModel.class);
    }

    private void setupToolbar() {

        IABResources resources = DIMain.getABResources();

        mToolbar.setBackgroundToolbarColor(resources.getColor(R.color.saleReport_toolbar_background_color));
        mToolbar.setBackgroundColor(resources.getColor(R.color.saleReport_toolbar_background_color));

        mToolbar.setTitle(resources.getString(com.srp.eways.R.string.salereport_toolbar_title));
        mToolbar.setShowTitle(true);
        mToolbar.setShowShop(false);
        mToolbar.showNavigationDrawer(true);
          mToolbar.setOnNavigationDrawerClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                toggleDrawer();
            }

        });
        mToolbar.setTitleTextColor(resources.getColor(R.color.white));
        mToolbar.setTitleTextSize(resources.getDimen(R.dimen.salereport_title_toolbar_size));
        mToolbar.setShowDeposit(resources.getBoolean(com.srp.eways.R.bool.credit_toolbar_has_deposit));
        mToolbar.setShowNavigationUp(true);
        mToolbar.setOnBackClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

}
