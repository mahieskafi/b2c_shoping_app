package com.srp.eways.model.banner


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("CreateDateTime")
    var createDateTime: String,
    @SerializedName("Deleted")
    var deleted: Boolean,
    @SerializedName("EndShowDate")
    var endShowDate: String,
    @SerializedName("Id")
    var id: Int,
    @SerializedName("Published")
    var published: Boolean,
    @SerializedName("ShowTitle")
    var showTitle: Boolean,
    @SerializedName("StartShowDate")
    var startShowDate: String,
    @SerializedName("Target")
    var target: String,
    @SerializedName("Title")
    var title: String,
    @SerializedName("Type")
    var type: Int,
    @SerializedName("Url")
    var url: String,
    @SerializedName("UserId")
    var userId: Int
)