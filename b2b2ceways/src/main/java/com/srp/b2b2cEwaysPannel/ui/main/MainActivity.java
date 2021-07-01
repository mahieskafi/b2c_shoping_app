package com.srp.b2b2cEwaysPannel.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.collection.SparseArrayCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.srp.b2b2cEwaysPannel.R;
import com.srp.b2b2cEwaysPannel.di.DI;
import com.srp.b2b2cEwaysPannel.ui.ContactFragment;
import com.srp.b2b2cEwaysPannel.ui.charge.ChargeFragment;
import com.srp.b2b2cEwaysPannel.ui.transaction.TransactionFragment;
import com.srp.eways.base.BaseViewModel;
import com.srp.eways.model.charge.BuyChargeResult;
import com.srp.eways.ui.aboutus.AboutUsFragment;
import com.srp.eways.ui.charge.ChargeViewModel;
import com.srp.eways.ui.deposit.DepositFragment;
import com.srp.eways.ui.deposit.increasedeposit.IncreaseDepositViewModel;
import com.srp.eways.ui.login.UserInfoViewModel;
import com.srp.eways.ui.main.CommonMainActivity;
import com.srp.eways.ui.main.MainRootIds;
import com.srp.eways.ui.navigation.BackStackMember;
import com.srp.eways.util.Utils;

import java.util.List;

import static com.srp.eways.util.Constants.DEEP_LINK_BUY_CHARGE_HOST_NAME;

public class MainActivity extends CommonMainActivity<BaseViewModel> implements MainRootIds {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private UserInfoViewModel mUserInfoViewModel;

    private boolean isFirstBackPressed = true;
    private Toast toast;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mUserInfoViewModel = DI.getViewModel(UserInfoViewModel.class);

        super.onCreate(savedInstanceState);

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

        mUserInfoViewModel.getCredit();
    }

    @Override
    protected int getRootTabId() {
        return ROOT_HOME;
    }

    @Override
    protected SparseArrayCompat<BackStackMember> createNavigationRoots() {
        SparseArrayCompat<BackStackMember> roots = new SparseArrayCompat<>(4);

        roots.put(ROOT_HOME, new BackStackMember(ChargeFragment.newInstance()));
        roots.put(ROOT_DEPOSIT, new BackStackMember(DepositFragment.newInstance()));
        roots.put(ROOT_TRANSACTIONS, new BackStackMember(TransactionFragment.newInstance()));
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

        getDeepLinkIntent(intent);
    }


    private void getDeepLinkIntent(Intent intent) {
        if (intent != null && intent.getAction() != null && intent.getAction().equals(Intent.ACTION_DEFAULT)) {
            if (intent.getData() != null) {
                if (!(intent.getData().getHost().equals(DEEP_LINK_BUY_CHARGE_HOST_NAME))) {
                    List<String> pathSegments = intent.getData().getPathSegments();
                    String uuid = pathSegments.get(0);
                    String uid = pathSegments.get(1);

                    IncreaseDepositViewModel mDepositViewModel = DI.getViewModel(IncreaseDepositViewModel.class);

                    if (mDepositViewModel != null) {
                        mDepositViewModel.getStatus(uid, uuid);
                    }
                } else {
                    ChargeViewModel mChargeViewModel = DI.getViewModel(ChargeViewModel.class);

                    if (mChargeViewModel != null) {
                        BuyChargeResult buyChargeResult = new BuyChargeResult();
                        buyChargeResult.setStatus(ChargeViewModel.REDIRECTED);

                        mChargeViewModel.getBuyChargeResultLive().setValue(buyChargeResult);

                        mChargeViewModel.getHasNewChargeLiveData().setValue(true);

                    }
                }
            }
        }
    }


    @Override
    protected void onDestroy() {
        if (toast != null) {
            toast.cancel();
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (!isDrawerOpened() && !onBackPress()) {
            if (isFirstBackPressed) {
                toast = Toast.makeText(this, getString(R.string.exit_app_text), Toast.LENGTH_LONG);
                toast.show();

                isFirstBackPressed = !isFirstBackPressed;

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isFirstBackPressed = !isFirstBackPressed;
                    }
                }, 2000);

                return;
            }

            DI.getViewModel(ChargeViewModel.class).makeInstanceNull();
            super.onChildBackPress();
        } else {
            if (!isFirstBackPressed) {
                isFirstBackPressed = true;
            }
        }
    }

    @Override
    public void gotoLogin() {

    }


    @Override
    public int getNavigationViewType() {
        return NAVIGATION_VIEW_TYPE_HAS_TOOLBAR;
    }
}
