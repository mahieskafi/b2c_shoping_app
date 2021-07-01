package com.srp.ewayspanel.model.storepage.slider

import com.google.gson.annotations.SerializedName
import com.srp.ewayspanel.model.store.slider.Slider

/**
 * Created by Eskafi on 10/23/2019.
 */
data class SliderResponse
(
        @SerializedName("Description")
        var description: String = "",

        //TODO: needs  SerializedName
        var sliderList: List<Slider> = listOf(),

        @SerializedName("Status")
        var status: Int = 0
)
