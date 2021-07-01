package com.srp.ewayspanel.ui.view

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewTreeObserver
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.srp.ewayspanel.R
import com.srp.ewayspanel.di.DI
import com.yashoid.sequencelayout.SequenceLayout

/**
 * Created by ErfanG on 2/15/2020.
 */
class EmptyShopCartView : SequenceLayout {

    private val mIcon : ImageView
    private val mTitle : TextView

    constructor(context: Context) : super(context)
    constructor(context: Context, attrSet : AttributeSet) : super(context, attrSet)

    init {

        LayoutInflater.from(context).inflate(R.layout.item_no_data_shop_cart, this, true)

        mIcon = findViewById(R.id.card_icon)
        mTitle = findViewById(R.id.top_text)

        addSequences(R.xml.sequences_item_no_data_shop_cart)

        val resources = DI.getABResources()

        viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener{
            override fun onGlobalLayout() {

                val lp = layoutParams as MarginLayoutParams
                val margin = resources.getDimenPixelSize(R.dimen.item_no_data_shop_cart_margin)
                lp.setMargins(margin, margin, margin, margin)
                layoutParams = lp

                viewTreeObserver.removeOnGlobalLayoutListener(this)
            }

        })


        mIcon.setImageDrawable(resources.getDrawable(R.drawable.ic_state_shop_empty))




        mTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.item_no_data_shop_cart_title_size).toFloat())
        mTitle.setTextColor(resources.getColor(R.color.item_no_data_shop_cart_title_color))
        mTitle.typeface = ResourcesCompat.getFont(context, R.font.iran_yekan_medium)
        mTitle.text = resources.getString(R.string.item_no_data_shop_cart_title)


    }
}