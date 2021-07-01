package com.srp.ewayspanel.ui.store.product

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.res.ResourcesCompat
import com.srp.eways.ui.view.button.ButtonElement
import com.srp.ewayspanel.R
import com.srp.ewayspanel.di.DI

class InventoryNotExistDialog : Dialog {

    interface ProductDialogActionListeners {

        fun onCancel()

    }


    private lateinit var mWarningIcon : ImageView
    private lateinit var mDescription : TextView
    private lateinit var mConfirmButton : ButtonElement
    private lateinit var mCardView : CardView

    constructor(context : Context  ) : super (context){


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.indentory_not_exist_dialog)
        val resources = DI.getABResources()

        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT ,ViewGroup.LayoutParams.WRAP_CONTENT )

        mCardView = findViewById(R.id.product_dialog_cardView)
        mWarningIcon = findViewById(R.id.warning_icon)
        mDescription = findViewById(R.id.description_dialog)
        mConfirmButton = findViewById(R.id.confirm_button)

        mCardView.background = resources.getDrawable(R.drawable.indentory_not_exist_dialog_background)

        mWarningIcon.setImageDrawable(resources.getDrawable(R.drawable.ic_indentory_not_exist))

        mDescription.setTextColor(resources.getColor(R.color.inventory_not_exist_dialog_description_text_color))
        mDescription.setTextSize(TypedValue.COMPLEX_UNIT_PX , resources.getDimenPixelSize(R.dimen.inventory_not_exist_dialog_description_text_size).toFloat())
        mDescription.typeface = ResourcesCompat.getFont(context , R.font.iran_yekan)
        mDescription.text = resources.getString(R.string.inventory_not_exist_dialog_description)

        mConfirmButton.setText(resources.getString(R.string.inventory_not_exist_dialog_confirm_button_text))
        mConfirmButton.setTextSize(resources.getDimenPixelSize(R.dimen.inventory_not_exist_dialog_confirm_button_text_size).toFloat())
        mConfirmButton.setTextColor(resources.getColor(R.color.inventory_not_exist_dialog_confirmbutton_text_color))
        mConfirmButton.setTextTypeFace(ResourcesCompat.getFont(context, R.font.iran_yekan)!!)
        mConfirmButton.background = resources.getDrawable(R.drawable.indentory_not_exist_dialog_confirm_button_background)
        mConfirmButton.isEnabled = true
        mConfirmButton.setOnClickListener {
            dismiss()
        }









    }






}