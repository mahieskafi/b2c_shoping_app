package com.srp.eways.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.srp.eways.di.DIMain;
import com.srp.eways.ui.charge.ChargeViewModel;
import com.srp.eways.ui.main.CommonMainActivity;
import com.srp.eways.util.Constants;
import com.srp.eways.util.Utils;
import com.srp.eways.util.analytic.EventSender;

/**
 * Created by Arcane on 7/16/2019.
 */
public abstract class BaseFragment<V extends BaseViewModel> extends Fragment {

    public static final int NO_CHILD = -1;

    private BaseActivity mActivity;

    private V mViewModel;

    public abstract V acquireViewModel();

    protected V getViewModel() {
        return mViewModel;
    }

    public int getActionMenu() {
        return 0;
    }

    public abstract
    @LayoutRes
    int getLayoutId();

    public EventSender getEventSender() {
        return DIMain.getEventSender();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        view.setTag(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(getLayoutId(), container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        mViewModel = acquireViewModel();

        handleToolbarAction();

        if (mViewModel != null) {
            mViewModel.isNeededToLogin().observe(this, new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean isNeededToLogin) {
                    if (isNeededToLogin != null && isNeededToLogin) {

                        if (getActivity() instanceof CommonMainActivity) {
                            ((CommonMainActivity) getActivity()).gotoLogin();
                            mViewModel.consumeNeededToLogin();
                        }
                    }
                }
            });
        }
    }

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);

        if (context instanceof BaseActivity) {

            BaseActivity activity = (BaseActivity) context;

            this.mActivity = activity;

            activity.onFragmentAttached();
        }
    }

    private void handleToolbarAction() {

        if (getActionMenu() != 0) {

            setHasOptionsMenu(true);
        } else {

            setHasOptionsMenu(false);
        }
    }

    @Override
    public void onDetach() {

        mActivity = null;

        super.onDetach();
    }

    public BaseActivity getBaseActivity() {

        return mActivity;
    }

    public boolean isNetworkConnected() {

        return mActivity.isNetworkConnected();
    }

//    protected abstract String getTitlePage();

    public interface Callback {

        void onFragmentAttached();

        void onFragmentDetached(String tag);
    }

    @Override
    public void onResume() {
        super.onResume();

        Utils.hideKeyboard(mActivity);
    }
}
