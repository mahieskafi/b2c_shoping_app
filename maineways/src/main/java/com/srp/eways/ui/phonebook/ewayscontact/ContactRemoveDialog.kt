package com.srp.eways.ui.phonebook.ewayscontact

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.ViewGroup

class ContactRemoveDialog : Dialog {
    
    interface ContactRemoveDialogActionListeners{

        fun onClose()

        fun onRemove()

        fun onKeep()
    }

    private lateinit var mView : ContactRemoveDialogView
    private var mListener : ContactRemoveDialogActionListeners

    constructor(context: Context, listener : ContactRemoveDialogActionListeners) : super(context){
        mListener = listener
        setCancelable(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window!!.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        mView = ContactRemoveDialogView(context)
        mView.setActions(mListener)
        setContentView(mView)
    }
}