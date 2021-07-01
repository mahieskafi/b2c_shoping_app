package com.srp.ewayspanel.ui.shopcart.productlist

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.ViewGroup

/**
 * Created by ErfanG on 1/14/2020.
 */
class ShopCartRemoveProductDialog : Dialog {

    interface ShopCartRemoveProductDialogActionListeners{

        fun onClose()

        fun onRemove()

        fun onKeep()
    }

    enum class DialogMode{
        ALL_MODE , SINGLE_MODE
    }

    private lateinit var mView : ShopCartRemoveProductDialogView
    private var mListener : ShopCartRemoveProductDialogActionListeners
    private lateinit var mMode : DialogMode

    constructor(context: Context, listener : ShopCartRemoveProductDialogActionListeners, mode : DialogMode) : super(context){
        mListener = listener
        mMode = mode
        setCancelable(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window!!.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        mView = ShopCartRemoveProductDialogView(context)
        mView.setActions(mListener)
        mView.setMode(mMode)
        setContentView(mView)
    }
}