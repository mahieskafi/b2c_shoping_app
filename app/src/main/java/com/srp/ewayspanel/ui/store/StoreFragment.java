package com.srp.ewayspanel.ui.store;


import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.Observer;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.srp.eways.di.DIMain;
import com.srp.eways.ui.main.MainRootIds;
import com.srp.eways.ui.navigation.INavigationController;
import com.srp.eways.ui.navigation.NavigationMemberFragment;
import com.srp.eways.ui.navigation.NavigationViewType;
import com.srp.eways.ui.view.toolbar.WeiredToolbar;
import com.srp.eways.util.Constants;
import com.srp.eways.util.Utils;
import com.srp.ewayspanel.R;
import com.srp.ewayspanel.di.DI;
import com.srp.ewayspanel.model.shopcart.ShopCartItemModel;
import com.srp.ewayspanel.model.storepage.category.CategoryItem;
import com.srp.ewayspanel.model.storepage.category.CategoryListResponse;
import com.srp.ewayspanel.model.storepage.filter.FilterProductRequest;
import com.srp.ewayspanel.model.storepage.product.ProductInventoryCheckModel;
import com.srp.ewayspanel.repository.storepage.StorePageRepository;
import com.srp.ewayspanel.repository.storepage.category.CategoryTreeNode;
import com.srp.eways.ui.login.UserInfoViewModel;
import com.srp.ewayspanel.ui.shopcart.ShopCartFragment;
import com.srp.ewayspanel.ui.shopcart.ShopCartViewModel;
import com.srp.ewayspanel.ui.store.filter.FilterChanged;
import com.srp.ewayspanel.ui.store.product.InventoryNotExistDialog;
import com.srp.ewayspanel.ui.store.product.detail.ProductDetailFragment;
import com.srp.ewayspanel.ui.store.search.SearchFragment;
import com.srp.eways.ui.view.EmptyView;
import com.srp.eways.ui.view.LoadingStateView;

import java.util.ArrayList;
import java.util.List;

import ir.abmyapp.androidsdk.IABResources;

import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;
import static com.srp.ewayspanel.ui.store.filter.FilterActivity.FILTER_REQUEST_EXTERA;

public class StoreFragment extends NavigationMemberFragment<StoreViewModel> {

    public static String STORE_TAB_INDEX = "store_tab_index";

    public static final int REQUEST_CODE_FILTER = 1005;

    public static final int VIEWSTATE_NO_INTERNET = 0;
    public static final int VIEWSTATE_SHOW_LOADING = 1;
    public static final int VIEWSTATE_SHOW_CONTENT = 2;
    public static final int VIEWSTATE_SHOW_EMPTY = 3;
    public static final int VIEWSTATE_SHOW_ERROR = 4;

    private WeiredToolbar mToolbar;
    private ViewPager mPager;
    private TabLayout mTabbar;

    private LoadingStateView mLoadingView;
    private EmptyView mEmptyView;
    private View mContentViewContainer;

    private UserInfoViewModel mUserInfoViewModel;

    private StoreCategoryPagerAdapter mPagerAdapter;

    private List<CategoryItem> mCategoryTreeNode;

    private int mDefaultTab;

