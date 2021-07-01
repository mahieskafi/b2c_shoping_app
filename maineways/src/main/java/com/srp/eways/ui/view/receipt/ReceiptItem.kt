package com.srp.eways.ui.view.receipt

import android.graphics.drawable.Drawable
import java.io.Serializable

/**
 * Created by Eskafi on 9/3/2019.
 */
data class ReceiptItem(
        val title: String,
        val value: String,
        val valueDescription: String? = null,
        val icon: Drawable? = null
) : Serializable {
    constructor(title: String, value: String, icon: Drawable?) : this(title = title, value = value, icon = icon, valueDescription = null)

}
