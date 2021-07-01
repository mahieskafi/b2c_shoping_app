package com.srp.ewayspanel.ui.transaction.order;


import androidx.lifecycle.MutableLiveData;

import com.srp.eways.base.BaseViewModel;
import com.srp.eways.network.BaseCallBackWrapper;
import com.srp.ewayspanel.di.DI;
import com.srp.ewayspanel.model.followorder.FollowOrderItem;
import com.srp.ewayspanel.model.transaction.order.OrderDetailResponse;
import com.srp.ewayspanel.model.transaction.order.OrderItem;
import com.srp.ewayspanel.model.transaction.order.OrderTransactionListResponse;
import com.srp.ewayspanel.model.transaction.order.UserOrdersSummaryResponse;
import com.srp.ewayspanel.repository.storetransaction.OrderTransactionRepository;

/**
 * Created by Eskafi on 2/2/2020.
 */
public class OrderTransactionViewModel extends BaseViewModel {

    private static OrderTransactionViewModel INSTANCE;

    private OrderTransactionRepository mStoreTransactionRepo;

    private final MutableLiveData<OrderTransactionListResponse> mStoreTransactionLiveData;
    private final MutableLiveData<UserOrdersSummaryResponse> mOrderSummaryLiveData;
    private final MutableLiveData<OrderDetailResponse> mOrderDetailLiveData;

    private final MutableLiveData<Integer> mLoadingLiveData;
    private final MutableLiveData<Boolean> mReNewListLiveData;

    private FollowOrderItem mOrderItem;

    public static OrderTransactionViewModel getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new OrderTransactionViewModel();
        }

        return INSTANCE;
    }

    public OrderTransactionViewModel() {

        mStoreTransactionRepo = DI.getStroreTransactionRepo();
        mStoreTransactionRepo.clear();

        mStoreTransactionLiveData = new MutableLiveData<>();
        mLoadingLiveData = new MutableLiveData<>();
        mOrderSummaryLiveData = new MutableLiveData<>();
        mOrderDetailLiveData = new MutableLiveData<>();
        mReNewListLiveData = new MutableLiveData<>();
    }

    public void getStoreTransactionList() {

        mStoreTransactionRepo.getOrderTransactionList(new BaseCallBackWrapper<OrderTransactionListResponse>(this) {
            @Override
            public void onError(int errorCode, String errorMessage) {
                OrderTransactionListResponse orderTransactionListResponse = new OrderTransactionListResponse(errorCode, errorMessage);

                mStoreTransactionLiveData.setValue(orderTransactionListResponse);
            }

            @Override
            public void onSuccess(OrderTransactionListResponse responseBody) {
                mStoreTransactionLiveData.setValue(responseBody);
            }
        });
    }

    public void getOrdersSummary() {
        mStoreTransactionRepo.getUserOrdersSummary(mOrderItem.getOrderID(), new BaseCallBackWrapper<UserOrdersSummaryResponse>(this) {
            @Override
            public void onError(int errorCode, String errorMessage) {
                UserOrdersSummaryResponse response = new UserOrdersSummaryResponse(errorCode, errorMessage);
                mOrderSummaryLiveData.setValue(response);
            }

            @Override
            public void onSuccess(UserOrdersSummaryResponse responseBody) {
                mOrderSummaryLiveData.setValue(responseBody);
            }
        });
    }

    public void getOrdersDetails() {
        mStoreTransactionRepo.getOrderDetail(mOrderItem.getOrderID(), new BaseCallBackWrapper<OrderDetailResponse>(this) {
            @Override
            public void onError(int errorCode, String errorMessage) {
                OrderDetailResponse response = new OrderDetailResponse(errorCode, errorMessage);
                mOrderDetailLiveData.setValue(response);
            }

            @Override
            public void onSuccess(OrderDetailResponse responseBody) {
                if (responseBody.getOrderDetails() != null && responseBody.getOrderDetails().size() > 0) {
                    mOrderDetailLiveData.setValue(responseBody);
                }
            }
        });
    }

    public boolean hasMore() {
        return mStoreTransactionRepo.hasMore();
    }

    public int getPageIndex() {
        return mStoreTransactionRepo.getPageIndex();
    }

    public MutableLiveData<OrderTransactionListResponse> getStoreTransactionLiveData() {
        return mStoreTransactionLiveData;
    }

    public void consumeOrderSummaryLiveData() {
        mOrderSummaryLiveData.setValue(null);
    }

    public void consumeOrderDetailLiveData() {
        mOrderDetailLiveData.setValue(null);
    }

    public void consumeStoreTransactionLiveData() {
        mOrderSummaryLiveData.setValue(null);
    }

    public void setSelectedOrderTransaction(FollowOrderItem orderItem) {
        mOrderItem = orderItem;
    }

    public MutableLiveData<UserOrdersSummaryResponse> getOrderSummaryLiveData() {
        return mOrderSummaryLiveData;
    }

    public MutableLiveData<OrderDetailResponse> getOrderDetailLiveData() {
        return mOrderDetailLiveData;
    }

    public FollowOrderItem getOrderItem() {
        return mOrderItem;
    }


    public void consumeReNewData(){
        mReNewListLiveData.setValue(null);
    }

    public MutableLiveData<Boolean> getReNewLive(){
        return mReNewListLiveData;
    }

    public void reNewList(){

        mStoreTransactionRepo.clear();

        mReNewListLiveData.setValue(true);
    }
}