    public static StoreFragment newInstance() {
        StoreFragment fragment = new StoreFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    public StoreFragment() {
        // Required empty public constructor
    }

    @Override
    public StoreViewModel acquireViewModel() {
        mUserInfoViewModel = DI.getViewModel(UserInfoViewModel.class);
        return DI.getViewModel(StoreViewModel.class);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_store;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mToolbar = view.findViewById(R.id.toolbar);

        mPager = view.findViewById(R.id.view_pager);
        mTabbar = view.findViewById(R.id.tab_layout);

        mLoadingView = view.findViewById(R.id.loadingstateview);
        mEmptyView = view.findViewById(R.id.emptyview);
        mContentViewContainer = view.findViewById(R.id.container_content);

        mPagerAdapter = new StoreCategoryPagerAdapter(getChildFragmentManager());

        mPager.setPageTransformer(true, null);
        mPager.setAdapter(mPagerAdapter);


        if (getArguments() != null) {
            mDefaultTab = getArguments().getInt(STORE_TAB_INDEX, mTabbar.getTabCount());

            if (mDefaultTab != 0) {
                mPagerAdapter.setDefaultItem((long) mDefaultTab);
            }
        }

        mTabbar.setupWithViewPager(mPager);

        setupToolbar();

        observeIsLoadingCategoryData();
        observeCategoryData();

        mLoadingView.setRetryListener(new LoadingStateView.RetryListener() {
            @Override
            public void onRetryButtonClicked() {
                getViewModel().getCategoryList(0);
            }
        });

        if (!isNetworkConnected()) {
            onViewStateChanged(VIEWSTATE_NO_INTERNET);
            mEmptyView.setEmptyText(R.string.loadingstateview_text_network_unavailable);
        } else {
            getViewModel().getCategoryList(0);
        }

        mToolbar.setShopIconAction(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                v.setClickable(false);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        v.setClickable(true);
                    }
                }, 400);
                if (!DI.getViewModel(ShopCartViewModel.class).hasAnythingToChange()) {
                    openFragment(ShopCartFragment.Companion.newInstance(),
                            NavigationViewType.NAVIGATION_VIEW_TYPE_HAS_TOOLBAR);
                } else {
                    Toast.makeText(getContext(), DI.getABResources().getString(R.string.shop_cart_product_list_is_updating_error_message), Toast.LENGTH_SHORT).show();
                }
            }
        });

        mToolbar.setDepositActionClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSwitchRoot(MainRootIds.ROOT_DEPOSIT);
            }
        });

        final ShopCartViewModel shopCartViewModel = DI.getViewModel(ShopCartViewModel.class);
        if (shopCartViewModel != null) {
            shopCartViewModel.getShopCartProductList().observe(this, new Observer<ArrayList<ShopCartItemModel>>() {
                @Override
                public void onChanged(ArrayList<ShopCartItemModel> shopCartItemModels) {
                    mToolbar.setProductCount(shopCartItemModels.size());
                }
            });

            shopCartViewModel.getBasketLoading().observe(this, new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean isLoading) {
                    if (isLoading) {
                        mToolbar.showProgress();
                    } else {
                        mToolbar.stopProgress();
                    }
                }
            });
        }

        shopCartViewModel.getInventoryForOpenFragmentLiveData().observe(getViewLifecycleOwner(), new Observer<ProductInventoryCheckModel>() {
            @Override
            public void onChanged(ProductInventoryCheckModel productInventoryCheckModel) {
                if (productInventoryCheckModel != null) {

                    if (productInventoryCheckModel.getStatus()) {
                        openFragment(ProductDetailFragment.newInstance(productInventoryCheckModel.getProductInfo()), NavigationViewType.NAVIGATION_VIEW_TYPE_HAS_TOOLBAR);
                    } else {
                        if (productInventoryCheckModel.getDescription() != null) {
                            Toast.makeText(getContext(), getString(R.string.loadingstateview_loading_connectiontimeout_error_text) + " " +
                                            getString(R.string.loadingstateview_loading_error_text)
                                    , Toast.LENGTH_SHORT).show();
                        } else {
                            new InventoryNotExistDialog(getContext()).show();
                        }
                    }
                    shopCartViewModel.consumeInventoryForOpenFragmentLiveData();
                }
            }
        });
    }

    @Override
    protected void getDataFromServer() {
        getViewModel().getCategoryList(0);
    }

    private void observeIsLoadingCategoryData() {
        getViewModel().isLoading().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                if (isLoading == null) {
                    return;
                }

                if (isLoading) {
                    onViewStateChanged(VIEWSTATE_SHOW_LOADING);

                    mLoadingView.setStateAndDescription(LoadingStateView.STATE_LOADING,
                            DI.getABResources().getString(R.string.loading_message),
                            true);
                }

                getViewModel().setLoadingCategoryDataConsumed();
            }
        });
    }

    private void observeCategoryData() {
        getViewModel().getCategoryData().observe(this, new Observer<CategoryListResponse>() {
            @Override
            public void onChanged(CategoryListResponse categoryData) {
                if (categoryData == null) {
                    return;
                }

                String errorString = categoryData.getDescription();

                if (errorString != null && !errorString.isEmpty()) {
                    onViewStateChanged(VIEWSTATE_SHOW_ERROR);
                    mLoadingView.setStateAndDescription(LoadingStateView.STATE_ERROR, errorString, true);
                    return;
                }

                //success condition
                onViewStateChanged(VIEWSTATE_SHOW_CONTENT);

                if (categoryData.getItems() != null) {
                    List<CategoryItem> categoryItemList = new ArrayList<>();
                    for (CategoryItem categoryItem : categoryData.getItems()) {
                        if (categoryItem.getProductCount() > 0 && categoryItem.getChildGroupCount() > 0) {
                            categoryItemList.add(categoryItem);
                        }
                    }

                    mPagerAdapter.setData(categoryItemList);

                    mCategoryTreeNode = categoryItemList;

                    setupTabbar();
                }

                getViewModel().setCategoryDataConsumed();
            }
        });
    }

    private void setupTabbar() {
        final IABResources resources = DI.getABResources();

        mTabbar.setSelectedTabIndicatorColor(resources.getColor(R.color.store_page_tabbar_indicator_color));

        final Typeface selectedTypeface = ResourcesCompat.getFont(getContext(), R.font.iran_yekan_bold);
        final Typeface unSelectedTypeface = ResourcesCompat.getFont(getContext(), R.font.iran_yekan);

        for (int i = 0; i < mTabbar.getTabCount(); i++) {

            TextView textView = new TextView(getContext());
            textView.setTextColor(resources.getColor(R.color.store_page_tabbar_unselected_text_color));
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.store_page_tabbar_text_size));
            textView.setGravity(Gravity.CENTER);
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            textView.setTypeface(unSelectedTypeface);
            textView.setText(mTabbar.getTabAt(i).getText());

            mTabbar.getTabAt(i).setCustomView(textView);

        }

        mTabbar.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if ((tab.getCustomView()) != null) {
                    ((TextView) tab.getCustomView()).setTypeface(selectedTypeface);
                    ((TextView) tab.getCustomView()).setTextColor(resources.getColor(R.color.store_page_tabbar_selected_text_color));

                    if (tab.getPosition() == mTabbar.getTabCount() - 1) {
                        mToolbar.clearSearch();
                    }

                    Object currentFragment = mPagerAdapter.instantiateItem(mPager, tab.getPosition());

                    if (currentFragment instanceof FilterChanged) {
                        ((FilterChanged) currentFragment).onFilterChanged(null);
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if ((tab.getCustomView()) != null) {
                    ((TextView) tab.getCustomView()).setTypeface(unSelectedTypeface);
                    ((TextView) tab.getCustomView()).setTextColor(resources.getColor(R.color.store_page_tabbar_unselected_text_color));
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                Object currentFragment = mPagerAdapter.instantiateItem(mPager, tab.getPosition());

                if (currentFragment instanceof FilterChanged) {
                    ((FilterChanged) currentFragment).onFilterChanged(null);
                }
            }
        });

        int selectedItem = mTabbar.getTabCount();

        if (getArguments() != null) {
            if (mDefaultTab != 0) {
                selectedItem = (selectedItem - 2) - mPagerAdapter.getSelectedPosition((long) mDefaultTab);
            }
        }
        mPager.setCurrentItem(selectedItem);


        ViewGroup.LayoutParams layoutParams = mTabbar.getLayoutParams();
        if (getArguments() != null && getArguments().getBoolean(Constants.IS_PAGE_TRANSACTION_EXECUTED_KEY)) {
            layoutParams.height = 0;
            mTabbar.setLayoutParams(layoutParams);

            animateIn();
        } else {
            layoutParams.height = DIMain.getABResources().getDimenPixelSize(com.srp.eways.R.dimen.bill_page_tabbar_height);
            mTabbar.setLayoutParams(layoutParams);
        }
    }

    private void setupToolbar() {
        IABResources resources = DI.getABResources();

        mToolbar.setTitle(resources.getString(R.string.store_page_category_title));
        mToolbar.setShowTitle(false);
        mToolbar.setShowDeposit(true);
        mToolbar.setShowShop(true);
        mToolbar.setBackgroundColor(resources.getColor(R.color.store_page_toolbar_background));
        mToolbar.setBackgroundToolbarColor(resources.getColor(R.color.store_page_toolbar_background));

        mToolbar.setOnSearchClickListener(new WeiredToolbar.SearchTextListener() {
            @Override
            public void onSearchListener(String text) {
                FilterProductRequest filterProductRequest = new FilterProductRequest();
                filterProductRequest.setText(text);

                openFragment(SearchFragment.newInstance(filterProductRequest));
            }
        });

        mToolbar.setOnNavigationDrawerClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                toggleDrawer();
            }

        });

        mUserInfoViewModel.getCreditLiveData().observe(this, new Observer<Long>() {

            @Override
            public void onChanged(Long deposit) {
                if (deposit != null) {
                    mToolbar.setDeposit(deposit);
                }
            }

        });
    }

    @Override
    public int getNavigationViewType() {
        return NAVIGATION_VIEW_TYPE_HAS_TOOLBAR_AND_BOTTOM_NAVIGATION;
    }

    private void onViewStateChanged(int chargePageViewState) {
        switch (chargePageViewState) {
            case VIEWSTATE_NO_INTERNET:
                mEmptyView.setVisibility(View.VISIBLE);

                mLoadingView.setVisibility(GONE);
                mContentViewContainer.setVisibility(GONE);
                break;
            case VIEWSTATE_SHOW_ERROR:
                mLoadingView.setVisibility(View.VISIBLE);

                mEmptyView.setVisibility(GONE);
                mContentViewContainer.setVisibility(GONE);
                break;
            case VIEWSTATE_SHOW_LOADING:
                mLoadingView.setVisibility(View.VISIBLE);

                mEmptyView.setVisibility(GONE);
                mContentViewContainer.setVisibility(GONE);
                break;
            case VIEWSTATE_SHOW_CONTENT:
                mContentViewContainer.setVisibility(View.VISIBLE);

                mEmptyView.setVisibility(GONE);
                mLoadingView.setVisibility(GONE);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Utils.hideKeyboard(getActivity());

        if (requestCode == REQUEST_CODE_FILTER) {

            if (resultCode == RESULT_OK) {
                FilterProductRequest filterProductRequest = (FilterProductRequest) data.getSerializableExtra(FILTER_REQUEST_EXTERA);

                if (filterProductRequest.getCategoryId() == 0) {
                    openFragment(SearchFragment.newInstance(filterProductRequest));
                    return;
                }

                Long parentCategoryId = filterProductRequest.getCategoryNodeRootParent();
                if (parentCategoryId != null ) {
                    int selectedItemPosition = mPagerAdapter.getSelectedPosition(parentCategoryId);

                    if (selectedItemPosition != -1) {
                        mPagerAdapter.setFilterProductRequest(selectedItemPosition, filterProductRequest);
                        if (mCategoryTreeNode != null && mPager.getCurrentItem() != (mCategoryTreeNode.size() - 1) - selectedItemPosition) {
                            mPager.setCurrentItem((mCategoryTreeNode.size() - 1) - selectedItemPosition, true);
                        }
                        if (mPagerAdapter.instantiateItem(mPager, mPager.getCurrentItem()) instanceof FilterChanged) {
                            ((FilterChanged) mPagerAdapter.instantiateItem(mPager, mPager.getCurrentItem())).onFilterChanged(filterProductRequest);
                        }
                    } else {
                        ((FilterChanged) mPagerAdapter.instantiateItem(mPager, mPager.getCurrentItem())).onFilterChanged(filterProductRequest);
                    }
                } else {
//                    ((FilterChanged) mPagerAdapter.instantiateItem(null, mPager.getCurrentItem())).onFilterChanged(filterProductRequest);
                    openFragment(SearchFragment.newInstance(filterProductRequest));
                }

            }
        }
    }

    @Override
    public boolean onBackPress() {

        if (mPagerAdapter.getCount() == 0) {
            return super.onBackPress();
        } else {
            INavigationController member = (INavigationController) mPagerAdapter.instantiateItem(mPager, mPager.getCurrentItem());

            return member.onBackPress();
        }
    }

    private void animateIn() {

        int tabBarHeight = DIMain.getABResources().getDimenPixelSize(com.srp.eways.R.dimen.bill_page_tabbar_height);

        ValueAnimator anim = ValueAnimator.ofInt(0, tabBarHeight);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = mTabbar.getLayoutParams();
                layoutParams.height = val;
                mTabbar.setLayoutParams(layoutParams);
            }
        });
        anim.setDuration(300);
        anim.start();
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            mToolbar.clearSearch();
        }
    }
}
