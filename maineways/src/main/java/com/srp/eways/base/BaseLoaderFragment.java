package com.srp.eways.base;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.srp.eways.ui.NetworkMonitor;


/**
 * Created by Eskafi on 9/29/2019.
 */
public abstract class BaseLoaderFragment<V extends BaseViewModel> extends BaseFragment<V> implements NetworkMonitor.OnConnectivityChangedListener {

    private static final String EXTRA_NETWORK_MONITOR = "network_monitor";

    private boolean mHasNoInternetError = false;

    private NetworkMonitor mNetworkMonitor;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mNetworkMonitor = new NetworkMonitor(this, this);

        if (savedInstanceState != null) {
            Bundle networkMonitorState = savedInstanceState.getBundle(EXTRA_NETWORK_MONITOR);

            if (networkMonitorState != null) {
                mNetworkMonitor.restoreInstanceState(networkMonitorState);
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBundle(EXTRA_NETWORK_MONITOR, mNetworkMonitor.saveInstanceState());
    }

    @Override
    public void onResume() {
        super.onResume();

        observeConnectivity();
    }

    @Override
    public void onConnectivityChanged(boolean isConnected) {
        if (isConnected) {
            //TODO this checking should be done
//            if (!mHasNoInternetError) {
//                return;
//            }

            getDataFromServer();
        } else {
            onInternetUnAvailable();
        }
    }

    abstract protected void getDataFromServer();

    protected void onInternetUnAvailable() {
    }

    public boolean isNetworkConnected() {
        return mNetworkMonitor.isConnected();
    }

    public void observeConnectivity() {
        mNetworkMonitor.observerConnectivity();
    }

    public void ignoreConnectivity() {
        mNetworkMonitor.ignoreConnectivity();
    }

    public void setHasNoInternetError(boolean hasNoInternetError) {
        mHasNoInternetError = hasNoInternetError;
    }
}
