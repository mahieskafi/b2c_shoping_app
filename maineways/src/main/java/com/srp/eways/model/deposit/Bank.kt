package com.srp.eways.model.deposit

import com.google.gson.annotations.SerializedName

/**
 * Created by Eskafi on 12/14/2019.
 */
data class Bank(
        @SerializedName("GId")
        val gId: Int,

        @SerializedName("PName")
        val pName: String,

        @SerializedName("EName")
        val eName: String,

        @SerializedName("BankType")
        val bankType: Int,

        var selcted: Boolean = false
)