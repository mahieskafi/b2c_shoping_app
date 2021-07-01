package com.srp.ewayspanel.ui.club;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;

import com.srp.eways.ui.charge.buy.RadioOptionModel;
import com.srp.eways.ui.charge.dialog.DialogContentView;
import com.srp.eways.ui.navigation.NavigationViewType;
import com.srp.eways.ui.view.NColumnRadioGroup;
import com.srp.eways.ui.view.ViewUtils;
import com.srp.eways.ui.view.dialog.BottomDialog;
import com.srp.eways.ui.view.toolbar.WeiredToolbar;
import com.srp.eways.util.HidingScrollListener;
import com.srp.eways.util.Utils;
import com.srp.ewayspanel.R;
import com.srp.ewayspanel.di.DI;
import com.srp.eways.model.login.UserInfo;
import com.srp.ewayspanel.model.shopcart.ShopCartItemModel;
import com.srp.ewayspanel.model.storepage.filter.FilterProductRequest;
import com.srp.ewayspanel.model.storepage.filter.FilteredProduct;
import com.srp.ewayspanel.model.storepage.filter.ProductInfo;
import com.srp.eways.ui.IContentLoadingStateManager;
import com.srp.eways.base.BasePageableListFragment;
import com.srp.eways.base.BaseRecyclerAdapter2;
import com.srp.eways.ui.login.UserInfoViewModel;
import com.srp.ewayspanel.model.storepage.filter.SortItem;
import com.srp.ewayspanel.model.storepage.product.ProductInventoryAddCheckModel;
import com.srp.ewayspanel.model.storepage.product.ProductInventoryCheckModel;
import com.srp.ewayspanel.ui.shopcart.ShopCartFragment;
import com.srp.ewayspanel.ui.shopcart.ShopCartViewModel;
import com.srp.ewayspanel.ui.store.GridProductFragment;
import com.srp.ewayspanel.ui.store.filter.FilterActivity;
import com.srp.ewayspanel.ui.store.filter.FilteredProductViewModel;
import com.srp.ewayspanel.ui.store.product.InventoryNotExistDialog;
import com.srp.ewayspanel.ui.store.product.ProductItemClickListener;
import com.srp.eways.ui.view.LoadingStateView;
import com.srp.ewayspanel.ui.store.product.detail.ProductDetailFragment;
import com.srp.ewayspanel.ui.store.search.SearchFragment;
import com.srp.ewayspanel.ui.view.store.StoreFilterHeaderView;
import com.srp.ewayspanel.ui.view.store.sort.SortRadioGroup;

import java.util.ArrayList;

import ir.abmyapp.androidsdk.IABResources;

import static android.app.Activity.RESULT_OK;
import static androidx.core.content.ContextCompat.getSystemService;
import static com.srp.ewayspanel.ui.store.StoreFragment.REQUEST_CODE_FILTER;
import static com.srp.ewayspanel.ui.store.filter.FilterActivity.FILTER_REQUEST_EXTERA;

