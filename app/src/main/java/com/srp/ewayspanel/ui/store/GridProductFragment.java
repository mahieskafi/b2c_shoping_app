package com.srp.ewayspanel.ui.store;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.UtteranceProgressListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;

import com.srp.eways.model.login.UserInfo;
import com.srp.eways.ui.login.UserInfoViewModel;
import com.srp.eways.ui.view.ViewUtils;
import com.srp.eways.util.Utils;
import com.srp.ewayspanel.R;
import com.srp.ewayspanel.di.DI;
import com.srp.ewayspanel.model.shopcart.ShopCartItemModel;
import com.srp.ewayspanel.model.storepage.filter.FilterProductRequest;
import com.srp.ewayspanel.model.storepage.filter.FilteredProduct;
import com.srp.ewayspanel.model.storepage.filter.ProductInfo;
import com.srp.eways.base.BasePageableListFragment;
import com.srp.eways.base.BaseRecyclerAdapter2;
import com.srp.ewayspanel.model.storepage.product.ProductInventoryAddCheckModel;
import com.srp.ewayspanel.ui.shopcart.ShopCartViewModel;
import com.srp.ewayspanel.ui.store.filter.FilteredProductViewModel;
import com.srp.ewayspanel.ui.store.product.InventoryNotExistDialog;
import com.srp.ewayspanel.ui.store.product.ProductAdapter;
import com.srp.ewayspanel.ui.store.product.ProductItemClickListener;
import com.srp.eways.ui.view.LoadingStateView;
import com.srp.eways.util.HidingScrollListener;

import java.util.ArrayList;

import ir.abmyapp.androidsdk.IABResources;
import okhttp3.internal.Util;

/**
 * Created by Eskafi on 11/10/2019.
 */
