package com.srp.ewayspanel.ui.view.shopitem

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.srp.ewayspanel.R
import com.srp.ewayspanel.di.DI

class ShopItemViewVertical : ShopItemView {

    constructor(context: Context) : super(context) {
        initialize(context, null, 0)
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        initialize(context, attributeSet, 0)
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyleAtr: Int) : super(context, attributeSet, defStyleAtr) {
        initialize(context, attributeSet, defStyleAtr)
    }

    override fun initialize(context: Context, attributeSet: AttributeSet?, defStyleAtr: Int) {
        super.initialize(context, attributeSet, defStyleAtr)

        val resources = DI.getABResources()

        background = resources.getDrawable(R.drawable.shop_item_background)

    }

    override fun getView(): Int {
        return R.layout.shop_item
    }

    override fun getSequence(): Int {
        return R.xml.sequences_shop_item
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

    override fun setCounterViewVisibility(isAvailable: Boolean){
        mCountView.counterViewDisable(!isAvailable)
        if (isAvailable) {
            mCountView.visibility = View.VISIBLE
            mDeleteProduct.visibility = View.VISIBLE
        } else {
            mCountView.visibility = View.INVISIBLE
            mDeleteProduct.visibility = View.INVISIBLE
            mProductOffValue.visibility = View.INVISIBLE
        }
    }

}