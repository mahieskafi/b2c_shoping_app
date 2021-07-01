package com.srp.eways.model.survey

import com.google.gson.annotations.SerializedName

/**
 * Created by ErfanG on 21/10/2019.
 */
data class Question(
        @SerializedName("text")
        var text: String = "",
        @SerializedName("type")
        var type: Int = -1,
        @SerializedName("choices")
        var choices: ArrayList<QuestionChoice> = ArrayList()
)