package com.srp.eways.ui.navigation;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.collection.SparseArrayCompat;
import androidx.fragment.app.Fragment;

import com.srp.eways.base.BaseFragment;
import com.srp.eways.base.BaseViewModel;

/**
 * Created by Eskafi on 11/3/2019.
 */
public abstract class NavigationFragment<V extends BaseViewModel> extends BaseFragment<V>
        implements INavigationController, NavigationType, NavigationViewType {

    private NavigationController mNavigationController;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        mNavigationController = new NavigationController(
                getChildFragmentManager(),
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

    public BackStackMember getBackStackMember(int id) {
        return  mNavigationController.getBackStackMember(id);
    }

    @Override
    public boolean onBackPress() {
        return mNavigationController.onBackPress();
    }

    protected INavigationController getParentNavigationController() {
        Fragment parentFragment = getParentFragment();

        if (parentFragment instanceof INavigationController) {
            return (INavigationController) parentFragment;
        } else {
            Activity activity = getActivity();

            if (activity != null) {
                if (activity instanceof INavigationController) {
                    return (INavigationController) activity;
                } else {
                    throw new RuntimeException("Parent navigation controller not found.");
                }
            } else {
                throw new RuntimeException("Fragment not attached.");
            }
        }
    }
}
