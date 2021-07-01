package com.srp.ewayspanel.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;

import com.srp.ewayspanel.di.DI;

/**
 * Created by Eskafi on 9/29/2019.
 */
public class NetworkMonitor implements LifecycleObserver {

    private static final String EXTRA_IS_OBSERVING = "is_observing";
    private static final String EXTRA_WAS_CONNECTED = "was_connected";

    public interface OnConnectivityChangedListener {

        void onConnectivityChanged(boolean isConnected);

    }

    private Context mContext;

    private OnConnectivityChangedListener mListener;

    private boolean mIsStarted = false;

    private boolean mIsObserving;
    private boolean mIsConnected;

    private Boolean mWasConnected = null;

    private BroadcastReceiver mConnectivityChangedReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            boolean isConnected = isConnected();

            if (isConnected != mIsConnected) {
                mIsConnected = isConnected;

                notifyConnectivityChanged();
            }
        }

    };

    public NetworkMonitor(LifecycleOwner lifecycleOwner, OnConnectivityChangedListener listener) {
        mContext = DI.getContext();

        lifecycleOwner.getLifecycle().addObserver(this);

        mListener = listener;

        Lifecycle.State state = lifecycleOwner.getLifecycle().getCurrentState();
        mIsStarted = state == Lifecycle.State.STARTED || state == Lifecycle.State.RESUMED;
    }

    public Bundle saveInstanceState() {
        Bundle state = new Bundle();

        state.putBoolean(EXTRA_IS_OBSERVING, mIsObserving);
        state.putBoolean(EXTRA_WAS_CONNECTED, mIsConnected);

        return state;
    }

    public void restoreInstanceState(Bundle savedInstanceState) {
        boolean isObserving = savedInstanceState.getBoolean(EXTRA_IS_OBSERVING);
        boolean wasConnected = savedInstanceState.getBoolean(EXTRA_WAS_CONNECTED);

        mIsObserving = isObserving;

        mWasConnected = wasConnected;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    void onStart() {
        mIsStarted = true;

        mIsConnected = isConnected();

        if (mIsObserving) {
            mIsObserving = false;

            observerConnectivity();
        }

        if (mWasConnected != null && mWasConnected != mIsConnected) {
            notifyConnectivityChanged();
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    void onStop() {
        if (mIsObserving) {
            ignoreConnectivity();

            mIsObserving = true;
        }

        mIsStarted = false;

        mWasConnected = isConnected();
    }

    public void observerConnectivity() {
        if (mIsObserving || !mIsStarted) {
            return;
        }

        mIsObserving = true;

        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);

        mContext.registerReceiver(mConnectivityChangedReceiver, intentFilter);
    }

    public void ignoreConnectivity() {
        if (!mIsObserving || !mIsStarted) {
            return;
        }

        mIsObserving = false;

        mContext.unregisterReceiver(mConnectivityChangedReceiver);
    }

    public boolean isObserving() {
        return mIsObserving;
    }

    private void notifyConnectivityChanged() {
        mListener.onConnectivityChanged(mIsConnected);
    }

    public boolean isConnected() {
        return isConnected(mContext);
    }

    private static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo ni = cm.getActiveNetworkInfo();

        return ni != null && ni.isConnectedOrConnecting();
    }
}