public class GridProductFragment extends BasePageableListFragment<FilteredProductViewModel>
        implements ProductItemClickListener, LoadingStateView.RetryListener, BasePageableListFragment.OnRecyclereScrollListener {


    public interface ProductListFragmentListener {
        void onShowFilterHeaderView();

        void onHideFilterHeaderView();

        View getFilterHeaderView();
    }

    public static final String FILTER_PRODUCT_REQUEST = "FilterProductRequest";
    public static final String IS_SEARCH_PRODUCT_REQUEST = "IsSearchProductRequest";
    public static final String IS_HORIZONTALY = "isHorizontaly";
    private static final int FRAGMENT_ID = 2;

    private ShopCartViewModel mShopCartViewModel = DI.getViewModel(ShopCartViewModel.class);

    private FrameLayout mViewParent;

    private GridLayoutManager mLayoutManager;

    private FilterProductRequest mFilterProductRequest;
    private boolean isSearch;

    private ProductListFragmentListener mParentFragmentListener;

    private UserInfoViewModel mUserInfoViewModel;
    private Long mLoyaltyScore = 0L;

    private int mFilterHeaderViewHeight;
    private View mFilterHeaderView;

    //    private boolean mFromMainPage = false;
    public boolean mIsHorizontally = false;

    public static GridProductFragment newInstance(FilterProductRequest filterProduct, boolean isSearch) {
        GridProductFragment fragment = new GridProductFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(FILTER_PRODUCT_REQUEST, filterProduct);
        bundle.putBoolean(IS_SEARCH_PRODUCT_REQUEST, isSearch);


        fragment.setArguments(bundle);
        return fragment;
    }

    public static GridProductFragment newInstance(FilterProductRequest filterProduct, boolean isSearch, boolean isHorizontal) {

        GridProductFragment returnValue = newInstance(filterProduct, isSearch);

        returnValue.setIsHorizontal(isHorizontal);

        return returnValue;
    }

    public void setIsHorizontal(boolean isHorizontal) {
        mIsHorizontally = isHorizontal;

        changeRecyclerLayout();
    }


    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final IABResources abResources = DI.getABResources();

        if (getArguments() != null) {
            mFilterProductRequest = (FilterProductRequest) (getArguments().getSerializable(FILTER_PRODUCT_REQUEST));
            isSearch = getArguments().getBoolean(IS_SEARCH_PRODUCT_REQUEST, false);
        }

        mViewParent = view.findViewById(R.id.view_parent);
        mViewParent.setBackgroundColor(abResources.getColor(R.color.background));

        mRecyclerView = view.findViewById(R.id.rv_filtered_product);


        mLoadingStateView = view.findViewById(R.id.loadingstateview);
        mEmptyView = view.findViewById(R.id.emptyview);


        mRecyclerView.setItemAnimator(null);

        changeRecyclerLayout();

        configFilterableList();
        observeUserInfo();
        setLoadingRetryListener(this);

        setPaginationScrollListener(this);

        if (mFilterProductRequest != null) {
            if (mFilterProductRequest.getPageIndex() == 0) {
                setLoadingState(LoadingStateView.STATE_LOADING);
            }

            if (isSearch) {
                getViewModel().searchProducts(mFilterProductRequest);
            } else {
                getViewModel().getFilteredProductList(mFilterProductRequest);
            }
        }

        getViewModel().getFilteredProductLiveData().observe(getViewLifecycleOwner(), new Observer<FilteredProduct>() {
            @Override
            public void onChanged(FilteredProduct filteredProduct) {


                if (filteredProduct != null) {
                    ArrayList<ProductInfo> productInfos = filteredProduct.getProducts();

                    int status = filteredProduct.getStatus();
                    mErrorMessage = filteredProduct.getDescription();

                    checkDataStatus(productInfos, status);

                    getViewModel().consumeFilteredProductLiveData();
                }

            }
        });

        mShopCartViewModel.getShopCartProductList().observe(getViewLifecycleOwner(), new Observer<ArrayList<ShopCartItemModel>>() {
            @Override
            public void onChanged(ArrayList<ShopCartItemModel> shopCartItemModels) {
                mAdapter.notifyDataSetChanged();
            }
        });

        mShopCartViewModel.getInventoryForAddLiveData().observe(getViewLifecycleOwner(), new Observer<ProductInventoryAddCheckModel>() {
            @Override
            public void onChanged(ProductInventoryAddCheckModel productInfo) {
                if (productInfo != null && productInfo.getObserverId() == FRAGMENT_ID) {
                    mAdapter.notifyDataSetChanged();

                    new InventoryNotExistDialog(getContext()).show();

                    mShopCartViewModel.consumeInventoryForAddLiveData();
                }
            }
        });

    }

    private void observeUserInfo() {
        mUserInfoViewModel.getUserInfoLiveData().observe(getViewLifecycleOwner(), new Observer<UserInfo>() {
            @Override
            public void onChanged(UserInfo userInfo) {
                if (userInfo == null) {
                    return;
                }

                Long loyaltyScore = userInfo.getLoyaltyScore();
                if (loyaltyScore != null) {
                    ((ProductAdapter) mAdapter).setLoyaltyScore(userInfo.getLoyaltyScore());

                }
            }
        });
    }

    private void configFilterableList() {
        if (mParentFragmentListener != null) {

            mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    switch (mAdapter.getItemViewType(position)) {
                        case BaseRecyclerAdapter2.VIEW_TYPE_ITEM:
                        case ProductAdapter.TYPE_CLUB:
                            return 1;
                        case ProductAdapter.TYPE_HEADER:
                            if (mIsHorizontally)
                                return 1;
                            return 2;
                        case BaseRecyclerAdapter2.VIEW_TYPE_LOADING_LOAD_MORE:
                            if (mIsHorizontally)
                                return 1;
                            return 2;
                        default:
                            return -1;
                    }
                }
            });

            mRecyclerView.setLayoutManager(mLayoutManager);

            mRecyclerView.addOnScrollListener(new HidingScrollListener() {
                @Override
                public void onHide() {
                    mParentFragmentListener.onHideFilterHeaderView();
                }

                @Override
                public void onShow() {
                    mParentFragmentListener.onShowFilterHeaderView();
                }
            });

            mFilterHeaderView = mParentFragmentListener.getFilterHeaderView();

            mFilterHeaderView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    mFilterHeaderViewHeight = mFilterHeaderView.getHeight();
                    mFilterHeaderView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                    ((ProductAdapter) mAdapter).setHeaderViewHeight(mFilterHeaderViewHeight);

                    FrameLayout.LayoutParams marginLayoutParams =
                            new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    marginLayoutParams.topMargin = mFilterHeaderViewHeight;

                    mLoadingStateView.setLayoutParams(marginLayoutParams);
                    mEmptyView.setLayoutParams(marginLayoutParams);
                }
            });
        }
    }

    public void changeRecyclerLayout() {
        mAdapter = new ProductAdapter(getContext(), new BaseRecyclerAdapter2.RetryClickListener() {
            @Override
            public void onClicked() {
                //TODO: retry
            }
        }, this, false, mIsHorizontally);
        mAdapter.setHasStableIds(true);

        if (mRecyclerView != null) {
            if (mIsHorizontally) {
                mLayoutManager = new GridLayoutManager(getContext(), 1);

            } else {
                mLayoutManager = new ViewUtils.RtlGridLayoutManager(getContext(), 2);
            }

            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (getParentFragment() instanceof ProductListFragmentListener) {
            mParentFragmentListener = (ProductListFragmentListener) getParentFragment();
        }
    }

    @Override
    protected void getDataFromServer() {
        if (isSearch) {
            getViewModel().searchProducts(mFilterProductRequest);
        } else {
            getViewModel().getFilteredProductList(mFilterProductRequest);
        }
    }

    @Override
    public FilteredProductViewModel acquireViewModel() {
        mUserInfoViewModel = DI.getViewModel(UserInfoViewModel.class);
        return DI.getViewModel(FilteredProductViewModel.class);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_product_grid;
    }

    @Override
    public void onItemClick(int position, ProductInfo item) {
        mShopCartViewModel.getInventoryAndOpenDetail(item);
    }

    @Override
    public void onRemoveClicked(int productId, int newCount, boolean isTotalCount) {
        mShopCartViewModel.removeProduct(productId, isTotalCount ? newCount : 1, isTotalCount);
    }

    @Override
    public void onAddClicked(com.srp.ewayspanel.model.shopcart.ProductInfo productInfo, int newCount, boolean isTotalCount) {
        mShopCartViewModel.getInventoryAndAddProduct(productInfo, isTotalCount ? newCount : 1, false, FRAGMENT_ID);
    }

    @Override
    public void onDeleteClicked(int productId) {
        mShopCartViewModel.removeProduct(productId, 0, true);
    }

    @Override
    public void onRetryButtonClicked() {
        setLoadingState(LoadingStateView.STATE_LOADING);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isSearch) {
                    getViewModel().searchProducts(mFilterProductRequest);
                } else {
                    getViewModel().getFilteredProductList(mFilterProductRequest);
                }
            }
        }, 1000);


    }

    @Override
    public boolean hasMoreItems() {
        return getViewModel().hasMore();
    }

    @Override
    public void loadMoreItems() {
        if (isSearch) {
            getViewModel().searchProducts(mFilterProductRequest);
        } else {
            getViewModel().getFilteredProductList(mFilterProductRequest);
        }
    }
}
