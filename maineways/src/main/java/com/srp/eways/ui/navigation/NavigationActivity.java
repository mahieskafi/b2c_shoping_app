package com.srp.eways.ui.navigation;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.collection.SparseArrayCompat;
import androidx.fragment.app.Fragment;

import com.srp.eways.base.BaseActivity;
import com.srp.eways.base.BaseViewModel;

public abstract class NavigationActivity<V extends BaseViewModel> extends BaseActivity<V>
        implements INavigationController, NavigationType, NavigationViewType {

    private NavigationController mNavigationController;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        mNavigationController = new NavigationController(
                getSupportFragmentManager(),
                getFragmentHostContainerId(),
                createNavigationRoots(),
                getRootTabId(),
                getNavigationType());
    }

    protected abstract int getRootTabId();

    protected abstract SparseArrayCompat<BackStackMember> createNavigationRoots();

    protected abstract int getFragmentHostContainerId();

    protected abstract int getNavigationType();

    @Override
    public void openFragment(Fragment fragment) {

        mNavigationController.openFragment(fragment);
    }

    @Override
    public void onSwitchRoot(int id) {

        mNavigationController.onSwitchRoot(id);
    }

    @Override
    public boolean onBackPress() {

        return mNavigationController.onBackPress();
    }

}
