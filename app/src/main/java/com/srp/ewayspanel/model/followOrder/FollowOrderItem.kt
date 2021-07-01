package com.srp.ewayspanel.model.followorder

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class FollowOrderItem(

        @SerializedName("OrderIds")
        val orderIDS: Any? = null,

        @SerializedName("OrderId")
        var orderID: Long = 0L,

        @SerializedName("BijacImageUrl")
        val bijacImageURL: Any? = null,

        @SerializedName("UserId")
        var userID: Long? = null,

        @SerializedName("RequestId")
        val requestID: Any? = null,

        @SerializedName("PaymentId")
        var paymentID: Long = 0L,

        @SerializedName("IsFirstOrder")
        val isFirstOrder: Any? = null,

        @SerializedName("TotalPrice")
        var totalPrice: Long = 0L,

        @SerializedName("PostPrice")
        var postPrice: Long = 0L,

        @SerializedName("DeliveryDatePost")
        val deliveryDatePost: Any? = null,

        @SerializedName("Gateway")
        var gateway: Long = 0L,

        @SerializedName("GatewayTitle")
        val gatewayTitle: String? = null,

        @SerializedName("TotalPoint")
        var totalPoint: Long = 0L,

        @SerializedName("OrderStatus")
        var orderStatus: Int = 0,

        @SerializedName("OrderStatus660")
        val orderStatus660: Any? = null,

        @SerializedName("OrderStatus660Text")
        val orderStatus660Text: String? = null,

        @SerializedName("OrderStatusType")
        val orderStatusType: Any? = null,

        @SerializedName("OrderStatusName")
        var orderStatusName: String = "",

        @SerializedName("OrderStatusTitle")
        val orderStatusTitle: Any? = null,

        @SerializedName("IsAdminCancelable")
        val isAdminCancelable: Boolean? = null,

        @SerializedName("PaymentType")
        var paymentType: Long = 0L,

        @SerializedName("PaymentTypeName")
        val paymentTypeName: String? = null,

        @SerializedName("BankName")
        val bankName: Any? = null,

        @SerializedName("DeliveryStatus")
        var deliveryStatus: Long = 0L,

        @SerializedName("DeliveryStatusText")
        val deliveryStatusText: Any? = null,

        @SerializedName("DeliveryStatusName")
        var deliveryStatusName: String = "",

        @SerializedName("DeliveryStatusDescription")
        val deliveryStatusDescription: Any? = null,

        @SerializedName("PostBarcode")
        val postBarcode: Any? = null,

        @SerializedName("CreateDate")
        var createDate: String = "",

        @SerializedName("CreateDateOrg")
        val createDateOrg: String? = null,

        @SerializedName("PaymentDate")
        var paymentDate: String = "",

        @SerializedName("SendDate")
        val sendDate: Any? = null,

        @SerializedName("IsDeleted")
        val isDeleted: Any? = null,

        @SerializedName("DeliveryAddress")
        val deliveryAddress: String? = null,

        @SerializedName("Vat")
        val vat: Long? = null,

        @SerializedName("Payment")
        var payment: Long = 0L,

        @SerializedName("Tax")
        val tax: Any? = null,

        @SerializedName("Discount")
        val discount: Any? = null,

        @SerializedName("UserName")
        val userName: Any? = null,

        @SerializedName("Firstname")
        val firstname: Any? = null,

        @SerializedName("Name")
        val name: Any? = null,

        @SerializedName("FullName")
        val fullName: String? = null,

        @SerializedName("CellPhone")
        val cellPhone: Any? = null,

        @SerializedName("StateName")
        val stateName: Any? = null,

        @SerializedName("TownName")
        val townName: Any? = null,

        @SerializedName("PostCode")
        val postCode: Any? = null,

        @SerializedName("Address")
        val address: Any? = null,

        @SerializedName("ProductName")
        val productName: Any? = null,

        @SerializedName("ProductId")
        val productID: Any? = null,

        @SerializedName("FinancialCode")
        val financialCode: Any? = null,

        @SerializedName("ExpertId")
        val expertID: Any? = null,

        @SerializedName("ExpertName")
        val expertName: Any? = null,

        @SerializedName("SaleChannel")
        val saleChannel: Any? = null,

        @SerializedName("SaleChannelName")
        val saleChannelName: Any? = null,

        @SerializedName("CategoryTitle")
        val categoryTitle: Any? = null,

        @SerializedName("PostMethod")
        var postMethod: Long = 0L,

        @SerializedName("PostMethodType")
        val postMethodType: Any? = null,

        @SerializedName("PostMethodName")
        var postMethodName: String = "",

        @SerializedName("PostType")
        val postType: Any? = null,

        @SerializedName("PostTypeName")
        val postTypeName: Any? = null,

        @SerializedName("OrderWeight")
        val orderWeight: Any? = null,

        @SerializedName("IsReversed")
        val isReversed: Any? = null,

        @SerializedName("PrintDate")
        val printDate: Any? = null,

        @SerializedName("PrintDateOrg")
        val printDateOrg: Any? = null,

        @SerializedName("PrintedStores")
        val printedStores: Any? = null,

        @SerializedName("SendFromTehranState")
        val sendFromTehranState: Any? = null,

        @SerializedName("UserPrinterId")
        val userPrinterID: Any? = null,

        @SerializedName("CommentCount")
        val commentCount: Long? = null,

        @SerializedName("ZipCode")
        val zipCode: Any? = null,

        @SerializedName("PhoneNumber")
        val phoneNumber: Any? = null,

        @SerializedName("NationalCode")
        val nationalCode: Any? = null,

        @SerializedName("Transferee")
        val transferee: Any? = null,

        @SerializedName("AdminPostMethod")
        val adminPostMethod: Any? = null,

        @SerializedName("DeliveryStatusModifiedDate")
        val deliveryStatusModifiedDate: Any? = null,

        @SerializedName("DeliveryStatusModifiedDateOrg")
        val deliveryStatusModifiedDateOrg: Any? = null,

        @SerializedName("DelivertyPeriodTimeDesc")
        val delivertyPeriodTimeDesc: Any? = null,

        @SerializedName("DeliveryToSendDate")
        val deliveryToSendDate: Any? = null,

        @SerializedName("DeliveryToSendDateString")
        val deliveryToSendDateString: Any? = null,

        @SerializedName("TrackingCode")
        val trackingCode: Any? = null,

        @SerializedName("QcState")
        val qcState: Any? = null,

        @SerializedName("QcStateText")
        val qcStateText: Any? = null,

        @SerializedName("QcUserId")
        val qcUserID: Any? = null,

        @SerializedName("QcUserName")
        val qcUserName: Any? = null,

        @SerializedName("QcCreateDate")
        val qcCreateDate: Any? = null,

        @SerializedName("QcCreateDateString")
        val qcCreateDateString: String? = null,

        @SerializedName("QcOrderCreateDate")
        val qcOrderCreateDate: Any? = null,

        @SerializedName("QcOrderCreateDateString")
        val qcOrderCreateDateString: String? = null,

        @SerializedName("ReponsibleCollecting")
        val reponsibleCollecting: Any? = null,

        @SerializedName("QcCompleted")
        val qcCompleted: Any? = null,

        @SerializedName("DefectTotalPrice")
        val defectTotalPrice: Any? = null,

        @SerializedName("QcVerifyRepayment")
        val qcVerifyRepayment: Any? = null,

        @SerializedName("QcVerifyCall")
        val qcVerifyCall: Any? = null,

        var isShowMore: Boolean = false
) : Serializable {
    constructor(orderId: Long) : this(orderID = orderId)
    constructor(orderID: Long, userId: Long, totalPrice: Long, orderStatus: Int, orderStatusName: String, paymentDate: String, payment: Long) :
            this(orderID = orderID, userID = userId, totalPrice = totalPrice, orderStatus = orderStatus, orderStatusName = orderStatusName,
                    paymentDate = paymentDate, payment = payment)

}
