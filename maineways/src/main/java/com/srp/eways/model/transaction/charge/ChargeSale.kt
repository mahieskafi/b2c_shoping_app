package com.srp.eways.model.transaction.charge


import com.google.gson.annotations.SerializedName
import com.srp.eways.R
import com.srp.eways.di.DIMain
import com.srp.eways.ui.view.receipt.Receipt
import com.srp.eways.ui.view.receipt.ReceiptItem
import com.srp.eways.util.Utils

data class ChargeSale(

        @SerializedName("Agent")
        var agent: Int = 0,

        @SerializedName("BankName")
        var bankName: String = "",

        @SerializedName("ChannelTypeName")
        var channelTypeName: String = "",

        @SerializedName("CustomerEmail")
        var customerEmail: String? = "",

        @SerializedName("CustomerMobile")
        var customerMobile: String? = "",

        @SerializedName("DeliverStatus")
        var deliverStatus: Int = 0,

//        @SerializedName("Eways")
//        var eways: Int = 0,

        @SerializedName("FromDate")
        var fromDate: String = "",

        @SerializedName("HashedCustomerMobile")
        var hashedCustomerMobile: String?= "",

        @SerializedName("Id")
        var id: Int = 0,

        @SerializedName("MerchanestShare")
        var merchanestShare: Int = 0,

        @SerializedName("PaymentDate")
        var paymentDate: String? = "",

        @SerializedName("PaymentGateways")
        var paymentGateways: Int = 0,

        @SerializedName("PaymentId")
        var paymentId: Long = 0,

        @SerializedName("PaymentValue")
        var paymentValue: Int = 0,

        @SerializedName("ProductCategory")
        var productCategory: Int = 0,

        @SerializedName("ProductName")
        var productName: String = "",

        @SerializedName("RequestDate")
        var requestDate: String = "",

        @SerializedName("RequestId")
        var requestId: String = "",

        @SerializedName("RequestQuentity")
        var requestQuentity: Int = 0,

        @SerializedName("RequestType")
        var requestType: Int = 0,

        @SerializedName("SiteName")
        var siteName: String = "",

        @SerializedName("ToDate")
        var toDate: String = "",

        @SerializedName("TransactionStatusTitle")
        var transactionStatusTitle: String = "",

        @SerializedName("UserFirstName")
        var userFirstName: String = "",

        @SerializedName("UserName")
        var userName: String = "",

        var isShowMore: Boolean = false

) {

    public fun getStatusCode(): Int {
        if (deliverStatus == 2) {
            return Receipt.STATUS_CODE_SUCCESS
        } else if (deliverStatus == 8) {
            return Receipt.STATUS_CODE_UNKNOWN
        } else {
            return Receipt.STATUS_CODE_FAILURE
        }
    }

    public fun createReceipt(): Receipt {
        val resources = DIMain.getABResources()

        val receipt = Receipt()

        receipt.statusCode = getStatusCode()
        val dateTime = paymentDate?.split(" ")

        val date = dateTime?.get(0)
        val time = dateTime?.get(1)?.split(":")?.get(0) + ":" + dateTime?.get(1)?.split(":")?.get(1)

        receipt.receiptItems.add(ReceiptItem(resources.getString(R.string.chargesale_transaction_type), Utils.toPersianNumber(productName), null,null))
        receipt.receiptItems.add(ReceiptItem(resources.getString(R.string.chargesale_mobilenumber), Utils.toPersianNumber(customerMobile), null,null))
        receipt.receiptItems.add(ReceiptItem(resources.getString(R.string.chargesale_paymentamount), Utils.toPersianPriceNumber(paymentValue), null,null))

        if(!dateTime.isNullOrEmpty() && !dateTime.get(0).isEmpty()) {
            receipt.receiptItems.add(ReceiptItem(resources.getString(R.string.chargesale_paymentdate), Utils.toPersianNumber(date), null, null))
        }
        if(!dateTime.isNullOrEmpty() && !dateTime.get(1).isEmpty()) {
            receipt.receiptItems.add(ReceiptItem(resources.getString(R.string.chargesale_payment_time), Utils.toPersianNumber(time), null, null))
        }

        receipt.receiptItems.add(ReceiptItem(resources.getString(R.string.chargesale_paymentid), Utils.toPersianNumber(paymentId.toString()), null,null))

        receipt.receiptType = Receipt.TRANSACTION

        return receipt
    }

}