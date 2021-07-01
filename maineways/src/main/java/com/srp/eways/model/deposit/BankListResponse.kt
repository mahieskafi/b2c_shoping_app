package com.srp.eways.model.deposit

import com.google.gson.annotations.SerializedName

/**
 * Created by Eskafi on 12/14/2019.
 */

data class BankListResponse(

        @SerializedName("Status")
        val status: Int = 0,

        @SerializedName("Description")
        val description: String = "",

        @SerializedName("Items")
        val items: List<Bank>? = null

)