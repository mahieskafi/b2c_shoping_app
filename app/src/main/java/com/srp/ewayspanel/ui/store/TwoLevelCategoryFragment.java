package com.srp.ewayspanel.ui.store;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.srp.eways.base.BaseViewModel;
import com.srp.eways.ui.navigation.NavigationMemberFragment;
import com.srp.eways.ui.view.EmptyView;
import com.srp.eways.ui.view.LoadingStateView;
import com.srp.ewayspanel.R;
import com.srp.ewayspanel.di.DI;
import com.srp.ewayspanel.model.storepage.category.CategoryItem;
import com.srp.ewayspanel.model.storepage.category.CategoryListResponse;
import com.srp.ewayspanel.model.storepage.filter.FilterProductRequest;
import com.srp.ewayspanel.ui.store.filter.ProductListFragment;
import com.srp.ewayspanel.ui.view.store.CategoryItemViewListener;

import java.util.ArrayList;
import java.util.List;

public class TwoLevelCategoryFragment extends NavigationMemberFragment<BaseViewModel> {

    public static final int STATE_NO_INTERNET = 2;
    public static final int STATE_SHOW_CONTENT = 3;
    public static final int STATE_NO_DATA = 4;


    public static final String EXTRA_CATEGORY_ID = "extraCategoryId";

    private boolean mIsDataReceived = false;

    public static TwoLevelCategoryFragment newInstance() {
        TwoLevelCategoryFragment fragment = new TwoLevelCategoryFragment();

        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    private EmptyView mNoData;
    private StoreViewModel mStoreViewModel;
    private LoadingStateView mLoadingStateView;
    private RecyclerView mRecyclerView;
    private CategoryListAdapter mAdapter;

    private String mErrorMessage;

    private List<CategoryItem> mCategoryItemList;

    private static @ColorInt
    int mCategoryColors[];

    private long mCategoryRootNodeId;

    @Override
    public BaseViewModel acquireViewModel() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_allcategories;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCategoryRootNodeId = getArguments().getLong(EXTRA_CATEGORY_ID);
        mStoreViewModel = DI.getViewModel(StoreViewModel.class);
        mCategoryItemList = new ArrayList<>();
    }

