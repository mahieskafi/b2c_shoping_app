package com.srp.ewayspanel.model.transaction.order

import com.google.gson.annotations.SerializedName

/**
 * Created by Eskafi on 2/8/2020.
 */

data class OrderDetail
(
        @SerializedName("IsWarehouseState")
        var isWarehouseState: Boolean = false,

        @SerializedName("RowNumber")
        var rowNumber: Int = 0,

        @SerializedName("Id")
        var id: Int = 0,

        @SerializedName("OrderId")
        var orderId: Int = 0,

        @SerializedName("PackId")
        var packId: Int = 0,

        @SerializedName("ProductId")
        var productId: Int = 0,

        @SerializedName("ProductName")
        var productName: String = "",

        @SerializedName("ProductFullName")
        var productFullName: String = "",

        @SerializedName("Quantity")
        var quantity: Int = 0,

        @SerializedName("Price")
        var price: Long = 0,

        @SerializedName("Point")
        var point: Int = 0,

        @SerializedName("IsDeleted")
        var isDeleted: Boolean = false,

        @SerializedName("UserId")
        var userId: Int = 0,

        @SerializedName("BuyUnitPrice")
        var buyUnitPrice: Long = 0,

        @SerializedName("UnitPriceInPackPrice")
        var unitPriceInPackPrice: Long = 0,

        @SerializedName("FinancialId")
        var financialId: String = "",

        @SerializedName("Coefficient")
        var coefficient: Int = 0,

        @SerializedName("FinancialCode")
        var financialCode: String = "",

        @SerializedName("ImageUrl")
        var imageUrl: String = "",

        @SerializedName("QcQuantity")
        var qcQuantity: Int = 0,

        @SerializedName("QcDiscord")
        var qcDiscord: Int = 0,

        @SerializedName("DefectivePrice")
        var defectivePrice: Int = 0,

        @SerializedName("CreateDate")
        var createDate: String = "",

        @SerializedName("IsProduct660")
        var isProduct660: Boolean = false

)