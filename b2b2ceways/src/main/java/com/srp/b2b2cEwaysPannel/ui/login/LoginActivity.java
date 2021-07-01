package com.srp.b2b2cEwaysPannel.ui.login;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.collection.SparseArrayCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.srp.b2b2cEwaysPannel.R;
import com.srp.b2b2cEwaysPannel.di.DI;
import com.srp.b2b2cEwaysPannel.ui.login.otp.OtpFragment;
import com.srp.b2b2cEwaysPannel.ui.login.phonenumber.PhoneNumberFragment;
import com.srp.b2b2cEwaysPannel.ui.login.register.RegisterFragment;
import com.srp.b2b2cEwaysPannel.ui.main.MainActivity;
import com.srp.eways.base.BaseViewModel;
import com.srp.eways.di.DIMain;
import com.srp.eways.ui.main.CommonMainActivity;
import com.srp.eways.ui.navigation.BackStackMember;
import com.srp.eways.util.Constants;

import java.util.ArrayList;
import java.util.Arrays;

import ir.abmyapp.androidsdk.IABResources;


public class LoginActivity extends CommonMainActivity<BaseViewModel> {

    public static Intent newIntent(Context context) {
        return new Intent(context, LoginActivity.class);
    }

    private static ArrayList<Pair<Integer, String>> PERMISSIONS = new ArrayList<>(Arrays.asList(new Pair<>(1001, Manifest.permission.RECEIVE_SMS)));

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        String token = DI.getPreferenceCache().getString(Constants.TOKEN_KEY);
        if (token != null && !token.isEmpty()) {
            startActivity(MainActivity.newIntent(getApplicationContext()));
            finish();
        }

        super.onCreate(savedInstanceState);

        IABResources abResources = DIMain.getABResources();

        View headerView = findViewById(R.id.login_header);
        ImageView logo = findViewById(R.id.logo);

        headerView.setBackground(abResources.getDrawable(R.drawable.login_header));
        logo.setImageDrawable(abResources.getDrawable(R.drawable.login_header_logo));


        for(Pair<Integer, String> item : PERMISSIONS){
            requestPermission(item);
        }
    }

    @Override
    protected int getRootTabId() {
        return LoginRootIds.ROOT_PHONENUMBER;
    }

    @Override
    protected SparseArrayCompat<BackStackMember> createNavigationRoots() {
        SparseArrayCompat<BackStackMember> roots = new SparseArrayCompat<>(3);

        roots.put(LoginRootIds.ROOT_PHONENUMBER, new BackStackMember(PhoneNumberFragment.getInstance()));
        roots.put(LoginRootIds.ROOT_REGISTER, new BackStackMember(RegisterFragment.getInstance()));
        roots.put(LoginRootIds.ROOT_OTP, new BackStackMember(OtpFragment.getInstance()));

        return roots;
    }

    @Override
    protected int getFragmentHostContainerId() {
        return R.id.content_container;
    }

    @Override
    protected int getNavigationType() {
        return NAVIGATION_TYPE_TAB;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public BaseViewModel getViewModel() {
        return null;
    }

    @Override
    public int getNavigationViewType() {
        return NAVIGATION_VIEW_TYPE_HAS_TOOLBAR;
    }

    @Override
    public void toggleDrawer() {
    }

    @Override
    public void onBackPressed() {
        if (!onBackPress()) {
            super.onChildBackPress();
        }
    }

    @Override
    public void gotoLogin() {

    }

    private void requestPermission(Pair<Integer, String> permission){

        if (ContextCompat.checkSelfPermission(this,
                permission.second)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{permission.second},
                    permission.first);
        }
        else {
            // Permission has already been granted
        }

    }
}
