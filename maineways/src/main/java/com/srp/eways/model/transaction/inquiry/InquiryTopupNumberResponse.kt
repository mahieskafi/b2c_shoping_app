package com.srp.eways.model.transaction.inquiry

import com.google.gson.annotations.SerializedName

/**
 * Created by Eskafi on 9/8/2019.
 */
data class InquiryTopupNumberResponse
(
        @SerializedName("Description")
        var description: String? = "",
        @SerializedName("Status")
        var status: Int = 0,
        @SerializedName("InquiryTopups")
        var topupList: ArrayList<InquiryTopup>?
)