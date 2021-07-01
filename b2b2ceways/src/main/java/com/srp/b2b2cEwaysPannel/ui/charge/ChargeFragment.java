package com.srp.b2b2cEwaysPannel.ui.charge;

import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.Observer;

import com.srp.b2b2cEwaysPannel.R;
import com.srp.b2b2cEwaysPannel.di.DI;
import com.srp.eways.ui.charge.MainChargePagerAdapter;
import com.srp.eways.ui.charge.MainChargeFragment;
import com.srp.eways.ui.navigation.INavigationController;

import ir.abmyapp.androidsdk.IABResources;

/**
 * Created by ErfanG on 3/9/2020.
 */
public class ChargeFragment extends MainChargeFragment {


    public static ChargeFragment newInstance() {
        ChargeFragment fragment = new ChargeFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public MainChargePagerAdapter getAdapter() {
        return new ChargePagerAdapter(getChildFragmentManager());
    }

    @Override
    public void setupWeiredToolbar() {
        IABResources resources = DI.getABResources();

        weiredToolbar.setTitle(resources.getString(R.string.buycharge_title));
        weiredToolbar.setShowTitle(true);
        weiredToolbar.setShowDeposit(true);
        weiredToolbar.setShowShop(false);
        weiredToolbar.setBackgroundColor(resources.getColor(R.color.buycharge_toolbar_background));

        weiredToolbar.setOnNavigationDrawerClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                toggleDrawer();
            }

        });

        getViewModel().getCreditLiveData().observe(this, new Observer<Long>() {

            @Override
            public void onChanged(Long deposit) {
                if (deposit != null) {
                    weiredToolbar.setDeposit(deposit);
                }
            }

        });
    }

    @Override
    public boolean onBackPress() {
        INavigationController member = (INavigationController) getPagerAdapter().instantiateItem(getViewPager(), getViewPager().getCurrentItem());

        return member.onBackPress();
    }


}
