package com.srp.eways.ui.bill.inquiry.views

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.CompoundButton
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import com.srp.eways.R
import com.srp.eways.di.DIMain
import com.srp.eways.model.bill.inquiry.BillInquiryResponse
import com.srp.eways.model.bill.inquiry.TermBill
import com.srp.eways.ui.bill.inquiry.BillType
import com.srp.eways.ui.view.button.ButtonElement

/**
 * Created by ErfanG on 5/1/2020.
 */
class BillInquiryDetailView : ConstraintLayout{

    interface OnBillChoiceClickListener{

        fun onChoiceClicked(termBill : TermBill)
    }

    private val mTitle : AppCompatTextView
    private val mChoicesContainer : LinearLayout

    private val mChoices = ArrayList<TermBill>()
    private val mChoicesView = ArrayList<BillInquiryChoiceView>()

    private val mSelectedView : BillInquiryChoiceView? = null

    private lateinit var mListener : OnBillChoiceClickListener


    constructor(context: Context?) : super(context!!)
    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context!!, attrs, defStyleAttr)


    init{

        LayoutInflater.from(context).inflate(R.layout.item_bill_inquiry_detail, this, true)

        mTitle = findViewById(R.id.title)

        mChoicesContainer = findViewById(R.id.choice_container)

        with(mTitle){
            val resources = DIMain.getABResources()

            setTextColor(resources.getColor(R.color.bill_inquiry_detail_info_title_color))
            setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.bill_inquiry_detail_info_title_size).toFloat())
            typeface = ResourcesCompat.getFont(context, R.font.iran_yekan)
        }

    }


    fun setupInfo(billInquiryResponse: BillInquiryResponse, billType: BillType, lastValue: TermBill?) {

        val resources = DIMain.getABResources()

        val keyValue = ArrayList<Pair<String, String>>()

        mChoices.clear()
        mChoicesView.clear()
        mChoicesContainer.removeAllViews()

        val lastTerm = BillInquiryChoiceView(context).apply {
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        }

        if(billInquiryResponse.lastTermBill != null){

            lastTerm.setTitle(resources.getString(R.string.bill_inquiry_detail_last_term_title))
            lastTerm.setPrice(billInquiryResponse.lastTermBill.price!!)

            mChoices.add(billInquiryResponse.lastTermBill)
            mChoicesView.add(lastTerm)
            mChoicesContainer.addView(lastTerm)

            if(lastValue != null && billInquiryResponse.lastTermBill == lastValue){
                lastTerm.isSelected = true
            }

        }

        if(billInquiryResponse.midTermBill != null){

            val midTerm = BillInquiryChoiceView(context).apply {
                layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            }

            midTerm.setTitle(resources.getString(R.string.bill_inquiry_detail_mid_term))
            midTerm.setPrice(billInquiryResponse.midTermBill.price!!)

            mChoices.add(billInquiryResponse.midTermBill)
            mChoicesView.add(midTerm)
            mChoicesContainer.addView(midTerm)

            if(lastValue != null && billInquiryResponse.midTermBill == lastValue){
                midTerm.isSelected = true
            }

        }

        applyListener()

    }

    fun setRadioListeners(listener : OnBillChoiceClickListener){

        mListener = listener

        applyListener()

    }

    private fun applyListener(){
        mChoicesView.forEachIndexed { index, choice ->

            val selectLogic : () -> Unit = {
                if(mSelectedView == null || (mSelectedView != choice)){
                    for(allChoice in mChoicesView){
                        allChoice.selectItem(false)
                    }

                    choice.selectItem(true)
                    mListener.onChoiceClicked(mChoices[index])
                }
                else{
                    //selected before
                }
            }

            choice.setOnClickListener{

                selectLogic()
            }

            choice.setOnRadioListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->

                if(isChecked) {
                    selectLogic()
                }
            })
        }
    }

    fun setTitleVisibility(visibility : Boolean){

        if(visibility){
            mTitle.visibility = View.VISIBLE
        }
        else {
            mTitle.visibility = View.GONE
        }
    }

    private fun setChoicesEnable(enable : Boolean){

        mChoicesView.forEach { choice ->
            choice.isEnabled = enable
        }

    }

    fun setDetailEnable(enable : Boolean){

        setChoicesEnable(enable)
    }


}