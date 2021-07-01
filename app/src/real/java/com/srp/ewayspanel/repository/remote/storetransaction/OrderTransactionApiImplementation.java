package com.srp.ewayspanel.repository.remote.storetransaction;

import com.srp.eways.network.CallBackWrapper;
import com.srp.ewayspanel.AppConfig;
import com.srp.ewayspanel.di.DI;
import com.srp.ewayspanel.model.transaction.order.OrderDetailResponse;
import com.srp.ewayspanel.model.transaction.order.OrderTransactionListResponse;
import com.srp.ewayspanel.model.transaction.order.UserOrdersSummaryResponse;
import com.srp.eways.repository.remote.DefaultRetroCallback;

/**
 * Created by Eskafi on 2/2/2020.
 */
public class OrderTransactionApiImplementation implements OrderTransactionApiService {

    private static OrderTransactionApiImplementation INSTANCE;

    public static OrderTransactionApiImplementation getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new OrderTransactionApiImplementation();
        }
        return INSTANCE;
    }

    private OrderTransactionApiRetrofit mStoreTransactionRetro;

    public OrderTransactionApiImplementation() {
        mStoreTransactionRetro = DI.provideApi(OrderTransactionApiRetrofit.class);
    }

    @Override
    public void getOrderTransactionList(int pageIndex, int pageSize, CallBackWrapper<OrderTransactionListResponse> callBack) {
        mStoreTransactionRetro.getStoreTransactionList(AppConfig.SERVER_VERSION, pageIndex, pageSize).enqueue(new DefaultRetroCallback<OrderTransactionListResponse>(callBack) {
            @Override
            protected void checkResponseForError(OrderTransactionListResponse response, ErrorInfo errorInfo) {
                if (response.getStatus() != 0) {
                    errorInfo.errorCode = response.getStatus();
                    errorInfo.errorMessage = response.getDescription();
                }
            }
        });
    }

    @Override
    public void getUserOrdersSummary(long orderId, CallBackWrapper<UserOrdersSummaryResponse> callBack) {
        mStoreTransactionRetro.getUserOrdersSummary(AppConfig.SERVER_VERSION, orderId).enqueue(new DefaultRetroCallback<UserOrdersSummaryResponse>(callBack) );
    }

    @Override
    public void getOrderDetails(long orderId, CallBackWrapper<OrderDetailResponse> callBack) {
        mStoreTransactionRetro.getOrderDetails(AppConfig.SERVER_VERSION, orderId).enqueue(new DefaultRetroCallback<OrderDetailResponse>(callBack) );

    }
}
