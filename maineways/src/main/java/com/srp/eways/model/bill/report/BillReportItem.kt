package com.srp.eways.model.bill.report


import com.google.gson.annotations.SerializedName

data class BillReportItem(
    @SerializedName("BankId")
    val bankId: Int = 0,
    @SerializedName("BankName")
    val bankName: String = "",
    @SerializedName("BatchId")
    val batchId: Any? = null,
    @SerializedName("BillId")
    val billId: String = "",
    @SerializedName("BillType")
    val billType: Int = 0,
    @SerializedName("BillTypeId")
    val billTypeId: Int = 0,
    @SerializedName("CustomerName")
    val customerName: String? = null,
    @SerializedName("CustomerPhone")
    val customerPhone: String? = null,
    @SerializedName("Description")
    val description: String = "",
    @SerializedName("FromLogDate")
    val fromLogDate: String = "",
    @SerializedName("Guid")
    val guid: String = "",
    @SerializedName("Id")
    val id: Int = 0,
    @SerializedName("InquiryNumber")
    val inquiryNumber: String? = null,
    @SerializedName("IsDeleted")
    val isDeleted: Boolean? = null,
    @SerializedName("IsRemovable")
    val isRemovable: Boolean = false,
    @SerializedName("LogDate")
    var logDate: String = "",
    @SerializedName("LogDateOrg")
    val logDateOrg: String = "",
    @SerializedName("OrderId")
    val orderId: Int = 0,
    @SerializedName("PaymentDeadLineDate")
    val paymentDeadLineDate: String? = null,
    @SerializedName("PaymentId")
    val paymentId: String = "",
    @SerializedName("Price")
    val price: Long = 0,
    @SerializedName("RPaymentID")
    val rPaymentID: String? = null,
    @SerializedName("ResultNumber")
    val resultNumber: Long = 0,
    @SerializedName("SettlementDate")
    val settlementDate: String = "",
    @SerializedName("Status")
    val status: Int = 0,
    @SerializedName("StatusName")
    val statusName: String = "",
    @SerializedName("TempResultNumber")
    val tempResultNumber: String = "",
    @SerializedName("ToLogDate")
    val toLogDate: String = "",
    @SerializedName("TransactionId")
    val transactionId: Int = 0,
    @SerializedName("Type")
    val type: Any? = null,
    @SerializedName("UpdateDate")
    val updateDate: String = "",
    @SerializedName("UpdateDateOrg")
    val updateDateOrg: String = "",
    @SerializedName("UserId")
    val userId: Int = 0,

    val isTopData : Boolean = false,
    val topDateText : String? = null,
    var isShowMore: Boolean = false
)