public class ClubFragment extends BasePageableListFragment<FilteredProductViewModel>
        implements StoreFilterHeaderView.FilterHeaderViewClickListener,
        IContentLoadingStateManager, ProductItemClickListener, LoadingStateView.RetryListener,
        BasePageableListFragment.OnRecyclereScrollListener, GridProductFragment.ProductListFragmentListener {

    public static ClubFragment newInstance() {
        return new ClubFragment();
    }

    public static final String FILTER_PRODUCT_REQUEST = "FilterProductRequest";
    public static final String IS_SEARCH_PRODUCT_REQUEST = "IsSearchProductRequest";
    private static final int FRAGMENT_ID = 1;

    private ShopCartViewModel mShopCartViewModel = DI.getViewModel(ShopCartViewModel.class);

    private static Long CLUB_ID = 2728L;

    private WeiredToolbar mToolbar;
    private View mContentViewContainer;

    private GridLayoutManager mLayoutManager;

    private StoreFilterHeaderView mFilterHeaderView;

    private AppCompatTextView mLoyaltyScoreTitle;
    private AppCompatTextView mLoyaltyScoreText;

    private UserInfoViewModel mUserInfoViewModel;

    private Long mLoyaltyScore = 0L;
    private FilterProductRequest mFilterProductRequest;

    private GridLayoutManager.SpanSizeLookup mGridSpanLookup;

    private DialogContentView mContentView;
    private SortRadioGroup mSortRadioGroup;
    private SortItem mSortItems;
    private boolean isSearch;

    public boolean mIsHorizontally = false;
    public int mMarginTop;
    public int mFilterHeaderViewHeight;

    private BottomDialog bottomDialog;


    public static ClubFragment newInstance(FilterProductRequest filterProduct, boolean isSearch) {
        ClubFragment fragment = new ClubFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(FILTER_PRODUCT_REQUEST, filterProduct);
        bundle.putBoolean(IS_SEARCH_PRODUCT_REQUEST, isSearch);


        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public FilteredProductViewModel acquireViewModel() {
        mUserInfoViewModel = DI.getViewModel(UserInfoViewModel.class);

        return DI.getViewModel(FilteredProductViewModel.class);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_club;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        IABResources abResources = DI.getABResources();

        mToolbar = view.findViewById(R.id.toolbar);
        mLoadingStateView = view.findViewById(R.id.loadingstateview);
        mEmptyView = view.findViewById(R.id.emptyview);

        mLoyaltyScoreTitle = view.findViewById(R.id.tv_loyalty_title);
        mLoyaltyScoreText = view.findViewById(R.id.tv_loyalty_score);

        mContentViewContainer = view.findViewById(R.id.container_content);
        mRecyclerView = view.findViewById(R.id.recyclerview);
        mFilterHeaderView = view.findViewById(R.id.filteritemvview);
        mFilterHeaderView.setListener(this);

        setupToolbar();
        setupBottomDialog();

        mMarginTop = abResources.getDimenPixelSize(R.dimen.loyaltyscoreheaderview_list_margin_top);

        Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.iran_yekan);

        mLoyaltyScoreTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, abResources.getDimenPixelSize(R.dimen.loyaltyscoreheaderview_title_textsize));
        mLoyaltyScoreTitle.setTypeface(typeface);
        mLoyaltyScoreTitle.setTextColor(abResources.getColor(R.color.loyaltyscore_title));
        mLoyaltyScoreTitle.setText(abResources.getString(R.string.loyaltyscore_title));

        mLoyaltyScoreText.setTextSize(TypedValue.COMPLEX_UNIT_PX, abResources.getDimenPixelSize(R.dimen.loyaltyscoreheaderview_value_textsize));
        mLoyaltyScoreText.setTypeface(typeface);
        mLoyaltyScoreText.setTextColor(abResources.getColor(R.color.loyaltyscore_value));

        mFilterProductRequest = new FilterProductRequest();
        mFilterProductRequest.setCategoryId(CLUB_ID);//Todo: set clubCategoryId dynamically

        configFilterableList();

        mAdapter = new ClubProductsAdapter(getContext(), new BaseRecyclerAdapter2.RetryClickListener() {
            @Override
            public void onClicked() {
                //TODO: retry
            }
        }, this, mIsHorizontally);

        mAdapter.setHasStableIds(true);
        mRecyclerView.setItemAnimator(null);
        mRecyclerView.setAdapter(mAdapter);

        setLoadingRetryListener(this);
        setPaginationScrollListener(this);

        observeUserInfo();

        observeClubData();

        getList();

        mShopCartViewModel.getShopCartProductList().observe(getViewLifecycleOwner(), new Observer<ArrayList<ShopCartItemModel>>() {
            @Override
            public void onChanged(ArrayList<ShopCartItemModel> shopCartItemModels) {
                if (mAdapter != null) {
                    mAdapter.notifyDataSetChanged();
                }
            }
        });

        mShopCartViewModel.getInventoryForAddLiveData().observe(getViewLifecycleOwner(), new Observer<ProductInventoryAddCheckModel>() {
            @Override
            public void onChanged(ProductInventoryAddCheckModel productInfo) {
                if (productInfo != null && productInfo.getObserverId() == FRAGMENT_ID) {
                    new InventoryNotExistDialog(getContext()).show();

                    mAdapter.notifyDataSetChanged();

                    mShopCartViewModel.consumeInventoryForAddLiveData();
                }
            }
        });

        mShopCartViewModel.getInventoryForOpenFragmentLiveData().observe(getViewLifecycleOwner(), new Observer<ProductInventoryCheckModel>() {
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
                    mShopCartViewModel.consumeInventoryForOpenFragmentLiveData();
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
                    mLoyaltyScoreText.setText(Utils.toPersianPriceNumber(userInfo.getLoyaltyScore()));
                    ((ClubProductsAdapter) mAdapter).setLoyaltyScore(userInfo.getLoyaltyScore());

//                    getList();
                }
            }
        });
    }

    private void getList() {
        if (!isNetworkConnected()) {
            setLoadingState(VIEWSTATE_NO_INTERNET_ERROR);

        } else {
            setLoadingState(VIEWSTATE_SHOW_LOADING);
            getViewModel().getFilteredProductList(mFilterProductRequest);
        }
    }

    private void observeClubData() {
        getViewModel().getFilteredProductLiveData().observe(getViewLifecycleOwner(), new Observer<FilteredProduct>() {
            @Override
            public void onChanged(FilteredProduct filteredProduct) {
                if (filteredProduct == null) {
                    return;
                }

                int status = filteredProduct.getStatus();
                mErrorMessage = filteredProduct.getDescription();

                ArrayList<ProductInfo> productInfos = filteredProduct.getProducts();

                checkDataStatus(productInfos, status);

                if (mAdapter.getData() != null && mAdapter.getData().size() <= 10) {
                    mRecyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                }

                getViewModel().consumeFilteredProductLiveData();
            }
        });
    }

    private void setupToolbar() {
        final IABResources resources = DI.getABResources();

        mToolbar.setTitle(resources.getString(R.string.fragment_club_title));
        mToolbar.setShowTitle(false);
        mToolbar.setShowDeposit(false);
        mToolbar.setShowShop(true);
        mToolbar.setBackgroundColor(resources.getColor(R.color.store_page_toolbar_background));

        mToolbar.setOnNavigationDrawerClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                toggleDrawer();
            }

        });

        mToolbar.setOnShopIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                v.setClickable(false);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        v.setClickable(true);
                    }
                }, 400);
                if (!mShopCartViewModel.hasAnythingToChange()) {
                    openFragment(ShopCartFragment.Companion.newInstance(),
                            NavigationViewType.NAVIGATION_VIEW_TYPE_HAS_TOOLBAR);
                } else {
                    Toast.makeText(getContext(), resources.getString(R.string.shop_cart_product_list_is_updating_error_message), Toast.LENGTH_SHORT).show();
                }
            }
        });

        final ShopCartViewModel shopCartViewModel = DI.getViewModel(ShopCartViewModel.class);
        if (shopCartViewModel != null) {
            shopCartViewModel.getShopCartProductList().observe(getViewLifecycleOwner(), new Observer<ArrayList<ShopCartItemModel>>() {
                @Override
                public void onChanged(ArrayList<ShopCartItemModel> shopCartItemModels) {
                    mToolbar.setProductCount(shopCartItemModels.size());
                }
            });

            shopCartViewModel.getBasketLoading().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
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

        mToolbar.setOnSearchClickListener(new WeiredToolbar.SearchTextListener() {
            @Override
            public void onSearchListener(String text) {
                FilterProductRequest filterProductRequest = new FilterProductRequest();
//                filterProductRequest.setCategoryId(CLUB_ID);
                filterProductRequest.setText(text);

                openFragment(SearchFragment.newInstance(filterProductRequest));
            }
        });
    }

    private void configFilterableList() {

        mLayoutManager = new ViewUtils.RtlGridLayoutManager(getContext(), 2);

        mGridSpanLookup = new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (mAdapter.getItemViewType(position)) {
                    case ClubProductsAdapter.TYPE_HEADER:
                    case BaseRecyclerAdapter2.VIEW_TYPE_LOADING_LOAD_MORE:
                        if (mIsHorizontally)
                            return 1;
                        return 2;
                    default:
                        return 1;
                }
            }
        };

        mLayoutManager.setSpanSizeLookup(mGridSpanLookup);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                mContentViewContainer.setVisibility(visibility);
            }
        });

        mRecyclerView.addOnScrollListener(new HidingScrollListener() {
            @Override
            public void onHide() {
                onHideFilterHeaderView();
            }

            @Override
            public void onShow() {
                onShowFilterHeaderView();
            }
        });

        mFilterHeaderView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mFilterHeaderViewHeight = mFilterHeaderView.getHeight() + mMarginTop;
                mFilterHeaderView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                ((ClubProductsAdapter) mAdapter).setHeaderViewHeight(mFilterHeaderViewHeight);
            }
        });
    }

    public void configRecyclerLayout() {
        ((ClubProductsAdapter) mAdapter).setHeaderViewHeight(mFilterHeaderViewHeight);
        ((ClubProductsAdapter) mAdapter).setIsHorizontal(mIsHorizontally);

        if (mIsHorizontally) {
            mLayoutManager = new GridLayoutManager(getContext(), 1);

        } else {
            mLayoutManager = new ViewUtils.RtlGridLayoutManager(getContext(), 2);
        }

        mLayoutManager.setSpanSizeLookup(mGridSpanLookup);
        mRecyclerView.setLayoutManager(mLayoutManager);
        setPaginationScrollListener(this);

        mRecyclerView.setAdapter(mAdapter);
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
        mShopCartViewModel.getInventoryAndAddProduct(productInfo, isTotalCount ? newCount : 1, isTotalCount, FRAGMENT_ID);

    }

    @Override
    public void onDeleteClicked(int productId) {
        mShopCartViewModel.removeProduct(productId, 0, true);
    }


    @Override
    protected void getDataFromServer() {
        if (mAdapter.getData() != null && mAdapter.getData().size() > 0) {
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        } else {
            mAdapter.clearData();
            getViewModel().clearData();
            getList();
        }
    }

    @Override
    public void onRetryButtonClicked() {
        getList();
    }

    @Override
    public boolean hasMoreItems() {
        return getViewModel().hasMore();
    }

    @Override
    public void loadMoreItems() {
        getViewModel().getFilteredProductList(mFilterProductRequest);
    }

    private void setupBottomDialog() {

        IABResources abResources = DI.getABResources();

        mSortRadioGroup = new SortRadioGroup(getContext());
        mSortItems = new SortItem();
        mSortRadioGroup.setData(mSortItems.getRadioList());

        bottomDialog = new BottomDialog();
        bottomDialog.setIcon(abResources.getDrawable(R.drawable.ic_filteritem_sort_new));
        bottomDialog.setTitle(abResources.getString(R.string.filter_sort_dialog_title));

        mContentView = new DialogContentView(getContext());

        mContentView.setIconVisibility(false);
        mContentView.setChoiceTitleVisibility(false);
        mContentView.showRial(false);
        mContentView.setBottomDialig(true);

        mContentView.setChildContentView(mSortRadioGroup);
        bottomDialog.setChildContentView(mContentView);

        bottomDialog.setListener(new BottomDialog.ConfirmationBottomDialogItemClickListener() {
            @Override
            public void onConfirmClicked() {
                bottomDialog.dismiss();
                resetAdapter();

                getList();
            }

            @Override
            public void onCancelClicked() {
                bottomDialog.dismiss();
            }
        });
    }

    @Override
    public void onFilterClicked() {
        startActivityForResult(FilterActivity.getIntent(getContext(), mFilterProductRequest), REQUEST_CODE_FILTER);
    }

    private void resetAdapter() {
        mAdapter = new ClubProductsAdapter(getContext(), null, this, mIsHorizontally);
        ((ClubProductsAdapter) mAdapter).setHeaderViewHeight(mFilterHeaderViewHeight);
        ((ClubProductsAdapter) mAdapter).setLoyaltyScore(mUserInfoViewModel.getUserInfoLiveData().getValue().getLoyaltyScore());
        mRecyclerView.setAdapter(mAdapter);

        getViewModel().clearData();
    }

    @Override
    public void onSortClicked() {
        int selectedPosition = mSortItems.getSelectedPosition(mFilterProductRequest.getOrder(), mFilterProductRequest.getSort());
        if (selectedPosition != -1) {
            mSortRadioGroup.setSelectedRadioButton(selectedPosition);
            bottomDialog.setButtonEnable(true);
        }

        mSortRadioGroup.setOnItemSelectedListener(new NColumnRadioGroup.RadioGroupListener() {
            @Override
            public void onItemSelected(int index, RadioOptionModel data) {
                bottomDialog.setButtonEnable(true);

                mFilterProductRequest.setSort(((SortItem.Item) data.option).getSort());
                mFilterProductRequest.setOrder(((SortItem.Item) data.option).getOrder());

                mFilterHeaderView.setSortSubtitle(((SortItem.Item) data.option).getTitle());
            }

            @Override
            public void onItemRemoved(int index, RadioOptionModel removedData) {
            }
        });

        bottomDialog.show(getChildFragmentManager(), "TAG");
    }

    @Override
    public void onChangeViewClicked() {
        mIsHorizontally = !mIsHorizontally;

        configRecyclerLayout();

        mFilterHeaderView.changeChangeViewIcon(mIsHorizontally);
    }

    @Override
    public void onShowFilterHeaderView() {
        mFilterHeaderView.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
    }

    @Override
    public void onHideFilterHeaderView() {
        mFilterHeaderView.animate().translationY(-2 * mFilterHeaderView.getHeight()).setInterpolator(new AccelerateInterpolator(2)).start();
    }

    @Override
    public View getFilterHeaderView() {
        return mFilterHeaderView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_FILTER) {

            if (resultCode == RESULT_OK) {
                FilterProductRequest filterProductRequest = (FilterProductRequest) data.getSerializableExtra(FILTER_REQUEST_EXTERA);

                if (filterProductRequest.getCategoryId() == CLUB_ID) {
                    mFilterProductRequest = filterProductRequest;
                    resetAdapter();
                    getList();
                    return;
                }

                if (filterProductRequest.getCategoryId() == 0) {
                    openFragment(SearchFragment.newInstance(filterProductRequest), NAVIGATION_VIEW_TYPE_HAS_TOOLBAR);
                    return;
                }

                Long parentCategoryId = filterProductRequest.getCategoryNodeRootParent();
                if (parentCategoryId != null && parentCategoryId != 0) {
                    if (parentCategoryId.equals(CLUB_ID)) {
                        mFilterProductRequest = filterProductRequest;
                        resetAdapter();
                        getList();
                    } else {
                        openFragment(SearchFragment.newInstance(filterProductRequest), NAVIGATION_VIEW_TYPE_HAS_TOOLBAR);
                    }

                } else {
                    openFragment(SearchFragment.newInstance(filterProductRequest), NAVIGATION_VIEW_TYPE_HAS_TOOLBAR);
                }
            }
        }
    }
}
