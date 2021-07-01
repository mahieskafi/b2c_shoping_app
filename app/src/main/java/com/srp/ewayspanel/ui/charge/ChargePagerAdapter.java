package com.srp.ewayspanel.ui.charge;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.srp.eways.ui.charge.MainChargePagerAdapter;
import com.srp.eways.ui.charge.buy.b.BuyChargeFragmentB;
import com.srp.ewayspanel.ui.charge.buy.BuyChargeFragmentA;

public class ChargePagerAdapter extends MainChargePagerAdapter {

    public ChargePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getChargeFragment(int designModel) {
        if (designModel == 0) {
            return BuyChargeFragmentA.newInstance();
        } else {
            return BuyChargeFragmentB.newInstance();
        }
    }
}
