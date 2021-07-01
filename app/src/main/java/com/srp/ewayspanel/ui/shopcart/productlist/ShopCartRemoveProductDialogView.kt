package com.srp.ewayspanel.ui.shopcart.productlist

import android.content.Context
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.srp.ewayspanel.R
import com.srp.ewayspanel.di.DI
import com.srp.eways.ui.view.button.ButtonElement
import com.yashoid.sequencelayout.SequenceLayout

/**
 * Created by ErfanG on 1/14/2020.
 */
class ShopCartRemoveProductDialogView : SequenceLayout {

    private val mBinIcon : ImageView
    private val mDescription : TextView
    private val mRemoveButton : View
    private val mRemoveText : TextView
    private val mRemoveLoading : ProgressBar
    private val mKeepButton : ButtonElement

    private lateinit var mListener : ShopCartRemoveProductDialog.ShopCartRemoveProductDialogActionListeners

    constructor(context : Context) : super(context)
    constructor(context : Context, attributeSet: AttributeSet) : super(context, attributeSet)
    constructor(context : Context, attributeSet: AttributeSet, defStyleAtr : Int) : super(context, attributeSet, defStyleAtr)

    init {

        val resources = DI.getABResources()

        layoutParams = LayoutParams(MeasureSpec.makeMeasureSpec(resources.getDimenPixelSize(R.dimen.shop_cart_remove_product_dialog_width), MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED))

        LayoutInflater.from(context).inflate(R.layout.shop_cart_remove_product_dialog, this, true)

        mBinIcon = findViewById(R.id.bin_image)
        mDescription = findViewById(R.id.description)
        mRemoveButton = findViewById(R.id.remove_button)
        mRemoveText = findViewById(R.id.remove_text)
        mRemoveLoading = findViewById(R.id.remove_loading)
        mKeepButton = findViewById(R.id.keep_button)

        addSequences(R.xml.sequences_shop_cart_remove_product_dialog)

        //region Set Attrs
        background = resources.getDrawable(R.drawable.shop_cart_remove_product_dialog_background)

        mBinIcon.setImageDrawable(resources.getDrawable(R.drawable.shop_cart_remove_product_dialog_warning))

        mDescription.setTextColor(resources.getColor(R.color.shop_cart_remove_product_dialog_description_color))
        mDescription.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.shop_cart_remove_product_dialog_description_size).toFloat())
        mDescription.typeface = ResourcesCompat.getFont(context, R.font.iran_yekan_medium)

        val btnFont  = ResourcesCompat.getFont(context, R.font.iran_yekan)

        mRemoveLoading.setPadding(resources.getDimenPixelSize(R.dimen.shop_cart_remove_product_dialog_remove_loading_padding_left), 0, 0, 0)
        mRemoveLoading.indeterminateDrawable.setColorFilter(resources.getColor(R.color.shop_cart_remove_product_dialog_remove_text_color), PorterDuff.Mode.SRC_IN)
        mRemoveLoading.visibility = View.GONE
        mRemoveText.text = resources.getString(R.string.shop_cart_remove_product_dialog_remove)
        btnFont?.let { mRemoveText.typeface = it }
        mRemoveText.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.shop_cart_remove_product_dialog_button_text_size).toFloat())
        mRemoveText.setTextColor(resources.getColor(R.color.shop_cart_remove_product_dialog_remove_text_color))
        mRemoveButton.background = resources.getDrawable(R.drawable.shop_cart_remove_product_dialog_remove_background)
        mRemoveButton.isEnabled = true


        mKeepButton.setText(resources.getString(R.string.shop_cart_remove_product_dialog_keep))
        btnFont?.let { mKeepButton.setTextTypeFace(it) }
        mKeepButton.setTextSize(resources.getDimenPixelSize(R.dimen.shop_cart_remove_product_dialog_button_text_size).toFloat())
        mKeepButton.setTextColor(resources.getColor(R.color.shop_cart_remove_product_dialog_keep_text_color))
        mKeepButton.setDisableBackground(resources.getDrawable(R.drawable.shop_cart_remove_product_dialog_keep_background))
        mKeepButton.setEnabledBackground(resources.getDrawable(R.drawable.shop_cart_remove_product_dialog_keep_background))
        mKeepButton.setEnable(true)
        //endregion
    }

    fun setActions(listener : ShopCartRemoveProductDialog.ShopCartRemoveProductDialogActionListeners){

        mListener = listener


        mKeepButton.setOnClickListener{
            listener.onKeep()
        }

        mRemoveButton.setOnClickListener {

            mKeepButton.setEnable(false)
            mRemoveButton.isEnabled = false

            val lp = mRemoveButton.layoutParams
            lp.width = (DI.getABResources().getDimenPixelSize(R.dimen.shop_cart_remove_product_dialog_button_width) * 1.3).toInt()
            mRemoveButton.layoutParams = lp

            val lp2 = mKeepButton.layoutParams
            lp2.width = (DI.getABResources().getDimenPixelSize(R.dimen.shop_cart_remove_product_dialog_button_width) * 0.7).toInt()
            mKeepButton.layoutParams = lp2

            requestLayout()

            mRemoveLoading.visibility = View.VISIBLE
            listener.onRemove()
        }
    }

    fun setMode(mode: ShopCartRemoveProductDialog.DialogMode) {
        val resources = DI.getABResources()
        when(mode){
            ShopCartRemoveProductDialog.DialogMode.ALL_MODE ->{
                mDescription.text = resources.getString(R.string.shop_cart_remove_product_dialog_all_title)
            }
            ShopCartRemoveProductDialog.DialogMode.SINGLE_MODE ->{
                mDescription.text = resources.getString(R.string.shop_cart_remove_product_dialog_single_title)
            }
        }
    }
}