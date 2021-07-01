package com.srp.eways.ui.view.bill.paymenttype

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.res.ResourcesCompat
import com.srp.eways.R
import com.srp.eways.di.DIMain
import com.srp.eways.model.deposit.Bank
import com.srp.eways.ui.bill.paymenttype.BankAdapter
import com.srp.eways.util.BillUtil
import ir.abmyapp.androidsdk.IABResources
import java.util.*

class BankItemView : LinearLayout {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private var mBankIcon: AppCompatImageView
    private var mBankName: AppCompatTextView

    private val mUnselectedTypeFace: Typeface
    private val mSelectedTypeFace: Typeface
    private val mFontSize: Float
    private val mTextColor: Int

    private var resources: IABResources

    private lateinit var mBank: Bank
    private var mListener: BankAdapter.BankItemClickListener? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.item_bank, this, true)

        mBankIcon = findViewById(R.id.bank_icon)
        mBankName = findViewById(R.id.bank_title)

        resources = DIMain.getABResources()

        mUnselectedTypeFace = ResourcesCompat.getFont(context, R.font.iran_yekan)!!
        mSelectedTypeFace = ResourcesCompat.getFont(context, R.font.iran_yekan_medium)!!

        mFontSize = resources.getDimenPixelSize(R.dimen.bill_payment_type_bank_item_font_size).toFloat()

        mTextColor = resources.getColor(R.color.bill_payment_type_bank_item_text_color)

        mBankName.setTextSize(TypedValue.COMPLEX_UNIT_PX, mFontSize)
        mBankName.setTextColor(mTextColor)

        val itemLP = LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        itemLP.setMargins(resources.getDimenPixelSize(R.dimen.bill_payment_type_bank_item_margin_sides),
                0, resources.getDimenPixelSize(R.dimen.bill_payment_type_bank_item_margin_sides), 0)
        layoutParams = itemLP

        setOnClickListener {
            mListener!!.onBankClickListener(mBank.gId)
        }
    }

    fun setBank(bank: Bank) {
        mBank = bank

        if (!bank.selcted) {
            setUnselectedIcon(bank.gId)
            setUnselectedText(bank.gId)
        } else {
            setSelectedIcon(bank.gId)
            setSelectedText(bank.gId)
        }
    }

    fun setBankClickListener(listener: BankAdapter.BankItemClickListener) {
        mListener = listener
    }

    private fun setUnselectedIcon(gId: Int) {

        mBankIcon.scaleX = 0.9f
        mBankIcon.scaleY = 0.9f
        mBankIcon.alpha = 0.6f

        mBankIcon.setImageDrawable(resources.getDrawable(BillUtil.getBankIcon(gId)))
    }

    private fun setUnselectedText(gId: Int) {
        mBankName.alpha = 0.6f
        mBankName.typeface = mUnselectedTypeFace

        mBankName.text = resources.getString(BillUtil.getBankName(gId))
    }


    private fun setSelectedIcon(gId: Int) {

        mBankIcon.scaleX = 1f
        mBankIcon.scaleY = 1f
        mBankIcon.alpha = 1f

        mBankIcon.setImageDrawable(resources.getDrawable(BillUtil.getBankIcon(gId)))
    }

    private fun setSelectedText(gId: Int) {
        mBankName.alpha = 1f
        mBankName.typeface = mSelectedTypeFace

        mBankName.text = resources.getString(BillUtil.getBankName(gId))
    }
}