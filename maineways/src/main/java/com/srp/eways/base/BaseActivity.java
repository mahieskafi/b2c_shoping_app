package com.srp.eways.base;

import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.srp.eways.di.DIMain;
import com.srp.eways.util.analytic.EventSender;

/**
 * Created by Arcane on 7/16/2019.
 */
public abstract class BaseActivity<V extends BaseViewModel> extends AppCompatActivity
        implements BaseFragment.Callback {

    private V mViewModel;

    public abstract
    @LayoutRes
    int getLayoutId();

    public abstract V getViewModel();

    public EventSender getEventSender() {
        return DIMain.getEventSender();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(getLayoutId());

        mViewModel = mViewModel == null ? getViewModel() : mViewModel;

        ViewGroup viewGroup = (ViewGroup) ((ViewGroup) this
                .findViewById(android.R.id.content)).getChildAt(0);
        viewGroup.setTag(this);
    }

    @Override
    public void onFragmentAttached() {
    }

    protected void setToolbarTitle(String title) {

        if (getSupportActionBar() != null) {

            getSupportActionBar().setTitle(title);
        }
//        getSupportActionBar().setTitle(getString(R.string.app_name));
    }

    @Override
    public void onFragmentDetached(String tag) {

    }

    public boolean isNetworkConnected() {

        NetworkInfo networkInfo = getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }

    private NetworkInfo getActiveNetworkInfo() {

        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return connectivityManager.getActiveNetworkInfo();
    }

}
