package com.srp.eways.ui.bill.receipt

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.res.ResourcesCompat
import com.srp.eways.R
import com.srp.eways.di.DIMain
import com.srp.eways.util.BillUtil
import com.srp.eways.util.Utils
import com.yashoid.sequencelayout.SequenceLayout

/**
 * Created by ErfanG on 5/10/2020.
 */
class MultipleBillReceiptItemView : SequenceLayout {


    private val mIcon : AppCompatImageView
    private val mName : AppCompatTextView
    private val mPrice : AppCompatTextView
    private val mPriceSuffix : AppCompatTextView
    private val mBillId : AppCompatTextView
    private val mBillIdSuffix : AppCompatTextView
    private val mPayId : AppCompatTextView
    private val mPayIdSuffix : AppCompatTextView


    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {

        LayoutInflater.from(context).inflate(R.layout.item_bill_receipt_multiple, this, true)

        mIcon = findViewById(R.id.status_icon)
        mName= findViewById(R.id.bill_name)
        mPrice = findViewById(R.id.bill_price)
        mPriceSuffix = findViewById(R.id.bill_price_suffix)
        mBillId = findViewById(R.id.bill_id)
        mBillIdSuffix = findViewById(R.id.bill_id_suffix)
        mPayId = findViewById(R.id.pay_id)
        mPayIdSuffix = findViewById(R.id.pay_id_suffix)

        addSequences(R.xml.sequences_bill_receipt_multiple_item)


        val resources = DIMain.getABResources()


        val lightFont = ResourcesCompat.getFont(context, R.font.iran_yekan_light)
        val mediumFont = ResourcesCompat.getFont(context, R.font.iran_yekan_medium)
        val boldFont = ResourcesCompat.getFont(context, R.font.iran_yekan_bold)


        mName.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.bill_receipt_multiple_item_name_size).toFloat())
        mName.setTextColor(resources.getColor(R.color.bill_receipt_multiple_item_name_color))
        mName.typeface = boldFont

        mPrice.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.bill_receipt_multiple_item_price_size).toFloat())
        mPrice.setTextColor(resources.getColor(R.color.bill_receipt_multiple_item_price_color))
        mPrice.typeface = mediumFont

        mPriceSuffix.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.bill_receipt_multiple_item_price_suffix_size).toFloat())
        mPriceSuffix.setTextColor(resources.getColor(R.color.bill_receipt_multiple_item_price_suffix_color))
        mPriceSuffix.typeface = lightFont
        mPriceSuffix.text = resources.getString(R.string.rial)


        mBillId.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.bill_receipt_multiple_item_bill_id_size).toFloat())
        mBillId.setTextColor(resources.getColor(R.color.bill_receipt_multiple_item_bill_id_color))
        mBillId.typeface = mediumFont

        mBillIdSuffix.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.bill_receipt_multiple_item_bill_id_suffix_size).toFloat())
        mBillIdSuffix.setTextColor(resources.getColor(R.color.bill_receipt_multiple_item_bill_id_suffix_color))
        mBillIdSuffix.typeface = lightFont
        mBillIdSuffix.text = resources.getString(R.string.bill_receipt_multiple_item_bill_id_suffix)


        mPayId.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.bill_receipt_multiple_item_pay_id_size).toFloat())
        mPayId.setTextColor(resources.getColor(R.color.bill_receipt_multiple_item_pay_id_color))
        mPayId.typeface = mediumFont

        mPayIdSuffix.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.bill_receipt_multiple_item_pay_id_suffix_size).toFloat())
        mPayIdSuffix.setTextColor(resources.getColor(R.color.bill_receipt_multiple_item_pay_id_suffix_color))
        mPayIdSuffix.typeface = lightFont
        mPayIdSuffix.text = resources.getString(R.string.bill_receipt_multiple_item_pay_id_suffix)


    }

    fun setBillName(name : String){

        mName.text = Utils.toPersianNumber(name)
    }

    fun setBillPrice(price : Long){

        mPrice.text = Utils.toPersianPriceNumber(price)
    }

    fun setBillId(billId : String){

        mBillId.text = Utils.toPersianNumber(billId)
    }

    fun setPayId(payId : String){

        mPayId.text = Utils.toPersianNumber(payId)
    }

    fun setStatus(status : Int){
        mIcon.setImageDrawable(BillUtil.getBillStatusIcon(status))
    }

}