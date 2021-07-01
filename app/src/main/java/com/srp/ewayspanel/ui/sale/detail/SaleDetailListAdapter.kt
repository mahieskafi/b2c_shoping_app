package com.srp.ewayspanel.ui.sale.detail

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import com.srp.eways.base.BaseRecyclerAdapter2
import com.srp.eways.base.BaseViewHolder
import com.srp.ewayspanel.R
import com.srp.ewayspanel.di.DI
import com.srp.ewayspanel.model.sale.detail.SaleDetailReportItem

class SaleDetailListAdapter constructor(mContext: Context, private val mActionClickListener: ActionsListener) :
        BaseRecyclerAdapter2<SaleDetailReportItem, SaleDetailViewHolder>(mContext) {

    interface ActionsListener {
        fun onShowMoreClickListener(isShowMore: Boolean, saleDetailReportItem: SaleDetailReportItem)
    }


    override fun onCreateViewHolder2(parent: ViewGroup, viewType: Int): SaleDetailViewHolder {

        val abResources = DI.getABResources()

        val itemView: View = SaleDetailItemView(parent.context)
        val marginLayoutParams = MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        marginLayoutParams.setMargins(0,
                abResources.getDimenPixelSize(R.dimen.salereport_item_view_margin_top),
                0,
                abResources.getDimenPixelSize(R.dimen.salereport_item_view_margin_top))

        itemView.layoutParams = marginLayoutParams

        return SaleDetailViewHolder(itemView, mActionClickListener)
    }


    override fun onBindViewHolder(holder: BaseViewHolder<*, *>, position: Int) {
        if (position == itemCount) {
            holder.onBind(null)
        } else if (position < data.size) {
            super.onBindViewHolder(holder, position)
        }
    }


}