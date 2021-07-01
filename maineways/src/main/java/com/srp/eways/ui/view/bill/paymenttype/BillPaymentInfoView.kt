package com.srp.eways.ui.view.bill.paymenttype

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import com.srp.eways.R
import com.srp.eways.di.DIMain
import com.srp.eways.ui.bill.inquiry.views.KeyValuePairView
import com.srp.eways.ui.bill.paymenttype.BillInfo
import com.srp.eways.util.BillUtil
import com.srp.eways.util.Utils


/**
 * Created by ErfanG on 5/19/2020.
 */
class BillPaymentInfoView : CardView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private var mTotalPrice: Long = 0

    init {

        val resources = DIMain.getABResources()

//        setBackgroundColor(resources.getColor(R.color.bill_payment_type_info_background_color))
        radius = resources.getDimenPixelSize(R.dimen.bill_payment_type_info_corner_radius).toFloat()
        ViewCompat.setElevation(this, resources.getDimenPixelSize(R.dimen.bill_payment_type_info_elevation).toFloat())

        val displayMetrics = context.resources.displayMetrics
        val heightPixel = displayMetrics.heightPixels
        if (heightPixel<resources.getDimenPixelSize(R.dimen.bill_payment_type_info_cardview_contentpadding_bottom)){
            setContentPadding(0, 0, 0, 50)
        }
    }

    fun setBills(billList: ArrayList<BillInfo>) {

        if (billList.size == 0) {
            throw IllegalArgumentException("BillList must contain at least one element")
        }

        removeAllViews()

        val resources = DIMain.getABResources()

        if (billList.size == 1) {

            val keyValue = ArrayList<Pair<String, String>>()

            val billInquiryResponse = billList[0]

            keyValue.add(Pair(resources.getString(R.string.bill_inquiry_detail_info_bill_type_key), resources.getString(R.string.bill_inquiry_detail_info_bill_type_value)))
            keyValue.add(Pair(resources.getString(R.string.bill_inquiry_detail_info_id_key), billInquiryResponse.billId.toString()))
            keyValue.add(Pair(resources.getString(R.string.bill_inquiry_detail_info_pay_id_key), billInquiryResponse.billPayId.toString()))
            keyValue.add(Pair(resources.getString(R.string.bill_inquiry_detail_info_type_key), BillUtil.getServiceName(billInquiryResponse.billTypeId)))
            keyValue.add(Pair(resources.getString(R.string.bill_inquiry_detail_info_price_key),
                    Utils.toPersianPriceNumber(billInquiryResponse.billPrice) + " " + resources.getString(R.string.rial)))


            val info = KeyValuePairView(context).apply {
                val lp = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)

                lp.bottomMargin = resources.getDimenPixelSize(R.dimen.bill_payment_type_info_single_margin_bottom)
                layoutParams = lp
                setPairs(keyValue)
            }
            addView(info)

            mTotalPrice = billInquiryResponse.billPrice

        } else {

            var totalCount = 0
            var totalPrice:Long = 0

            val parent = LinearLayout(context).apply {
                orientation = LinearLayout.VERTICAL
                layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            }

            for (i in 1..8) {

                var count = 0
                var price:Long = 0

                for (item in billList) {

                    if (i == item.billTypeId) {
                        count++
                        price += item.billPrice
                    }
                }

                if (count > 0) {

                    totalCount += count
                    totalPrice += price

                    val title = AppCompatTextView(context).apply {
                        layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                        gravity = Gravity.RIGHT
                        setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.bill_payment_info_inner_title_size).toFloat())
                        setTextColor(resources.getColor(R.color.bill_patment_info_inner_title_color))
                        typeface = ResourcesCompat.getFont(context, R.font.iran_yekan)
                        text = "قبض " + BillUtil.getServiceName(i)
                    }

                    val countText = AppCompatTextView(context).apply {
                        layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                        gravity = Gravity.CENTER
                        setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.bill_payment_info_inner_count_size).toFloat())
                        setTextColor(resources.getColor(R.color.bill_patment_info_inner_count_color))
                        typeface = ResourcesCompat.getFont(context, R.font.iran_yekan)
                        text = Utils.toPersianNumber(count) + resources.getString(R.string.bill_payment_type_info_count_suffix)
                    }

                    val priceText = AppCompatTextView(context).apply {
                        layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                        gravity = Gravity.LEFT
                        setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.bill_payment_info_inner_price_size).toFloat())
                        setTextColor(resources.getColor(R.color.bill_patment_info_inner_price_color))
                        typeface = ResourcesCompat.getFont(context, R.font.iran_yekan)
                        text = Utils.toPersianPriceNumber(price) + " " + resources.getString(R.string.rial)
                    }


                    val midParent = LinearLayout(context).apply {
                        orientation = LinearLayout.HORIZONTAL
                        val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 3f)

                        lp.bottomMargin = resources.getDimenPixelSize(R.dimen.bill_payment_type_info_row_margin_bottom)
                        lp.rightMargin = resources.getDimenPixelSize(R.dimen.bill_payment_type_info_row_margin_side)
                        lp.leftMargin = resources.getDimenPixelSize(R.dimen.bill_payment_type_info_row_margin_side)

                        if (i == 1) {
                            lp.topMargin = resources.getDimenPixelSize(R.dimen.bill_payment_type_info_row_margin_top)
                        }

                        layoutParams = lp


                        addView(priceText)
                        addView(countText)
                        addView(title)
                    }

                    parent.addView(midParent)
                }
            }

            val divider = View(context).apply {
                val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, resources.getDimenPixelSize(R.dimen.bill_payment_type_info_divider_height))

                lp.leftMargin = resources.getDimenPixelSize(R.dimen.bill_payment_type_info_divider_margin)
                lp.rightMargin = resources.getDimenPixelSize(R.dimen.bill_payment_type_info_divider_margin)
                lp.bottomMargin = resources.getDimenPixelSize(R.dimen.bill_payment_type_info_divider_margin_bottom)

                layoutParams = lp

                setBackgroundColor(resources.getColor(R.color.bill_payment_type_info_divider_color))
            }
            parent.addView(divider)


            val countKey = AppCompatTextView(context).apply {
                layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                gravity = Gravity.RIGHT
                setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.bill_payment_info_total_count_title_size).toFloat())
                setTextColor(resources.getColor(R.color.bill_payment_info_total_count_title_color))
                typeface = ResourcesCompat.getFont(context, R.font.iran_yekan)
                text = resources.getString(R.string.bill_payment_type_info_total_count_title)
            }

            val countValue = AppCompatTextView(context).apply {
                layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                gravity = Gravity.LEFT
                setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.bill_payment_info_total_count_size).toFloat())
                setTextColor(resources.getColor(R.color.bill_payment_info_total_count_color))
                typeface = ResourcesCompat.getFont(context, R.font.iran_yekan)
                text = Utils.toPersianNumber(totalCount) + resources.getString(R.string.bill_payment_type_info_count_suffix)
            }

            val totalCountView = LinearLayout(context).apply {
                orientation = LinearLayout.HORIZONTAL
                val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 2f)

                lp.rightMargin = resources.getDimenPixelSize(R.dimen.bill_payment_type_info_bottom_row_margin_side)
                lp.leftMargin = resources.getDimenPixelSize(R.dimen.bill_payment_type_info_bottom_row_margin_side)
                lp.bottomMargin = resources.getDimenPixelSize(R.dimen.bill_payment_type_info_bottom_row_margin_bottom)

                layoutParams = lp

                addView(countValue)
                addView(countKey)
            }

            parent.addView(totalCountView)


            val priceKey = AppCompatTextView(context).apply {
                layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                gravity = Gravity.RIGHT
                setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.bill_payment_info_total_price_title_size).toFloat())
                setTextColor(resources.getColor(R.color.bill_payment_info_total_price_title_color))
                typeface = ResourcesCompat.getFont(context, R.font.iran_yekan_medium)
                text = resources.getString(R.string.bill_payment_type_info_total_price_title)
            }

            val priceValue = AppCompatTextView(context).apply {
                layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                gravity = Gravity.LEFT
                setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.bill_payment_info_total_price_size).toFloat())
                setTextColor(resources.getColor(R.color.bill_payment_info_total_price_color))
                typeface = ResourcesCompat.getFont(context, R.font.iran_yekan_bold)
                text = Utils.toPersianPriceNumber(totalPrice) + " " + resources.getString(R.string.rial)
            }

            val totalPriceView = LinearLayout(context).apply {
                orientation = LinearLayout.HORIZONTAL
                val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 2f)

                lp.rightMargin = resources.getDimenPixelSize(R.dimen.bill_payment_type_info_bottom_row_margin_side)
                lp.leftMargin = resources.getDimenPixelSize(R.dimen.bill_payment_type_info_bottom_row_margin_side)
                lp.bottomMargin = resources.getDimenPixelSize(R.dimen.bill_payment_type_info_bottom_row_margin_bottom)

                layoutParams = lp

                addView(priceValue)
                addView(priceKey)
            }

            parent.addView(totalPriceView)

            addView(parent)

            mTotalPrice = totalPrice

            requestLayout()
        }

    }

    fun getTotalPrice(): Long {
        return mTotalPrice
    }
}