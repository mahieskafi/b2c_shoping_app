package com.srp.eways.ui.view.phonebook

import android.R.attr.button
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.widget.AbsListView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.res.ResourcesCompat
import com.srp.eways.R
import com.srp.eways.di.DIMain
import com.srp.eways.util.Utils
import com.yashoid.sequencelayout.SequenceLayout

class EwaysContactDetailItemView : SequenceLayout {

    private val mRemoveIcon: AppCompatImageView
    private val mEditIcon: AppCompatImageView
    private val mContactName: AppCompatTextView
    private val mContactPhoneNumber: AppCompatTextView
    private val mCreditSuffix: AppCompatTextView
    private val mCredit: AppCompatTextView
    private val mCreditTitle: AppCompatTextView

    private var mSelectedBackground: Drawable
    private var mUnSelectedBackground: Drawable

    private var mIsSelected = false

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)


    init {

        LayoutInflater.from(context).inflate(R.layout.item_eways_contact_detail_view, this, true)


        mRemoveIcon = findViewById(R.id.delete_contact_icon)
        mEditIcon = findViewById(R.id.edit_contact_icon)
        mContactName = findViewById(R.id.contact_name)
        mContactPhoneNumber = findViewById(R.id.contact_phone)
        mCreditSuffix = findViewById(R.id.contact_credit_suffix)
        mCredit = findViewById(R.id.contact_credit)
        mCreditTitle = findViewById(R.id.title_credit)

        addSequences(R.xml.sequences_eways_contact_detail_item)

        val resources = DIMain.getABResources()

        mSelectedBackground = resources.getDrawable(R.drawable.eways_contact_detail_item_view_selected_background)
        mUnSelectedBackground = resources.getDrawable(R.drawable.eways_contact_detail_item_view_unselected_background)

        background = mUnSelectedBackground

        mRemoveIcon.setImageDrawable(resources.getDrawable(R.drawable.ic_contact_remove))
        mEditIcon.setImageDrawable(resources.getDrawable(R.drawable.ic_contact_edit))

        var padding = resources.getDimenPixelSize(R.dimen.ewaysphonebook_contact_detail_remove_icon_padding)
        mRemoveIcon.setPadding(padding, padding, padding, padding)
        mEditIcon.setPadding(padding, padding, padding, padding)

        val mediumFont = ResourcesCompat.getFont(context, R.font.iran_yekan_medium)
        val regularFont = ResourcesCompat.getFont(context, R.font.iran_yekan)

        mContactName.apply {
            typeface = regularFont
            setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.ewaysphonebook_contact_item_name_text_size).toFloat())
            setTextColor(resources.getColor(R.color.ewaysphonebook_contact_item_name_text_color))
        }

        mContactPhoneNumber.apply {
            typeface = regularFont
            setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.ewaysphonebook_contact_item_phonenumber_text_size).toFloat())
            setTextColor(resources.getColor(R.color.ewaysphonebook_contact_item_phonenumber_text_color))
        }

        mCreditTitle.apply {
            typeface = regularFont
            setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.ewaysphonebook_contact_item_credit_title_text_size).toFloat())
            setTextColor(resources.getColor(R.color.ewaysphonebook_contact_item_name_text_color))
            text = resources.getString(R.string.ewaysphonebook_contact_item_credit_text_title)
        }

        mCredit.apply {
            typeface = mediumFont
            setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.ewaysphonebook_contact_item_name_text_size).toFloat())
            setTextColor(resources.getColor(R.color.ewaysphonebook_contact_item_credit_text_color))
        }

        mCreditSuffix.apply {
            typeface = regularFont
            setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.ewaysphonebook_contact_item_credit_suffix_text_size).toFloat())
            setTextColor(resources.getColor(R.color.ewaysphonebook_contact_item_credit_text_color))
            text = resources.getString(R.string.rial)
        }

    }

    fun setName(name: String) {
        mContactName.text = name
    }

    fun setPhoneNumber(number: String) {
        mContactPhoneNumber.text = Utils.toPersianNumber(number)
    }

    fun setCredit(credit: Long) {
        mCredit.setLayoutParams(AbsListView.LayoutParams(0, 0))
        mCredit.text = Utils.toPersianPriceNumber(credit)
    }

    fun setCreditVisibility(visibility: Int) {
        mCredit.visibility = visibility
        mCreditTitle.visibility = visibility
        mCreditSuffix.visibility = visibility
    }


    fun selectItem(isSelected: Boolean) {

        if (mIsSelected == isSelected) {
            return
        }

        mIsSelected = isSelected

        background = if (isSelected) {
            mSelectedBackground
        } else {
            mUnSelectedBackground
        }

        invalidate()
    }

    fun isItemSelected() = mIsSelected

    fun setRemoveClickListener(listener: OnClickListener) {
        mRemoveIcon.setOnClickListener(listener)
    }

    fun setEditClickListener(listener: OnClickListener) {
        mEditIcon.setOnClickListener(listener)
    }

    fun setItemClickListener(listener: OnClickListener) {
        setOnClickListener(listener)
    }

}