package com.srp.ewayspanel.repository.storetransaction;

import com.srp.eways.network.CallBackWrapper;
import com.srp.ewayspanel.di.DI;
import com.srp.ewayspanel.model.transaction.order.OrderDetailResponse;
import com.srp.ewayspanel.model.transaction.order.OrderTransactionListResponse;
import com.srp.ewayspanel.model.transaction.order.UserOrdersSummaryResponse;
import com.srp.ewayspanel.repository.remote.storetransaction.OrderTransactionApiService;

/**
 * Created by Eskafi on 2/2/2020.
 */
public class OrderTransactionRepositoryImplementation implements OrderTransactionRepository {

    private static final int PAGE_SIZE = 10;

    private static OrderTransactionRepositoryImplementation INSTANCE;

    private OrderTransactionApiService mOrderTransactionApiService;

    private int mPageIndex = 0;
    private boolean mHasMore = true;

    public static OrderTransactionRepositoryImplementation getInstance() {

        if (INSTANCE == null) {

            INSTANCE = new OrderTransactionRepositoryImplementation();
        }

        return INSTANCE;
    }

    public OrderTransactionRepositoryImplementation() {

        mOrderTransactionApiService = DI.getStoreTransactionListApi();
    }

    @Override
    public void getOrderTransactionList(final CallBackWrapper<OrderTransactionListResponse> callBack) {

        mOrderTransactionApiService.getOrderTransactionList(mPageIndex, PAGE_SIZE, new CallBackWrapper<OrderTransactionListResponse>() {
            @Override
            public void onError(int errorCode, String errorMessage) {

                callBack.onError(errorCode, errorMessage);
            }

            @Override
            public void onSuccess(OrderTransactionListResponse responseBody) {
                int nextPageIndex = responseBody.getPageIndex() + 1;

                if (mPageIndex != nextPageIndex) {
                    mPageIndex = nextPageIndex;
                    if (responseBody.getOrderItems() != null) {
                        mHasMore = responseBody.getOrderItems().size() >= PAGE_SIZE;
                    }

                    callBack.onSuccess(responseBody);
                }
            }
        });
    }

    @Override
    public Boolean hasMore() {
        return mHasMore;
    }

    @Override
    public int getPageIndex() {
        return mPageIndex;
    }

    @Override
    public void clear() {
        mPageIndex = 0;

        mHasMore = false;
    }

    @Override
    public void getUserOrdersSummary(long orderId, final CallBackWrapper<UserOrdersSummaryResponse> callBack) {
        mOrderTransactionApiService.getUserOrdersSummary(orderId, new CallBackWrapper<UserOrdersSummaryResponse>() {
            @Override
            public void onError(int errorCode, String errorMessage) {
                callBack.onError(errorCode, errorMessage);
            }

            @Override
            public void onSuccess(UserOrdersSummaryResponse responseBody) {
                callBack.onSuccess(responseBody);
            }
        });
    }

    @Override
    public void getOrderDetail(long orderId, final CallBackWrapper<OrderDetailResponse> callBack) {
        mOrderTransactionApiService.getOrderDetails(orderId, new CallBackWrapper<OrderDetailResponse>() {
            @Override
            public void onError(int errorCode, String errorMessage) {
                callBack.onError(errorCode, errorMessage);
            }

            @Override
            public void onSuccess(OrderDetailResponse responseBody) {
                callBack.onSuccess(responseBody);
            }
        });
    }
}
