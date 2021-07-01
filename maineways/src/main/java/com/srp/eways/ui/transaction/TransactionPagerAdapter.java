package com.srp.eways.ui.transaction;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

/**
 * Created by Eskafi on 2/10/2020.
 */
public abstract class TransactionPagerAdapter extends FragmentStatePagerAdapter {

    public TransactionPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public abstract Fragment getItem(int position);

    @Override
    public abstract int getCount();
}
