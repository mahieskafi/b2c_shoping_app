package com.srp.eways.ui.navigation;

import androidx.collection.SparseArrayCompat;

import java.util.List;

/**
 * Created by Eskafi on 8/11/2019.
 */
public class TabCoreNavigationController extends AbsCoreNavigationController {

    public TabCoreNavigationController(int defaultRootId, SparseArrayCompat roots) {
        super(defaultRootId, roots);
    }

    @Override
    protected Object switchRootInternal(int currentRootId) {
        List<Object> currentBackStack = getBackStackForRoot();

        return currentBackStack.get(currentBackStack.size() - 1);
    }

}
