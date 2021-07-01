package com.srp.eways.model.transaction.inquiry

import com.google.gson.annotations.SerializedName

/**
 * Created by Eskafi on 9/14/2019.
 */

data class InquiryRequest(
        @SerializedName("StartDate")
        var startDate: String,
        @SerializedName("EndDate")
        var endDate: String
)