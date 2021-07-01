package com.srp.eways.ui.charge;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.srp.eways.R;
import com.srp.eways.di.DIMain;
import com.srp.eways.ui.charge.inquiry.ChargeInquiryFragment;
import com.srp.eways.ui.transaction.charge.ChargeTransactionFragment;

import ir.abmyapp.androidsdk.IABResources;

public abstract class MainChargePagerAdapter extends FragmentStatePagerAdapter {

    private static final int[] TABBAR_TITLES = {R.string.charge_weiredtab_tab3_title, R.string.charge_weiredtab_tab2_title
            , R.string.charge_weiredtab_tab1_title};


    public interface PagerFragmentLifeCycle {
        void onResumePagerFragment();
    }

    public MainChargePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;

        int designModel = DIMain.getABResources().getInt(R.integer.abtesting_designmodel);

        switch (position) {
            case 0:
                fragment = ChargeTransactionFragment.newInstance(false);
                break;
            case 1:
                fragment = ChargeInquiryFragment.newInstance();
                break;
            case 2:
                fragment = getChargeFragment(designModel);
                break;

            default:
                throw new RuntimeException("There is no fragment for position = " + position);
        }

        return fragment;
    }

    public abstract Fragment getChargeFragment(int designModel);


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

}
