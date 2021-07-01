package com.srp.eways.ui.view.navigationdraweritem

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.srp.eways.R
import com.srp.eways.di.DIMain

class NavigationDrawerSubItem : ViewGroup {

    private val mTitle = TextView(context)


    constructor(context: Context) : super(context)
    {
    }
    constructor(context: Context, atributeSet: AttributeSet?) : super(context, atributeSet)

    init {

        val abResources = DIMain.getABResources()

        setBackgroundColor(abResources.getColor(R.color.navigation_drawer_sub_item_background))

        val typeface = ResourcesCompat.getFont(context, R.font.iran_yekan)
        mTitle.typeface = typeface

        //title
        mTitle.gravity = Gravity.RIGHT
        mTitle.textAlignment = View.TEXT_ALIGNMENT_GRAVITY

        addView(mTitle)

        layoutParams = LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                DIMain.getABResources().getDimenPixelSize(R.dimen.navigation_drawer_sub_item_height))

        requestLayout()
    }


    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {

        val abResources = DIMain.getABResources()
        val viewHeight = abResources.getDimenPixelSize(R.dimen.navigation_drawer_sub_item_height)

        mTitle.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
        mTitle.layout(
                width - abResources.getDimenPixelSize(R.dimen.navigation_drawer_subItem_container_margin_right) - mTitle.measuredWidth,
                viewHeight / 2 - mTitle.measuredHeight / 2,
                width - abResources.getDimenPixelSize(R.dimen.navigation_drawer_subItem_container_margin_right),
                viewHeight / 2 + mTitle.measuredHeight / 2)

    }

    fun setTitleTextColor(color: Int) {
        mTitle.setTextColor(color)
    }

    fun setTitleTextSize(size: Float) {
        mTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, size)
    }

    fun setTitleText(text: String) {
        mTitle.text = text
    }

    fun setAction(action: View.OnClickListener) {
        this.setOnClickListener(action)
    }


}