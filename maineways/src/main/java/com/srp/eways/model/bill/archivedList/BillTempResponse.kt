package com.srp.eways.model.bill.archivedList

import com.google.gson.annotations.SerializedName
import com.srp.eways.model.bill.archivedList.BillTemp
import java.util.ArrayList

data class BillTempResponse(

        @SerializedName("Status")
        var status: Int,

        @SerializedName("Description")
        var description: String,

        @SerializedName("Data")
        val data: ArrayList<BillTemp>? = null
) {
    constructor(errorCode: Int, errorMessage: String) : this(status = errorCode, description = errorMessage)
}