package com.srp.eways.ui.bill.inquiry.views

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.res.ResourcesCompat
import com.srp.eways.R
import com.srp.eways.di.DIMain
import com.srp.eways.util.Utils

/**
 * Created by ErfanG on 5/2/2020.
 */
class KeyValuePairView : LinearLayout {

    private lateinit var mPairs : ArrayList<Pair<String, String>>
    private val mPairsView = ArrayList<Pair<TextView, TextView>>()

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        orientation = VERTICAL
    }


    fun setPairs(pairs : ArrayList<Pair<String, String>>){

        mPairs = pairs

        addPairs()
    }

    private fun addPairs(){

        val resources = DIMain.getABResources()

        removeAllViews()
        mPairsView.clear()

        for(pair in mPairs){

            val keyView = createKeyView()
            val valueView = createValueView()

            keyView.text = Utils.toPersianNumber(pair.first)
            valueView.text = Utils.toPersianNumber(pair.second)

            mPairsView.add(Pair(keyView, valueView))

            val innerLinear = LinearLayout(context)
            innerLinear.orientation = HORIZONTAL
            innerLinear.weightSum = 2f

            val parentParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            parentParams.leftMargin = resources.getDimenPixelSize(R.dimen.bill_inquiry_detail_info_margin_side)
            parentParams.rightMargin = resources.getDimenPixelSize(R.dimen.bill_inquiry_detail_info_margin_side)
            parentParams.topMargin = resources.getDimenPixelSize(R.dimen.bill_inquiry_detail_info_margin_side)

            innerLinear.layoutParams = parentParams

            val childParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1f)
            innerLinear.addView(valueView, childParams)
            innerLinear.addView(keyView, childParams)

            addView(innerLinear)
        }

    }

    private fun createValueView(): AppCompatTextView {

        val value = AppCompatTextView(context)

        val resources = DIMain.getABResources()

        with(value){
            gravity = Gravity.LEFT

            setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.bill_inquiry_detail_info_value_size).toFloat())
            setTextColor(resources.getColor(R.color.bill_inquiry_detail_info_value_color))
            typeface = ResourcesCompat.getFont(context, R.font.iran_yekan)
        }


        return value
    }

    private fun createKeyView(): AppCompatTextView{

        val key = AppCompatTextView(context)
        val resources = DIMain.getABResources()

        with(key){

            gravity = Gravity.RIGHT
            setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.bill_inquiry_detail_info_key_size).toFloat())
            setTextColor(resources.getColor(R.color.bill_inquiry_detail_info_key_color))
            typeface = ResourcesCompat.getFont(context, R.font.iran_yekan)
        }


        return key
    }


}