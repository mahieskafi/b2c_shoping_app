package com.srp.ewayspanel.ui.view.shimmer

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.res.ResourcesCompat
import com.srp.eways.di.DIMain
import com.srp.eways.ui.view.button.ButtonElement
import com.srp.ewayspanel.R

class ErrorView : LinearLayout {

    private var mTitleText: AppCompatTextView
    private var mDescriptionText: AppCompatTextView
    private var mMainButton: ButtonElement
    private var mSecondButton: ButtonElement

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        LayoutInflater.from(context).inflate(R.layout.splash_error_view, this, true)

        var abResources = DIMain.getABResources()

        mTitleText = findViewById(R.id.title)
        mDescriptionText = findViewById(R.id.description)
        mMainButton = findViewById(R.id.main_button)
        mSecondButton = findViewById(R.id.second_button)

        var titleFont = ResourcesCompat.getFont(context, R.font.iran_yekan_extrabold)
        var descriptionFont = ResourcesCompat.getFont(context, R.font.iran_yekan)

        mTitleText.typeface = titleFont
        mTitleText.setTextSize(TypedValue.COMPLEX_UNIT_PX, abResources.getDimen(R.dimen.splash_error_view_title_font_size))
        mTitleText.setTextColor(abResources.getColor(R.color.splash_error_view_title_text_color))
        mTitleText.visibility = View.GONE

        mDescriptionText.typeface = descriptionFont
        mDescriptionText.setTextSize(TypedValue.COMPLEX_UNIT_PX, abResources.getDimen(R.dimen.splash_error_view_description_font_size))
        mDescriptionText.setTextColor(abResources.getColor(R.color.splash_error_view_description_text_color))

        with(mMainButton) {
            setTextTypeFace(ResourcesCompat.getFont(context, R.font.iran_yekan)!!)
            setTextSize(abResources.getDimenPixelSize(R.dimen.splash_error_view_main_button_text_font_size).toFloat())
            setTextColor(resources.getColor(R.color.white))
            setEnabledBackground(resources.getDrawable(R.drawable.button_element_default_background_enabled))
            setLoadingColorFilter(resources.getColor(R.color.bill_receipt_multiple_main_button_text_color))
            hasIcon(false)
            setEnable(true)
            visibility = View.GONE
        }

        with(mSecondButton) {
            setTextTypeFace(ResourcesCompat.getFont(context, R.font.iran_yekan)!!)
            setTextSize(abResources.getDimenPixelSize(R.dimen.splash_error_view_main_button_text_font_size).toFloat())
            setTextColor(resources.getColor(R.color.splash_error_view_second_button_text_color))
            setEnabledBackground(resources.getDrawable(R.drawable.bill_inquiry_detail_save_background_enabled))
            setLoadingColorFilter(resources.getColor(R.color.bill_receipt_multiple_main_button_text_color))
            hasIcon(false)
            setEnable(true)
            visibility = View.GONE
        }
    }

    public fun setTitle(title: String) {
        mTitleText.visibility = View.VISIBLE
        mTitleText.text = title
    }

    public fun setDescription(description: String) {
        mDescriptionText.text = description
    }

    public fun setMainButtonTitle(text: String) {
        mMainButton.visibility = View.VISIBLE
        mMainButton.setText(text)
    }

    public fun setSecondButtonTitle(text: String) {
        mSecondButton.visibility = View.VISIBLE
        mSecondButton.setText(text)
    }

    public fun setMainButtonClickListener(listener: OnClickListener) {
        mMainButton.setOnClickListener(listener)
    }

    public fun setSecondButtonClickListener(listener: OnClickListener) {
        mSecondButton.setOnClickListener(listener)
    }
}