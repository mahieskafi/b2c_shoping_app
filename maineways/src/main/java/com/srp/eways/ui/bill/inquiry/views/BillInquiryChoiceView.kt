package com.srp.eways.ui.bill.inquiry.views

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.widget.CompoundButton
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.res.ResourcesCompat
import com.srp.eways.R
import com.srp.eways.di.DIMain
import com.srp.eways.util.Utils
import com.yashoid.sequencelayout.SequenceLayout

/**
 * Created by ErfanG on 5/2/2020.
 */
class BillInquiryChoiceView : SequenceLayout {

    private val mPrice : AppCompatTextView
    private val mTitle : AppCompatTextView
    private val mRadio : AppCompatRadioButton
    private val mPriceSuffix : AppCompatTextView

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)


    init{

        LayoutInflater.from(context).inflate(R.layout.item_bill_inquiry_choice, this, true)


        mPrice = findViewById(R.id.price)
        mPriceSuffix = findViewById(R.id.price_suffix)
        mTitle = findViewById(R.id.title)
        mRadio = findViewById(R.id.radio)

        addSequences(R.xml.sequences_bill_inquiry_choice)


        setUpViews()

    }

    private fun setUpViews() {

        val resources = DIMain.getABResources()

        mPrice.setTextColor(resources.getColor(R.color.bill_inquiry_detail_choice_price_color))
        mPrice.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.bill_inquiry_detail_choice_price_size).toFloat())
        mPrice.typeface = ResourcesCompat.getFont(context, R.font.iran_yekan_medium)
        mPrice.text = Utils.toPersianPriceNumber(mPrice.text.toString())

        mPriceSuffix.setTextColor(resources.getColor(R.color.bill_inquiry_detail_choice_price_suffix_color))
        mPriceSuffix.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.bill_inquiry_detail_choice_price_suffix_size).toFloat())
        mPriceSuffix.typeface = ResourcesCompat.getFont(context, R.font.iran_yekan)
        mPriceSuffix.text = resources.getString(R.string.bill_inquiry_detail_choice_price_suffix)


        mTitle.setTextColor(resources.getColor(R.color.bill_inquiry_detail_choice_title_color))
        mTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.bill_inquiry_detail_choice_title_size).toFloat())
        mTitle.typeface = ResourcesCompat.getFont(context, R.font.iran_yekan_bold)


    }


    fun setPrice(price : Long){
        mPrice.text = Utils.toPersianPriceNumber(price)
    }

    fun setTitle(title : String){
        mTitle.text = Utils.toPersianNumber(title)
    }

    fun selectItem(isSelected: Boolean) {

        if(mRadio.isChecked != isSelected) {
            mRadio.isChecked = isSelected
        }
    }

    override fun setEnabled(enabled: Boolean) {

        mRadio.isClickable = enabled
        isClickable = enabled
        super.setEnabled(enabled)
    }

    fun setOnRadioListener(listener : CompoundButton.OnCheckedChangeListener) {

        mRadio.setOnCheckedChangeListener(listener)
    }


}