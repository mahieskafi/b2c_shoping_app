package com.srp.eways.model.bill.archivedList

import androidx.room.Entity

import androidx.room.PrimaryKey

import androidx.annotation.NonNull
import androidx.annotation.Nullable
import com.google.gson.annotations.SerializedName

@Entity(tableName = "bill")
data class BillTemp
(

        @PrimaryKey
        @NonNull
        @SerializedName("Id")
        val id: Int,

        @SerializedName("BillId")
        val billId: String,

        @SerializedName("PaymentId")
        val paymentId: String,

        @SerializedName("PaymentDeadLineDate")
        val paymentDeadLineDate: String? = null,

        @SerializedName("Description")
        val description: String? = null,

        @SerializedName("CustomerName")
        val customerName: String? = null,

        @SerializedName("CustomerPhone")
        val customerPhone: String? = null,

        @SerializedName("Price")
        val price: Long,

        @SerializedName("BillTypeId")
        val billTypeId: Int,

        @SerializedName("BankName")
        val bankName: String? = null,

        @SerializedName("Status")
        val status: Int = 700,

        @SerializedName("StatusName")
        val statusName: String? = null,

        @SerializedName("IsRemovable")
        val isRemovable: Boolean = true,

        @SerializedName("IsDeleted")
        val isDeleted: Boolean = false,

        @SerializedName("UserId")
        val userId: Int = 0,

        @SerializedName("LogDate")
        val logDate: String? = null,

        @SerializedName("InquiryNumber")
        val inquiryNumber: String? = null,

        var isSelected: Boolean = false,

        var isShowMore: Boolean = false
) {
    constructor(billInquiryNumber: String?, billId: String, payId: String, billType: Int, price: Long) : this(id = 0, inquiryNumber = billInquiryNumber, billId = billId, paymentId = payId, billTypeId = billType, price = price)
}