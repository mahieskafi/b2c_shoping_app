package com.srp.eways.model.charge.result.topinquiry


import com.google.gson.annotations.SerializedName

data class Item(
    @SerializedName("CustomerMobile")
    var customerMobile: String = "",
    @SerializedName("DeliverStatus")
    var deliverStatus: Int = 0,
    @SerializedName("RequestDate")
    var requestDate: String = "",
    @SerializedName("RequestId")
    var requestId: String = "",
    @SerializedName("RequestType")
    var requestType: Int = 0,
    @SerializedName("SaleUnitPrice")
    var saleUnitPrice: Double = 0.0,
    @SerializedName("ProductName")
    var productName: String = ""
)