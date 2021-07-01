package com.srp.ewayspanel.repository.remote.storetransaction;

import com.srp.ewayspanel.model.transaction.order.OrderDetailResponse;
import com.srp.ewayspanel.model.transaction.order.OrderTransactionListResponse;
import com.srp.ewayspanel.model.transaction.order.UserOrdersSummaryResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Eskafi on 2/2/2020.
 */
public interface OrderTransactionApiRetrofit {

    @GET("service/v{version}/store/followorder/{pageIndex}/{pageSize}")
    Call<OrderTransactionListResponse> getStoreTransactionList(@Path("version") int version, @Path("pageIndex") int pageIndex, @Path("pageSize") int pageSize);

    @GET("service/v{version}/store/getuserordersummary/{orderId}")
    Call<UserOrdersSummaryResponse> getUserOrdersSummary(@Path("version") int version, @Path("orderId") long orderId);

    @GET("service/v{version}/store/getuserorderdetails/{orderId}")
    Call<OrderDetailResponse> getOrderDetails(@Path("version") int version, @Path("orderId") long orderId);
}
