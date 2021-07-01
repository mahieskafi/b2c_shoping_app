package com.srp.eways.ui.navigation;

import androidx.collection.SparseArrayCompat;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Eskafi on 8/11/2019.
 */
public class DrawerCoreNavigationController extends AbsCoreNavigationController {

    public DrawerCoreNavigationController(int defaultRootId, SparseArrayCompat roots) {
        super(defaultRootId, roots);
    }

    @Override
    protected Object switchRootInternal(int currentRootId) {
        List<Object> currentBackStack = getBackStackForRoot();

        if (currentBackStack.size() > 1) {
            Iterator<Object> iterator = currentBackStack.iterator();

            if (iterator.hasNext()) {
                iterator.next();
            }

            while (iterator.hasNext()) {
                iterator.next();
                iterator.remove();
            }

            getBackStack().put(currentRootId, currentBackStack);

            return currentBackStack.get(0);
        }
        else {
            return currentBackStack.get(currentBackStack.size() - 1);
        }
    }

}
