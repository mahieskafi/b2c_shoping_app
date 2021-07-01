package com.srp.eways.ui.view.phonebook

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.cardview.widget.CardView
import androidx.core.content.res.ResourcesCompat
import com.srp.eways.R
import com.srp.eways.di.DIMain
import com.srp.eways.ui.bill.ShowMoreClickListener
import com.srp.eways.util.Utils

class EwaysContactItemView : RelativeLayout {

    private val ANIMATION_DURATION = 200L

    private val mDetailView: EwaysContactDetailItemView
    private val mMore: AppCompatImageView
    private val mCard: CardView
    private val mDetailContainer: RelativeLayout
    private val mDescription: AppCompatTextView
    private val mDescriptionTitle: AppCompatTextView
    private val mDate: AppCompatTextView
    private val mTime: AppCompatTextView

    private var mSelectedBackgroundColor: Drawable
    private var mUnSelectedBackgroundColor: Drawable

    private var mOpen = false

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {

        LayoutInflater.from(context).inflate(R.layout.item_eways_contact_view, this, true)

        val abResources = DIMain.getABResources()

        mDetailView = findViewById(R.id.detail)
        mMore = findViewById(R.id.more_icon)
        mCard = findViewById(R.id.card_detail)
        mDetailContainer = findViewById(R.id.contact_detail_container)
        mDescription = findViewById(R.id.description)
        mDescriptionTitle = findViewById(R.id.description_title)
        mDate = findViewById(R.id.date)
        mTime = findViewById(R.id.time)

        val regularFont = ResourcesCompat.getFont(context, R.font.iran_yekan)
        val lightFont = ResourcesCompat.getFont(context, R.font.iran_yekan_light)

        mUnSelectedBackgroundColor = abResources.getDrawable(R.drawable.eways_contact_item_view_unselected_background)
        mSelectedBackgroundColor = abResources.getDrawable(R.drawable.eways_contact_item_view_selected_background)

        mDetailContainer.visibility = View.GONE

        mCard.background = mUnSelectedBackgroundColor

        mDetailContainer.visibility = View.GONE

        mDescriptionTitle.apply {
            typeface = lightFont
            setTextSize(TypedValue.COMPLEX_UNIT_PX, abResources.getDimenPixelSize(R.dimen.ewaysphonebook_contact_item_credit_title_text_size).toFloat())
            setTextColor(resources.getColor(R.color.ewaysphonebook_contact_item_description_text_color))
            visibility = View.GONE
            text = resources.getString(R.string.ewaysphonebook_contact_item_description_title)
        }

        mDescription.apply {
            typeface = regularFont
            setTextSize(TypedValue.COMPLEX_UNIT_PX, abResources.getDimenPixelSize(R.dimen.ewaysphonebook_contact_item_credit_title_text_size).toFloat())
            setTextColor(resources.getColor(R.color.ewaysphonebook_contact_item_description_text_color))
            visibility = View.GONE
        }

        mDate.apply {
            typeface = lightFont
            setTextSize(TypedValue.COMPLEX_UNIT_PX, abResources.getDimenPixelSize(R.dimen.ewaysphonebook_contact_item_date_text_size).toFloat())
            setTextColor(resources.getColor(R.color.ewaysphonebook_contact_item_description_text_color))
        }

        mTime.apply {
            typeface = lightFont
            setTextSize(TypedValue.COMPLEX_UNIT_PX, abResources.getDimenPixelSize(R.dimen.ewaysphonebook_contact_item_date_text_size).toFloat())
            setTextColor(resources.getColor(R.color.ewaysphonebook_contact_item_description_text_color))
        }
    }

    fun setDescription(description: String) {
        mDescription.setText(description)
        if (mDescription.length()>0){
            mDescription.visibility = View.VISIBLE
            mDescriptionTitle.visibility = View.VISIBLE
        }else{
            mDescription.visibility = View.GONE
            mDescriptionTitle.visibility = View.GONE
        }
    }

    fun setName(name: String) {
        mDetailView.setName(name)
    }

    fun setPhoneNumber(number: String) {
        mDetailView.setPhoneNumber(number)
    }

    fun setCredit(credit: Long) {
        mDetailView.setCredit(credit)
    }

    fun setTime(time: String) {
        mTime.text = Utils.toPersianNumber(time)
    }

    fun setDate(date: String) {
        mDate.text = Utils.changeDateToRtl(date);
    }


    fun setItemSelected(itemSelected: Boolean) {
        var abResources = DIMain.getABResources()
        var detailViewMargin = abResources.getDimenPixelSize(R.dimen.ewaysphonebook_contact_detail_container_margin_background_margin)
        var detailViewLayoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                abResources.getDimenPixelSize(R.dimen.ewaysphonebook_contact_detail_view_height))

        mDetailView.selectItem(itemSelected)

        if (itemSelected) {

            detailViewLayoutParams.setMargins(detailViewMargin, detailViewMargin, detailViewMargin, detailViewMargin)

            mCard.background = mSelectedBackgroundColor
            mDetailContainer.visibility = View.VISIBLE
        } else {
            detailViewLayoutParams.setMargins(0, 0, 0, 0)

            mCard.background = mUnSelectedBackgroundColor
            mDetailContainer.visibility = View.GONE
        }

        mDetailView.layoutParams = detailViewLayoutParams
    }

    fun setShowMore(itemShowMore: Boolean) {
        openClose(itemShowMore)
    }

    fun setOnRemoveClickListener(listener: OnClickListener) {

        mDetailView.setRemoveClickListener(listener)
    }

    fun setOnEditClickListener(listener: OnClickListener) {

        mDetailView.setEditClickListener(listener)
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
            rotationAnim.duration = ANIMATION_DURATION

            rotationAnim.start()

            setItemSelected(open)

        }
    }
}