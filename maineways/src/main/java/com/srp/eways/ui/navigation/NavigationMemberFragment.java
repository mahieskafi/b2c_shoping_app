package com.srp.eways.ui.navigation;

import android.app.Activity;

import androidx.fragment.app.Fragment;

import com.srp.eways.base.BaseViewModel;
import com.srp.eways.util.Utils;

public abstract class NavigationMemberFragment<V extends BaseViewModel> extends DrawerFragment<V>
        implements INavigationController, NavigationViewType {

    @Override
    public void openFragment(Fragment fragment) {
        Utils.hideKeyboard(getActivity());
        getParentNavigationController().openFragment(fragment);
    }

    public void openFragment(Fragment fragment, int viewType) {
        Utils.hideKeyboard(getActivity());
        getParentNavigationController(viewType).openFragment(fragment);
    }

    @Override
    public void onSwitchRoot(int id) {

        Utils.hideKeyboard(getActivity());
        getParentNavigationController().onSwitchRoot(id);
    }

    @Override
    public boolean onBackPress() {

        Utils.hideKeyboard(getActivity());
        return false;
    }

    @Override
    public int getNavigationViewType() {
        return -1;
    }

    private INavigationController getParentNavigationController() {
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
            }
            else {
                throw new RuntimeException("Fragment not attached.");
            }
        }
    }

    private INavigationController getParentNavigationController(int viewType) {
        Fragment parentFragment = getParentFragment();

        while (parentFragment != null) {
            if (parentFragment instanceof INavigationController &&
                    parentFragment instanceof NavigationViewType &&
                    ((NavigationViewType) parentFragment).getNavigationViewType() == viewType) {
                return (INavigationController) parentFragment;
            }
            parentFragment = parentFragment.getParentFragment();
        }

        Activity activity = getActivity();

        if (activity != null) {
            if (activity instanceof INavigationController &&
                    activity instanceof NavigationViewType &&
                    ((NavigationViewType) activity).getNavigationViewType() == viewType) {
                return (INavigationController) activity;
            }
            else {
                throw new RuntimeException("Parent navigation controller not found.");
            }
        }
        else {
            throw new RuntimeException("Fragment not attached.");
        }
    }
}

