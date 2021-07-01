package com.srp.ewayspanel.repository.remote.storetransaction;

import com.srp.eways.network.CallBackWrapper;
import com.srp.ewayspanel.model.transaction.order.OrderDetailResponse;
import com.srp.ewayspanel.model.transaction.order.OrderTransactionListResponse;
import com.srp.ewayspanel.model.transaction.order.UserOrdersSummaryResponse;

/**
 * Created by Eskafi on 2/3/2020.
 */
public interface OrderTransactionApiService {

    void getOrderTransactionList(int pageIndex, int pageSize, CallBackWrapper<OrderTransactionListResponse> callBack);

    void getUserOrdersSummary(int orderId, CallBackWrapper<UserOrdersSummaryResponse> callBack);

    void getOrderDetails(int orderId, CallBackWrapper<OrderDetailResponse> callBack);
}
