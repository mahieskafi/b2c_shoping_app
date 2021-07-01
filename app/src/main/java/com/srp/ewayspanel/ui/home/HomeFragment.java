package com.srp.ewayspanel.ui.home;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.SparseArrayCompat;
import androidx.lifecycle.Observer;

import com.srp.eways.base.BaseViewModel;
import com.srp.eways.di.DIMain;
import com.srp.eways.ui.charge.ChargeViewModel;
import com.srp.eways.ui.navigation.BackStackMember;
import com.srp.eways.ui.navigation.NavigationFragment;
import com.srp.eways.ui.view.LoadingStateView;
import com.srp.eways.util.LinearLayoutSoftKeyboardDetector;
import com.srp.ewayspanel.R;
import com.srp.ewayspanel.di.DI;
import com.srp.ewayspanel.ui.bill.BillFragment;
import com.srp.ewayspanel.ui.charge.ChargeFragment;
import com.srp.ewayspanel.ui.club.ClubFragment;
import com.srp.ewayspanel.ui.landing.LandingFragment;
import com.srp.ewayspanel.ui.shopcart.ShopCartViewModel;
import com.srp.ewayspanel.ui.store.StoreFragment;
import com.srp.ewayspanel.ui.view.bottomnavigation.BottomNavigationView;
import com.srp.eways.util.Utils;

import static com.srp.eways.ui.main.MainRootIds.ROOT_HOME;
import static com.srp.eways.ui.main.MainRootIds.ROOT_DEPOSIT;
import static com.srp.eways.util.Constants.IS_PAGE_TRANSACTION_EXECUTED_KEY;
import static com.srp.ewayspanel.ui.bill.BillFragment.BILL_TAB_INDEX;
import static com.srp.ewayspanel.ui.home.HomeRootIds.ROOT_BILL;
import static com.srp.ewayspanel.ui.home.HomeRootIds.ROOT_CHARGE;
import static com.srp.ewayspanel.ui.home.HomeRootIds.ROOT_CLUB;
import static com.srp.ewayspanel.ui.home.HomeRootIds.ROOT_STORE;
import static com.srp.ewayspanel.ui.store.StoreFragment.STORE_TAB_INDEX;
import static com.srp.ewayspanel.ui.view.landing.LandingUtil.SERVICE_BILL_CODE;
import static com.srp.ewayspanel.ui.view.landing.LandingUtil.SERVICE_BILL_INQUIRY_CODE;
import static com.srp.ewayspanel.ui.view.landing.LandingUtil.SERVICE_MOBILE_LIST_CODE;
import static com.srp.ewayspanel.ui.view.landing.LandingUtil.STORE_MOBILE_LIST_TAB_INDEX;

public class HomeFragment extends NavigationFragment<BaseViewModel> {

    public static String TAB_INDEX_KEY = "tab_index_key";

    private BottomNavigationView mBottomNavigationView;
    private int mTabIndex = ROOT_CHARGE;
    private int mSubTabIndex = 0;
    private boolean mIsPageTransactionsExecuted = true;

    public static HomeFragment newInstance(int tabIndex) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putInt(TAB_INDEX_KEY, tabIndex);

