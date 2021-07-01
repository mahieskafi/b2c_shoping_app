package com.srp.ewayspanel.ui.store.product.detail;

import android.animation.ObjectAnimator;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.MetricAffectingSpan;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.Observer;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.srp.eways.model.login.UserInfo;
import com.srp.eways.ui.login.UserInfoViewModel;
import com.srp.eways.ui.navigation.NavigationMemberFragment;
import com.srp.eways.ui.navigation.NavigationViewType;
import com.srp.eways.ui.view.button.ButtonElement;
import com.srp.eways.ui.view.EmptyView;
import com.srp.eways.ui.view.LoadingStateView;
import com.srp.eways.ui.view.toolbar.WeiredToolbar;
import com.srp.eways.util.KeyboardVisibilityChangeListener;
import com.srp.eways.util.Utils;
import com.srp.ewayspanel.R;
import com.srp.ewayspanel.di.DI;
import com.srp.ewayspanel.model.shopcart.ShopCartItemModel;
import com.srp.ewayspanel.model.storepage.filter.ProductInfo;
import com.srp.ewayspanel.model.storepage.product.ProductAttribute;
import com.srp.ewayspanel.model.storepage.product.ProductDetail;
import com.srp.ewayspanel.model.storepage.product.ProductDetailModel;
import com.srp.ewayspanel.model.storepage.product.ProductDetailResponse;
import com.srp.ewayspanel.ui.club.ClubProductsAdapter;
import com.srp.ewayspanel.ui.shopcart.ShopCartFragment;
import com.srp.ewayspanel.ui.shopcart.ShopCartViewModel;
import com.srp.ewayspanel.ui.store.product.detail.imagefullscreen.ProductImageSliderFragment;
import com.srp.ewayspanel.ui.view.CounterView2;
import com.srp.ewayspanel.ui.view.store.product.ProductDetailAddToBasketButton;
import com.srp.ewayspanel.ui.view.store.product.ProductTechnicalInfoItemView;
import com.srp.ewayspanel.ui.view.store.product.ProductTechnicalInfoView;
import com.srp.ewayspanel.ui.view.textjustify.JustifyTextView;

import java.util.ArrayList;
import java.util.List;

import ir.abmyapp.androidsdk.IABResources;

import static com.srp.ewayspanel.ui.store.product.detail.ProductImageSliderAdapter.SMALL_IMAGE_VIEW_TYPE;

public class ProductDetailFragment extends NavigationMemberFragment<ProductViewModel> {

    public static final int STATE_NO_INTERNET = 0;
    public static final int STATE_LOADING = 1;
    public static final int STATE_ERROR = 2;
    public static final int STATE_SHOW_CONTENT = 3;

    public static final String EXTRA_PRODUCT_INFO = "productInfo";
    public static final String EXTRA_PRODUCT_ID = "productInfoId";

    public static ProductDetailFragment newInstance(ProductInfo productInfo) {
        ProductDetailFragment fragment = new ProductDetailFragment();

        Bundle args = new Bundle();
        args.putParcelable(EXTRA_PRODUCT_INFO, productInfo);

        fragment.setArguments(args);

        return fragment;
    }

