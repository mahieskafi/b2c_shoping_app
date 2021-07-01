package com.srp.ewayspanel.ui.view.shopitem

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import com.srp.ewayspanel.R
import com.srp.ewayspanel.di.DI

class ShopItemViewHorizontal : ShopItemView {

    private lateinit var mRowContainer: View

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

        mRowContainer = findViewById(R.id.row_background)

        val resources = DI.getABResources()

        mCountView.background = resources.getDrawable(R.drawable.horizontal_counter_view_background)
        mRowContainer.background = resources.getDrawable(R.drawable.shop_item_background_horizontal)

        mProductName.setLineSpacing(0f, 1.3f)
        mProductName.minLines = 2

    }

    override fun getView(): Int {
        return R.layout.shop_item_horizontally
    }

    override fun getSequence(): Int {
        return R.xml.sequences_shop_item_horizontally
    }

    override fun setIsBuy(isBuy: Boolean) {
        val resources = DI.getABResources()

        if (isBuy) {
            mRowContainer.background = resources.getDrawable(R.drawable.shop_item_background_buy)

        } else {
            mRowContainer.background = resources.getDrawable(R.drawable.shop_item_background_horizontal)
        }
    }

    override fun getIsHorizontal(): Boolean {
        return true
    }

    override fun setCounterViewVisibility(isAvailable: Boolean) {
        mCountView.counterViewDisable(!isAvailable)

        mCountView.visibility = View.VISIBLE
        mDeleteProduct.visibility = View.VISIBLE

        if (!isAvailable) {
            findSequenceById("seq").spans[0].size = 25f
            findSequenceById("seq1").spans[0].size = 14f
            mProductOffValue.visibility = View.INVISIBLE
        } else {
            findSequenceById("seq").spans[0].size = 34f
            findSequenceById("seq1").spans[0].size = 28f
        }
    }

}