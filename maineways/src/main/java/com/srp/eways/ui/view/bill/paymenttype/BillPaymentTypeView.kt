package com.srp.eways.ui.view.bill.paymenttype

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.cardview.widget.CardView
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.srp.eways.R
import com.srp.eways.di.DIMain
import com.srp.eways.model.deposit.Bank
import com.srp.eways.model.deposit.BankListResponse
import com.srp.eways.ui.bill.paymenttype.BankAdapter
import com.srp.eways.ui.bill.paymenttype.BillInfo
import com.srp.eways.ui.bill.paymenttype.BillPaymentTypeFragment
import com.srp.eways.ui.view.LoadingStateView
import com.srp.eways.ui.view.bill.BillConfirmationDialog
import com.srp.eways.ui.view.button.ButtonElement
import com.srp.eways.ui.view.dialog.ConfirmationDialog
import com.srp.eways.util.BillUtil
import ir.abmyapp.androidsdk.IABResources

class BillPaymentTypeView : LinearLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    interface ConfirmNotEnoughDialogClickListener {
        fun onConfirmClicked(payingPrice: Long, currentCredit: Long)
    }

    private var mBankCardView: CardView
    private var mBankRecycler: RecyclerView
    private lateinit var mBankAdapter: BankAdapter
    private var mTitleText: AppCompatTextView

    private var mPaymentButton: ButtonElement
    private var mSaveButton: ButtonElement
    private var mButtonContainer: RelativeLayout
    private var mSaveDescription: AppCompatTextView
    private var mSaveIcon: AppCompatImageView
    private var mBankLoadingStateView: LoadingStateView

    private var mInfoView: BillPaymentInfoView

    private lateinit var mBillList: ArrayList<BillInfo>
    private var mNotEnoughDialogConfirmClickListener: ConfirmNotEnoughDialogClickListener? = null

    private var mSelectedBank: Bank? = null

    private lateinit var mBankListResponseObserver: Observer<BankListResponse>

    init {
        LayoutInflater.from(context).inflate(R.layout.bill_payment_type_view, this, true)

        var abResources: IABResources = DIMain.getABResources()

        mInfoView = findViewById(R.id.payment_info)
        mBankCardView = findViewById(R.id.bank_card)
        mBankRecycler = findViewById(R.id.rv_bank)
        mPaymentButton = findViewById(R.id.btn_payment)
        mTitleText = findViewById(R.id.payment_info_title)
        mSaveButton = findViewById(R.id.save_button)
        mSaveDescription = findViewById(R.id.save_button_description)
        mSaveIcon = findViewById(R.id.save_button_description_icon)
        mButtonContainer = findViewById(R.id.button_container)
        mBankLoadingStateView = findViewById(R.id.loadingstateview)

        mTitleText.setTextSize(TypedValue.COMPLEX_UNIT_PX, abResources.getDimen(R.dimen.bill_payment_type_info_title_size))
        mTitleText.setTextColor(abResources.getColor(R.color.bill_patment_info_inner_title_color))
        mTitleText.typeface = ResourcesCompat.getFont(context!!, R.font.iran_yekan)!!
        mTitleText.text = abResources.getString(R.string.bill_payment_type_title_text)

        val cardLayoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, abResources.getDimenPixelSize(R.dimen.bill_payment_type_bank_card_height))
        cardLayoutParams.setMargins(abResources.getDimenPixelSize(R.dimen.bill_payment_type_bank_card_margin_side),
                abResources.getDimenPixelSize(R.dimen.bill_payment_type_bank_card_margin_top),
                abResources.getDimenPixelSize(R.dimen.bill_payment_type_bank_card_margin_side),
                0)
        mBankCardView.layoutParams = cardLayoutParams

        mBankRecycler.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, true)
        mBankAdapter = BankAdapter(object : BillPaymentTypeFragment.BankItemSelectedListener {
            override fun onBankItemSelected(bank: Bank) {
                mPaymentButton.setEnable(true)
                mSelectedBank = bank
            }

        })
        mBankRecycler.adapter = mBankAdapter

        mPaymentButton.setDisableBackground(abResources.getDrawable(R.drawable.login_button_background_disabled))
        mPaymentButton.setEnabledBackground(abResources.getDrawable(R.drawable.login_button_background_enabled))
        mPaymentButton.setTextTypeFace(ResourcesCompat.getFont(context, R.font.iran_yekan_light)!!)
        mPaymentButton.setTextColor(abResources.getColor(R.color.bill_payment_summary_button_text_color))
        mPaymentButton.setTextSize(abResources.getDimen(R.dimen.bill_payment_type_button_text_size))
        mPaymentButton.setText(abResources.getString(R.string.bill_payment_summary_button_text))
        mPaymentButton.setLoadingVisibility(View.GONE)
        mPaymentButton.setLoadingColorFilter(abResources.getColor(R.color.white))
        mPaymentButton.setEnable(false)

        mSaveButton.setText(abResources.getString(R.string.bill_inquiry_detail_save_button_text))
        mSaveButton.setTextSize(abResources.getDimenPixelSize(R.dimen.bill_inquiry_detail_save_button_text_size).toFloat())
        mSaveButton.setTextColor(abResources.getColor(R.color.bill_inquiry_detail_save_button_text_color_disable))
        mSaveButton.setEnabledBackground(abResources.getDrawable(R.drawable.bill_inquiry_detail_save_background_enabled))
        mSaveButton.setDisableBackground(abResources.getDrawable(R.drawable.button_element_default_background_disabled))
        mSaveButton.setLoadingColorFilter(abResources.getColor(R.color.bill_inquiry_detail_save_button_text_color_enable))
        mSaveButton.hasIcon(false)
        mSaveButton.setEnable(false)

        mSaveIcon.setImageDrawable(abResources.getDrawable(R.drawable.bill_inquiry_detail_save_button_description_icon))
        mSaveIcon.visibility = View.GONE

        with(mSaveDescription) {

            typeface = ResourcesCompat.getFont(context, R.font.iran_yekan)
            setTextColor(abResources.getColor(R.color.bill_inquiry_detail_save_button_text_color_enable))
            setTextSize(TypedValue.COMPLEX_UNIT_PX, abResources.getDimenPixelSize(R.dimen.bill_inquiry_detail_save_button_description_size).toFloat())
            text = abResources.getString(R.string.bill_inquiry_detail_save_button_description)
            visibility = View.INVISIBLE
        }

        mBankLoadingStateView.setViewOrientation(HORIZONTAL)
        mBankLoadingStateView.setStateAndDescription(LoadingStateView.STATE_LOADING, abResources.getString(R.string.loading_message), true)
        mBankRecycler.visibility = View.GONE
    }

    fun setInfo(billInfo: ArrayList<BillInfo>) {
        mInfoView.setBills(billInfo)
    }

    fun setSaveButtonVisibility(visibility: Int) {
        mSaveButton.visibility = visibility
        mSaveIcon.visibility = visibility
        mSaveDescription.visibility = visibility
    }

    fun setSaveButtonEnabled(enabled: Boolean) {
        if (enabled) {
            mSaveButton.setEnable(true)
            mSaveButton.setTextColor(DIMain.getABResources().getColor(R.color.bill_inquiry_detail_save_button_text_color_enable))
            mSaveDescription.visibility = View.VISIBLE
        } else {
            mSaveButton.setEnable(false)
            mSaveButton.setTextColor(DIMain.getABResources().getColor(R.color.bill_inquiry_detail_save_button_text_color_disable))
            mSaveDescription.visibility = View.INVISIBLE
        }
    }

    fun showNotEnoughCreditDialog(currentCredit: Long) {
        var abResources: IABResources = DIMain.getABResources()

        val dialog = BillConfirmationDialog(context!!)
        dialog.setIcon(abResources.getDrawable(R.drawable.ic_bill_error_dialog_icon))
        dialog.setText(abResources.getString(R.string.bill_payment_type_notenough_credit_text))
        dialog.setTitle(abResources.getString(R.string.bill_payment_type_notenough_title))
        dialog.setCancelButtonText(abResources.getString(R.string.bill_payment_type_notenough_credit_return_text))
        dialog.setConfirmButtonText(abResources.getString(R.string.bill_payment_type_notenough_credit_increase_text))
        dialog.setClickListener(object : ConfirmationDialog.ConfirmationDialogItemClickListener {
            override fun onConfirmClicked() {
                dialog.dismiss()
                mNotEnoughDialogConfirmClickListener?.onConfirmClicked(mInfoView.getTotalPrice().toLong(), currentCredit)
            }

            override fun onCancelClicked() {
                dialog.dismiss()
            }
        })
        dialog.show()
    }

    fun setSaveClickListener(clickListener: OnClickListener) {
        mSaveButton.setClickListener(clickListener)
    }

    fun setPaymentClickListener(clickListener: OnClickListener) {
        mPaymentButton.setClickListener(clickListener)
    }

    fun setNotEnoughDialogConfirmClickListener(clickListener: ConfirmNotEnoughDialogClickListener) {
        mNotEnoughDialogConfirmClickListener = clickListener
    }

    fun setLoadingRetryClickListener(retryListener: LoadingStateView.RetryListener) {
        mBankLoadingStateView.setRetryListener(retryListener)
    }

    fun setBankList(bankList: List<Bank>) {
        if (bankList.isNotEmpty()) {
            mBankCardView.visibility = View.VISIBLE
            mBankLoadingStateView.visibility = View.GONE
            mBankRecycler.visibility = View.VISIBLE

            val validBankList: MutableList<Bank> = java.util.ArrayList()

            for (i in bankList.indices) {
                val bank: Bank = bankList[i]
                if (BillUtil.getBankName(bank.gId) != 0) {
                    bank.selcted = false
                    validBankList.add(bank)
                }
            }

            mBankAdapter.setData(validBankList)
        } else {
            mBankLoadingStateView.visibility = View.VISIBLE
            mBankRecycler.visibility = View.GONE
            mBankLoadingStateView.setStateAndDescription(LoadingStateView.STATE_ERROR, DIMain.getABResources().getString(R.string.network_error_undefined), true)

        }
    }

    fun setBankListError(error: String) {
        mBankLoadingStateView.visibility = View.VISIBLE
        mBankRecycler.visibility = View.GONE
        mBankLoadingStateView.setStateAndDescription(LoadingStateView.STATE_ERROR, error, true)
    }

    fun setPaymentButtonLoadingVisibility(visibility: Boolean) {
        if (visibility) {
            mPaymentButton.setLoadingVisibility(View.VISIBLE)
        } else {
            mPaymentButton.setLoadingVisibility(View.GONE)
        }
    }

    fun setSaveButtonLoadingVisibility(visibility: Boolean) {
        if (visibility) {
            mSaveButton.setLoadingVisibility(View.VISIBLE)
        } else {
            mSaveButton.setLoadingVisibility(View.GONE)
        }
    }

    fun getBillsTotalPrice(): Long {
        return mInfoView.getTotalPrice()
    }

    fun getSelectedBank(): Bank? {
        return mSelectedBank
    }

    fun clearSelectedBank() {
        mSelectedBank = null
        mPaymentButton.setEnable(false)
    }
}