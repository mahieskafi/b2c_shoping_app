package com.srp.eways.model.deposit.transaction

import com.google.gson.annotations.SerializedName

/**
 * Created by Eskafi on 9/3/2019.
 */
data class DepositTransactionItem(
        @SerializedName("IncreaseCreditRequestId")
        val requestId: Int = 0,
        @SerializedName("Guid")
        val guid: String? = null,
        @SerializedName("IncreaseCreditType")
        val type: Int = 0,
        @SerializedName("RequestDateOrg")
        val requestDateOrg: String?= null,
        @SerializedName("RequestDate")
        val requestDate: String? = null,
        @SerializedName("Payment")
        val payment: Long = 0,
        @SerializedName("StatusCode")
        val statusCode: Int = 0,
        @SerializedName("Status")
        val status: Int = 0,
        @SerializedName("BankName")
        val bankName: String? = null,
        @SerializedName("BankLogo")
        val bankLogo: String? = null,
        @SerializedName("Description")
        val description: String? = null,
        @SerializedName("PaymentId")
        val paymentId: Long = 0,
        @SerializedName("StatusName")
        val statusName: String? = null,

        var isShowMore: Boolean = false
)
