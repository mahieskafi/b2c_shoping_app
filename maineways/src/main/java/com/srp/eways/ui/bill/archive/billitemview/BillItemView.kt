package com.srp.eways.ui.bill.archive.billitemview

import android.animation.ValueAnimator
import android.content.Context
import android.content.res.ColorStateList
import android.opengl.Visibility
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.cardview.widget.CardView
import androidx.core.content.res.ResourcesCompat
import com.srp.eways.R
import com.srp.eways.di.DIMain
import com.srp.eways.ui.bill.ShowMoreClickListener
import com.srp.eways.util.Utils

/**
 * Created by ErfanG on 4/18/2020.
 */
class BillItemView : RelativeLayout {


    interface OnCheckboxClickListener {

        fun onCheckClicked(isChecked: Boolean)
    }

    private val ANIM_DURATION = 200L

    private val mCheckBox: AppCompatCheckBox
    private val mDetailView: DetailBillItemView
    private val mMore: AppCompatImageView
    private val mCard: CardView
    private val mDetailContainer: RelativeLayout
    private val mInquiryNumberText: AppCompatTextView
    private val mInquiryNumberTitle: AppCompatTextView
    private val mPayIdText: AppCompatTextView
    private val mPayIdTitle: AppCompatTextView

    private var mOpen = false

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {

        LayoutInflater.from(context).inflate(R.layout.item_bill, this, true)

        val abResources = DIMain.getABResources()

        mCheckBox = findViewById(R.id.checkbox)
        mDetailView = findViewById(R.id.detail)
        mMore = findViewById(R.id.more_icon)
        mCard = findViewById(R.id.card_detail)
        mDetailContainer = findViewById(R.id.bill_detail_container)
        mInquiryNumberText = findViewById(R.id.inquiry_number)
        mInquiryNumberTitle = findViewById(R.id.inquiry_number_title)
        mPayIdText = findViewById(R.id.pay_id)
        mPayIdTitle = findViewById(R.id.pay_id_title)

        val regularFont = ResourcesCompat.getFont(context, R.font.iran_yekan)
        val lightFont = ResourcesCompat.getFont(context, R.font.iran_yekan_light)

        mDetailContainer.visibility = View.GONE

        mCard.setCardBackgroundColor(abResources.getColor(R.color.bill_archive_item_card_close_background))
        mDetailContainer.visibility = View.GONE

        mPayIdText.typeface = regularFont
        mPayIdText.setTextSize(TypedValue.COMPLEX_UNIT_PX, abResources.getDimenPixelSize(R.dimen.bill_archive_item_value_text_size).toFloat())
        mPayIdText.setTextColor(resources.getColor(R.color.bill_item_name_color))

        mInquiryNumberText.typeface = regularFont
        mInquiryNumberText.setTextSize(TypedValue.COMPLEX_UNIT_PX, abResources.getDimenPixelSize(R.dimen.bill_archive_item_value_text_size).toFloat())
        mInquiryNumberText.setTextColor(resources.getColor(R.color.bill_item_name_color))

        mPayIdTitle.typeface = lightFont
        mPayIdTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, abResources.getDimenPixelSize(R.dimen.bill_archive_item_value_title_size).toFloat())
        mPayIdTitle.setTextColor(resources.getColor(R.color.bill_item_name_color))
        mPayIdTitle.text = abResources.getString(R.string.bill_detail_dialog_payment_id_title)

        mInquiryNumberTitle.typeface = lightFont
        mInquiryNumberTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, abResources.getDimenPixelSize(R.dimen.bill_archive_item_value_title_size).toFloat())
        mInquiryNumberTitle.setTextColor(resources.getColor(R.color.bill_item_name_color))
        mInquiryNumberTitle.text = abResources.getString(R.string.bill_detail_dialog_inquiry_number_title)


    }

    fun setTitle(title: String) {
        mDetailView.setTitle(title)
    }

    fun setTime(time: String) {
        mDetailView.setTime(time)
    }

    fun setDate(date: String) {
        mDetailView.setDate(date)
    }

    fun setNumber(number: String) {
        mDetailView.setNumber(number)
    }

    fun setPayId(number: String) {
        mPayIdText.text = Utils.toPersianNumber(number)
    }

    fun setInquiryId(number: String?) {
        if (number != null && number.isNotEmpty()) {
            mInquiryNumberText.visibility = View.VISIBLE
            mInquiryNumberTitle.visibility = View.VISIBLE
            mInquiryNumberText.text = Utils.toPersianNumber(number)
        } else {
            mInquiryNumberText.visibility = View.INVISIBLE
            mInquiryNumberTitle.visibility = View.INVISIBLE
            mInquiryNumberText.text = "-"
        }
    }

    fun setPrice(price: Long) {
        mDetailView.setPrice(price)
    }

    fun setIconBackgroundColor(color: Int) {
        mDetailView.setColor(color)
    }

    fun setIcon(icon: Int) {
        mDetailView.setIcon(icon)
    }

    fun setOnCheckboxClickListener(listener: OnCheckboxClickListener) {

        mCheckBox.setOnClickListener {
            //                mDetailView.selectItem(isChecked)
            listener.onCheckClicked(mCheckBox.isChecked)
        }
    }

    fun setItemSelected(itemSelected: Boolean) {
        mDetailView.selectItem(itemSelected)
        mCheckBox.isChecked = itemSelected
    }

    fun setShowMore(itemShowMore: Boolean) {
        openClose(itemShowMore)
    }

    fun setOnDeleteClickListener(listener: OnClickListener) {

        mDetailView.setDeleteClickListener(listener)
    }


    fun setOnItemClickListener(listener: OnClickListener) {
        mDetailView.setOnClickListener(listener)
    }

    fun setOnShowMoreClickListener(listener: ShowMoreClickListener) {
        mMore.setOnClickListener {
            openClose(!mOpen)
            listener.onShowMoreClicked(mOpen)
        }
    }

    private fun openClose(open: Boolean) {

        val abResources = DIMain.getABResources()

        if (mOpen != open) {
            mOpen = open

            val rotationAnim: ValueAnimator = if (mOpen) {
                ValueAnimator.ofFloat(0f, 180f)
            } else {
                ValueAnimator.ofFloat(180f, 0f)
            }

            rotationAnim.addUpdateListener {
                mMore.rotation = it.animatedValue as Float
            }
            rotationAnim.duration = ANIM_DURATION

            rotationAnim.start()

            if (mOpen) {
                mCard.setCardBackgroundColor(abResources.getColor(R.color.bill_archive_item_card_open_background))
                mDetailContainer.visibility = View.VISIBLE
            } else {
                mCard.setCardBackgroundColor(abResources.getColor(R.color.bill_archive_item_card_close_background))
                mDetailContainer.visibility = View.GONE

            }

        }
    }

}