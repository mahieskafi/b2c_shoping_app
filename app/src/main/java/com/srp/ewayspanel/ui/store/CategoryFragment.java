package com.srp.ewayspanel.ui.store;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.collection.SparseArrayCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.srp.eways.base.BaseViewModel;
import com.srp.eways.ui.navigation.BackStackMember;
import com.srp.eways.ui.navigation.NavigationFragment;
import com.srp.ewayspanel.R;
import com.srp.ewayspanel.model.storepage.filter.FilterProductRequest;
import com.srp.ewayspanel.ui.store.filter.FilterChanged;
import com.srp.ewayspanel.ui.store.filter.ProductListFragment;

/**
 * Created by Eskafi on 11/3/2019.
 */
public class CategoryFragment extends NavigationFragment<BaseViewModel> implements FilterChanged {

    private final static String FILTER_REQUEST = "filter_request";
    private final static String CATEGORY_TREE_NODE_ID = "extraCategoryId";
    private final static int CATEGORY_ID = 0;
    private final static int PRODUCT_LIST_ID = 1;

    public static CategoryFragment newInstance(long categoryTreeNodeId, FilterProductRequest filterProductRequest) {

        Bundle args = new Bundle();
        args.putLong(CATEGORY_TREE_NODE_ID, categoryTreeNodeId);
        args.putSerializable(FILTER_REQUEST, filterProductRequest);

        CategoryFragment fragment = new CategoryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FilterProductRequest filterProductRequest = (FilterProductRequest) getArguments().getSerializable(FILTER_REQUEST);

        if (filterProductRequest != null) {
            openFragment(ProductListFragment.newInstance(filterProductRequest, false));
        }
    }

    @Override
    public BaseViewModel acquireViewModel() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_category_container;
    }

    @Override
    protected int getRootTabId() {
        return CATEGORY_ID;
    }

    public void setCategoryTreeNodeId(Long categoryTreeNodeId) {
        Bundle args = new Bundle();
        args.putLong(CATEGORY_TREE_NODE_ID, categoryTreeNodeId);

        setArguments(args);
    }

    @Override
    protected SparseArrayCompat<BackStackMember> createNavigationRoots() {
        SparseArrayCompat<BackStackMember> roots = new SparseArrayCompat<>(1);

        Bundle bundle = new Bundle();
        bundle.putLong(CATEGORY_TREE_NODE_ID, getArguments().getLong(CATEGORY_TREE_NODE_ID));

        roots.put(CATEGORY_ID, new BackStackMember(TwoLevelCategoryFragment.newInstance(), bundle));
        return roots;
    }

    @Override
    protected int getFragmentHostContainerId() {
        return R.id.f_container;
    }

    @Override
    protected int getNavigationType() {
        return NAVIGATION_TYPE_TAB;
    }

    @Override
    public int getNavigationViewType() {
        return -1;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (getParentFragment() != null) {
            getParentFragment().onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onFilterChanged(FilterProductRequest filterProductRequest) {
        if (!isAdded()) return;

        FragmentManager fragmentManager = getChildFragmentManager();
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.f_container);

        if (currentFragment instanceof ProductListFragment) {
            if (filterProductRequest == null) {
                onBackPress();
            } else {
                ((ProductListFragment) currentFragment).setNewFilter(filterProductRequest);
            }
        }
    }
}
