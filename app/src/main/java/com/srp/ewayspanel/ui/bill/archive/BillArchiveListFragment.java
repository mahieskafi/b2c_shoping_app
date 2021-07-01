package com.srp.ewayspanel.ui.bill.archive;

import android.os.Bundle;

import com.srp.eways.ui.bill.MainBillFragment;
import com.srp.eways.ui.bill.archive.MainBillArchiveListFragment;
import com.srp.ewayspanel.di.DI;

public class BillArchiveListFragment extends MainBillArchiveListFragment<BillArchiveViewModel> {

    public BillArchiveListFragment(MainBillFragment.OnPageChangeListener pageChangeListener) {
        mPageChangeListener = pageChangeListener;
    }

    public static BillArchiveListFragment newInstance(MainBillFragment.OnPageChangeListener pageChangeListener) {

        Bundle args = new Bundle();

        BillArchiveListFragment fragment = new BillArchiveListFragment(pageChangeListener);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public BillArchiveViewModel acquireViewModel() {
        return DI.getViewModel(BillArchiveViewModel.class);
    }
}
