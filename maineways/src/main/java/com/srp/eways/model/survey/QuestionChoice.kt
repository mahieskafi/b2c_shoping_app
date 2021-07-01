package com.srp.eways.model.survey

import com.google.gson.annotations.SerializedName

/**
 * Created by ErfanG on 21/10/2019.
 */
data class QuestionChoice(
        @SerializedName("text")
        var text: String = "",
        @SerializedName("image")
        var imageUrl: String = "",
        @SerializedName("event")
        var event: String = ""
)