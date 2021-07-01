package com.srp.ewayspanel.ui.club

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import com.srp.ewayspanel.R
import com.srp.ewayspanel.di.DI
import com.srp.ewayspanel.ui.view.shopitem.ShopItemView

class ClubItemViewVertical : ClubItem {


    constructor(context: Context) : super(context) {
        initialize(context, null, 0)
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        initialize(context, attributeSet, 0)
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyleAtr: Int) : super(
        context,
        attributeSet,
        defStyleAtr
    ) {
        initialize(context, attributeSet, defStyleAtr)
    }

    override fun initialize(context: Context, attributeSet: AttributeSet?, defStyleAtr: Int) {
        super.initialize(context, attributeSet, defStyleAtr)

        val resources = DI.getABResources()

        background = resources.getDrawable(R.drawable.shop_item_background)
    }

    override fun getView(): Int {
        return R.layout.club_item
    }

    override fun getSequence(): Int {
        return R.xml.sequences_club_item
    }

    override fun setIsBuy(isBuy: Boolean) {
        val resources = DI.getABResources()

        background = if (isBuy) {
            resources.getDrawable(R.drawable.shop_item_background_buy)

        } else {
            resources.getDrawable(R.drawable.shop_item_background)
        }
    }

    override fun getIsHorizontal(): Boolean {
        return false
    }


}