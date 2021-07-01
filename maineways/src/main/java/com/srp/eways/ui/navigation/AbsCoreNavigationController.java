package com.srp.eways.ui.navigation;

import androidx.collection.SparseArrayCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eskafi on 8/11/2019.
 */
public abstract class AbsCoreNavigationController {

    private int mCurrentRootId;

    private SparseArrayCompat<List<Object>> mBackStack;

    private SparseArrayCompat<Object> mRoots;

    public AbsCoreNavigationController(int defaultRootId, SparseArrayCompat roots) {
        mCurrentRootId = defaultRootId;
        mRoots = roots;

        mBackStack = new SparseArrayCompat<>();

        for (int i = 0; i < mRoots.size(); i++) {
            setBackStackForRoot(mRoots.keyAt(i), mRoots.valueAt(i));
        }
    }

    public final Object switchRoot(int id) {
        mCurrentRootId = id;

        return switchRootInternal(mCurrentRootId);
    }

    abstract protected Object switchRootInternal(int currentRootId);

    public Object onBackPress(Object object) {
        List<Object> list = getBackStackForRoot();
        if (list.remove(object)) {
            mBackStack.put(mCurrentRootId, list);
            return object;
        }
        return null;
    }

    public Object openFragment(Object object) {
        List<Object> currentBackStack = getBackStackForRoot();
        currentBackStack.add(object);
        mBackStack.put(mCurrentRootId, currentBackStack);

        return object;
    }

    public SparseArrayCompat<List<Object>> getBackStack() {
        return mBackStack;
    }

    public List<Object> getBackStackForRoot() {
        List<Object> tabBackStack = mBackStack.get(mCurrentRootId);

        if (tabBackStack == null) {
            tabBackStack = new ArrayList<>();
            mBackStack.put(mCurrentRootId, tabBackStack);
        }

        return tabBackStack;
    }

    private void setBackStackForRoot(int id, Object object) {
        List<Object> tabBackStack = mBackStack.get(id);

        if (tabBackStack == null) {
            tabBackStack = new ArrayList<>();
            tabBackStack.add(object);
            mBackStack.put(id, tabBackStack);
        }
    }
}
