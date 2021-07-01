package com.srp.eways.ui.view.bill

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.res.ResourcesCompat
import com.srp.eways.R
import com.srp.eways.di.DIMain
import com.srp.eways.ui.view.ViewUtils
import com.srp.eways.ui.view.button.ButtonElement
import com.srp.eways.ui.view.dialog.ConfirmationDialog

class BillConfirmationDialog : Dialog {

    private val mIcon: AppCompatImageView
    private val mText: AppCompatTextView
    private val mTitle: AppCompatTextView
    private val mCancelButton: ButtonElement
    private val mConfirmButton: ButtonElement
    private val mButtonContainer: LinearLayout

    private var mListener: ConfirmationDialog.ConfirmationDialogItemClickListener? = null

    constructor(context: Context) : super(context)

    init {
        setContentView(R.layout.dialog_confirmation_bill)

        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        window?.setGravity(Gravity.CENTER)

        window?.attributes?.width = ((ViewUtils.getDisplayMetrics(context).widthPixels * 0.8).toInt())

        mIcon = findViewById(R.id.icon)
        mText = findViewById(R.id.text)
        mTitle = findViewById(R.id.title)
        mCancelButton = findViewById(R.id.b_cancel)
        mConfirmButton = findViewById(R.id.b_confirm)
        mButtonContainer = findViewById(R.id.button_container)


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val abResources = DIMain.getABResources()

        val textLayoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        textLayoutParams.rightMargin = abResources.getDimenPixelSize(R.dimen.bill_confirm_dialog_text_margin_sides)
        textLayoutParams.leftMargin = abResources.getDimenPixelSize(R.dimen.bill_confirm_dialog_text_margin_sides)
        textLayoutParams.topMargin = abResources.getDimenPixelSize(R.dimen.bill_confirm_dialog_text_margin_top)
        textLayoutParams.bottomMargin = abResources.getDimenPixelSize(R.dimen.bill_confirm_dialog_text_margin_bottom)

        mText.layoutParams = textLayoutParams

        val buttonContainerLayoutParams = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        buttonContainerLayoutParams.leftMargin = abResources.getDimenPixelSize(R.dimen.bill_confirm_dialog_button_margin_sides)
        buttonContainerLayoutParams.rightMargin = abResources.getDimenPixelSize(R.dimen.bill_confirm_dialog_button_margin_sides)
        buttonContainerLayoutParams.bottomMargin = abResources.getDimenPixelSize(R.dimen.bill_confirm_dialog_button_margin_bottom)

        buttonContainerLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        mButtonContainer.layoutParams = buttonContainerLayoutParams

//        mIcon.setImageDrawable(abResources.getDrawable(R.drawable.ic_bill_dialog_question_mark))

        mText.setTextSize(TypedValue.COMPLEX_UNIT_PX, abResources.getDimen(R.dimen.bill_confirm_dialog_text_size))
        mText.setTextColor(abResources.getColor(R.color.bill_confirmation_dialog_text_color))
        mText.typeface = ResourcesCompat.getFont(context, R.font.iran_yekan)

        mTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, abResources.getDimen(R.dimen.bill_confirm_dialog_title_text_size))
        mTitle.setTextColor(abResources.getColor(R.color.bill_confirmation_dialog_text_color))
        mTitle.typeface = ResourcesCompat.getFont(context, R.font.iran_yekan)

        mCancelButton.setEnabledBackground(abResources.getDrawable(R.drawable.button_cancel_bill_dialog_background_enabled))
        mCancelButton.setTextTypeFace(ResourcesCompat.getFont(context, R.font.iran_yekan)!!)
        mCancelButton.setTextColor(abResources.getColor(R.color.bill_confirmation_button_cancel_text_color))
        mCancelButton.setTextSize(abResources.getDimen(R.dimen.bill_confirm_dialog_button_text_size))
        mCancelButton.setEnable(true)
        mCancelButton.setLoadingVisibility(View.GONE)


        mConfirmButton.setEnabledBackground(abResources.getDrawable(R.drawable.button_confirm_bill_dialog_background_enabled))
        mConfirmButton.setTextTypeFace(ResourcesCompat.getFont(context, R.font.iran_yekan)!!)
        mConfirmButton.setTextColor(abResources.getColor(R.color.white))
        mConfirmButton.setTextSize(abResources.getDimen(R.dimen.bill_confirm_dialog_button_text_size))
        mConfirmButton.setEnable(true)
        mConfirmButton.setLoadingVisibility(View.GONE)

    }

    fun setIcon(icon: Drawable) {
        mIcon.setImageDrawable(icon)
    }

    fun setText(text: String) {
        mText.text = text
    }

    fun setTitle(title: String) {
        mTitle.visibility = View.VISIBLE

        val abResources = DIMain.getABResources()

        val textLayoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        textLayoutParams.rightMargin = 0
        textLayoutParams.leftMargin = 0
        textLayoutParams.topMargin = abResources.getDimenPixelSize(R.dimen.bill_confirm_dialog_title_margin_top)
        textLayoutParams.bottomMargin = abResources.getDimenPixelSize(R.dimen.bill_confirm_dialog_title_margin_bottom)

        mTitle.layoutParams = textLayoutParams

        mTitle.text = title

    }

    fun setCancelButtonText(text: String) {
        mCancelButton.setText(text)
    }

    fun setConfirmButtonText(text: String) {
        mConfirmButton.setText(text)
    }

    fun setTextColor(color: Int) {
        mText.setTextColor(color)
    }

    fun setTextSize(textSize: Float) {
        mText.textSize = textSize
    }

    fun setTypeface(typeface: Typeface) {
        mText.typeface = typeface
    }

    fun setMinLines(minLines: Int) {
        mText.minLines = minLines
    }

    fun setClickListener(listener: ConfirmationDialog.ConfirmationDialogItemClickListener) {
        mListener = listener

        mCancelButton.setOnClickListener { mListener?.onCancelClicked() }

        mConfirmButton.setOnClickListener { mListener?.onConfirmClicked() }
    }

}