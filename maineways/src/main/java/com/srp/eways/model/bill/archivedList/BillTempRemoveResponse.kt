package com.srp.eways.model.bill.archivedList

import com.google.gson.annotations.SerializedName

data class BillTempRemoveResponse(

        @SerializedName("Status")
        var status: Int,

        @SerializedName("Description")
        var description: String
)