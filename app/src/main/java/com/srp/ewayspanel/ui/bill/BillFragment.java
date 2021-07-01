package com.srp.ewayspanel.ui.bill;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.srp.eways.base.BaseViewModel;
import com.srp.eways.di.DIMain;
import com.srp.eways.ui.bill.MainBillFragment;
import com.srp.eways.ui.bill.MainBillFragmentPagerAdapter;
import com.srp.eways.ui.bill.MainBillViewModel;
import com.srp.eways.ui.bill.inquiry.BillInquiryFragment;
import com.srp.eways.ui.bill.payment.BillPaymentFragment;
import com.srp.eways.ui.bill.paymenttype.BillPaymentTypeViewModel;
import com.srp.eways.ui.bill.report.BillReportFragment;
import com.srp.eways.ui.main.MainRootIds;
import com.srp.eways.ui.navigation.NavigationViewType;
import com.srp.eways.ui.view.toolbar.WeiredToolbar;
import com.srp.ewayspanel.R;
import com.srp.ewayspanel.di.DI;
import com.srp.ewayspanel.model.shopcart.ShopCartItemModel;
import com.srp.ewayspanel.model.storepage.filter.FilterProductRequest;
import com.srp.ewayspanel.ui.bill.archive.BillArchiveListFragment;
import com.srp.ewayspanel.ui.shopcart.ShopCartFragment;
import com.srp.ewayspanel.ui.shopcart.ShopCartViewModel;
import com.srp.ewayspanel.ui.store.search.SearchFragment;

import java.util.ArrayList;

/**
 * Created by ErfanG on 4/14/2020.
 */
public class BillFragment extends MainBillFragment {

    static final int BILL_PAYMENT_FRAGMENT = 0;
    static final int BILL_INQUIRY_FRAGMENT = 1;
    static final int BILL_ARCHIVED_FRAGMENT = 2;
    static final int BILL_REPORT_FRAGMENT = 3;

    private BillPaymentTypeViewModel billPaymentTypeViewModel = DIMain.getViewModel(BillPaymentTypeViewModel.Companion.getInstance().getClass());

    public static BillFragment newInstance() {
        BillFragment fragment = new BillFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public MainBillFragmentPagerAdapter getPagerAdapter() {
        return new BillFragmentPagerAdapter(getChildFragmentManager(), getContext(), new OnPageChangeListener() {

            @Override
            public void onPageChanged(int pageNumber) {
                goToPage(pageNumber);
            }

            @Override
            public Fragment getFragment(int fragmentId) {
                switch (fragmentId) {
                    case BILL_REPORT_FRAGMENT:
                        return BillReportFragment.newInstance();

                    case BILL_ARCHIVED_FRAGMENT:
                        return BillArchiveListFragment.newInstance(this);

                    case BILL_INQUIRY_FRAGMENT:
                        return BillInquiryFragment.newInstance(this);

                    case BILL_PAYMENT_FRAGMENT: {
                        BillPaymentFragment billPaymentFragment = BillPaymentFragment.newInstance();
                        billPaymentFragment.setPageChangeListener(this);
                        return billPaymentFragment;
                    }

                }
                return null;
            }
        });
    }

    @Override
    public void setupToolbar() {
        super.setupToolbar();
        mToolbar.setShowShop(true);


        mUserInfoViewModel.getCreditLiveData().observe(this, new Observer<Long>() {

            @Override
            public void onChanged(Long deposit) {
                if (deposit != null) {
                    mToolbar.setDeposit(deposit);
                }
            }

        });

        mToolbar.setOnSearchClickListener(new WeiredToolbar.SearchTextListener() {
            @Override
            public void onSearchListener(String text) {
                FilterProductRequest filterProductRequest = new FilterProductRequest();
                filterProductRequest.setText(text);

                openFragment(SearchFragment.newInstance(filterProductRequest));
            }
        });

        DI.getViewModel(ShopCartViewModel.class).getShopCartProductList().observe(this, new Observer<ArrayList<ShopCartItemModel>>() {
            @Override
            public void onChanged(ArrayList<ShopCartItemModel> shopCartItemModels) {
                mToolbar.setProductCount(shopCartItemModels.size());
            }
        });

        mToolbar.setShopIconAction(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                v.setClickable(false);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        v.setClickable(true);
                    }
                }, 400);
                if (!DI.getViewModel(ShopCartViewModel.class).hasAnythingToChange()) {
                    openFragment(ShopCartFragment.Companion.newInstance(),
                            NavigationViewType.NAVIGATION_VIEW_TYPE_HAS_TOOLBAR);
                } else {
                    Toast.makeText(getContext(), DI.getABResources().getString(R.string.shop_cart_product_list_is_updating_error_message), Toast.LENGTH_SHORT).show();
                }
            }
        });

        mToolbar.setDepositActionClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSwitchRoot(MainRootIds.ROOT_DEPOSIT);
            }
        });
    }

    @Override
    public BaseViewModel acquireViewModel() {
        return DI.getViewModel(MainBillViewModel.class);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            billPaymentTypeViewModel.setClearBillInput(true);
            mToolbar.clearSearch();
        }
    }
}
