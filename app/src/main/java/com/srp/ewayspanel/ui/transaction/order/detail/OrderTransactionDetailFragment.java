package com.srp.ewayspanel.ui.transaction.order.detail;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.srp.eways.ui.navigation.NavigationMemberFragment;
import com.srp.eways.ui.view.toolbar.WeiredToolbar;
import com.srp.eways.util.JalaliCalendar;
import com.srp.ewayspanel.R;
import com.srp.ewayspanel.di.DI;
import com.srp.ewayspanel.model.followorder.FollowOrderItem;
import com.srp.ewayspanel.model.transaction.order.OrderDetail;
import com.srp.ewayspanel.model.transaction.order.OrderDetailResponse;
import com.srp.ewayspanel.model.transaction.order.OrderItem;
import com.srp.ewayspanel.model.transaction.order.OrderSummaryResult;
import com.srp.ewayspanel.model.transaction.order.UserOrdersSummaryResponse;
import com.srp.eways.base.BaseRecyclerAdapter2;
import com.srp.ewayspanel.ui.shopcart.ShopCartViewModel;
import com.srp.ewayspanel.ui.store.product.detail.ProductDetailFragment;
import com.srp.ewayspanel.ui.transaction.order.list.OrderStatus;
import com.srp.ewayspanel.ui.transaction.order.OrderTransactionViewModel;
import com.srp.eways.ui.view.LoadingStateView;
import com.srp.eways.ui.view.button.LeftIconButtonView;
import com.srp.eways.ui.view.receipt.ReceiptItem;
import com.srp.eways.ui.view.receipt.ReceiptListView;
import com.srp.eways.util.Utils;

import java.util.ArrayList;
import java.util.List;

import ir.abmyapp.androidsdk.IABResources;

import static com.srp.eways.network.NetworkResponseCodes.SUCCESS;


/**
 * Created by Eskafi on 2/4/2020.
 */
public class OrderTransactionDetailFragment extends NavigationMemberFragment<OrderTransactionViewModel> {

    public static final String ORDER_TRANSACTION_DETAIL_HAS_TOOLBAR = "order_transaction_detail_has_toolbar";

    private AppCompatTextView mDetailTitleTextView;
    private CardView mOrderDetailCard;
    private ReceiptListView mOrderDetailList;
    private CardView mOrderFactorCard;
    private ReceiptListView mOrderFactorList;
    private CardView mDeliveryInfoCard;

    private WeiredToolbar mToolbar;

    private LeftIconButtonView mLoadMoreButton;

    private LoadingStateView mDetailLoading;
    private LoadingStateView mProductsLoading;

    private RecyclerView mProductList;
    private OrderProductAdapter mProductListAdapter;

    private FollowOrderItem mOrderItem;

    private int orderDetailTitleTextColor;
    private int orderDetailTitleTextSize;

    private OrderSummaryResult mOrderSummaryResult = null;

    private OrderTransactionViewModel mViewModel;

    private List<OrderDetail> mOrderDetails = new ArrayList<>();

    private int mVisibleItemPosition = 3;
    private int mStepItemCount = 3;
    private int endPosition=0;

    private String mOrederStatus;
    View ChildView;
    int RecyclerViewItemPosition;

    private TextView mSendDelivery;
    private TextView mSendType;
    private TextView mSendsddress;
    private TextView mPostCode;

    private TextView mTotalPrice;
    private TextView mBox;
    private TextView mPoint;
    private TextView mStatus;
    private TextView mCode;
    private TextView mResultText;
    private ImageView mResultIcon;
    private TextView mDate;
    private TextView mTime;
    private TextView mDiscount;

    private TextView mFinalPriceTitle;
    private TextView mFinalPriceValue;

    public static OrderTransactionDetailFragment newInstance() {
        return new OrderTransactionDetailFragment();
    }

