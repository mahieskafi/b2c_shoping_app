package com.srp.eways.model.transaction.inquiry

import com.google.gson.annotations.SerializedName
import com.srp.eways.R
import com.srp.eways.di.DIMain
import com.srp.eways.ui.view.receipt.Receipt
import com.srp.eways.ui.view.receipt.ReceiptItem
import com.srp.eways.util.Utils

/**
 * Created by Eskafi on 9/8/2019.
 */
data class InquiryTopup
(
        @SerializedName("UserId")
        var userId: Int? = 0,
        @SerializedName("UserName")
        var userName: String? = "",
        @SerializedName("SerialNo")
        var serialNo: String? = "",
        @SerializedName("RequestId")
        var requestId: String? = "",
        @SerializedName("Price")
        var price: String? = "",
        @SerializedName("Date")
        var requestDate: String? = "",
        @SerializedName("Status")
        var status: String? = "",
        @SerializedName("Message")
        var message: String? = "",
        @SerializedName("BankOrderId")
        var bankOrderId: String? = "",
        @SerializedName("SattlementDate")
        var sattlementDate: String? = "",
        @SerializedName("PaymentId")
        var paymentId: String? = "",
        @SerializedName("ResultCode")
        var requestCode: Int? = 0,
        @SerializedName("TypeDesc")
        var type: String? = "",
        @SerializedName("PackageName")
        var packageName: String? = "",

        var isShowMore: Boolean = false
) {
    public fun createReceipt(): Receipt {
        val resources = DIMain.getABResources()

        val receipt = Receipt()

        //TODO status should be int
        receipt.statusCode = status?.toInt()!!
        val dateTime = requestDate?.split(" ")

        //TODO send mobile number
//                receipt.receiptItems.add(ReceiptItem(resources.getString(R.string.chargesale_mobilenumber), Utils.toPersianNumber(customerMobile), null))
        receipt.receiptItems.add(ReceiptItem(resources.getString(R.string.chargesale_paymentdate), Utils.toPersianNumber(dateTime?.get(0)), null, null))
        receipt.receiptItems.add(ReceiptItem(resources.getString(R.string.chargesale_paymentid), Utils.toPersianNumber(paymentId.toString()), null, null))
        receipt.receiptItems.add(ReceiptItem(resources.getString(R.string.chargesale_paymentamount), Utils.toPersianPriceNumber(price), null, null))

        return receipt
    }
}