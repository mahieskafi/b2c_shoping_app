package com.srp.ewayspanel.ui.shopcart.confirm

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.res.ResourcesCompat
import com.srp.eways.ui.view.button.ButtonElement
import com.srp.eways.util.Utils
import com.srp.ewayspanel.R
import com.srp.ewayspanel.di.DI
import okhttp3.internal.Util

class ShopCartPaymentDialog : Dialog {

    interface ShopCartPaymentDialogActionListeners {

        fun onCancel()

        fun onPayment()

    }

    private var mListener: ShopCartPaymentDialogActionListeners

    private lateinit var mWarningIcon: ImageView
    private lateinit var mDescription: TextView
    private lateinit var mTitle: TextView
    private lateinit var mCancelButton: ButtonElement
    private lateinit var mPaymentButton: ButtonElement
    private lateinit var mRootView: CardView

    constructor(context: Context, listener: ShopCartPaymentDialogActionListeners) : super(context) {
        mListener = listener
        setCancelable(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        setContentView(R.layout.shop_cart_payment_dialog)

        val resources = DI.getABResources()

        mRootView = findViewById(R.id.root_view)
        mWarningIcon = findViewById(R.id.icon)
        mTitle = findViewById(R.id.title)
        mDescription = findViewById(R.id.text)
        mCancelButton = findViewById(R.id.b_cancel)
        mPaymentButton = findViewById(R.id.b_confirm)

        mRootView.background = resources.getDrawable(R.drawable.shop_cart_remove_product_dialog_background)

        mWarningIcon.setImageDrawable(resources.getDrawable(R.drawable.shop_cart_remove_product_dialog_warning))

        mTitle.setTextColor(resources.getColor(R.color.shop_cart_remove_product_dialog_description_color))
        mTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.shop_cart_confirmation_pay_title_size).toFloat())
        mTitle.typeface = ResourcesCompat.getFont(context, R.font.iran_yekan)

        mDescription.setTextColor(resources.getColor(R.color.shop_cart_remove_product_dialog_description_color))
        mDescription.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.shop_cart_confirmation_pay_title_size).toFloat())
        mDescription.typeface = ResourcesCompat.getFont(context, R.font.iran_yekan)

        mCancelButton.setTextSize(resources.getDimenPixelSize(R.dimen.shop_cart_dialog_button_text_size).toFloat())
        mCancelButton.setTextColor(resources.getColor(R.color.shop_cart_remove_product_dialog_remove_text_color))
        mCancelButton.setTextTypeFace(ResourcesCompat.getFont(context, R.font.iran_yekan)!!)
        mCancelButton.background = resources.getDrawable(R.drawable.shop_cart_remove_product_dialog_remove_background)
        mCancelButton.isEnabled = true
        mCancelButton.setText(resources.getString(R.string.shop_cart_payment_dialog_cancel_button_text))
        mCancelButton.setOnClickListener {
            if (mListener != null) {
                mListener.onCancel()
            }
            dismiss()
        }

        mPaymentButton.setTextSize(resources.getDimenPixelSize(R.dimen.shop_cart_dialog_button_text_size).toFloat())
        mPaymentButton.setTextColor(resources.getColor(R.color.shop_cart_remove_product_dialog_keep_text_color))
        mPaymentButton.setEnabledBackground(resources.getDrawable(R.drawable.shop_cart_remove_product_dialog_keep_background))
        mPaymentButton.setEnable(true)
        mPaymentButton.setText(resources.getString(R.string.shop_cart_payment_dialog_payment_button_text))
        mPaymentButton.setOnClickListener {
            if (mListener != null) {
                mListener.onPayment()
            }
            dismiss()
        }

    }

    fun setTitleValue(value: Long) {
        val resources = DI.getABResources()

        mTitle.text = (resources.getString(R.string.shop_cart_payment_dialog_deposit_is_less_increase_deposit_text1) +
                Utils.toPersianPriceNumber(100000) + " " + resources.getString(R.string.shop_cart_payment_dialog_deposit_is_less_increase_deposit_text4) +
                resources.getString(R.string.shop_cart_payment_dialog_deposit_is_less_increase_deposit_text2) + " " +
                Utils.toPersianPriceNumber(value) + " " + resources.getString(R.string.shop_cart_payment_dialog_deposit_is_less_increase_deposit_text3))
        mDescription.visibility = View.GONE
    }

    fun setDescription(value: Long) {
        val resources = DI.getABResources()

        mTitle.text = resources.getString(R.string.shop_cart_payment_dialog_deposit_is_less_title)
        mDescription.visibility = View.VISIBLE
        mDescription.text = (resources.getString(R.string.shop_cart_payment_dialog_deposit_is_less_description) + " " +
                Utils.toPersianPriceNumber(value) + " " + resources.getString(R.string.rial))
    }
}