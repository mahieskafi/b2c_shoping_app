package com.srp.ewayspanel.repository.storetransaction;

import com.srp.eways.network.CallBackWrapper;
import com.srp.ewayspanel.model.transaction.order.OrderDetailResponse;
import com.srp.ewayspanel.model.transaction.order.OrderTransactionListResponse;
import com.srp.ewayspanel.model.transaction.order.UserOrdersSummaryResponse;

/**
 * Created by Eskafi on 2/2/2020.
 */
public interface OrderTransactionRepository {

    void getOrderTransactionList(CallBackWrapper<OrderTransactionListResponse> callBack);

    Boolean hasMore();

    int getPageIndex();

    void clear();

    void getUserOrdersSummary(long orderId, CallBackWrapper<UserOrdersSummaryResponse> callBack);

    void getOrderDetail(long orderId, CallBackWrapper<OrderDetailResponse> callBack);
}
