package com.srp.ewayspanel.ui.bill;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.srp.eways.ui.bill.MainBillFragment;
import com.srp.eways.ui.bill.MainBillFragmentPagerAdapter;
import com.srp.eways.ui.bill.inquiry.BillInquiryFragment;
import com.srp.eways.ui.bill.navigation.BillNavigationFragment;
import com.srp.eways.ui.bill.payment.BillPaymentFragment;
import com.srp.eways.ui.bill.report.BillReportFragment;
import com.srp.ewayspanel.ui.bill.archive.BillArchiveListFragment;

public class BillFragmentPagerAdapter extends MainBillFragmentPagerAdapter {

    private MainBillFragment.OnPageChangeListener mListener;

    public BillFragmentPagerAdapter(FragmentManager fm, Context context, MainBillFragment.OnPageChangeListener onPageChangeListener) {
        super(fm, context);

        mListener = onPageChangeListener;
    }

    @Override
    public Fragment getItem(int position) {
        BillNavigationFragment billNavigationFragment;
        switch (position) {
            case 0:
                billNavigationFragment = BillNavigationFragment.newInstance(BillFragment.BILL_REPORT_FRAGMENT);
                billNavigationFragment.setPageChangeListener(mListener);
                return billNavigationFragment;

            case 1:
                billNavigationFragment = BillNavigationFragment.newInstance(BillFragment.BILL_ARCHIVED_FRAGMENT);
                billNavigationFragment.setPageChangeListener(mListener);
                return billNavigationFragment;

            case 2:
                billNavigationFragment = BillNavigationFragment.newInstance(BillFragment.BILL_INQUIRY_FRAGMENT);
                billNavigationFragment.setPageChangeListener(mListener);
                return billNavigationFragment;

            case 3:
                billNavigationFragment = BillNavigationFragment.newInstance(BillFragment.BILL_PAYMENT_FRAGMENT);
                billNavigationFragment.setPageChangeListener(mListener);
                return billNavigationFragment;

        }
        return null;
    }
}