    @Override
    public void onViewCreated(@NonNull final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = view.findViewById(R.id.categorylist);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        mLoadingStateView = view.findViewById(R.id.loading_stateview1);
        mNoData = view.findViewById(R.id.emptyview1);
//        mNoInternetView.setEmptyText(R.string.network_error_no_internet);

        mAdapter = new CategoryListAdapter(getContext(), mCategoryItemViewListener);

        mIsDataReceived = false;

        observeCategoryCopy();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

//                mLoadingStateView.setStateAndDescription(LoadingStateView.STATE_LOADING, getString(R.string.network_error_no_internet), false);
                if (!mIsDataReceived) {
                    mLoadingStateView.setVisibility(View.VISIBLE);
                }
            }
        }, 2000);
    }

    private void observeCategoryCopy() {
        mStoreViewModel.getCategoryList(mCategoryRootNodeId);

        if (!isNetworkConnected()) {
            onViewStateChanged(STATE_NO_INTERNET);
        } else {
            observeCategoryData();
        }

    }

    private void observeCategoryData() {
        mStoreViewModel.getCategoryData().observe(getViewLifecycleOwner(), new Observer<CategoryListResponse>() {
            @Override
            public void onChanged(CategoryListResponse categoryData) {
                mIsDataReceived = true;

                if (categoryData == null) {
                    return;
                }

                String errorString = categoryData.getDescription();

                if (errorString != null && !errorString.isEmpty()) {
                    mErrorMessage = errorString;
                    onViewStateChanged(LoadingStateView.STATE_ERROR);
                    return;
                }

                //success condition
//                onViewStateChanged(VIEWSTATE_SHOW_CONTENT);

                if (categoryData.getItems() != null && categoryData.getItems().size() > 0) {

                    if (categoryData.getItems().get(0).getDepth() == 3) {
                        mAdapter.expandCategory(categoryData.getItems(), categoryData.getItems().get(0).getParentId());

                    } else if (categoryData.getItems().get(0).getDepth() > 3) {

                        CategoryItem newItem = categoryData.getItems().get(0);
                        ArrayList<CategoryItem> currentChildren = new ArrayList<>();

                        for (int i = 0; i < mAdapter.getData().size(); i++) {
                            if (mAdapter.getData().get(i).getCategoryId().equals(newItem.getPath()[newItem.getDepth() - 3])) {
                                currentChildren.addAll(mAdapter.getData().get(i).getChildren());
                                break;
                            }
                        }
                        for (int i = 0; i < currentChildren.size(); i++) {
                            if (currentChildren.get(i).getCategoryId().equals(newItem.getPath()[0])) {
                                currentChildren.addAll(i + 1, categoryData.getItems());
                                break;
                            }
                        }
                        mAdapter.expandCategory(currentChildren, newItem.getPath()[newItem.getDepth() - 3]);
                    } else {
                        List<CategoryItem> categoryItemList = new ArrayList<>();
                        for (CategoryItem categoryItem : categoryData.getItems()) {
                            if (categoryItem.getProductCount() > 0) {
                                categoryItemList.add(categoryItem);
                            }
                        }

                        mAdapter.setData(categoryItemList);
                        mRecyclerView.setAdapter(mAdapter);
                    }
                    onViewStateChanged(STATE_SHOW_CONTENT);
                } else {

                    onViewStateChanged(STATE_NO_DATA);
//                    mErrorMessage = " no data";
//                    onViewStateChanged(LoadingStateView.STATE_ERROR);
                }

                mStoreViewModel.setCategoryDataConsumed();
            }
        });
    }

    private CategoryItemViewListener mCategoryItemViewListener = new CategoryItemViewListener() {
        @Override
        public void onSeeAllProductsFromCategory(CategoryItem categoryTreeNode) {

            FilterProductRequest filterProductRequest = new FilterProductRequest();
            filterProductRequest.setCategoryId(categoryTreeNode.getCategoryId());


            openFragment(ProductListFragment.newInstance(filterProductRequest, false));
        }

        @Override
        public void onCategoryHeaderClicked(CategoryItem categoryTreeNode) {
            if (categoryTreeNode.isExpanded()) {
                mAdapter.showExpandLoading(categoryTreeNode.getCategoryId());
                mStoreViewModel.getCategoryList(categoryTreeNode.getCategoryId());
                onViewStateChanged(STATE_SHOW_CONTENT);
            }
        }

        @Override
        public void onCategorySubItemClicked(CategoryItem categoryItem, CategoryItem categorySubItem, int index) {
            categorySubItem.setExpanded(false);
            onCategorySubItemArrowClicked(categoryItem, categorySubItem, index);

            FilterProductRequest filterProductRequest = new FilterProductRequest();
            filterProductRequest.setCategoryId(categorySubItem.getCategoryId());
            filterProductRequest.setCategoryNodeRootParent(categoryItem.getCategoryId());

            openFragment(ProductListFragment.newInstance(filterProductRequest, false));
        }

        @Override
        public void onCategorySubItemArrowClicked(CategoryItem categoryItem, CategoryItem categorySubItem, int index) {
            mAdapter.onSubCategorySelected(categoryItem, categorySubItem);

            if (categorySubItem.isExpanded() && categorySubItem.getChildGroupCount() > 0) {
                mStoreViewModel.getCategoryList(categorySubItem.getCategoryId());
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (getParentFragment() != null) {
            getParentFragment().onActivityResult(requestCode, resultCode, data);
        }
    }


//    private void observeLoading() {
//        getViewModel().isLoading().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
//            @Override
//            public void onChanged(Boolean isLoading) {
//                if (isLoading) {
//                    onViewStateChanged(STATE_LOADING);
//
//                    mLoadingStateView.setStateAndDescription(LoadingStateView.STATE_LOADING, DI.getABResources().getString(R.string.loading_message), true);
//                } else {
//                    mLoadingStateView.setVisibility(View.GONE);
//                }
//            }
//        });
//    }

    private void onViewStateChanged(int state) {
        switch (state) {
            case STATE_NO_INTERNET:
                mLoadingStateView.setStateAndDescription(LoadingStateView.STATE_ERROR, getString(R.string.network_error_no_internet), false);
                mNoData.setVisibility(View.GONE);
                mLoadingStateView.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);

                break;

            case STATE_NO_DATA:
                mNoData.setVisibility(View.VISIBLE);
                mLoadingStateView.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.GONE);

                break;

            case STATE_SHOW_CONTENT:
                mLoadingStateView.setVisibility(View.GONE);
                mNoData.setVisibility(View.GONE);

                mRecyclerView.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    protected void getDataFromServer() {

    }
}
