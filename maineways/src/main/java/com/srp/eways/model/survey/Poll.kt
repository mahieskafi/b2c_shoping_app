package com.srp.eways.model.survey

import com.google.gson.annotations.SerializedName

/**
 * Created by ErfanG on 21/10/2019.
 */
data class Poll(
        @SerializedName("id")
        var id: String = "",
        @SerializedName("title")
        var title: String = "",
        @SerializedName("description")
        var description: String = "",
        @SerializedName("endTitle")
        var endTitle: String = "",
        @SerializedName("endDescription")
        var endDescription: String = "",
        @SerializedName("openEvent")
        var openEvent: String = "",
        @SerializedName("endEvent")
        var endEvent: String = "",
        @SerializedName("questions")
        var questions: ArrayList<Question> = ArrayList()

)