package com.srp.ewayspanel.ui.shopcart.productlist

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.srp.ewayspanel.R
import com.srp.ewayspanel.di.DI
import com.srp.eways.util.Utils
import com.yashoid.sequencelayout.SequenceLayout

/**
 * Created by ErfanG on 12/30/2019.
 */
class ShopCartTopItemView : SequenceLayout {

    private val mItemCount : TextView
    private val mTitle : TextView
    private val mDeleteTitle : TextView
    private val mDeleteIcon : ImageView
    private val mDeleteButton : View

    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)
    constructor(context: Context, attributeSet: AttributeSet, defAtrSet : Int) : super(context, attributeSet, defAtrSet)

    init {
        LayoutInflater.from(context).inflate(R.layout.shop_cart_top_item, this, true)

        mItemCount = findViewById(R.id.item_count)
        mTitle = findViewById(R.id.title)
        mDeleteTitle = findViewById(R.id.delete_title)
        mDeleteIcon = findViewById(R.id.delete_icon)
        mDeleteButton = findViewById(R.id.delete_button)

        addSequences(R.xml.sequences_shop_cart_top_item)

        val resources = DI.getABResources()

        mTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.shop_cart_top_item_title_size).toFloat())
        mTitle.setTextColor(resources.getColor(R.color.shop_cart_top_item_title_color))
        mTitle.text = resources.getString(R.string.shoo_cart_top_item_title)
        mTitle.typeface = ResourcesCompat.getFont(context, R.font.iran_yekan_medium)

        mItemCount.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.shop_cart_top_item_title_size).toFloat())
        mItemCount.setTextColor(resources.getColor(R.color.shop_cart_top_item_title_color))
        mItemCount.typeface = ResourcesCompat.getFont(context, R.font.iran_yekan_medium)


        mDeleteTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.shop_cart_top_item_delete_title_size).toFloat())
        mDeleteTitle.setTextColor(resources.getColor(R.color.shop_cart_top_item_delete_title_color))
        mDeleteTitle.text = resources.getString(R.string.shoo_cart_top_item_delete_title)
        mDeleteTitle.typeface = ResourcesCompat.getFont(context, R.font.iran_yekan)

        mDeleteIcon.setImageDrawable(resources.getDrawable(R.drawable.shop_cart_item_remove_icon))

//        mDeleteButton.background = resources.getDrawable(R.drawable.shop_cart_item_remove_background)

        mItemCount.visibility= GONE
        mTitle.visibility= GONE


    }

    fun setDeleteAction(action : OnClickListener){

        mDeleteButton.setOnClickListener(action)
        mDeleteTitle.setOnClickListener(action)
        mDeleteIcon.setOnClickListener(action)
    }

    fun setProductCount(count : Int){
        mItemCount.text = " ".plus(Utils.toPersianNumber(count))
    }
}