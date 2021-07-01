package com.srp.ewayspanel.ui.transaction.order.list

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.srp.ewayspanel.R
import com.srp.ewayspanel.di.DI
import com.srp.eways.util.Utils
import ir.abmyapp.androidsdk.IABResources

/**
 * Created by Eskafi on 2/2/2020.
 */
class OrderItemView : FrameLayout {

    lateinit var mResult: ImageView
    lateinit var mType: TextView
    lateinit var mPaymentNumber: TextView
    lateinit var mDetailTransaction: ImageView
    lateinit var mDateIcon: ImageView
    lateinit var mDate: TextView
    lateinit var mTimeIcon: ImageView
    lateinit var mTime: TextView
    lateinit var mValue: TextView
    lateinit var mValueSuffix: TextView

//    lateinit var mFailedIcon: Drawable
//    lateinit var mUnknownIcon: Drawable
//    lateinit var mOkIcon: Drawable

    var mDetailTransactionIconPadding: Int = 0

    constructor(context: Context) : super(context) {
        initilize(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initilize(context, attrs)
    }


    fun initilize(context: Context, attrs: AttributeSet?) {

        clipChildren = false
        clipToPadding = false
        super.setClipChildren(false)
        super.setClipToPadding(false)
        LayoutInflater.from(context).inflate(R.layout.item_order_transaction, this, true)

        mResult = findViewById(R.id.transaction_result)
        mType = findViewById(R.id.transaction_type)
        mPaymentNumber = findViewById(R.id.payment_number)
        mDetailTransaction = findViewById(R.id.transaction_detail)
        mDateIcon = findViewById(R.id.transaction_date_icon)
        mDate = findViewById(R.id.transaction_date)
        mTimeIcon = findViewById(R.id.transaction_time_icon)
        mTime = findViewById(R.id.transaction_time)
        mValue = findViewById(R.id.transaction_value)
        mValueSuffix = findViewById(R.id.transaction_value_suffix)

        //region AB attrs

        val AB = DI.getABResources()

        mDetailTransactionIconPadding = AB.getDimenPixelSize(R.dimen.order_transaction_item_open_detail_icon_padding)

//        //Result icon
//        mFailedIcon = AB.getDrawable(R.drawable.transaction_item_result_failed)
//        mUnknownIcon = AB.getDrawable(R.drawable.transaction_item_result_unknown)
//        mOkIcon = AB.getDrawable(R.drawable.transaction_item_result_ok)
//        mResult.setImageDrawable(mOkIcon)

        //Transaction_type
        mType.setTextColor(AB.getColor(R.color.transaction_charge_item_type_color))
        mType.setTextSize(TypedValue.COMPLEX_UNIT_PX, AB.getDimenPixelSize(R.dimen.transaction_charge_item_type_size).toFloat())
        mType.gravity = Gravity.TOP

        //Payment number
        mPaymentNumber.setTextColor(AB.getColor(R.color.transaction_charge_item_phone_color))
        mPaymentNumber.setTextSize(TypedValue.COMPLEX_UNIT_PX, AB.getDimenPixelSize(R.dimen.transaction_charge_item_phone_size).toFloat())

        mDetailTransaction.setImageDrawable(AB.getDrawable(R.drawable.ic_order_transaction_open_detail_icon))
        mDetailTransaction.setPadding(mDetailTransactionIconPadding, mDetailTransactionIconPadding, mDetailTransactionIconPadding, mDetailTransactionIconPadding)
        //Value Suffix
        mValueSuffix.setTextColor(AB.getColor(R.color.transaction_charge_item_value_suffix_color))
        mValueSuffix.setTextSize(TypedValue.COMPLEX_UNIT_PX, AB.getDimenPixelSize(R.dimen.transaction_charge_item_value_suffix_size).toFloat())
        mValueSuffix.setText(AB.getString(R.string.transaction_charge_item_value_suffix_string))

        //Value
        mValue.setTextColor(AB.getColor(R.color.transaction_charge_item_value_color))
        mValue.setTextSize(TypedValue.COMPLEX_UNIT_PX, AB.getDimenPixelSize(R.dimen.transaction_charge_item_value_size).toFloat())

        //DateIcon
        mDateIcon.setImageDrawable(AB.getDrawable(R.drawable.transaction_item_date_icon))

        //Date
        mDate.setTextColor(AB.getColor(R.color.transaction_charge_item_date_color))
        mDate.setTextSize(TypedValue.COMPLEX_UNIT_PX, AB.getDimenPixelSize(R.dimen.transaction_charge_item_date_size).toFloat())

        //TimeIcon
        mTimeIcon.setImageDrawable(AB.getDrawable(R.drawable.transaction_item_time_icon))

        //Time
        mTime.setTextColor(AB.getColor(R.color.transaction_charge_item_time_color))
        mTime.setTextSize(TypedValue.COMPLEX_UNIT_PX, AB.getDimenPixelSize(R.dimen.transaction_charge_item_time_size).toFloat())

        //endregion


        setAttrs(attrs)

    }

    private fun setAttrs(attrs: AttributeSet?) {

        if (attrs != null) {

            val attrArray = context.obtainStyledAttributes(attrs, R.styleable.TransactionItem, 0, 0)

            val mainTypefaceResourceId = attrArray.getResourceId(R.styleable.TransactionItem_firstFont, 0)
            val secondTypefaceResourceId = attrArray.getResourceId(R.styleable.TransactionItem_secondFont, 0)


            val mainTypeface = ResourcesCompat.getFont(context, mainTypefaceResourceId)
            val secondTypeface = ResourcesCompat.getFont(context, secondTypefaceResourceId)

            mValue.typeface = mainTypeface
            mType.typeface = mainTypeface

            mValueSuffix.typeface = secondTypeface
            mDate.typeface = secondTypeface
            mTime.typeface = secondTypeface
            mPaymentNumber.typeface = secondTypeface

            attrArray.recycle()
        } else {
            val first = ResourcesCompat.getFont(context, R.font.iran_yekan)
            val second = ResourcesCompat.getFont(context, R.font.iran_yekan_light)

            mValue.setTypeface(first)
            mType.setTypeface(first)

            mValueSuffix.setTypeface(second)
            mDate.setTypeface(second)
            mTime.setTypeface(second)
            mPaymentNumber.setTypeface(second)
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
    }


    fun setPaymentNumber(phoneNumber: String) {
        mPaymentNumber.setText(Utils.toPersianNumber(phoneNumber))
    }

    fun setTransactionValue(value: Long) {
        mValue.setText(Utils.toPersianNumber(String.format("%,d", value)))
    }

    fun setTime(value: String) {
        mTime.setText(Utils.toPersianNumber(value))
    }

    fun setDate(value: String) {
        mDate.setText(Utils.toPersianNumber(value))
    }

    fun setType(type: String) {
        mType.setText(Utils.toPersianNumber(type))
    }


    fun setResult(orderStatus: Int) {
        val abResource: IABResources = DI.getABResources()

        mType.setTextColor(abResource.getColor(OrderStatus.getResultColor(orderStatus)))
        mResult.setImageDrawable(abResource.getDrawable(OrderStatus.getResultDrawable(orderStatus)))
    }
}
