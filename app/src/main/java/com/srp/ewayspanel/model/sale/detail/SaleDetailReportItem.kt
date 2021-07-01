package com.srp.ewayspanel.model.sale.detail

import com.google.gson.annotations.SerializedName

data class SaleDetailReportItem(

        @SerializedName("Id")
        var id: Int,

        @SerializedName("UserFirstName")
        var userFirstName: String,

        @SerializedName("UserName")
        var userName: String,

        @SerializedName("TransactionStatusTitle")
        var transactionStatusTitle: String,

        @SerializedName("ProductName")
        var productName: String,

        @SerializedName("SiteName")
        var siteName: String,

        @SerializedName("ChannelTypeName")
        var channelTypeName: String,

        @SerializedName("BankName")
        var bankName: String,

        @SerializedName("RequestDate")
        var requestDate: String,

        @SerializedName("Agent")
        var agent: Int,

        @SerializedName("RequestType")
        var requestType: Int,

        @SerializedName("RequestQuentity")
        var requestQuantity: Int,

        @SerializedName("CustomerMobile")
        var customerMobile: String,

        @SerializedName("HashedCustomerMobile")
        var hashedCustomerMobile: String,

        @SerializedName("CustomerEmail")
        var customerEmail: String,

        @SerializedName("PaymentDate")
        var paymentDate: String,

        @SerializedName("PaymentGateways")
        var paymentGateways: Int,

        @SerializedName("PaymentId")
        var paymentId: String,

        @SerializedName("PaymentValue")
        var paymentValue: Long,

        @SerializedName("MerchanestShare")
        var merchanestShare: Int,

        @SerializedName("DeliverStatus")
        var deliverStatus: Int,

//        @SerializedName("Eways")
//        var eways: Long,

        @SerializedName("ProductCategory")
        var productCategory: Int,

        @SerializedName("RequestId")
        var requestId: String,

        @SerializedName("FromDate")
        var fromDate: String,

        @SerializedName("ToDate")
        var toDate: String,

        var isShowMore: Boolean = false
)