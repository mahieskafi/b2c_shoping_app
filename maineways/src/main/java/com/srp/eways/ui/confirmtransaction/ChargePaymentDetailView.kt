package com.srp.eways.ui.confirmtransaction

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.srp.eways.R
import com.srp.eways.di.DIMain
import com.srp.eways.ui.view.button.ButtonElement
import com.srp.eways.ui.view.receipt.ReceiptListView
import ir.abmyapp.androidsdk.IABResources

class ChargePaymentDetailView : LinearLayout {

    private lateinit var mClickListener: ConfirmTransactionClickListener
    private val mTitleText: TextView
    private val mReceiptList: ReceiptListView
    private val mPayButton: ButtonElement
    private val mPaidPriceText: TextView
    private val mPaidPriceValueText: TextView
    private val mPaidPriceValueDescriptionText: TextView
    private val mPaidPriceIcon: ImageView

    private val AB: IABResources = DIMain.getABResources()

    private lateinit var mConfirmTransaction: ConfirmTransaction


    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)


    init {

        LayoutInflater.from(context).inflate(R.layout.charge_confirm_payment_detail, this, true)


        mTitleText = findViewById(com.srp.eways.R.id.title)
        mReceiptList = findViewById(com.srp.eways.R.id.receipt_list_view)

        mPaidPriceText = findViewById(com.srp.eways.R.id.txt_title)
        mPaidPriceValueText = findViewById(com.srp.eways.R.id.txt_value)
        mPaidPriceValueDescriptionText = findViewById(com.srp.eways.R.id.value_description)
        mPaidPriceIcon = findViewById(com.srp.eways.R.id.iv_icon)

        mPayButton = findViewById(com.srp.eways.R.id.b_payment)
    }

    fun setupTransaction(confirmTransaction: ConfirmTransaction) {
        mConfirmTransaction = confirmTransaction

        setupView()
    }

    private fun setupView() {
        mTitleText.setTextColor(AB.getColor(com.srp.eways.R.color.confirm_transaction_text_color))
        mTitleText.text = if (mConfirmTransaction.getTitle() == null) AB.getString(R.string.confirm_transaction_title) else mConfirmTransaction.getTitle()
        mReceiptList.setTextColor(AB.getColor(com.srp.eways.R.color.confirm_transaction_text_color))
        mReceiptList.setTextSize(AB.getDimenPixelSize(R.dimen.confirm_transaction_values_text_size).toFloat())
        mReceiptList.setReceiptItem(mConfirmTransaction.getReceiptItems())
        if (mConfirmTransaction.getIcon() != null) {
            mPaidPriceIcon.visibility = View.VISIBLE
            mPaidPriceIcon.setImageDrawable(mConfirmTransaction.getIcon())
        }
        setupPaidPriceText()
        setupPaidPriceValue()
        setupPaidPriceValueDescription()
        setupPaymentButton()
    }

    private fun setupPaidPriceText() {
        mPaidPriceText.setTextColor(AB.getColor(com.srp.eways.R.color.confirm_transaction_text_color))
        mPaidPriceText.setTextSize(TypedValue.COMPLEX_UNIT_PX, AB.getDimenPixelSize(R.dimen.confirm_transaction_title_text_size).toFloat())
        val titleTypeface = ResourcesCompat.getFont(context, com.srp.eways.R.font.iran_yekan_medium)
        if (titleTypeface != null) {
            mPaidPriceText.typeface = titleTypeface
        }
        mPaidPriceText.text = if (mConfirmTransaction.getTitlePaidPrice() == null) AB.getString(R.string.confirm_transaction_paid_price_text) else mConfirmTransaction.getTitlePaidPrice()
    }

    private fun setupPaidPriceValue() {
        mPaidPriceValueText.setTextColor(AB.getColor(com.srp.eways.R.color.confirm_transaction_paid_price_text_color))
        mPaidPriceValueText.setTextSize(TypedValue.COMPLEX_UNIT_PX, AB.getDimenPixelSize(R.dimen.confirm_transaction_values_text_size).toFloat())
        val valueTypeface = ResourcesCompat.getFont(context, com.srp.eways.R.font.iran_yekan_medium)
        if (valueTypeface != null) {
            mPaidPriceValueText.typeface = valueTypeface
        }
        mPaidPriceValueText.setText(mConfirmTransaction.getValuePaidPrice())
    }

    private fun setupPaidPriceValueDescription() {
        mPaidPriceValueDescriptionText.setTextColor(AB.getColor(com.srp.eways.R.color.confirm_transaction_text_color))
        val valueDescriptionTypeface = ResourcesCompat.getFont(context, com.srp.eways.R.font.iran_yekan_light)
        if (valueDescriptionTypeface != null) {
            mPaidPriceValueDescriptionText.typeface = valueDescriptionTypeface
        }
        mPaidPriceValueDescriptionText.visibility = View.VISIBLE
        mPaidPriceValueDescriptionText.text = AB.getString(R.string.rial)
    }

    private fun setupPaymentButton() {
        mPayButton.setTextColor(AB.getColor(R.color.confirm_transaction_button_payment_text_color))
        mPayButton.setTextSize(AB.getDimenPixelSize(R.dimen.confirm_transaction_button_payment_text_size).toFloat())
        mPayButton.setEnabledBackground(AB.getDrawable(R.drawable.login_button_background_enabled))
        mPayButton.setText(if (mConfirmTransaction.getPayButtonText() == null) AB.getString(R.string.confirm_transaction_payment) else mConfirmTransaction.getPayButtonText())
        mPayButton.setLoadingColorFilter(AB.getColor(R.color.confirm_transaction_button_payment_text_color))
        mPayButton.setLoadingVisibility(View.GONE)
        val typeface = ResourcesCompat.getFont(context, R.font.iran_yekan_light)
        if (typeface != null) {
            mPayButton.setTextTypeFace(typeface)
        }
        mPayButton.setEnable(true)
        mPayButton.setOnClickListener {
            mClickListener.onPayClicked()
        }
    }

    fun setPaymentLoadingVisibility(visibility: Int) {
        mPayButton.setLoadingVisibility(visibility)
    }

    fun setClickListener(payClickListener: ConfirmTransactionClickListener) {
        mClickListener = payClickListener
    }
}