        fragment.setArguments(args);
        return fragment;
    }

    public void setSubTabIndex(int subTabIndex) {
        mSubTabIndex = subTabIndex;
    }

    private BottomNavigationView.BottomNavigationClickListener mOnBottomNavigationListener =
            new BottomNavigationView.BottomNavigationClickListener() {
                @Override
                public void onTabClicked(int tabIndex) {
                    Utils.hideKeyboard(getActivity());

                    mIsPageTransactionsExecuted = false;
                    getBackStackMember(tabIndex).putBundle(getFragmentBundle());

                    onSwitchRoot(tabIndex);

                }

                @Override
                public void onTabReselected(int tabIndex) {

                }
            };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            mTabIndex = getArguments().getInt(TAB_INDEX_KEY);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mIsPageTransactionsExecuted = true;

        mBottomNavigationView = view.findViewById(R.id.bottom_nav);
        mBottomNavigationView.setBottomNavigationClickListener(mOnBottomNavigationListener);
        mBottomNavigationView.setSelectedTab(mTabIndex);

        LinearLayoutSoftKeyboardDetector mainLayout = view.findViewById(R.id.root_view);
        mainLayout.setListener(new LinearLayoutSoftKeyboardDetector.Listener() {
            @Override
            public void onSoftKeyboardShown(boolean isShowing) {
                if (isShowing) {
                    mBottomNavigationView.setVisibility(View.GONE);

                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mBottomNavigationView.setVisibility(View.VISIBLE);
                        }
                    }, 80);
                }
            }
        });


        final LoadingStateView loadingStateView = view.findViewById(com.srp.eways.R.id.loadingstateview);
        final FrameLayout loadingContainer = view.findViewById(com.srp.eways.R.id.container_loading);

        final ShopCartViewModel shopCartViewModel = DI.getViewModel(ShopCartViewModel.INSTANCE.getClass());
        shopCartViewModel.isInventoryForOpenFragmentProgress().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                if (isLoading != null) {
                    if (isLoading) {
                        loadingContainer.setVisibility(View.VISIBLE);

                        loadingStateView.setStateAndDescription(LoadingStateView.STATE_LOADING,
                                "",
                                false);
                    } else {
                        loadingContainer.setVisibility(View.GONE);
                    }
                    shopCartViewModel.consumeInventoryForOpenFragmentProgress();
                }
            }
        });

    }


    @Override
    protected int getRootTabId() {
        return mTabIndex;
    }

    @Override
    protected SparseArrayCompat<BackStackMember> createNavigationRoots() {
        SparseArrayCompat<BackStackMember> roots = new SparseArrayCompat<>(BottomNavigationView.TAB_COUNT);

        roots.put(ROOT_CLUB, new BackStackMember(ClubFragment.newInstance()));
        roots.put(ROOT_CHARGE, new BackStackMember(ChargeFragment.newInstance(), getFragmentBundle()));
        roots.put(ROOT_BILL, new BackStackMember(BillFragment.newInstance(), getBillFragmentBundle()));
        roots.put(ROOT_STORE, new BackStackMember(StoreFragment.newInstance(), getStoreFragmentBundle()));
        roots.put(ROOT_HOME, new BackStackMember(LandingFragment.newInstance()));

        return roots;
    }

    private Bundle getFragmentBundle() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(IS_PAGE_TRANSACTION_EXECUTED_KEY, mIsPageTransactionsExecuted);
        return bundle;
    }

    private Bundle getBillFragmentBundle() {
        Bundle bundle = new Bundle();
        if (mTabIndex == ROOT_BILL) {
            bundle.putInt(BILL_TAB_INDEX, mSubTabIndex);
        }
        bundle.putBoolean(IS_PAGE_TRANSACTION_EXECUTED_KEY, mIsPageTransactionsExecuted);
        return bundle;
    }

    private Bundle getStoreFragmentBundle() {
        Bundle bundle = new Bundle();
        if (mTabIndex == ROOT_STORE) {
            bundle.putInt(STORE_TAB_INDEX, mSubTabIndex);
        }
        bundle.putBoolean(IS_PAGE_TRANSACTION_EXECUTED_KEY, mIsPageTransactionsExecuted);
        return bundle;
    }

    @Override
    public BaseViewModel acquireViewModel() {
        return new BaseViewModel();
    }

    protected int getFragmentHostContainerId() {
        return R.id.f_container;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }

    protected int getNavigationType() {
        return NAVIGATION_TYPE_TAB;
    }

    @Override
    public int getNavigationViewType() {
        return NAVIGATION_VIEW_TYPE_HAS_TOOLBAR_AND_BOTTOM_NAVIGATION;
    }

    @Override
    public void onSwitchRoot(int id) {

        if (id == ROOT_HOME) {
            getParentNavigationController().onSwitchRoot(id);
        } else if (id == ROOT_DEPOSIT) {
            getParentNavigationController().onSwitchRoot(id);
        } else {
            super.onSwitchRoot(id);
        }

        if (id == getRootTabId()) {
            if (mBottomNavigationView.getCurrentTabIndex() != mTabIndex) {
                mBottomNavigationView.setSelectedTab(mTabIndex);
            }
        }

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (!hidden) {
            DI.getViewModel(ShopCartViewModel.class).callGetShopCartList(null);
        } else {
            DIMain.getViewModel(ChargeViewModel.class).makeInstanceNull();
        }
    }
}
