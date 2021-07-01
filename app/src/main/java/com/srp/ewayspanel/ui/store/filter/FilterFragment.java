package com.srp.ewayspanel.ui.store.filter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.srp.eways.base.BaseFragment;
import com.srp.eways.util.Utils;
import com.srp.ewayspanel.R;
import com.srp.ewayspanel.di.DI;
import com.srp.ewayspanel.di.DICommon;
import com.srp.ewayspanel.model.storepage.filter.FilterProductRequest;
import com.srp.ewayspanel.repository.storepage.StorePageRepository;
import com.srp.ewayspanel.repository.storepage.category.CategoryTreeNode;

import com.srp.ewayspanel.ui.store.StoreViewModel;
import com.srp.eways.ui.view.EmptyView;
import com.srp.eways.ui.view.LoadingStateView;
import com.srp.eways.ui.view.button.ButtonElement;
import com.srp.ewayspanel.ui.view.store.filter.FilterHeaderView;
import com.srp.ewayspanel.ui.view.toolbar.FilterToolbar;

import ir.abmyapp.androidsdk.IABResources;

import static android.view.View.GONE;
import static com.srp.ewayspanel.ui.store.filter.FilterActivity.FILTER_REQUEST_EXTERA;

public class FilterFragment extends BaseFragment<StoreViewModel> {

    public static final int VIEWSTATE_NO_INTERNET = 0;
    public static final int VIEWSTATE_SHOW_LOADING = 1;
    public static final int VIEWSTATE_SHOW_CONTENT = 2;
    public static final int VIEWSTATE_SHOW_EMPTY = 3;
    public static final int VIEWSTATE_SHOW_ERROR = 4;

    private boolean mIsCanceledFilter = false;

    public static FilterFragment newInstance(FilterProductRequest filterProductRequest) {
        FilterFragment fragment = new FilterFragment();
        Bundle args = new Bundle();
        args.putSerializable(FILTER_REQUEST_EXTERA, filterProductRequest);

        fragment.setArguments(args);
        return fragment;
    }

    private FilterToolbar mToolbar;
    private ButtonElement mConfirmButton;


    private View mContentViewContainer;

    private RecyclerView mCategoryList;
    private CategoryListAdapter mCategoryListAdapter;

    private FilterProductRequest mFilterProductRequest;

    private long mInitiallySelectedCategoryId = 0;

