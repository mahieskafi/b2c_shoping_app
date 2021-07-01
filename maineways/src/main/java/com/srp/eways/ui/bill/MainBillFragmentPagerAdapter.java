package com.srp.eways.ui.bill;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.srp.eways.R;
import com.srp.eways.di.DIMain;

import ir.abmyapp.androidsdk.IABResources;

public abstract class MainBillFragmentPagerAdapter extends FragmentPagerAdapter {

    private static final int[] TABBAR_TITLES = {R.string.bill_page_tabbar_tab_1_title, R.string.bill_page_tabbar_tab_2_title
            , R.string.bill_page_tabbar_tab_3_title, R.string.bill_page_tabbar_tab_4_title};

    private Context mContext;

    public MainBillFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mContext = context;
    }

    @Override
    public int getCount() {
        return TABBAR_TITLES.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        IABResources abResources = DIMain.getABResources();

        return abResources.getString(TABBAR_TITLES[position]);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
