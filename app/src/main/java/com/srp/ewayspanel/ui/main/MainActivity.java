package com.srp.ewayspanel.ui.main;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.collection.SparseArrayCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;

import com.srp.eways.base.BaseViewModel;
import com.srp.eways.di.DIMain;
import com.srp.eways.di.DIMainCommon;
import com.srp.eways.model.login.UserInfo;
import com.srp.eways.ui.NetworkMonitor;
import com.srp.eways.ui.bill.payment.BillPaymentViewModel;
import com.srp.eways.ui.bill.paymenttype.BillPaymentTypeViewModel;
import com.srp.eways.ui.bill.receipt.ReceiptViewModel;
import com.srp.eways.ui.charge.ChargeViewModel;
import com.srp.eways.ui.charge.MainChargeFragment;
import com.srp.eways.ui.main.CommonMainActivity;
import com.srp.eways.ui.main.MainRootIds;
import com.srp.eways.ui.navigation.BackStackMember;
import com.srp.eways.util.Constants;
import com.srp.eways.util.ToastManager;
import com.srp.ewayspanel.App;
import com.srp.ewayspanel.R;
import com.srp.ewayspanel.di.DI;
import com.srp.eways.ui.aboutus.AboutUsFragment;
import com.srp.eways.ui.deposit.DepositFragment;
import com.srp.eways.ui.deposit.increasedeposit.IncreaseDepositViewModel;
import com.srp.ewayspanel.model.followorder.FollowOrderItem;
import com.srp.ewayspanel.model.login.Address;
import com.srp.ewayspanel.model.storepage.filter.FilterProductRequest;
import com.srp.ewayspanel.model.transaction.order.OrderItem;
import com.srp.ewayspanel.ui.bill.BillFragment;
import com.srp.ewayspanel.ui.charge.ChargeFragment;
import com.srp.ewayspanel.ui.club.ClubFragment;
import com.srp.ewayspanel.ui.contact.ContactFragment;
import com.srp.eways.ui.login.UserInfoViewModel;
import com.srp.ewayspanel.ui.followorder.FollowOrderFragment;
import com.srp.ewayspanel.ui.landing.LandingFragment;
import com.srp.ewayspanel.ui.login.AddressViewModel;
import com.srp.ewayspanel.ui.login.LoginActivity;
import com.srp.ewayspanel.ui.sale.SaleReportFragment;
import com.srp.eways.util.Utils;
import com.srp.ewayspanel.ui.shopcart.ShopCartViewModel;
import com.srp.ewayspanel.ui.store.filter.ProductListFragment;
import com.srp.ewayspanel.ui.store.product.detail.ProductDetailFragment;
import com.srp.ewayspanel.ui.transaction.TransactionFragment;
import com.srp.ewayspanel.ui.transaction.order.OrderTransactionViewModel;
import com.srp.ewayspanel.ui.transaction.order.detail.OrderTransactionDetailFragment;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Eskafi on 7/20/2019.
 */
public class MainActivity extends CommonMainActivity<BaseViewModel> implements MainRootIds, GetBannerLink, MainChargeFragment.GetBannerLinkCharge {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private UserInfoViewModel mUserInfoViewModel;

    private NetworkMonitor mNetworkMonitor;

    private boolean isFirstBackPressed = true;
    private Toast toast;
    private boolean mIsNeededToLogin = false;

