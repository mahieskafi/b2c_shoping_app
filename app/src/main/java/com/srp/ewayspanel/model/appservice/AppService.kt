package com.srp.ewayspanel.model.appservice

import com.google.gson.annotations.SerializedName

data class AppService(

        @SerializedName("ServiceCode")
        var id: Int,

        var icon: Int,

        @SerializedName("ServiceName")
        var title: String,

        @SerializedName("Priority")
        var priority: Int,

        @SerializedName("IsActive")
        var isActive: Boolean,

        @SerializedName("IsVisible")
        var isVisible: Boolean,

        var isSelected: Boolean
)