    @Override
    public StoreViewModel acquireViewModel() {
        return DICommon.getViewModel(StoreViewModel.class);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_filter;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        IABResources abResources = DI.getABResources();

        mFilterProductRequest = new FilterProductRequest();
        if (getArguments() != null && getArguments().getSerializable(FILTER_REQUEST_EXTERA) != null) {
            mFilterProductRequest = (FilterProductRequest) getArguments().getSerializable(FILTER_REQUEST_EXTERA);
        }

        mToolbar = view.findViewById(R.id.toolbar);
        mConfirmButton = view.findViewById(R.id.confirm_button);

        mConfirmButton.setEnabledBackground(abResources.getDrawable(R.drawable.login_button_background_enabled));
        mConfirmButton.setDisableBackground(abResources.getDrawable(R.drawable.login_button_background_disabled));
        mConfirmButton.setText(abResources.getString(R.string.filter_button_text));
        mConfirmButton.setEnable(true);
        mConfirmButton.setTextColor(abResources.getColor(R.color.filter_button_text_color));
        mConfirmButton.setTextSize(abResources.getDimenPixelSize(R.dimen.button_element_text_size));
        mConfirmButton.setTextTypeFace(ResourcesCompat.getFont(getContext(), R.font.iran_yekan));
        mConfirmButton.setClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Utils.hideKeyboard(getActivity());

                if (mCategoryListAdapter != null && mCategoryListAdapter.getHeaderView() != null) {
                    FilterHeaderView filterHeaderView = mCategoryListAdapter.getHeaderView();

                    mFilterProductRequest.setMaxPrice(filterHeaderView.getMaxPrice());
                    mFilterProductRequest.setMinPrice(filterHeaderView.getMinPrice());
                    mFilterProductRequest.setText(filterHeaderView.getSearchValue());
                    mFilterProductRequest.setOnlyAvailable(filterHeaderView.IsAvailability());

                    Intent intent = new Intent();
                    intent.putExtra(FILTER_REQUEST_EXTERA, mFilterProductRequest);

                    if (getActivity() != null) {
                        (getActivity()).setResult(Activity.RESULT_OK, intent);
                    }
                    getActivity().finish();
                }
            }
        });


        mContentViewContainer = view.findViewById(R.id.container_content);

        mCategoryList = view.findViewById(R.id.category_list);
        mCategoryList.setLayoutManager(new LinearLayoutManager(getContext()));

        setupToolbar();

        observeIsLoadingCategoryData();
        observeCategoryData();

        mCategoryListAdapter = new CategoryListAdapter(null, new CategoryNodeListener() {
            @Override
            public void selectedNode(CategoryTreeNode selectedNode) {
                mFilterProductRequest.setCategoryId(selectedNode.getCategoryId());

                mIsCanceledFilter = false;
            }

            @Override
            public void selectedNodeRootParent(Long rootParent) {
                mFilterProductRequest.setCategoryNodeRootParent(rootParent);

                mIsCanceledFilter = false;
            }

        }, new CategoryListAdapter.onLoadingRetryListener() {
            @Override
            public void onLoadingRetry() {
                getViewModel().getCategoryRawList();
            }
        }, VIEWSTATE_SHOW_LOADING);

        mCategoryList.setAdapter(mCategoryListAdapter);
        mCategoryListAdapter.setFilterHeader(mFilterProductRequest);


        if (!isNetworkConnected()) {
            mCategoryListAdapter.setLoadingState(VIEWSTATE_NO_INTERNET);
        } else {
            getViewModel().getCategoryRawList();
        }
    }

    private void observeIsLoadingCategoryData() {
        getViewModel().isLoading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                if (isLoading == null) {
                    return;
                }

                if (isLoading) {
                    mCategoryListAdapter.setLoadingState(VIEWSTATE_SHOW_LOADING);
                }

                getViewModel().setLoadingCategoryDataConsumed();
            }
        });
    }

    private void observeCategoryData() {
        getViewModel().getCategoryRawData().observe(this, new Observer<StorePageRepository.CategoryData>() {
            @Override
            public void onChanged(StorePageRepository.CategoryData categoryData) {
                if (categoryData == null) {
                    return;
                }

                String errorString = categoryData.errorMessage;

                if (errorString != null) {
                    mCategoryListAdapter.setLoadingState(VIEWSTATE_SHOW_ERROR);
                    return;
                }

                //success condition
                mCategoryListAdapter.setLoadingState(VIEWSTATE_SHOW_CONTENT);
                if (categoryData.categoryTree != null) {
                    //Todo: remove prev lines
                    observeTreeCopy();
                }

                getViewModel().setCategoryRawDataConsumed();
            }
        });
    }

    private void observeTreeCopy() {
        getViewModel().copyCategoryTree(new StoreViewModel.CopyCategoryTreeListener() {
            @Override
            public void onCopied(StorePageRepository.CategoryData categoryData, CategoryTreeNode copy) {
                mCategoryListAdapter.setLoadingState(VIEWSTATE_SHOW_CONTENT);

//                if (mCategoryListAdapter == null) {
//                   mCategoryListAdapter.setNewData();
//                    mCategoryList.setAdapter(mCategoryListAdapter);
//                } else {
                mCategoryListAdapter.setNewData(categoryData);
//                }

                if (mInitiallySelectedCategoryId != 0) {
                    mCategoryListAdapter.setCategorySelected(mInitiallySelectedCategoryId);
                }

                mCategoryListAdapter.setCategorySelected(mFilterProductRequest.getCategoryId());
                mCategoryListAdapter.setFilterHeader(mFilterProductRequest);
            }
        });
    }

    private void setupToolbar() {

        mToolbar.setListener(new FilterToolbar.FilterToolbarClickListener() {
            @Override
            public void onExitClicked() {
                getActivity().finish();
            }

            @Override
            public void onCancelAllClicked() {

                if (mCategoryListAdapter != null && mCategoryListAdapter.getHeaderView() != null) {
                    mCategoryListAdapter.getHeaderView().cancelAllFilter();


                    if (!mIsCanceledFilter) {
                        if (mCategoryListAdapter.getSelectedCategory() != -1) {
                            mCategoryListAdapter.resetSelection();
                            mCategoryListAdapter.notifyDataSetChanged();
                        }

//                        mFilterProductRequest.setCategoryId(0);
//                        mFilterProductRequest.setCategoryNodeRootParent(0L);

                        mIsCanceledFilter = true;
                    }
                }
            }
        });

    }


}
