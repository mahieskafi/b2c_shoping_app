package com.srp.eways.ui.navigation;

import androidx.collection.SparseArrayCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import java.util.List;


/**
 * Created by Eskafi on 8/11/2019.
 */
public class NavigationController implements INavigationController, NavigationType {

    private FragmentManager mFragmentManager;

    private int mContainerId;

    private int mDefaultRoot;
    private SparseArrayCompat<BackStackMember> mRoots;

    private AbsCoreNavigationController mBaseNavigation;

    public NavigationController(FragmentManager fragmentManager, int containerId, SparseArrayCompat<BackStackMember> roots, int defaultRoot, int navigationType) {
        mFragmentManager = fragmentManager;

        mContainerId = containerId;

        mDefaultRoot = defaultRoot;

        mBaseNavigation = createCoreNavigationController(roots, defaultRoot, navigationType);

        this.mRoots = roots;

        onSwitchRoot(defaultRoot);
    }

    private AbsCoreNavigationController createCoreNavigationController(SparseArrayCompat<BackStackMember> roots, int defaultRoot, int navigationType) {
        switch (navigationType) {
            case NAVIGATION_TYPE_DRAWER:
                return new DrawerCoreNavigationController(defaultRoot, roots);
            case NAVIGATION_TYPE_TAB:
                return new TabCoreNavigationController(defaultRoot, roots);
        }

        throw new IllegalArgumentException("Navigation type '" + navigationType + "' not defined.");
    }

    @Override
    public boolean onBackPress() {
        List<Object> backStackFragment = mBaseNavigation.getBackStackForRoot();
        BackStackMember lastFragment;
        if (backStackFragment.size() > 0) {
            lastFragment = (BackStackMember) backStackFragment.get(backStackFragment.size() - 1);

            Fragment fragment = lastFragment.getFragment();

            boolean onBackPress = (fragment instanceof NavigationMemberFragment || fragment instanceof NavigationFragment) && ((INavigationController) fragment).onBackPress();

            if (!onBackPress && (backStackFragment.size() > 1)) {

                BackStackMember popedFragment = (BackStackMember) mBaseNavigation.onBackPress(lastFragment);
                if (popedFragment != null) {
                    FragmentTransaction ft = mFragmentManager.beginTransaction();
                    ft.hide(popedFragment.getFragment());

                    backStackFragment = mBaseNavigation.getBackStackForRoot();
                    BackStackMember currentFragment = (BackStackMember) backStackFragment.get(backStackFragment.size() - 1);
                    ft.show(currentFragment.getFragment());
                    ft.commit();
                    return true;
                }
            } else if (backStackFragment.size() == 1 && !onBackPress) {
                if (isBackStackMemberHome(lastFragment))
                    return false;
                else {
                    int rootid = getHome();
                    if (fragment instanceof INavigationController) {
                        ((INavigationController) fragment).onSwitchRoot(rootid);
                    } else {
                        onSwitchRoot(rootid);
                    }
                    return true;
                }
            } else if (onBackPress) {
                return true;
            }
        }
        return false;
    }

    private boolean isBackStackMemberHome(BackStackMember member) {
        return mRoots.get(mDefaultRoot).equals(member);
    }

    @Override
    public void openFragment(Fragment fragment) {

        Fragment current = null;

        if (mBaseNavigation.getBackStackForRoot().size() > 0) {
            current = ((BackStackMember) mBaseNavigation.getBackStackForRoot().get(mBaseNavigation.getBackStackForRoot().size() - 1)).getFragment();
        }
        BackStackMember member = BackStackMember.create(fragment);
        BackStackMember backStackMember = (BackStackMember) mBaseNavigation.openFragment(member);
        Fragment baseFragment = backStackMember.getFragment();
        if (backStackMember.getBundle() != null)
            baseFragment.setArguments(backStackMember.getBundle());

        FragmentTransaction ft = mFragmentManager.beginTransaction();

        if (current != null)
            ft.hide(current);

        List<Fragment> fragments = mFragmentManager.getFragments();
        if (fragments.size() > 0) {
            for (Fragment availableFragment : fragments) {
                if (baseFragment == availableFragment) {
                    ft.show(baseFragment);
                    ft.commit();
                    return;
                }
            }
        }

        ft.add(mContainerId, baseFragment);
        ft.commit();
    }

    private int getHome() {
        for (int i = 0; i < mRoots.size(); i++)
            if (isBackStackMemberHome(mRoots.valueAt(i)))
                return mRoots.keyAt(i);
        return 0;

    }

    @Override
    public void onSwitchRoot(int id) {
        BackStackMember backStackMember = getBackStackMember(id);

        Fragment baseFragment = backStackMember.getFragment();
        baseFragment.setArguments(backStackMember.getBundle());

        FragmentTransaction ft = mFragmentManager.beginTransaction();

        List<Fragment> fragments = mFragmentManager.getFragments();
        if (fragments.size() > 0) {

            for (Fragment fragment : fragments) {
                if (fragment != null && fragment.isVisible()) {
                    ft.hide(fragment);
                }
            }

            for (Fragment fragment : fragments) {
                if (baseFragment == fragment) {
                    ft.show(baseFragment);
                    ft.commit();
                    return;
                }
            }
        }
        ft.add(mContainerId, baseFragment);
        ft.commit();
    }

    public BackStackMember getBackStackMember(int id) {
        return (BackStackMember) mBaseNavigation.switchRoot(id);
    }
}
