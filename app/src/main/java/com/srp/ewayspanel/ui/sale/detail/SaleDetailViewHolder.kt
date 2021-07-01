package com.srp.ewayspanel.ui.sale.detail

import android.view.View
import com.srp.eways.base.BaseViewHolder
import com.srp.eways.ui.bill.ShowMoreClickListener
import com.srp.eways.util.Utils
import com.srp.ewayspanel.model.sale.detail.SaleDetailReportItem

class SaleDetailViewHolder constructor(mView: View, actionClickListener: SaleDetailListAdapter.ActionsListener) : BaseViewHolder<SaleDetailReportItem, View>(mView) {

    private val mActionClickListener = actionClickListener

    override fun onBind(item: SaleDetailReportItem) {

        val itemView = view as SaleDetailItemView

        itemView.setTypeTitle(item.productName)

        itemView.setProductTitle(item.transactionStatusTitle)

        itemView.setBillId(item.paymentId)

        itemView.setPrice(item.paymentValue)

        if (item.paymentDate != null) {
            itemView.setDate(item.paymentDate.split(" ")[0])
            itemView.setTime(item.paymentDate.split(" ")[1].split(":")[0] + ":" +
                    item.paymentDate.split(" ")[1].split(":")[1])
        } else {
            itemView.setDate("")
            itemView.setTime("")
        }

        val details = arrayListOf<String>()
        details.add(item.bankName)
        details.add(item.userName)
        details.add(Utils.toPersianNumber(item.merchanestShare))
//        details.add("")
        details.add(Utils.toPersianNumber(item.requestQuantity))
        details.add(Utils.toPersianNumber(item.agent))
        details.add(Utils.toPersianNumber(item.customerMobile))

        itemView.setMoreDetails(details)

        itemView.setShowMore(item.isShowMore)

        itemView.setOnShowMoreClickListener(object : ShowMoreClickListener {
            override fun onShowMoreClicked(isShowMore: Boolean) {
                mActionClickListener.onShowMoreClickListener(isShowMore, item)
            }
        })
    }
}