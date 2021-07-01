package com.srp.ewayspanel.ui.landing

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.res.ResourcesCompat
import com.srp.eways.di.DIMain
import com.srp.eways.ui.view.ViewUtils
import com.srp.ewayspanel.R

class RecentlyChangesDialog : Dialog {

    private val mTitle: AppCompatTextView
    private val mDescription: AppCompatTextView
    private val mIcon: AppCompatImageView

    constructor(context: Context) : super(context)
    constructor(context: Context, themeResId: Int) : super(context, themeResId)
    constructor(context: Context, cancelable: Boolean, cancelListener: DialogInterface.OnCancelListener?) : super(context, cancelable, cancelListener)

    init {
        setContentView(R.layout.dialog_recently_changes)

        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        window?.setGravity(Gravity.CENTER)

        window?.attributes?.width = ((ViewUtils.getDisplayMetrics(context).widthPixels * 0.8).toInt())
        setCanceledOnTouchOutside(true)

        mTitle = findViewById(R.id.title)
        mDescription = findViewById(R.id.text)
        mIcon = findViewById(R.id.icon)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val abResources = DIMain.getABResources()


        mDescription.setTextSize(TypedValue.COMPLEX_UNIT_PX, abResources.getDimen(R.dimen.dialog_recently_changes_text_size))
        mDescription.setTextColor(abResources.getColor(R.color.dialog_recently_changes_title_color))
        mDescription.typeface = ResourcesCompat.getFont(context, R.font.iran_yekan)

        mTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, abResources.getDimen(R.dimen.dialog_recently_changes_title_text_size))
        mTitle.setTextColor(abResources.getColor(R.color.dialog_recently_changes_title_color))
        mTitle.typeface = ResourcesCompat.getFont(context, R.font.iran_yekan_medium)
        mTitle.text = abResources.getString(R.string.changes_dialog_title)

        mIcon.setImageDrawable(abResources.getDrawable(R.drawable.ic_new_changes_icon))
    }

    fun setDescription(description: String) {
        mDescription.text = description
        show()
    }
}