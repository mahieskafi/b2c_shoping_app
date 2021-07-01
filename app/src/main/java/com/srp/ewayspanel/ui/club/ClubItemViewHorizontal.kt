package com.srp.ewayspanel.ui.club

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.srp.ewayspanel.R
import com.srp.ewayspanel.di.DI

class ClubItemViewHorizontal : ClubItem{
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

    }

    override fun getView(): Int {
        return R.layout.club_item_horizontal
    }

    override fun getSequence(): Int {
        return R.xml.sequences_club_item_horizontal
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
}