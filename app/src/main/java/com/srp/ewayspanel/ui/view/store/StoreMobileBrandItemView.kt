package com.srp.ewayspanel.ui.view.store

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.res.ResourcesCompat
import com.srp.ewayspanel.R
import com.srp.ewayspanel.di.DI
import com.srp.ewayspanel.ui.store.mobilelist.util.MobileBrandUtil

class StoreMobileBrandItemView : FrameLayout {

    private var mIcon: AppCompatImageView
    private var mTitle: AppCompatTextView

    private var mRootView: LinearLayout

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        LayoutInflater.from(context).inflate(R.layout.item_mobile_list_brand, this, true)

        mIcon = findViewById(R.id.icon)
        mTitle = findViewById(R.id.title)
        mRootView = findViewById(R.id.root_view)

        val abResources = DI.getABResources()

        var iconLayoutParams = LinearLayout.LayoutParams(abResources.getDimenPixelSize(R.dimen.mobile_list_brand_item_icon_width),
                abResources.getDimenPixelSize(R.dimen.mobile_list_brand_item_icon_height)
        )
        iconLayoutParams.setMargins(
                abResources.getDimenPixelSize(R.dimen.mobile_list_brand_item_icon_margin_left),
                abResources.getDimenPixelSize(R.dimen.mobile_list_product_recycler_margin_top),
                abResources.getDimenPixelSize(R.dimen.mobile_list_brand_item_icon_margin_right),
                abResources.getDimenPixelSize(R.dimen.mobile_list_brand_item_icon_margin_bottom)
        )
        mIcon.layoutParams = iconLayoutParams

        mTitle.typeface = ResourcesCompat.getFont(context, R.font.iran_yekan_bold)
        mTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, abResources.getDimen(R.dimen.mobile_list_brand_item_title_text_size))
        mTitle.setTextColor(abResources.getColor(R.color.mobile_list_brand_item_title_text_color))

        var titleLayoutParams = LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        titleLayoutParams.setMargins(
               0,
                0,
                0,
                abResources.getDimenPixelSize(R.dimen.mobile_list_product_recycler_margin_bottom)
        )
        mTitle.layoutParams = titleLayoutParams

        mRootView.layoutParams = FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
    }

    fun setTitle(title: String) {
        mTitle.text = title
    }

    fun setItemSelected(isSelected: Boolean) {
        val abResources = DI.getABResources()
        if (isSelected) {
            mIcon.background = abResources.getDrawable(R.drawable.mobile_list_brand_item_selected_background)
        } else {
            mIcon.background = abResources.getDrawable(R.drawable.mobile_list_brand_item_unselected_background)
        }
    }


    fun setIcon(brandId: Int) {
        val abResources = DI.getABResources()

        mIcon.setImageDrawable(abResources.getDrawable(MobileBrandUtil.getBrandIcon(brandId)))
    }

    fun setListener(listener: View.OnClickListener) {
        mRootView.setOnClickListener(listener)
    }
}