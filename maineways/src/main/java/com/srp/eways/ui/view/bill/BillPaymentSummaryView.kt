package com.srp.eways.ui.view.bill

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.cardview.widget.CardView
import androidx.core.content.res.ResourcesCompat
import com.srp.eways.R
import com.srp.eways.di.DIMain
import com.srp.eways.ui.view.button.ButtonElement
import com.srp.eways.util.Utils

class BillPaymentSummaryView : CardView {

    interface PaymentClickListener {
        fun onPaymentClicked();
    }

    private val mBillCountTitle: AppCompatTextView
    private val mBillCountValueText: AppCompatTextView
    private val mBillPriceTitle: AppCompatTextView
    private val mBillPriceValueText: AppCompatTextView
    private val mPaymentButton: ButtonElement
    private val mBillCountValueDescription: AppCompatTextView
    private val mBillPriceValueDescription: AppCompatTextView

    private val mBillPriceContainer: LinearLayout
    private val mBillCountContainer: LinearLayout
    private val mBillValuesContainer: RelativeLayout

    private var mClickListener: PaymentClickListener? = null

    constructor (context: Context) : super(context)

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

    constructor(context: Context, attributeSet: AttributeSet, defStyle: Int) : super(context, attributeSet, defStyle)

    init {
        LayoutInflater.from(context).inflate(R.layout.view_bill_payment_summary, this, true)

        val abResources = DIMain.getABResources()

        cardElevation = abResources.getDimen(R.dimen.bill_payment_summary_view_elevation)

        mBillCountTitle = findViewById(R.id.key_billcount)
        mBillCountValueText = findViewById(R.id.value_billcount)
        mBillPriceTitle = findViewById(R.id.key_billprice)
        mBillPriceValueText = findViewById(R.id.value_billprice)
        mPaymentButton = findViewById(R.id.btn_payment)
        mBillCountValueDescription = findViewById(R.id.value_description_billcount)
        mBillPriceValueDescription = findViewById(R.id.value_description_billprice)

        mBillCountContainer = findViewById(R.id.count_container)
        mBillPriceContainer = findViewById(R.id.price_container)
        mBillValuesContainer = findViewById(R.id.values_container)

        val paddingSides = abResources.getDimenPixelSize(R.dimen.bill_payment_summary_padding_sides)
        val paddingTop = abResources.getDimenPixelSize(R.dimen.bill_payment_summary_padding_top)
        val paddingBottom = abResources.getDimenPixelSize(R.dimen.bill_payment_summary_padding_bottom)

        setContentPadding(paddingSides, paddingTop, paddingSides, paddingBottom)

        val valuesContainerLayoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        valuesContainerLayoutParams.marginEnd = abResources.getDimenPixelSize(R.dimen.bill_payment_summary_values_container_margin_sides)
        valuesContainerLayoutParams.marginStart = abResources.getDimenPixelSize(R.dimen.bill_payment_summary_values_container_margin_sides)
        valuesContainerLayoutParams.bottomMargin = abResources.getDimenPixelSize(R.dimen.bill_payment_summary_values_container_margin_sides)

        mBillValuesContainer.layoutParams = valuesContainerLayoutParams

        val titleTypeface = ResourcesCompat.getFont(context, R.font.iran_yekan)
        val valueTypeface = ResourcesCompat.getFont(context, R.font.iran_yekan_bold)

        mBillCountTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, abResources.getDimen(R.dimen.bill_payment_summary_titles_text_size))
        mBillCountTitle.typeface = titleTypeface
        mBillCountTitle.text = abResources.getString(R.string.bill_payment_summary_bill_count_title)

        mBillPriceTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, abResources.getDimen(R.dimen.bill_payment_summary_titles_text_size))
        mBillPriceTitle.typeface = titleTypeface
        mBillPriceTitle.text = abResources.getString(R.string.bill_payment_summary_bill_price_title)

        mBillCountValueDescription.setTextSize(TypedValue.COMPLEX_UNIT_PX, abResources.getDimen(R.dimen.bill_payment_summary_values_description_text_size))
        mBillCountValueDescription.typeface = titleTypeface
        mBillCountValueDescription.setTextColor(abResources.getColor(R.color.bill_payment_summary_values_enabled_text_color))
        mBillCountValueDescription.text = abResources.getString(R.string.bill_payment_summary_bill_count_value_description)

        mBillPriceValueDescription.setTextSize(TypedValue.COMPLEX_UNIT_PX, abResources.getDimen(R.dimen.bill_payment_summary_values_description_text_size))
        mBillPriceValueDescription.typeface = titleTypeface
        mBillPriceValueDescription.setTextColor(abResources.getColor(R.color.bill_payment_summary_values_enabled_text_color))
        mBillPriceValueDescription.text = abResources.getString(R.string.rial)

        mBillPriceValueText.setTextSize(TypedValue.COMPLEX_UNIT_PX, abResources.getDimen(R.dimen.bill_payment_summary_values_text_size))
        mBillPriceValueText.typeface = valueTypeface

        mBillCountValueText.setTextSize(TypedValue.COMPLEX_UNIT_PX, abResources.getDimen(R.dimen.bill_payment_summary_values_text_size))
        mBillCountValueText.typeface = valueTypeface


        setupButton()
        setViewEnabled(false)
    }

    private fun setupButton() {
        val abResources = DIMain.getABResources()

        mPaymentButton.setDisableBackground(abResources.getDrawable(R.drawable.login_button_background_disabled))
        mPaymentButton.setEnabledBackground(abResources.getDrawable(R.drawable.login_button_background_enabled))
        mPaymentButton.setTextTypeFace(ResourcesCompat.getFont(context, R.font.iran_yekan)!!)
        mPaymentButton.setTextColor(abResources.getColor(R.color.bill_payment_summary_button_text_color))
        mPaymentButton.setTextSize(abResources.getDimen(R.dimen.bill_payment_summary_button_text_size))
        mPaymentButton.setText(abResources.getString(R.string.bill_payment_summary_button_text))
        mPaymentButton.setLoadingVisibility(View.GONE)

        mPaymentButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                mClickListener?.onPaymentClicked()
            }

        })
    }

    fun setViewEnabled(isEnabled: Boolean) {
        val abResources = DIMain.getABResources()

        if (isEnabled) {
            mPaymentButton.setEnable(true)
            mBillCountTitle.setTextColor(abResources.getColor(R.color.bill_payment_summary_values_enabled_text_color))
            mBillPriceTitle.setTextColor(abResources.getColor(R.color.bill_payment_summary_values_enabled_text_color))
            mBillCountValueText.setTextColor(abResources.getColor(R.color.bill_payment_summary_values_enabled_text_color))
            mBillPriceValueText.setTextColor(abResources.getColor(R.color.bill_payment_summary_values_enabled_text_color))

            mBillPriceValueDescription.visibility = View.VISIBLE
            mBillCountValueDescription.visibility = View.VISIBLE

            mBillPriceContainer.gravity = Gravity.START
            mBillCountContainer.gravity = Gravity.END
        } else {
            mPaymentButton.setEnable(false)
            mBillCountTitle.setTextColor(abResources.getColor(R.color.bill_payment_summary_values_disabled_text_color))
            mBillPriceTitle.setTextColor(abResources.getColor(R.color.bill_payment_summary_values_disabled_text_color))
            mBillCountValueText.setTextColor(abResources.getColor(R.color.bill_payment_summary_values_disabled_text_color))
            mBillPriceValueText.setTextColor(abResources.getColor(R.color.bill_payment_summary_values_disabled_text_color))

            mBillCountValueText.text = "ـــــــ"
            mBillPriceValueText.text = "ـــــــ"

            mBillPriceValueDescription.visibility = View.GONE
            mBillCountValueDescription.visibility = View.GONE

            mBillPriceContainer.gravity = Gravity.CENTER_HORIZONTAL
            mBillCountContainer.gravity = Gravity.CENTER_HORIZONTAL
        }
    }

    fun setCount(count: Int) {
        mBillCountValueText.text = Utils.toPersianNumber(count)
    }

    fun setPrice(price: Long) {
        mBillPriceValueText.text = Utils.toPersianPriceNumber(price)
    }

    fun setButtonLoadingVisibility(isLoading: Boolean) {
        if (isLoading) {
            mPaymentButton.setLoadingVisibility(View.VISIBLE)
        } else {
            mPaymentButton.setLoadingVisibility(View.GONE)
        }
    }

    fun setClickListener(listener: PaymentClickListener) {
        mClickListener = listener
    }
}