    public static ProductDetailFragment newInstance(int id) {

        ProductDetailFragment fragment = new ProductDetailFragment();
        Bundle args = new Bundle();
        args.putInt(EXTRA_PRODUCT_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    private ShopCartViewModel mShopCartViewModel = DI.getViewModel(ShopCartViewModel.class);

    private WeiredToolbar mToolbar;

    private ViewPager mProductImageSlider;
    private TabLayout mTabLayout;
//    private View mImageZoom;

    private ProductImageSliderAdapter mImageSliderAdapter;

    private FrameLayout mAddToBasketContainer;
    private TextView mTextProductName;
    private TextView mTextDiscount;
    private TextView mTextProductPrice;
    private TextView mTextCountErorr;
    //    private DiscountPercentView mOffPercentView;
    private AppCompatTextView mOffPercentView;

    private AppCompatTextView mTextGuarantee;
    //    private AppCompatTextView mTextSeller;
    private CardView mProductGeneralInfoContainer;

    private CounterView2 mCounterView;
//    private CardView mCounterContainer;

    private ScrollView mContainerContent;
    private LoadingStateView mLoadingStateView;
    private EmptyView mNoInternetView;

    private View mContainerDescription;
    private JustifyTextView mTextDescription;

    private CardView mTechnicalInfoViewContainer;
    private ProductTechnicalInfoView mTechnicalInfoView;

    private TextView mTitleViewTechnicalInfo;

    private ProductInfo mProductInfo;
    private int mProductID;

    private MetricAffectingSpan mPriceSpan;

    private ProductDetailAddToBasketButton mAddToBasketButton;

    private AppCompatTextView mPoint;
    private AppCompatImageView mStarPoint;

    private UserInfoViewModel mUserInfoViewModel;

    private Observer<Boolean> mAddToBasketLoadingListener = new Observer<Boolean>() {
        @Override
        public void onChanged(Boolean isLoading) {

            if (isLoading != null) {
                if (isLoading) {
                    mAddToBasketButton.setLoading(true);
                    mAddToBasketButton.setClickable(false);
                } else {
                    mAddToBasketButton.setLoading(false);
                    mAddToBasketButton.setClickable(true);
                    String serverResponse = mShopCartViewModel.getIndependentError();

                    if (serverResponse != null) {
                        Toast.makeText(getContext(), serverResponse, Toast.LENGTH_LONG).show();
                        ProductDetail productDetail = getViewModel().getProductDetail().getValue().productDetailModel.getProduct();
                        setupCounterView(Math.min(productDetail.getMaxCount(), productDetail.getStock().intValue()),
                                mShopCartViewModel.getProductCount(productDetail.getId().intValue()));
                    }

                    mShopCartViewModel.getIndependentLoading().setValue(null);
                    mShopCartViewModel.getIndependentLoading().removeObserver(this);
                }
            }
        }
    };

    @Override
    public ProductViewModel acquireViewModel() {
        mUserInfoViewModel = DI.getViewModel(UserInfoViewModel.class);
        return DI.getViewModel(ProductViewModel.class);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_productdetail;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        mProductInfo = args.getParcelable(EXTRA_PRODUCT_INFO);
        mProductID = args.getInt(EXTRA_PRODUCT_ID);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        IABResources resources = DI.getABResources();

        mToolbar = view.findViewById(R.id.toolbar);

        mContainerContent = view.findViewById(R.id.container_content);

        mAddToBasketContainer = view.findViewById(R.id.bottom_holder);

        mTextProductName = view.findViewById(R.id.text_name);
        mTextDiscount = view.findViewById(R.id.text_discount);
        mTextProductPrice = view.findViewById(R.id.text_price);
        mOffPercentView = view.findViewById(R.id.off_percent_view);
        mTextCountErorr = view.findViewById(R.id.count_error);
//        mTextSeller = view.findViewById(R.id.text_seller_value);
        mProductGeneralInfoContainer = view.findViewById(R.id.container_productgeneralinfocard);

        mCounterView = view.findViewById(R.id.counterview);
//        mCounterContainer = view.findViewById(R.id.counter_container);

        mContainerDescription = view.findViewById(R.id.container_description);
        mTextDescription = view.findViewById(R.id.product_text_description);

        mTechnicalInfoView = view.findViewById(R.id.productdetail_technicalinfo);
        mTechnicalInfoViewContainer = view.findViewById(R.id.container_technicalinfo);
        mTitleViewTechnicalInfo = view.findViewById(R.id.technicalinfo_title_view);

        mLoadingStateView = view.findViewById(R.id.loadingstateview);
        mNoInternetView = view.findViewById(R.id.emptyview);
        mNoInternetView.setEmptyText(R.string.network_error_no_internet);

//        mImageZoom = view.findViewById(R.id.image_zoom);

        mProductImageSlider = view.findViewById(R.id.productslider);
        mTabLayout = view.findViewById(R.id.tablayout);

        mAddToBasketButton = view.findViewById(R.id.button_addtobasket);

        mAddToBasketButton.setEnable(true);

        mStarPoint = view.findViewById(R.id.ic_point);
        mPoint = view.findViewById(R.id.txt_point);
        mStarPoint.setImageDrawable(resources.getDrawable(R.drawable.star_point_icon_enable));
        mStarPoint.setVisibility(View.GONE);
        mPoint.setVisibility(View.GONE);

        mCounterView.setCountChangeListener(new CounterView2.Counter2ChangeListener() {

            @Override
            public void onCountNotChanged(int count) {

            }

            @Override
            public void onCountRemoved(int newCount, boolean isTotalCount) {
                updateCount(newCount);
            }

            @Override
            public void onCountAdded(int newCount, boolean isTotalCount) {
                updateCount(newCount);
            }
        });

        mProductImageSlider.setPageMargin(DI.getABResources().getDimenPixelSize(R.dimen.productitemslider_pagemargin));
        mTabLayout.setupWithViewPager(mProductImageSlider);

        mLoadingStateView.setRetryListener(new LoadingStateView.RetryListener() {
            @Override
            public void onRetryButtonClicked() {
                getDataFromServer();
            }
        });

        mImageSliderAdapter = new ProductImageSliderAdapter(SMALL_IMAGE_VIEW_TYPE);
        mImageSliderAdapter.setListener(new ProductImageSliderAdapter.ProductSliderAdapterListener() {
            @Override
            public void onProductImageClicked(String url) {
                openFragment(ProductImageSliderFragment.newInstance(getViewModel().getProductDetail().getValue()));
            }
        });

//        mImageZoom.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                openFragment(ProductImageSliderFragment.newInstance(getViewModel().getProductDetail().getValue()));
//            }
//        });

        mProductImageSlider.setAdapter(mImageSliderAdapter);

        setupToolbar();

        observeLoading();
        observeProductDetail();

        if (isNetworkConnected()) {
            getDataFromServer();
        } else {
            onViewStateChanged(STATE_NO_INTERNET);
        }

        mPriceSpan = new MetricAffectingSpan() {

            @Override
            public void updateDrawState(TextPaint textPaint) {
                textPaint.setTextSize(DI.getABResources().getDimen(R.dimen.product_detail_price_rial_text));
                textPaint.setTypeface(ResourcesCompat.getFont(getContext(), R.font.iran_sans_regular));
            }

            @Override
            public void updateMeasureState(TextPaint textPaint) {
                textPaint.setTextSize(DI.getABResources().getDimen(R.dimen.product_detail_price_rial_text));
                textPaint.setTypeface(ResourcesCompat.getFont(getContext(), R.font.iran_sans_regular));
            }
        };

        mAddToBasketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateCount(1);
            }
        });

    }

    private void updateCount(int count) {
        if (!mShopCartViewModel.hasAnythingToChange()) {

            mShopCartViewModel.getIndependentLoading().observeForever(mAddToBasketLoadingListener);

            mShopCartViewModel.updateProductDirect(getViewModel().getProductDetail().getValue().productDetailModel.getProduct().getId().intValue(),
                    count);

            mCounterView.setCount(count, false);
            setButtonVisibility(count);
        } else {
            Toast.makeText(getContext(), DI.getABResources().getString(R.string.shop_cart_product_list_is_updating_error_message), Toast.LENGTH_LONG).show();
        }
    }

    private void observeLoading() {
        getViewModel().isLoading().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                if (isLoading) {
                    onViewStateChanged(STATE_LOADING);

                    mLoadingStateView.setStateAndDescription(LoadingStateView.STATE_LOADING, DI.getABResources().getString(R.string.loading_message), true);
                } else {
                    mLoadingStateView.setVisibility(View.GONE);
                }
            }
        });
    }

    private void observeProductDetail() {
        getViewModel().getProductDetail().observe(getViewLifecycleOwner(), new Observer<ProductDetailResponse>() {
            @Override
            public void onChanged(ProductDetailResponse productDetailResponse) {
                if (productDetailResponse.errorMessage != null) {
                    onViewStateChanged(STATE_ERROR);
                    if (productDetailResponse.errorCode == -2) {
                        mLoadingStateView.setStateAndDescription(LoadingStateView.STATE_ERROR, getString(R.string.productdetail_notfound_error), true);
                        mLoadingStateView.setButtonText(getString(R.string.survey_end_page_button_text));
                        mLoadingStateView.setRetryListener(new LoadingStateView.RetryListener() {
                            @Override
                            public void onRetryButtonClicked() {
                                onBackPressed();
                            }
                        });
                    } else {
                        mLoadingStateView.setStateAndDescription(LoadingStateView.STATE_ERROR, productDetailResponse.errorMessage, true);
                        mLoadingStateView.setRetryListener(new LoadingStateView.RetryListener() {
                            @Override
                            public void onRetryButtonClicked() {
                                getDataFromServer();
                            }
                        });
                    }

                    return;
                }

                onViewStateChanged(STATE_SHOW_CONTENT);
                onDataReady(productDetailResponse.productDetailModel);
            }
        });
    }

    private void onDataReady(ProductDetailModel productDetailModel) {
        if (productDetailModel.getProduct() != null) {
            final ProductDetail productDetail = productDetailModel.getProduct();

            if (productDetail.getImages() != null) {
                mImageSliderAdapter.setUrls(productDetail.getImages());
            } else {
                //todo: show no picture
            }

            mTextProductName.setLineSpacing(0, 1.5f);
            mTextProductName.setText(productDetail.getName());
            if (Math.min(productDetail.getMaxCount(), productDetail.getStock().intValue()) == 0) {

//                mTextProductPrice.setVisibility(View.GONE);
//                setDiscountText(null);
//                setOffPercent(0);
                mTextCountErorr.setVisibility(View.VISIBLE);
                mAddToBasketContainer.setVisibility(View.GONE);
                mTextDiscount.setTextColor(DI.getABResources().getColor(R.color.product_detail_disable_product_price_color));
            }

            setAmountText(productDetail.getPrice());

            if (productDetail.getHaveDiscount()) {
                setOffPercent(productDetail.getDiscount());
                setDiscountText(productDetail.getOldPrice());
            } else {
                setOffPercent(0);
                setDiscountText(null);
            }


//            if (!productDetail.getSellerName().isEmpty()) {
//                mTextSeller.setText(productDetail.getSellerName());
//            } else {
//                mProductGeneralInfoContainer.setVisibility(View.GONE);
//            }

            int addedProductToBasketCount = 0;//TOdo: check basket items and find count for this item.
            setupCounterView(Math.min(productDetail.getMaxCount(), productDetail.getStock().intValue()), mShopCartViewModel.getProductCount(productDetail.getId().intValue()));

            setTechnicalInfo(productDetail.getAttributes());

            String description = productDetail.getDescription();
            setDescription(description);

            mUserInfoViewModel.getUserInfoLiveData().observe(getViewLifecycleOwner(), new Observer<UserInfo>() {
                @Override
                public void onChanged(UserInfo userInfo) {
                    if (userInfo == null) {
                        return;
                    }
                    if (productDetail.getPoint() > 0) {

                        mStarPoint.setVisibility(View.VISIBLE);
                        mPoint.setText(Utils.toPersianPriceNumber(productDetail.getPoint()));
                        mPoint.setVisibility(View.VISIBLE);

                        if (userInfo.getLoyaltyScore() < productDetail.getPoint()) {
                            mAddToBasketButton.setEnable(false);
                            mAddToBasketButton.setClickable(false);
                            mStarPoint.setImageDrawable(DI.getABResources().getDrawable(R.drawable.star_point_icon_disable));
                        }
                    }
                }
            });

        } else {
            onViewStateChanged(STATE_ERROR);
            mLoadingStateView.setStateAndDescription(LoadingStateView.STATE_ERROR, productDetailModel.getDescription(), false);
        }
    }

    private void setupToolbar() {
        IABResources abResources = DI.getABResources();

        mToolbar.setBackgroundColor(abResources.getColor(R.color.toolbar_background));
        mToolbar.setTitle(abResources.getString(R.string.productdetail_title_fragment));
        mToolbar.setShowTitle(true);
        mToolbar.setShowShop(true);
        mToolbar.setShowNavigationUp(true);
        mToolbar.setTitleTextColor(abResources.getColor(R.color.white));
        mToolbar.setTitleTextSize(abResources.getDimen(R.dimen.title_toolbar_size));
        mToolbar.setShowDeposit(abResources.getBoolean(R.bool.sale_report_toolbar_has_deposit));
        mToolbar.setOnBackClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mToolbar.setOnNavigationDrawerClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                toggleDrawer();
            }

        });


        mTextDescription.setTypeface(ResourcesCompat.getFont(getContext(), R.font.iran_yekan));
        mTextDescription.setTextSize(TypedValue.COMPLEX_UNIT_PX, abResources.getDimen(R.dimen.landing_service_list_title_text_size));

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
    }

    private void setDiscountText(Long discountAmount) {
        if (discountAmount == null) {
            mTextDiscount.setVisibility(View.GONE);

            return;
        }

        mTextDiscount.setVisibility(View.VISIBLE);
        mTextDiscount.setText(Utils.toPersianPriceNumber(discountAmount));
    }

    private void setAmountText(Long amount) {
        String amountText = Utils.toPersianPriceNumber(amount != null ? amount : 0L);
        String rialText = getContext().getResources().getString(R.string.rial);

        SpannableStringBuilder ssb = new SpannableStringBuilder(amountText + " " + rialText);

        ssb.setSpan(mPriceSpan, amountText.length(), ssb.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        mTextProductPrice.setText(ssb);
    }

    private void setupCounterView(int maxCount, int count) {

        setButtonVisibility(count);

        mCounterView.setMaxCount(maxCount);
        mCounterView.setCount(count, false);
    }

    private void setButtonVisibility(int count) {
        if (count > 0) {
            mCounterView.setVisibility(View.VISIBLE);
            mAddToBasketButton.setVisibility(View.GONE);
        } else {
            mCounterView.setVisibility(View.GONE);
            mAddToBasketButton.setVisibility(View.VISIBLE);
        }
    }

    private void setOffPercent(int offPercent) {
        if (offPercent == 0) {
            mOffPercentView.setVisibility(View.GONE);

            return;
        }


        mOffPercentView.setVisibility(View.VISIBLE);

        mOffPercentView.setText(String.format(DI.getABResources().getString(R.string.percent), Utils.toPersianNumber(offPercent)));
    }

    private void setTechnicalInfo(List<ProductAttribute> attributes) {
        List<ProductTechnicalInfoItemView.ProductTechnicalItem> data = new ArrayList<>();

        for (ProductAttribute attribute : attributes) {
            String title = attribute.getTitle();

            String value = "";
            for (int i = 0; i < attribute.getItems().size(); ++i) {
                value += attribute.getItems().get(i).getDescription();
                if (i != attribute.getItems().size() - 1) {
                    value += "\n";
                }
            }

            data.add(new ProductTechnicalInfoItemView.ProductTechnicalItem(title, value));
        }

        mTechnicalInfoView.setData(data);

        mTechnicalInfoViewContainer.setVisibility(data.size() == 0 ? View.GONE : View.VISIBLE);
        mTitleViewTechnicalInfo.setVisibility(data.size() == 0 ? View.GONE : View.VISIBLE);
    }

    private void setDescription(String description) {
        if (!TextUtils.isEmpty(description)) {
            String s;
            mContainerDescription.setVisibility(View.VISIBLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                s = (Html.fromHtml(description, Html.FROM_HTML_MODE_COMPACT)).toString();
            } else {
                s = (Html.fromHtml(description).toString());
            }

            mTextDescription.setText(s, true);

        } else {
            mContainerDescription.setVisibility(View.GONE);
            mTextDescription.setText("");
        }
    }

    @Override
    protected void getDataFromServer() {
        if (mProductInfo != null) {
            getViewModel().getProductDetail(mProductInfo.getId());
        } else if (mProductID != 0) {
            getViewModel().getProductDetail(mProductID);
        }
    }

    private void onViewStateChanged(int state) {
        switch (state) {
            case STATE_NO_INTERNET:
                mNoInternetView.setVisibility(View.VISIBLE);

                mLoadingStateView.setVisibility(View.GONE);
                mContainerContent.setVisibility(View.GONE);

                break;

            case STATE_LOADING:
                mLoadingStateView.setVisibility(View.VISIBLE);

                mNoInternetView.setVisibility(View.GONE);
                mContainerContent.setVisibility(View.GONE);

                break;

            case STATE_ERROR:
                mLoadingStateView.setVisibility(View.VISIBLE);

                mNoInternetView.setVisibility(View.GONE);
                mContainerContent.setVisibility(View.GONE);

                break;

            case STATE_SHOW_CONTENT:
                mContainerContent.setVisibility(View.VISIBLE);

                mLoadingStateView.setVisibility(View.GONE);
                mNoInternetView.setVisibility(View.GONE);

                break;
        }
    }

}