    public static Intent newIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public BaseViewModel getViewModel() {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        deepLink();

        mNetworkMonitor = new NetworkMonitor(this, new NetworkMonitor.OnConnectivityChangedListener() {
            @Override
            public void onConnectivityChanged(boolean isConnected) {
                if (isConnected) {
                    mUserInfoViewModel.getCredit();
                }
            }
        });

        mDrawerLayout = findViewById(R.id.drawerlayout);
        mDrawerToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout,
                0,
                0) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);

            }

            public void onDrawerOpened(View drawerView) {
                Utils.hideKeyboard(MainActivity.this);

                super.onDrawerOpened(drawerView);
            }
        };
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        mUserInfoViewModel = DI.getViewModel(UserInfoViewModel.class);

        mUserInfoViewModel.getCredit();

        final AddressViewModel mAddressViewModel = DI.getViewModel(AddressViewModel.class);

        mAddressViewModel.consumeAddressListLiveData();
        mAddressViewModel.consumeInsertStatusLiveData();

        mUserInfoViewModel.getValidTokenErrorLiveData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String errorMessage) {
                if (errorMessage != null) {
                    gotoLogin();
                }
            }
        });

        mAddressViewModel.getAllAddresses();
        mAddressViewModel.getAddressListLiveData().observe(this, new Observer<List<Address>>() {
            @Override
            public void onChanged(List<Address> addresses) {
                ShopCartViewModel mShopCartViewModel = DI.getViewModel(ShopCartViewModel.class);

                if (addresses != null && addresses.size() > 0) {
                    if (mShopCartViewModel != null) {
                        mShopCartViewModel.setSelectedProvinceAndCity(addresses.get(0).getStateId(),
                                addresses.get(0).getCityId());
                    }
                    mAddressViewModel.getAddressListLiveData().removeObserver(this);
                } else {
                    UserInfo userInfo = mUserInfoViewModel.getUserInfoLiveData().getValue();
                    if (mShopCartViewModel != null && userInfo != null) {
                        mShopCartViewModel.setSelectedProvinceAndCity(userInfo.getStateId(),
                                userInfo.getTownId());
                    }
                }
            }
        });

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
    }

    public void deepLink() {

        Intent intent = getIntent();
        Uri data = intent.getData();

        if (data == null) {
            return;
        }

        List<String> params = data.getPathSegments();

        if (params != null) {
            if (params.contains(Constants.DEEPLINK_DETAIL)) {
                int prefixId = Integer.parseInt(params.get(3));
                deepLinkAction(params.get(params.size() - 3), prefixId);

            } else if (params.contains(Constants.DEEPLINK_LIST)) {
                int prefixId = Integer.parseInt(params.get(2));
                deepLinkAction(params.get(params.size() - 8), prefixId);

            } else if (params.get(params.size() - 1).indexOf("a") != -1 || params.get(params.size() - 1).indexOf("A") != -1) {
                deepLinkAction(params.get(params.size() - 1), 0);
            }
        }
    }


    @Override
    public void getDeepUrl(String url) {
        List<String> mPrefix = Arrays.asList(url.split("/"));

        if (mPrefix.get(4).toLowerCase().contains(Constants.DEEPLINK_INCREASECREDIT)) {
            deepLinkAction(Constants.DEEPLINK_INCREASECREDIT, 0);

        } else if (mPrefix.get(4).toLowerCase().contains(Constants.DEEPLINK_TRANSACTION)) {
            deepLinkAction(Constants.DEEPLINK_TRANSACTION, 0);

        } else if (mPrefix.get(4).toLowerCase().contains(Constants.DEEPLINK_SALES)) {
            deepLinkAction(Constants.DEEPLINK_SALES, 0);

        } else if (mPrefix.get(4).toLowerCase().contains(Constants.DEEPLINK_CONTACTSALEEXPERT)) {
            deepLinkAction(Constants.DEEPLINK_CONTACTSALEEXPERT, 0);

        } else if (mPrefix.get(4).toLowerCase().contains(Constants.DEEPLINK_ABOUTUS)) {
            deepLinkAction(Constants.DEEPLINK_ABOUTUS, 0);

        } else if (mPrefix.get(4).toLowerCase().contains(Constants.DEEPLINK_BILLPAY)) {
            deepLinkAction(Constants.DEEPLINK_BILLPAY, 0);

        } else if (mPrefix.get(4).toLowerCase().contains(Constants.DEEPLINK_CHARGEPAY)) {
            deepLinkAction(Constants.DEEPLINK_CHARGEPAY, 0);

        } else if (mPrefix.get(4).toLowerCase().contains(Constants.DEEPLINK_BASHGAH)) {
            deepLinkAction(Constants.DEEPLINK_BASHGAH, 0);

        } else if (mPrefix.get(4).toLowerCase().contains(Constants.DEEPLINK_DETAIL)) {
            int productid = Integer.parseInt(mPrefix.get(6));
            deepLinkAction(Constants.DEEPLINK_DETAIL, productid);

        } else if (mPrefix.get(4).toLowerCase().contains(Constants.DEEPLINK_LIST)) {
            int productid = Integer.parseInt(mPrefix.get(5));
            deepLinkAction(Constants.DEEPLINK_LIST, productid);
        }

    }

    @Override
    public void gotoLogin() {
        if (!mIsNeededToLogin) {
            mIsNeededToLogin = true;

            DI.getPreferenceCache().put(Constants.USER_INFO, "");
            DI.getPreferenceCache().put(Constants.TOKEN_KEY, "");

            mUserInfoViewModel.consumeUserInfoLiveData();
            mUserInfoViewModel.consumeCreditLiveData();
            mUserInfoViewModel.invalidateCredit();

            ChargeViewModel chargeViewModel = DIMain.getViewModel(ChargeViewModel.class);
            if (chargeViewModel != null) {
                chargeViewModel.makeInstanceNull();
            }

            finish();
            startActivity(LoginActivity.Companion.newIntent(getBaseContext()));

            mUserInfoViewModel.consumeValidTokenErrorLiveData();
            mUserInfoViewModel.makeInstanceNull();
        }
    }

    @Override
    public void getDeepUrlCharge(String url) {
        getDeepUrl(url);
    }


    public void deepLinkAction(String deepLink, int id) {

        switch (deepLink) {

            case Constants.DEEPLINK_INCREASECREDIT:
                onSwitchRoot(ROOT_DEPOSIT);
                break;

            case Constants.DEEPLINK_TRANSACTION:
                onSwitchRoot(ROOT_TRANSACTIONS);
                break;

            case Constants.DEEPLINK_SALES:
                onSwitchRoot(ROOT_SALE_REPORT);
                break;

            case Constants.DEEPLINK_CONTACTSALEEXPERT:
                onSwitchRoot(ROOT_SUPPORT);
                break;

            case Constants.DEEPLINK_ABOUTUS:
                onSwitchRoot(ROOT_ABOUT_US);
                break;

            case Constants.DEEPLINK_DETAIL:
                openFragment(ProductDetailFragment.newInstance(id));
                break;

            case Constants.DEEPLINK_LIST:
                FilterProductRequest mFilterProductListener = new FilterProductRequest();
                mFilterProductListener.setCategoryId(id);
                mFilterProductListener.setMinPrice(0L);
                mFilterProductListener.setMaxPrice(1000000000L);
                mFilterProductListener.setOrder(0);
                mFilterProductListener.setSort(0);
                mFilterProductListener.setPageIndex(0);
                mFilterProductListener.setPageSize(0);
                mFilterProductListener.setOnlyAvailable(true);
//                mFilterProductListener.setText(null);
                mFilterProductListener.setCategoryNodeRootParent(null);
                mFilterProductListener.setSelectedBrands(null);
                openFragment(ProductListFragment.newInstance(mFilterProductListener, false));
                break;

            case Constants.DEEPLINK_BILLPAY:
                openFragment(BillFragment.newInstance());
                break;

            case Constants.DEEPLINK_CHARGEPAY:
                openFragment(ChargeFragment.newInstance());
                break;

            case Constants.DEEPLINK_BASHGAH:
                openFragment(ClubFragment.newInstance());
                break;
        }

    }


    @Override
    public void onResume() {
        super.onResume();

        mNetworkMonitor.observerConnectivity();

        mUserInfoViewModel.getCredit();

    }

    @Override
    protected int getRootTabId() {
        return ROOT_HOME;
    }

    @Override
    protected SparseArrayCompat<BackStackMember> createNavigationRoots() {
        SparseArrayCompat<BackStackMember> roots = new SparseArrayCompat<>(6);

        roots.put(ROOT_HOME, new BackStackMember(LandingFragment.newInstance()));
        roots.put(ROOT_DEPOSIT, new BackStackMember(DepositFragment.newInstance()));
        roots.put(ROOT_TRANSACTIONS, new BackStackMember(TransactionFragment.newInstance()));
        roots.put(ROOT_SALE_REPORT, new BackStackMember(SaleReportFragment.newInstance()));
        roots.put(ROOT_SUPPORT, new BackStackMember(ContactFragment.newInstance()));
        roots.put(ROOT_ABOUT_US, new BackStackMember(AboutUsFragment.newInstance()));

        return roots;
    }

    public void toggleDrawer() {
        if (mDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {
            mDrawerLayout.closeDrawer(Gravity.RIGHT);
        } else {
            mDrawerLayout.openDrawer(Gravity.RIGHT);
        }
    }

    @Override
    protected int getFragmentHostContainerId() {
        return R.id.content_container;
    }

    @Override
    protected int getNavigationType() {
        return NAVIGATION_TYPE_DRAWER;
    }

    public boolean isDrawerOpened() {
        if (mDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {
            mDrawerLayout.closeDrawer(Gravity.RIGHT);
            return true;
        }
        return false;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        getDeepLinkIntentIncreaseDeposit(intent);
        setIntent(intent);
        deepLink();
    }


    private void getDeepLinkIntentIncreaseDeposit(Intent intent) {
        if (intent != null && intent.getAction() != null && intent.getAction().equals(Intent.ACTION_DEFAULT)) {
            if (intent.getData() != null) {
                if (intent.getData().getHost().equals(Constants.DEEPLINK_CREDIT)) {
                    List<String> pathSegments = intent.getData().getPathSegments();
                    String uuid = pathSegments.get(1);
                    String uid = pathSegments.get(2);
                    if (Integer.parseInt(pathSegments.get(0)) == 8) {
                        IncreaseDepositViewModel mDepositViewModel = DI.getViewModel(IncreaseDepositViewModel.class);

                        if (mDepositViewModel != null) {
                            mDepositViewModel.getStatus(uid, uuid);
                        }
                    }
                } else if (intent.getData().getHost().equals(Constants.DEEPLINK_BILL)) {
                    BillPaymentTypeViewModel billPaymentTypeViewModel = DI.getViewModel(BillPaymentTypeViewModel.class);

                    if (billPaymentTypeViewModel != null) {
                        List<String> pathSegments = intent.getData().getPathSegments();
                        String requestId = pathSegments.get(1);
                        if (Integer.parseInt(pathSegments.get(0)) == 8) {
                            billPaymentTypeViewModel.deepLinkResponseReceived(requestId);
                        }
                    }

                } else if (intent.getData().getHost().equals(Constants.DEEPLINK_STORE)) {
                    List<String> pathSegments = intent.getData().getPathSegments();
                    long orderId = Long.parseLong(pathSegments.get(0));

//                    onSwitchRoot(ROOT_HOME);
                    if (orderId != 0) {
                        OrderTransactionViewModel orderTransactionViewModel = DI.getViewModel(OrderTransactionViewModel.class);

                        orderTransactionViewModel.setSelectedOrderTransaction(new FollowOrderItem(orderId));
                        orderTransactionViewModel.consumeOrderSummaryLiveData();
                        orderTransactionViewModel.consumeOrderDetailLiveData();

                        openFragment(OrderTransactionDetailFragment.newInstance(true));
                    }
                }
            }
        }
    }

    @Override
    protected void onStop() {
        if (toast != null) {
            toast.cancel();
        }
        ToastManager.dismissAllToasts();
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        if (!isDrawerOpened() && !onBackPress()) {
            DI.getViewModel(ChargeViewModel.class).makeInstanceNull();

            super.onChildBackPress();
        }
//            if (isFirstBackPressed) {
//                toast = Toast.makeText(this, getString(R.string.exit_app_text), Toast.LENGTH_LONG);
//                toast.show();
//
//                isFirstBackPressed = !isFirstBackPressed;
//
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        isFirstBackPressed = !isFirstBackPressed;
//                    }
//                }, 2000);
//
//                return;
//            }
//
//            DI.getViewModel(ChargeViewModel.class).makeInstanceNull();
//
//            super.onChildBackPress();
//        } else {
//            if (!isFirstBackPressed) {
//                isFirstBackPressed = true;
//            }
//        }
    }


    @Override
    public int getNavigationViewType() {
        return NAVIGATION_VIEW_TYPE_HAS_TOOLBAR;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String permissions[], @NotNull int[] grantResults) {
        switch (requestCode) {
            case Constants.ZXING_CAMERA_PERMISSION: {
                BillPaymentViewModel billPaymentViewModel = DIMainCommon.getViewModel(BillPaymentViewModel.class);

                if (billPaymentViewModel != null) {
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        billPaymentViewModel.setPermissionAccessed(true);
                    } else {
                        billPaymentViewModel.setPermissionAccessed(false);
                    }
                }
                break;
            }
            case Constants.WRITE_STORAGE_PERMISSION: {
                ReceiptViewModel receiptViewModel = DIMainCommon.getViewModel(ReceiptViewModel.class);

                if (receiptViewModel != null) {
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        receiptViewModel.setPermissionAccessed(true);
                    } else {
                        receiptViewModel.setPermissionAccessed(false);
                    }
                }
                break;
            }
        }
    }
}
