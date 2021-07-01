package com.srp.eways.ui.transaction.deposit

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.srp.eways.base.BaseRecyclerAdapter
import com.srp.eways.R
import com.srp.eways.di.DIMain
import com.srp.eways.model.deposit.transaction.DepositTransactionItem
import com.srp.eways.model.transaction.charge.ChargeSale
import com.srp.eways.ui.bill.ShowMoreClickListener
import com.srp.eways.ui.transaction.charge.OnChargeTransactionItemClickListener
import com.srp.eways.ui.view.inquiryitem.ChargeInquiryItem
import com.srp.eways.ui.view.transacionitem.ChargeTransactionItem
import com.srp.eways.util.JalaliCalendar

/**
 * Created by ErfanG on 08/09/2019.
 */
class DepositTransactionListAdapter : BaseRecyclerAdapter<DepositTransactionItem, DepositTransactionListAdapter.DepositViewHolder> {

    private val mContext: Context
    private val mOnDepositTransactionItemClickListener: com.srp.eways.ui.view.transacionitem.DepositTransactionItem.OnDepositTransactionItemClickListener

    constructor(context: Context, retryListener: RetryClickListener,
                onDepositTransactionItemClickListener: com.srp.eways.ui.view.transacionitem.DepositTransactionItem.OnDepositTransactionItemClickListener) : super(context, retryListener) {
        mContext = context
        mOnDepositTransactionItemClickListener = onDepositTransactionItemClickListener
    }

    override fun onCreateViewHolder2(parent: ViewGroup, viewType: Int): DepositViewHolder {

        val abResources = DIMain.getABResources()

        val itemView: View = com.srp.eways.ui.view.transacionitem.DepositTransactionItem(mContext)

        val marginLayoutParams = ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        marginLayoutParams.setMargins(abResources.getDimenPixelSize(R.dimen.item_charge_transaction_result_width),
                abResources.getDimenPixelSize(R.dimen.salereport_item_view_margin_top),
                abResources.getDimenPixelSize(R.dimen.item_charge_transaction_result_width),
                abResources.getDimenPixelSize(R.dimen.salereport_item_view_margin_top))

//        marginLayoutParams.height = abResources.getDimenPixelSize(R.dimen.item_deposit_transaction_back1_height)
        itemView.layoutParams = marginLayoutParams

        return DepositViewHolder(itemView, mOnDepositTransactionItemClickListener)

    }

    override fun onBindViewHolder2(holder: RecyclerView.ViewHolder, position: Int) {
        val depositSale = getData().get(position)

        if (holder is DepositViewHolder && depositSale != null) {

            val AB = DIMain.getABResources()

            holder.depositTransaction = depositSale
            holder.setTrackingNumber(depositSale.requestId)
            holder.setValue(depositSale.payment.toInt())
            holder.setResult(depositSale.status)
            holder.setType(AB.getString(R.string.deposit_transaction_type))
            val dateTime = depositSale.requestDate?.split(" ")
            holder.setTime(dateTime!![1].substring(0, 5))
            holder.setDate(dateTime!![0])
            holder.setMoreDetail(depositSale.paymentId.toString())
        }
    }

    class DepositViewHolder : RecyclerView.ViewHolder {
        val item: com.srp.eways.ui.view.transacionitem.DepositTransactionItem

        var depositTransaction: DepositTransactionItem? = null

        constructor(view: View, onDepositTransactionItemClickListener: com.srp.eways.ui.view.transacionitem.DepositTransactionItem.OnDepositTransactionItemClickListener) : super(view) {
            item = view as com.srp.eways.ui.view.transacionitem.DepositTransactionItem

            item.setOnShowMoreClickListener(object : ShowMoreClickListener {
                override fun onShowMoreClicked(isShowMore: Boolean) {
                    onDepositTransactionItemClickListener.onShowMoreClickListener(isShowMore, depositTransaction!!)
                }
            })
        }

        fun setTrackingNumber(trackingNumber: Int) {
            item.setTrackingNumber(trackingNumber.toString())
        }

        fun setResult(newResult: Int) {
            item.setResult(newResult)
        }

        fun setType(newType: String) {
            item.setType(newType)
        }

        fun setDate(date: String) {
            val parts = date.split("/")

            item.setDate("" + parts[2].toInt() + " " + JalaliCalendar.getPersianMonthName(parts[1].toInt()))
        }

        fun setTime(time: String) {
            item.setTime(time)
        }

        fun setValue(newValue: Int) {
            item.setTransactionValue(newValue)
        }

        fun setMoreDetail(paymentId: String) {
            item.setMoreDetails(paymentId)
            item.setShowMore(depositTransaction!!.isShowMore)
        }

    }
}