package com.srp.ewayspanel.repository.remote.storetransaction;

import android.os.Handler;

import com.srp.eways.network.CallBackWrapper;
import com.srp.eways.network.NetworkResponseCodes;
import com.srp.eways.repository.remote.BaseApiImplementation;
import com.srp.ewayspanel.di.DI;
import com.srp.ewayspanel.model.transaction.order.OrderDetail;
import com.srp.ewayspanel.model.transaction.order.OrderDetailResponse;
import com.srp.ewayspanel.model.transaction.order.OrderSummaryResult;
import com.srp.ewayspanel.model.transaction.order.OrderTransactionListResponse;
import com.srp.ewayspanel.model.transaction.order.UserOrdersSummaryResponse;

import java.util.ArrayList;

/**
 * Created by ErfanG on 3/11/2020.
 */
public class OrderTransactionApiImplementation extends BaseApiImplementation implements OrderTransactionApiService {


    private static OrderTransactionApiImplementation INSTANCE;

    public static OrderTransactionApiImplementation getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new OrderTransactionApiImplementation();
        }
        return INSTANCE;
    }


    public OrderTransactionApiImplementation() {
    }

    @Override
    public void getOrderTransactionList(int pageIndex, int pageSize, final CallBackWrapper<OrderTransactionListResponse> callBack) {
        setMode(NetworkResponseCodes.SUCCESS);

        if (!handleCall(callBack)) {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    callBack.onSuccess(handleInternalOrderTransactionList());
                }
            }, getResponseDelay());
        }
    }

    private OrderTransactionListResponse handleInternalOrderTransactionList(){

        String string = "{\n" +
                "  \"OrderItems\": [\n" +
                "    {\n" +
                "      \"OrderIds\": null,\n" +
                "      \"OrderId\": 569082,\n" +
                "      \"UserId\": null,\n" +
                "      \"RequestId\": null,\n" +
                "      \"IsFirstOrder\": null,\n" +
                "      \"TotalPrice\": 500,\n" +
                "      \"PostPrice\": 0,\n" +
                "      \"DeliveryDatePost\": null,\n" +
                "      \"Gateway\": -1,\n" +
                "      \"GatewayTitle\": \"پرداخت از سپرده\",\n" +
                "      \"TotalPoint\": 500,\n" +
                "      \"OrderStatus\": 3,\n" +
                "      \"OrderStatus660\": null,\n" +
                "      \"OrderStatus660Text\": \"\",\n" +
                "      \"OrderStatusType\": null,\n" +
                "      \"OrderStatusName\": \"پرداخت شده\",\n" +
                "      \"OrderStatusTitle\": null,\n" +
                "      \"IsAdminCancelable\": false,\n" +
                "      \"PaymentType\": 0,\n" +
                "      \"PaymentTypeName\": null,\n" +
                "      \"BankName\": null,\n" +
                "      \"DeliveryStatus\": 4,\n" +
                "      \"DeliveryStatusText\": null,\n" +
                "      \"DeliveryStatusName\": \"آماده سازی برای ارسال\",\n" +
                "      \"DeliveryStatusDescription\": null,\n" +
                "      \"PostBarcode\": null,\n" +
                "      \"CreateDate\": \"1398/11/08 10:58:52\",\n" +
                "      \"CreateDateOrg\": \"2020-01-28T10:58:52.043\",\n" +
                "      \"PaymentDate\": \"1398/11/08 10:58:52\",\n" +
                "      \"SendDate\": null,\n" +
                "      \"IsDeleted\": null,\n" +
                "      \"DeliveryAddress\": \"تهران -  تهران -  جمهوری 510\",\n" +
                "      \"Vat\": 0,\n" +
                "      \"Payment\": 200,\n" +
                "      \"Tax\": null,\n" +
                "      \"Discount\": null,\n" +
                "      \"UserName\": null,\n" +
                "      \"Firstname\": null,\n" +
                "      \"Name\": null,\n" +
                "      \"FullName\": \" \",\n" +
                "      \"CellPhone\": null,\n" +
                "      \"StateName\": null,\n" +
                "      \"TownName\": null,\n" +
                "      \"PostCode\": null,\n" +
                "      \"Address\": null,\n" +
                "      \"ProductName\": null,\n" +
                "      \"ProductId\": null,\n" +
                "      \"FinancialCode\": null,\n" +
                "      \"ExpertId\": null,\n" +
                "      \"ExpertName\": null,\n" +
                "      \"SaleChannel\": null,\n" +
                "      \"SaleChannelName\": null,\n" +
                "      \"CategoryTitle\": null,\n" +
                "      \"PostMethod\": 8,\n" +
                "      \"PostMethodType\": null,\n" +
                "      \"PostMethodName\": \"تحویل حضوری\",\n" +
                "      \"PostType\": null,\n" +
                "      \"PostTypeName\": null,\n" +
                "      \"IsReversed\": null,\n" +
                "      \"PrintDate\": null,\n" +
                "      \"PrintDateOrg\": null,\n" +
                "      \"SendFromTehranState\": null,\n" +
                "      \"UserPrinterId\": null,\n" +
                "      \"CommentCount\": 0,\n" +
                "      \"ZipCode\": null,\n" +
                "      \"PhoneNumber\": null,\n" +
                "      \"NationalCode\": null,\n" +
                "      \"Transferee\": null,\n" +
                "      \"AdminPostMethod\": null,\n" +
                "      \"QcState\": null,\n" +
                "      \"QcStateText\": null,\n" +
                "      \"QcUserId\": null,\n" +
                "      \"QcUserName\": null,\n" +
                "      \"QcCreateDate\": null,\n" +
                "      \"QcCreateDateString\": \"\",\n" +
                "      \"QcOrderCreateDate\": null,\n" +
                "      \"QcOrderCreateDateString\": \"\",\n" +
                "      \"ReponsibleCollecting\": null,\n" +
                "      \"QcCompleted\": null,\n" +
                "      \"DefectTotalPrice\": null,\n" +
                "      \"QcVerifyRepayment\": null,\n" +
                "      \"QcVerifyCall\": null\n" +
                "    },\n" +
                "    {\n" +
                "      \"OrderIds\": null,\n" +
                "      \"OrderId\": 565468,\n" +
                "      \"UserId\": null,\n" +
                "      \"RequestId\": null,\n" +
                "      \"IsFirstOrder\": null,\n" +
                "      \"TotalPrice\": 2303000,\n" +
                "      \"PostPrice\": 0,\n" +
                "      \"DeliveryDatePost\": null,\n" +
                "      \"Gateway\": -1,\n" +
                "      \"GatewayTitle\": \"پرداخت از سپرده\",\n" +
                "      \"TotalPoint\": 2303000,\n" +
                "      \"OrderStatus\": 1,\n" +
                "      \"OrderStatus660\": null,\n" +
                "      \"OrderStatus660Text\": \"\",\n" +
                "      \"OrderStatusType\": null,\n" +
                "      \"OrderStatusName\": \"لغو شده\",\n" +
                "      \"OrderStatusTitle\": null,\n" +
                "      \"IsAdminCancelable\": false,\n" +
                "      \"PaymentType\": 0,\n" +
                "      \"PaymentTypeName\": null,\n" +
                "      \"BankName\": null,\n" +
                "      \"DeliveryStatus\": 0,\n" +
                "      \"DeliveryStatusText\": null,\n" +
                "      \"DeliveryStatusName\": \"ارسال نشده\",\n" +
                "      \"DeliveryStatusDescription\": null,\n" +
                "      \"PostBarcode\": null,\n" +
                "      \"CreateDate\": \"1398/11/05 12:25:52\",\n" +
                "      \"CreateDateOrg\": \"2020-01-25T12:25:52.12\",\n" +
                "      \"PaymentDate\": \"1398/11/05 12:25:52\",\n" +
                "      \"SendDate\": null,\n" +
                "      \"IsDeleted\": null,\n" +
                "      \"DeliveryAddress\": \"تهران\",\n" +
                "      \"Vat\": 0,\n" +
                "      \"Payment\": 1863000,\n" +
                "      \"Tax\": null,\n" +
                "      \"Discount\": null,\n" +
                "      \"UserName\": null,\n" +
                "      \"Firstname\": null,\n" +
                "      \"Name\": null,\n" +
                "      \"FullName\": \" \",\n" +
                "      \"CellPhone\": null,\n" +
                "      \"StateName\": null,\n" +
                "      \"TownName\": null,\n" +
                "      \"PostCode\": null,\n" +
                "      \"Address\": null,\n" +
                "      \"ProductName\": null,\n" +
                "      \"ProductId\": null,\n" +
                "      \"FinancialCode\": null,\n" +
                "      \"ExpertId\": null,\n" +
                "      \"ExpertName\": null,\n" +
                "      \"SaleChannel\": null,\n" +
                "      \"SaleChannelName\": null,\n" +
                "      \"CategoryTitle\": null,\n" +
                "      \"PostMethod\": 1,\n" +
                "      \"PostMethodType\": null,\n" +
                "      \"PostMethodName\": \"پیک موتوری\",\n" +
                "      \"PostType\": null,\n" +
                "      \"PostTypeName\": null,\n" +
                "      \"IsReversed\": null,\n" +
                "      \"PrintDate\": null,\n" +
                "      \"PrintDateOrg\": null,\n" +
                "      \"SendFromTehranState\": null,\n" +
                "      \"UserPrinterId\": null,\n" +
                "      \"CommentCount\": 0,\n" +
                "      \"ZipCode\": null,\n" +
                "      \"PhoneNumber\": null,\n" +
                "      \"NationalCode\": null,\n" +
                "      \"Transferee\": null,\n" +
                "      \"AdminPostMethod\": null,\n" +
                "      \"QcState\": null,\n" +
                "      \"QcStateText\": null,\n" +
                "      \"QcUserId\": null,\n" +
                "      \"QcUserName\": null,\n" +
                "      \"QcCreateDate\": null,\n" +
                "      \"QcCreateDateString\": \"\",\n" +
                "      \"QcOrderCreateDate\": null,\n" +
                "      \"QcOrderCreateDateString\": \"\",\n" +
                "      \"ReponsibleCollecting\": null,\n" +
                "      \"QcCompleted\": null,\n" +
                "      \"DefectTotalPrice\": null,\n" +
                "      \"QcVerifyRepayment\": null,\n" +
                "      \"QcVerifyCall\": null\n" +
                "    },\n" +
                "    {\n" +
                "      \"OrderIds\": null,\n" +
                "      \"OrderId\": 565466,\n" +
                "      \"UserId\": null,\n" +
                "      \"RequestId\": null,\n" +
                "      \"IsFirstOrder\": null,\n" +
                "      \"TotalPrice\": 0,\n" +
                "      \"PostPrice\": 0,\n" +
                "      \"DeliveryDatePost\": null,\n" +
                "      \"Gateway\": -1,\n" +
                "      \"GatewayTitle\": \"پرداخت از سپرده\",\n" +
                "      \"TotalPoint\": 0,\n" +
                "      \"OrderStatus\": 23,\n" +
                "      \"OrderStatus660\": null,\n" +
                "      \"OrderStatus660Text\": \"\",\n" +
                "      \"OrderStatusType\": null,\n" +
                "      \"OrderStatusName\": \"ناموفق - ناموجود\",\n" +
                "      \"OrderStatusTitle\": null,\n" +
                "      \"IsAdminCancelable\": false,\n" +
                "      \"PaymentType\": 0,\n" +
                "      \"PaymentTypeName\": null,\n" +
                "      \"BankName\": null,\n" +
                "      \"DeliveryStatus\": 0,\n" +
                "      \"DeliveryStatusText\": null,\n" +
                "      \"DeliveryStatusName\": \"ارسال نشده\",\n" +
                "      \"DeliveryStatusDescription\": null,\n" +
                "      \"PostBarcode\": null,\n" +
                "      \"CreateDate\": \"1398/11/05 12:24:15\",\n" +
                "      \"CreateDateOrg\": \"2020-01-25T12:24:15.847\",\n" +
                "      \"PaymentDate\": \"1398/11/05 12:24:15\",\n" +
                "      \"SendDate\": null,\n" +
                "      \"IsDeleted\": null,\n" +
                "      \"DeliveryAddress\": \"تهران\",\n" +
                "      \"Vat\": 0,\n" +
                "      \"Payment\": 0,\n" +
                "      \"Tax\": null,\n" +
                "      \"Discount\": null,\n" +
                "      \"UserName\": null,\n" +
                "      \"Firstname\": null,\n" +
                "      \"Name\": null,\n" +
                "      \"FullName\": \" \",\n" +
                "      \"CellPhone\": null,\n" +
                "      \"StateName\": null,\n" +
                "      \"TownName\": null,\n" +
                "      \"PostCode\": null,\n" +
                "      \"Address\": null,\n" +
                "      \"ProductName\": null,\n" +
                "      \"ProductId\": null,\n" +
                "      \"FinancialCode\": null,\n" +
                "      \"ExpertId\": null,\n" +
                "      \"ExpertName\": null,\n" +
                "      \"SaleChannel\": null,\n" +
                "      \"SaleChannelName\": null,\n" +
                "      \"CategoryTitle\": null,\n" +
                "      \"PostMethod\": 1,\n" +
                "      \"PostMethodType\": null,\n" +
                "      \"PostMethodName\": \"پیک موتوری\",\n" +
                "      \"PostType\": null,\n" +
                "      \"PostTypeName\": null,\n" +
                "      \"IsReversed\": null,\n" +
                "      \"PrintDate\": null,\n" +
                "      \"PrintDateOrg\": null,\n" +
                "      \"SendFromTehranState\": null,\n" +
                "      \"UserPrinterId\": null,\n" +
                "      \"CommentCount\": 0,\n" +
                "      \"ZipCode\": null,\n" +
                "      \"PhoneNumber\": null,\n" +
                "      \"NationalCode\": null,\n" +
                "      \"Transferee\": null,\n" +
                "      \"AdminPostMethod\": null,\n" +
                "      \"QcState\": null,\n" +
                "      \"QcStateText\": null,\n" +
                "      \"QcUserId\": null,\n" +
                "      \"QcUserName\": null,\n" +
                "      \"QcCreateDate\": null,\n" +
                "      \"QcCreateDateString\": \"\",\n" +
                "      \"QcOrderCreateDate\": null,\n" +
                "      \"QcOrderCreateDateString\": \"\",\n" +
                "      \"ReponsibleCollecting\": null,\n" +
                "      \"QcCompleted\": null,\n" +
                "      \"DefectTotalPrice\": null,\n" +
                "      \"QcVerifyRepayment\": null,\n" +
                "      \"QcVerifyCall\": null\n" +
                "    }\n" +
                "  ],\n" +
                "  \"Total\": 33,\n" +
                "  \"MinCreateDate\": \"undefined\",\n" +
                "  \"MaxCreateDate\": \"undefined\",\n" +
                "  \"OrderStatus\": -1,\n" +
                "  \"BeginOrderId\": 0,\n" +
                "  \"EndOrderId\": 0,\n" +
                "  \"Order\": null,\n" +
                "  \"Sort\": null,\n" +
                "  \"PageIndex\": 0,\n" +
                "  \"PageSize\": 3,\n" +
                "  \"Status\": 0,\n" +
                "  \"Description\": \"\"\n" +
                "}";

        return DI.getGson().fromJson(string, OrderTransactionListResponse.class);
    }

    @Override
    public void getUserOrdersSummary(int orderId, final CallBackWrapper<UserOrdersSummaryResponse> callBack) {

        setMode(NetworkResponseCodes.SUCCESS);

        if (!handleCall(callBack)) {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    callBack.onSuccess(handleInternalUserOrdersSummary());
                }
            }, getResponseDelay());
        }
    }

    private UserOrdersSummaryResponse handleInternalUserOrdersSummary(){
        UserOrdersSummaryResponse ordersSummaryResponse = new UserOrdersSummaryResponse();

        OrderSummaryResult orderSummaryResult = new OrderSummaryResult();
        orderSummaryResult.setPayment(2000);
        orderSummaryResult.setTotalPoint(20);
        orderSummaryResult.setTotalPrice(5000);
        orderSummaryResult.setPostPrice(1000);
        orderSummaryResult.setFullName("سعیده مرادی");
        orderSummaryResult.setDeliveryAddress("تهران جمهوری");
        orderSummaryResult.setPostMethodName("پیک موتوری");
        orderSummaryResult.setOrderStatus(3);
        orderSummaryResult.setDeliveryStatus(3);
        orderSummaryResult.setDeliveryStatusDescription("تحویل داده شده");

        ordersSummaryResponse.setStatus(0);
        ordersSummaryResponse.setOrderSummaryResult(orderSummaryResult);

        return ordersSummaryResponse;
    }

    @Override
    public void getOrderDetails(int orderId, final CallBackWrapper<OrderDetailResponse> callBack) {

        setMode(NetworkResponseCodes.SUCCESS);

        if (!handleCall(callBack)) {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    callBack.onSuccess(handleInternalOrderDetail());
                }
            }, getResponseDelay());
        }
    }

    private OrderDetailResponse handleInternalOrderDetail(){

        OrderDetailResponse orderDetailResponse = new OrderDetailResponse();
        ArrayList<OrderDetail> orderDetails = new ArrayList<>();

        for(int i = 0; i < 5; i++){
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setImageUrl("https://staticcontent.eways.co//upload/ProductPictures/74492.jpg");
            orderDetail.setProductName("اسپیکر بی سیم تسکو TS2375 - مشکی-نقره ای");
            orderDetail.setProductId(74492);
            orderDetail.setQuantity(7);
            orderDetail.setPrice(1732808);
            orderDetail.setPoint(0);
            orderDetail.setBuyUnitPrice(1992729);

            orderDetails.add(orderDetail);
        }


        orderDetailResponse.setStatus(0);
        orderDetailResponse.setOrderDetails(orderDetails);

        return orderDetailResponse;

    }
}