    public static OrderTransactionDetailFragment newInstance(boolean hasToolbar) {
        Bundle args = new Bundle();
        args.putBoolean(ORDER_TRANSACTION_DETAIL_HAS_TOOLBAR, hasToolbar);

        OrderTransactionDetailFragment fragment = new OrderTransactionDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mOrderItem = mViewModel.getOrderItem();

        final IABResources abResources = DI.getABResources();

        orderDetailTitleTextSize = abResources.getDimenPixelSize(R.dimen.order_transaction_detail_title_text_size);
        orderDetailTitleTextColor = abResources.getColor(R.color.order_transaction_detail_title_text_color);

        mDetailTitleTextView = view.findViewById(R.id.order_detail_text);
        mOrderDetailCard = view.findViewById(R.id.order_detail_card);
        final AppCompatTextView productsTitleTextView = view.findViewById(R.id.product_list_title);

        mToolbar = view.findViewById(R.id.toolbar);
        if (getArguments() != null && getArguments().getBoolean(ORDER_TRANSACTION_DETAIL_HAS_TOOLBAR)) {
            setupToolbar();
        }


        mSendDelivery = view.findViewById(R.id.oreder_send_delivery);
        mSendType = view.findViewById(R.id.order_send_type);
        mSendsddress = view.findViewById(R.id.order_send_address);
        mPostCode = view.findViewById(R.id.order_post_code);

        mTotalPrice = view.findViewById(R.id.oreder_detail_totalprice);
        mBox = view.findViewById(R.id.order_detail_box);
        mPoint = view.findViewById(R.id.order_detail_point);
        mStatus = view.findViewById(R.id.order_detail_status);
        mCode = view.findViewById(R.id.order_detail_code);
        mResultText = view.findViewById(R.id.order_detail_result_title);
        mResultIcon = view.findViewById(R.id.iv_result);
        mDate = view.findViewById(R.id.order_detail_date);
        mTime = view.findViewById(R.id.order_detail_time);
        mDiscount = view.findViewById(R.id.order_detail_discount);

        mFinalPriceTitle = view.findViewById(R.id.order_detail_final_price_title);
        mFinalPriceValue = view.findViewById(R.id.order_detail_final_price);

        mOrderFactorCard = view.findViewById(R.id.order_factor_card);
        mDeliveryInfoCard = view.findViewById(R.id.delivery_info_card);

        mLoadMoreButton = view.findViewById(R.id.loadmore_button);

        mDetailLoading = view.findViewById(R.id.loadingstateview);
        mDetailLoading.setStateAndDescription(LoadingStateView.STATE_LOADING,
                abResources.getString(R.string.loading_message), true);
        mDetailLoading.setRetryListener(new LoadingStateView.RetryListener() {
            @Override
            public void onRetryButtonClicked() {
                mDetailLoading.setStateAndDescription(LoadingStateView.STATE_LOADING,
                        abResources.getString(R.string.loading_message), true);

                mViewModel.getOrdersSummary();
            }
        });


        mProductsLoading = view.findViewById(R.id.products_loadingstateview);
        mProductsLoading.setViewOrientation(LinearLayout.HORIZONTAL);
        mProductsLoading.setStateAndDescription(LoadingStateView.STATE_LOADING,
                abResources.getString(R.string.loading_message), true);
        mProductsLoading.setRetryListener(new LoadingStateView.RetryListener() {
            @Override
            public void onRetryButtonClicked() {
                mViewModel.getOrdersDetails();
            }
        });
        mProductsLoading.setVisibility(View.GONE);

        mProductList = view.findViewById(R.id.product_list);
        mProductList.setLayoutManager(new LinearLayoutManager(view.getContext()));
        mProductListAdapter = new OrderProductAdapter(view.getContext(), new BaseRecyclerAdapter2.RetryClickListener() {
            @Override
            public void onClicked() {

            }
        }, new OrderProductAdapter.onOrderProductClickListener() {
            @Override
            public void onItemClick(int id) {
                openFragment(ProductDetailFragment.newInstance(mOrderDetails.get(id).getProductId()),NAVIGATION_VIEW_TYPE_HAS_TOOLBAR);
            }
        });
        mProductList.setAdapter(mProductListAdapter);

        mOrderFactorCard.setVisibility(View.GONE);
        mDeliveryInfoCard.setVisibility(View.GONE);

        mDetailTitleTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, orderDetailTitleTextSize);
        mDetailTitleTextView.setTextColor(orderDetailTitleTextColor);
        mDetailTitleTextView.setTypeface(ResourcesCompat.getFont(view.getContext(), R.font.iran_yekan_medium));
        mDetailTitleTextView.setText(abResources.getString(R.string.order_transaction_detail_title));
        mDetailTitleTextView.setVisibility(View.GONE);


        productsTitleTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, orderDetailTitleTextSize);
        productsTitleTextView.setTextColor(orderDetailTitleTextColor);
        productsTitleTextView.setTypeface(ResourcesCompat.getFont(view.getContext(), R.font.iran_yekan_medium));
        productsTitleTextView.setText(abResources.getString(R.string.order_transaction_detail_product_list_title));
        productsTitleTextView.setVisibility(View.GONE);

        if (!isNetworkConnected()) {
            mDetailLoading.setStateAndDescription(LoadingStateView.STATE_ERROR, abResources.getString(R.string.loadingstateview_text_network_unavailable), true);
        } else {
            mViewModel.getOrdersSummary();
        }


        mViewModel.getOrderSummaryLiveData().observe(getViewLifecycleOwner(), new Observer<UserOrdersSummaryResponse>() {
            @Override
            public void onChanged(UserOrdersSummaryResponse userOrdersSummaryResponse) {
                if (userOrdersSummaryResponse != null && userOrdersSummaryResponse.getOrderSummaryResult() != null) {
                    if (userOrdersSummaryResponse.getStatus() == SUCCESS) {
                        mOrderSummaryResult = userOrdersSummaryResponse.getOrderSummaryResult();

                        mDetailLoading.setVisibility(View.GONE);

                        mOrederStatus = "نامشخص";
                        if (mOrderSummaryResult.getOrderStatus() != null &&
                                OrderStatus.Companion.getOrderStatus(mOrderSummaryResult.getOrderStatus()) == OrderStatus.SUCCESS) {

                            mOrederStatus = mOrderSummaryResult.getDeliveryStatusDescription();
                        }

                        if (mOrderSummaryResult != null) {
                            setupOrderDetail(new OrderItem(mOrderSummaryResult.getOrderId(),
                                    mOrderSummaryResult.getUserId(),
                                    mOrderSummaryResult.getTotalPrice(),
                                    mOrderSummaryResult.getOrderStatus(),
                                    mOrderSummaryResult.getOrderStatusDescription(),
                                    mOrderSummaryResult.getOrderDate(),
                                    mOrderSummaryResult.getPayment()), view);

                            setupOrderFactor(view);
                        }

                        mProductsLoading.setVisibility(View.VISIBLE);

                        mViewModel.getOrdersDetails();


                    } else {
                        mDetailLoading.setVisibility(View.VISIBLE);
                        mDetailLoading.setStateAndDescription(LoadingStateView.STATE_ERROR, userOrdersSummaryResponse.getDescription(), true);
                    }
                }
            }
        });

        mViewModel.getOrderDetailLiveData().observe(getViewLifecycleOwner(), new Observer<OrderDetailResponse>() {
            @Override
            public void onChanged(OrderDetailResponse orderDetailResponse) {
                if (orderDetailResponse != null && orderDetailResponse.getOrderDetails() != null) {

                    if (orderDetailResponse.getStatus() == SUCCESS) {
                        mProductsLoading.setVisibility(View.GONE);

                        productsTitleTextView.setVisibility(View.VISIBLE);

                        mOrderDetails = orderDetailResponse.getOrderDetails();
                        if (mOrderDetails.size() != 0) {
                            if (mOrderDetails.size() <= 5) {
                                mProductListAdapter.setNewData(mOrderDetails);
                            } else {
                                mProductListAdapter.setNewData(mOrderDetails.subList(0, mVisibleItemPosition));
                                mLoadMoreButton.setVisibility(View.VISIBLE);
                            }
                        }

                    } else {
                        mProductsLoading.setVisibility(View.VISIBLE);
                        mProductsLoading.setStateAndDescription(LoadingStateView.STATE_ERROR, orderDetailResponse.getDescription(), true);

                    }
                }
            }
        });

        setupLoadMoreButton();
    }

    private void setupOrderDetail(OrderItem orderItem, View view) {
        IABResources abResources = DI.getABResources();

        mDetailTitleTextView.setVisibility(View.VISIBLE);


//        if (!orderItem.getOrderStatusName().isEmpty()) {
//            mStatus.setTextColor(ContextCompat.getColor(getContext(), OrderStatus.Companion.getResultColor(orderItem.getOrderStatus())));
//        }

        mTotalPrice.setText(Utils.toPersianPriceNumber(mOrderSummaryResult.getTotalPrice()) + " ریال");
        mBox.setText(Utils.toPersianPriceNumber(mOrderSummaryResult.getPostPrice()) + " ریال");
        mPoint.setText(Utils.toPersianNumber("" + mOrderSummaryResult.getTotalPoint()));
        mStatus.setText("" + mOrederStatus);
        mCode.setText(Utils.toPersianNumber("" + orderItem.getOrderId()));
        mResultText.setText(mOrderSummaryResult.getOrderStatusDescription());
        mResultIcon.setImageDrawable(abResources.getDrawable(OrderStatus.Companion.getResultDrawable(mOrderSummaryResult.getOrderStatus())));
        mDiscount.setText(Utils.toPersianPriceNumber(mOrderSummaryResult.getDiscount()) + " " + getString(R.string.rial));

        if (OrderStatus.SUCCESS == OrderStatus.Companion.getOrderStatus(mOrderSummaryResult.getOrderStatus())) {
            mFinalPriceTitle.setText(abResources.getString(R.string.order_transaction_detail_delivery_final_price_title_success));
        } else {
            mFinalPriceTitle.setText(abResources.getString(R.string.order_transaction_detail_delivery_final_price_title_failed));
        }
        mFinalPriceValue.setText(Utils.toPersianPriceNumber(mOrderSummaryResult.getPayment()) + " " + getString(R.string.rial));

        String dateTime = mOrderSummaryResult.getOrderDate();
        if (dateTime != null && !dateTime.isEmpty()) {

            String[] separatedDateTime = dateTime.split(" ");
            mTime.setText(Utils.toPersianNumber(separatedDateTime[1].substring(0, 5)));

            mDate.setText(Utils.toPersianNumber(separatedDateTime[0]));
        }

        mOrderDetailCard.setCardElevation(abResources.getDimenPixelSize(R.dimen.order_transaction_detail_card_elevation));
        mOrderDetailCard.setRadius(abResources.getDimenPixelSize(R.dimen.order_transaction_detail_card_radius));

        mOrderDetailCard.setVisibility(View.VISIBLE);

    }

    private void setupOrderFactor(View view) {
        IABResources abResources = DI.getABResources();

        AppCompatTextView factorTitleTextView = view.findViewById(R.id.factor_title);

        factorTitleTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, orderDetailTitleTextSize);
        factorTitleTextView.setTextColor(orderDetailTitleTextColor);
        factorTitleTextView.setTypeface(ResourcesCompat.getFont(view.getContext(), R.font.iran_yekan_medium));
        factorTitleTextView.setText(abResources.getString(R.string.order_transaction_detail_factor_title));
        factorTitleTextView.setVisibility(View.VISIBLE);

        mSendDelivery.setText(mOrderSummaryResult.getFullName());
        mSendType.setText(mOrderSummaryResult.getPostMethodName());
        mSendsddress.setText(mOrderSummaryResult.getDeliveryAddress());
        mPostCode.setText(Utils.toPersianNumber(mOrderSummaryResult.getPostCode()));

        mOrderFactorCard.setCardElevation(abResources.getDimenPixelSize(R.dimen.order_transaction_detail_card_elevation));
        mOrderFactorCard.setRadius(abResources.getDimenPixelSize(R.dimen.order_transaction_detail_card_radius));
        mOrderFactorCard.setVisibility(View.VISIBLE);

    }


    private void setupLoadMoreButton() {
        IABResources abResources = DI.getABResources();

        mLoadMoreButton.setTextSize(abResources.getDimenPixelSize(R.dimen.order_transaction_detail_more_items_button_text_size));
        mLoadMoreButton.setTextColor(abResources.getColor(R.color.order_transaction_detail_more_items_button_text_color));
        mLoadMoreButton.setEnabledBackground(abResources.getDrawable(R.drawable.login_button_background_enabled));
        mLoadMoreButton.setText(abResources.getString(R.string.order_transaction_detail_more_items_button_text));
        mLoadMoreButton.setIconNextLevel(abResources.getDrawable(R.drawable.ic_loading_white));

        final Animation loadingAnimate = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_infinite);

        mLoadMoreButton.setLoadingVisibility(View.GONE);

        Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.iran_yekan_light);
        if (typeface != null) {
            mLoadMoreButton.setTextTypeFace(typeface);
        }
        mLoadMoreButton.setEnable(true);
        mLoadMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoadMoreButton.getIconImageView().clearAnimation();
                mLoadMoreButton.getIconImageView().startAnimation(loadingAnimate);

                endPosition = mStepItemCount + mVisibleItemPosition;
                if (endPosition < mOrderDetails.size()) {
                    mProductListAdapter.appendData(mOrderDetails.subList(mVisibleItemPosition, endPosition));
                    mStepItemCount = endPosition;
                } else {
                    if (mOrderDetails.subList(mVisibleItemPosition, mOrderDetails.size()).size() == 0) {
                        List<OrderDetail> orderDetailList = new ArrayList<>();
                        orderDetailList.add(mOrderDetails.get(mOrderDetails.size() - 1));

                        mProductListAdapter.appendData(orderDetailList);
                    } else {
                        mProductListAdapter.appendData(mOrderDetails.subList(endPosition-3, mOrderDetails.size()));
                    }
                    mLoadMoreButton.setVisibility(View.GONE);
                }
                mLoadMoreButton.getIconImageView().clearAnimation();
            }
        });

        mLoadMoreButton.setVisibility(View.GONE);
    }

    private void setupToolbar() {
        IABResources resources = DI.getABResources();

        mToolbar.setTitle(resources.getString(R.string.shop_receipt_result_detail_page_title));
        mToolbar.setShowTitle(true);
        mToolbar.setShowDeposit(false);
        mToolbar.setShowShop(false);
        mToolbar.setBackgroundColor(resources.getColor(R.color.store_page_toolbar_background));
        mToolbar.setShowNavigationDrawer(false);
        mToolbar.setShowNavigationUp(true);
        mToolbar.setVisibility(View.VISIBLE);
        mToolbar.showNavigationDrawer(true);
        mToolbar.setOnBackClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public OrderTransactionViewModel acquireViewModel() {
        return mViewModel = DI.getViewModel(OrderTransactionViewModel.class);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_order_transaction_detail;
    }

    @Override
    public boolean onBackPress() {
        if (getArguments() != null && getArguments().getBoolean(ORDER_TRANSACTION_DETAIL_HAS_TOOLBAR)) {
            final ShopCartViewModel shopCartViewModel = DI.getViewModel(ShopCartViewModel.class);
            if (shopCartViewModel != null) {
                shopCartViewModel.callGetShopCartList(null);
            }
        }

        return false;
    }
}
