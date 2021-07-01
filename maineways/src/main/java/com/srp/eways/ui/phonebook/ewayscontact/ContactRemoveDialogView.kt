package com.srp.eways.ui.phonebook.ewayscontact

import android.content.Context
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.srp.eways.R
import com.srp.eways.di.DIMain
import com.srp.eways.ui.view.button.ButtonElement
import com.yashoid.sequencelayout.SequenceLayout

class ContactRemoveDialogView : SequenceLayout {

    private val mBinIcon : ImageView
    private val mDescription : TextView
    private val mRemoveButton : View
    private val mRemoveText : TextView
    private val mRemoveLoading : ProgressBar
    private val mKeepButton : ButtonElement

    private lateinit var mListener : ContactRemoveDialog.ContactRemoveDialogActionListeners

    constructor(context : Context) : super(context)
    constructor(context : Context, attributeSet: AttributeSet) : super(context, attributeSet)
    constructor(context : Context, attributeSet: AttributeSet, defStyleAtr : Int) : super(context, attributeSet, defStyleAtr)

    init {

        val resources = DIMain.getABResources()

        layoutParams = ViewGroup.LayoutParams(View.MeasureSpec.makeMeasureSpec(resources.getDimenPixelSize(R.dimen.contact_remove_dialog_width), View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))

        LayoutInflater.from(context).inflate(R.layout.contact_remove_dialog, this, true)

        mBinIcon = findViewById(R.id.bin_image)
        mDescription = findViewById(R.id.description)
        mRemoveButton = findViewById(R.id.remove_button)
        mRemoveText = findViewById(R.id.remove_text)
        mRemoveLoading = findViewById(R.id.remove_loading)
        mKeepButton = findViewById(R.id.keep_button)

        addSequences(R.xml.sequences_contact_remove_dialog)

        //region Set Attrs
        background = resources.getDrawable(R.drawable.contact_remove_dialog_backgrounde)

        mBinIcon.setImageDrawable(resources.getDrawable(R.drawable.contact_remove_dialog_warning))
        mDescription.text = resources.getString(R.string.contact_remove_dialog_single_title)

        mDescription.setTextColor(resources.getColor(R.color.contact_remove_dialog_description_color))
        mDescription.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.contact_remove_dialog_description_size).toFloat())
        mDescription.typeface = ResourcesCompat.getFont(context, R.font.iran_yekan)

        val btnFont  = ResourcesCompat.getFont(context, R.font.iran_yekan)

        mRemoveLoading.setPadding(resources.getDimenPixelSize(R.dimen.contact_remove_dialog_remove_loading_padding_left), 0, 0, 0)
        mRemoveLoading.indeterminateDrawable.setColorFilter(resources.getColor(R.color.contact_remove_dialog_remove_text_color), PorterDuff.Mode.SRC_IN)
        mRemoveLoading.visibility = View.GONE
        mRemoveText.text = resources.getString(R.string.contact_remove_dialog_remove)
        btnFont?.let { mRemoveText.typeface = it }
        mRemoveText.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.contact_remove_dialog_button_text_size).toFloat())
        mRemoveText.setTextColor(resources.getColor(R.color.contact_remove_dialog_remove_text_color))
        mRemoveButton.background = resources.getDrawable(R.drawable.contact_remove_dialog_remove_background)
        mRemoveButton.isEnabled = true


        mKeepButton.setText(resources.getString(R.string.contact_remove_dialog_keep))
        btnFont?.let { mKeepButton.setTextTypeFace(it) }
        mKeepButton.setTextSize(resources.getDimenPixelSize(R.dimen.contact_remove_dialog_button_text_size).toFloat())
        mKeepButton.setTextColor(resources.getColor(R.color.contact_remove_dialog_keep_text_color))
        mKeepButton.setDisableBackground(resources.getDrawable(R.drawable.contact_remove_dialog_keep_background))
        mKeepButton.setEnabledBackground(resources.getDrawable(R.drawable.contact_remove_dialog_keep_background))
        mKeepButton.setEnable(true)
        //endregion
    }

    fun setActions(listener : ContactRemoveDialog.ContactRemoveDialogActionListeners){

        mListener = listener


        mKeepButton.setOnClickListener{
            listener.onKeep()
        }

        mRemoveButton.setOnClickListener {

            mKeepButton.setEnable(false)
            mRemoveButton.isEnabled = false

            val lp = mRemoveButton.layoutParams
            lp.width = (DIMain.getABResources().getDimenPixelSize(R.dimen.contact_remove_dialog_button_width) * 1.3).toInt()
            mRemoveButton.layoutParams = lp

            val lp2 = mKeepButton.layoutParams
            lp2.width = (DIMain.getABResources().getDimenPixelSize(R.dimen.contact_remove_dialog_button_width) * 0.7).toInt()
            mKeepButton.layoutParams = lp2

            requestLayout()

            mRemoveLoading.visibility = View.VISIBLE
            listener.onRemove()
        }
    }
}