package com.srp.ewayspanel.ui.view.landing

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.res.ResourcesCompat
import com.srp.ewayspanel.R
import com.srp.ewayspanel.di.DI
import com.srp.eways.ui.view.ShadowLayout

class AppServiceItemView : FrameLayout {

    private var mIcon: AppCompatImageView
    private var mTitle: AppCompatTextView
    private var mInactiveDescription: AppCompatTextView

    private var mRootView: LinearLayout

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        LayoutInflater.from(context).inflate(R.layout.item_landing_service, this, true)

        mIcon = findViewById(R.id.icon)
        mTitle = findViewById(R.id.title)
        mRootView = findViewById(R.id.root_view)
        mInactiveDescription = findViewById(R.id.tv_inactive_description)

        val abResources = DI.getABResources()

        var iconLayoutParams = LayoutParams(abResources.getDimenPixelSize(R.dimen.landing_service_item_icon_width),
                abResources.getDimenPixelSize(R.dimen.landing_service_item_icon_height)
        )

        mIcon.layoutParams = iconLayoutParams

        mTitle.typeface = ResourcesCompat.getFont(context, R.font.iran_yekan)
        mTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, abResources.getDimen(R.dimen.landing_service_item_title_text_size))

        mInactiveDescription.typeface = ResourcesCompat.getFont(context, R.font.iran_yekan)
        mInactiveDescription.setTextSize(TypedValue.COMPLEX_UNIT_PX, abResources.getDimen(R.dimen.landing_service_item_inactive_description_text_size))

        mInactiveDescription.visibility = View.GONE

        var rootViewLayoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        rootViewLayoutParams.setMargins(
                0,
                abResources.getDimenPixelSize(R.dimen.landing_service_item_icon_margin_top),
               0,
                abResources.getDimenPixelSize(R.dimen.landing_service_item_icon_margin_bottom)
        )

        mRootView.layoutParams = rootViewLayoutParams
    }

    fun setTitle(title: String) {
        mTitle.text = title
    }

    fun setActive(isActivated: Boolean) {
        val abResources = DI.getABResources()
        if (isActivated) {
            mIcon.background = abResources.getDrawable(R.drawable.landing_services_item_background)
            mTitle.setTextColor(abResources.getColor(R.color.landing_service_item_title_active_text_color))
            mInactiveDescription.visibility=View.INVISIBLE
            mRootView.isEnabled = true
        } else {
//            mShadowLayout.setIsShadowed(false)

            mIcon.background = abResources.getDrawable(R.drawable.ic_landing_services_item_inactive_background)
            mTitle.setTextColor(abResources.getColor(R.color.landing_service_item_title_inactive_text_color))
            mInactiveDescription.visibility = View.VISIBLE
            mInactiveDescription.text = abResources.getString(R.string.landing_service_item_inactive_description_text)
            mRootView.isEnabled = false
        }
    }

    fun setItemSelected(isSelected: Boolean) {
        val abResources = DI.getABResources()
        if (isSelected) {
            mIcon.background = abResources.getDrawable(R.drawable.landing_services_item_selected_background)
            mTitle.setTextColor(abResources.getColor(R.color.landing_service_item_title_selected_text_color))

        } else {
           setActive(mRootView.isEnabled)
        }
    }

    fun setIcon(serviceCode: Int, isActivate: Boolean) {
        if (isActivate) {
            //TODO getICon
        } else {

        }
    }

    fun setIcon(@DrawableRes iconRes: Int) {
        val abResources = DI.getABResources()

        mIcon.setImageDrawable(abResources.getDrawable(iconRes))
    }

    fun setListener(listener: View.OnClickListener) {
        mRootView.setOnClickListener(listener)
    }
}