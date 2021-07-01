package com.srp.eways.ui.bill.receipt

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
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

class ReceiptForShairingView : LinearLayout {

    private val mStatusIcon: AppCompatImageView
    private val mTitle: AppCompatTextView
    private val mHeaderTitle: AppCompatTextView
    private val mDetails: KeyValuePairView
    private val mReceiptContainer: RelativeLayout



    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {

        LayoutInflater.from(context).inflate(R.layout.item_bill_receipt_for_shairing, this, true)


        mStatusIcon = findViewById(R.id.status_icon)
        mTitle = findViewById(R.id.title)
        mHeaderTitle = findViewById(R.id.receipt_header_title)
        mDetails = findViewById(R.id.detail_key_value)
        mReceiptContainer = findViewById(R.id.receipt_container)
        val resources = DIMain.getABResources()

        mTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.bill_receipt_single_title_size).toFloat())
        mTitle.setTextColor(resources.getColor(R.color.bill_receipt_single_title_color))
        mTitle.typeface = ResourcesCompat.getFont(context, R.font.iran_yekan)


        mStatusIcon.setImageDrawable(BillUtil.getBillStatusIcon(0))
        mTitle.text = "پرداخت قبض با موفقیت انجام شد"

    }

    fun setStatus(status: Int) {
        mStatusIcon.setImageDrawable(BillUtil.getBillStatusIcon(status))
    }

    fun setBillResult(billResponse: BillPaymentResponse) {

        val billTemp = billResponse.result!!.bills!![0]

        val mPairs = ArrayList<Pair<String, String>>()

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

        mStatusIcon.setImageDrawable(ResourcesCompat.getDrawable(resources, BillUtil.getBillReportStatusIcon(billTemp.status), null))
    }

    fun setChargeResult(receipt: Receipt) {
        val mPairs = ArrayList<Pair<String, String>>()

        for (receiptItem: ReceiptItem in receipt.receiptItems) {
            mPairs.add(Pair(receiptItem.title, receiptItem.value))
        }
        mDetails.setPairs(mPairs)


        if (receipt.getStatusCode() == 0) {

            if (receipt.getReceiptType() == Receipt.RECEIPT_CHARGE) {
                mTitle.text = (resources.getString(R.string.receipt_buy_charge))
                mHeaderTitle.text = (resources.getString(R.string.receipt_buy_charge_h1))
            } else if (receipt.getReceiptType() == Receipt.RECEIPT_INCREASE_DEPOSIT) {
                mTitle.text = (resources.getString(R.string.receipt_increase_deposit))
                mHeaderTitle.text = (resources.getString(R.string.receipt_increase_deposit_h1))
            } else if (receipt.getReceiptType() == Receipt.RECEIPT_INTERNET) {
                mTitle.text = (resources.getString(R.string.receipt_buy_internet))
                mHeaderTitle.text = (resources.getString(R.string.receipt_buy_internet_h1))
            }
        } else {
            mStatusIcon.setImageDrawable(BillUtil.getBillStatusIcon(2))
            mTitle.text = (resources.getString(R.string.receipt_transaction_failed))
            mHeaderTitle.text = (resources.getString(R.string.receipt_transaction_failed))
        }
    }


    fun setDescription(description: String) {
        mTitle.text = description
    }

    fun getViewForSaving(): View {
        return mReceiptContainer
    }
}