package com.srp.eways.ui.bill.receipt

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.res.ResourcesCompat
import com.srp.eways.R
import com.srp.eways.di.DIMain
import com.srp.eways.model.bill.BillPaymentResponse
import com.srp.eways.ui.bill.inquiry.views.KeyValuePairView
import com.srp.eways.ui.bill.report.BillReportItemView
import com.srp.eways.ui.view.receipt.Receipt
import com.srp.eways.ui.view.receipt.ReceiptItem
import com.srp.eways.util.BillUtil
import com.srp.eways.util.Utils

/**
 * Created by ErfanG on 5/10/2020.
 */
class ReceiptItemView : LinearLayout {

    private val mStatusIcon: AppCompatImageView
    private val mTitle: AppCompatTextView
    private val mDetails: KeyValuePairView

    //    private val mAddButton: AppCompatImageButton
//    private val mAddDescription: AppCompatTextView
    private val mSaveButton: AppCompatImageButton
    private val mShareButton: AppCompatImageButton
    private val mReceiptContainer: LinearLayout
    private val mActionContainer: RelativeLayout


    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {

        LayoutInflater.from(context).inflate(R.layout.item_bill_receipt_single, this, true)


        mStatusIcon = findViewById(R.id.status_icon)
        mTitle = findViewById(R.id.title)
        mDetails = findViewById(R.id.detail_key_value)
//        mAddButton = findViewById(R.id.add_button)
//        mAddDescription = findViewById(R.id.add_description)
        mShareButton = findViewById(R.id.share_button)
        mSaveButton = findViewById(R.id.save_button)
        mReceiptContainer = findViewById(R.id.receipt_container)
        mActionContainer = findViewById(R.id.actions_container)

        val resources = DIMain.getABResources()

        mTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.bill_receipt_single_title_size).toFloat())
        mTitle.setTextColor(resources.getColor(R.color.bill_receipt_single_title_color))
        mTitle.typeface = ResourcesCompat.getFont(context, R.font.iran_yekan)


        mStatusIcon.setImageDrawable(BillUtil.getBillStatusIcon(0))
        mTitle.text = "پرداخت قبض با موفقیت انجام شد"


//        mAddDescription.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.bill_receipt_single_add_description_size).toFloat())
//        mAddDescription.setTextColor(resources.getColor(R.color.bill_receipt_single_add_description_color))
//        mAddDescription.typeface = ResourcesCompat.getFont(context, R.font.iran_yekan)
//        mAddDescription.text = resources.getString(R.string.bill_receipt_single_add_description)

//        mAddButton.background = resources.getDrawable(R.drawable.bill_receipt_single_add_button_background)

        mSaveButton.background = resources.getDrawable(R.drawable.bill_report_item_save)
        mShareButton.background = resources.getDrawable(R.drawable.bill_report_item_share)
    }

    fun setStatus(status: Int) {
        mStatusIcon.setImageDrawable(BillUtil.getBillStatusIcon(status))
    }

    fun setBillResult(billResponse: BillPaymentResponse) {

        val billTemp = billResponse.result!!.bills!![0]

        val mPairs = ArrayList<Pair<String, String>>()
//TODO server should send us paymentNumber
//        mPairs.add(Pair(resources.getString(R.string.receipt_bill_paymentid_title), billTemp.inquiryNumber
//                ?: "-----"))
        mPairs.add(Pair(resources.getString(R.string.receipt_bill_transaction_type_title), resources.getString(R.string.bill_inquiry_detail_info_bill_type_value)))
        mPairs.add(Pair(resources.getString(R.string.receipt_bill_id_title), billTemp.billId))
        mPairs.add(Pair(resources.getString(R.string.receipt_bill_pay_id_title), billTemp.paymentId))
        mPairs.add(Pair(resources.getString(R.string.receipt_bill_type_title), BillUtil.getServiceName(billTemp.billTypeId)))
        if ((billTemp.billTypeId == BillUtil.SERVICE_PHONE || billTemp.billTypeId == BillUtil.SERVICE_MOBILE) && billTemp.inquiryNumber != null) {
            mPairs.add(Pair(resources.getString(R.string.bill_detail_dialog_inquiry_number_title), billTemp.inquiryNumber))
        }
        mPairs.add(Pair(resources.getString(R.string.receipt_bill_price_title), Utils.toPersianPriceNumber(billTemp.price) + " ریال"))

        mDetails.setPairs(mPairs)

        if (BillUtil.getBillReportStatus(billTemp.status) == BillReportItemView.Status.OK) {
            mTitle.text = Utils.toPersianNumber("پرداخت قبض با موفقیت انجام شد")
        } else {
            mTitle.text = BillUtil.getDefaultBillStatusDescription(billTemp.status)
        }
//        if (billResponse.description != null && billResponse.description.isNotEmpty()) {
//            mTitle.text = Utils.toPersianNumber(billResponse.description)
//        } else {
//            mTitle.text = BillUtil.getDefaultBillStatusDescription(billResponse.status)
//        }


        mStatusIcon.setImageDrawable(ResourcesCompat.getDrawable(resources, BillUtil.getBillReportStatusIcon(billTemp.status), null))

    }

    fun setChargeResult(receipt: Receipt) {
        val mPairs = ArrayList<Pair<String, String>>()

        for (receiptItem: ReceiptItem in receipt.receiptItems) {
            mPairs.add(Pair(receiptItem.title, receiptItem.value))
        }
        mDetails.setPairs(mPairs)
    }

    fun setAddButtonAction(listener: OnClickListener) {
//        mAddButton.setOnClickListener(listener)
    }

    fun setShareButtonAction(listener: OnClickListener) {
        mShareButton.setOnClickListener(listener)
    }

    fun setSaveButtonAction(listener: OnClickListener) {
        mSaveButton.setOnClickListener(listener)
    }

    fun getViewForSaving(): View {
        return mReceiptContainer
    }

    fun setActionContainerVisibility(visibility: Int) {
        mActionContainer.visibility = visibility
    }

    fun setDetailMargin(left: Int, top: Int, right: Int, bottom: Int) {
        var detailLayoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        detailLayoutParams.setMargins(left, top, right, bottom)
        mDetails.layoutParams = detailLayoutParams
    }

    fun setDescription(description: String) {
        mTitle.text = description
    }

    fun setDescriptionColor(textColor: Int) {
        mTitle.setTextColor(textColor)
    }

    fun setStatusIcon(drawable: Drawable) {
        mStatusIcon.setImageDrawable(drawable)
    }
}