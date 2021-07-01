package com.srp.eways.ui.transaction.charge

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.srp.eways.R
import com.srp.eways.base.BaseRecyclerAdapter
import com.srp.eways.di.DIMain
import com.srp.eways.model.transaction.charge.ChargeSale
import com.srp.eways.ui.bill.ShowMoreClickListener
import com.srp.eways.ui.view.transacionitem.ChargeTransactionItem
import com.srp.eways.util.JalaliCalendar
import com.srp.eways.util.PersianPriceFormatter

/**
 * Created by ErfanG on 04/09/2019.
 */
class ChargeTransactionListAdapter : BaseRecyclerAdapter<ChargeSale, ChargeTransactionListAdapter.ChargeViewHolder> {

    private val mOnChargeTransactionItemClickListener: OnChargeTransactionItemClickListener

    private val mContext: Context

    constructor(context: Context,
                retryListener: RetryClickListener,
                onChargeTransactionItemClickListener: OnChargeTransactionItemClickListener) : super(context, retryListener) {
        mContext = context

        mOnChargeTransactionItemClickListener = onChargeTransactionItemClickListener
    }

    override fun onCreateViewHolder2(parent: ViewGroup, viewType: Int): ChargeViewHolder {

        val abResources = DIMain.getABResources()

        val itemView: View = ChargeTransactionItem(parent.context)
        val marginLayoutParams = ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        marginLayoutParams.setMargins(abResources.getDimenPixelSize(R.dimen.item_charge_transaction_result_width),
                abResources.getDimenPixelSize(R.dimen.salereport_item_view_margin_top),
                abResources.getDimenPixelSize(R.dimen.item_charge_transaction_result_width),
                abResources.getDimenPixelSize(R.dimen.salereport_item_view_margin_top))

        itemView.layoutParams = marginLayoutParams

        return ChargeViewHolder(
                itemView, onChargeTransactionItemClickListener = mOnChargeTransactionItemClickListener)

    }

    override fun onBindViewHolder2(holder: RecyclerView.ViewHolder, position: Int) {
        val chargeSale = getData()[position]

        if (holder is ChargeViewHolder && chargeSale != null) {
            holder.chargeSale = chargeSale
            chargeSale.customerMobile?.let { holder.setPhoneNumber(it) }
            holder.setValue(chargeSale.paymentValue)
            holder.setResult(chargeSale.deliverStatus)
            holder.setType(chargeSale.productName)
            holder.setMoreDetail(chargeSale.paymentId.toString())
            var dateTime = chargeSale.paymentDate?.split(" ")

            if(dateTime != null){
                holder.setTime(dateTime[1].substring(0, 5))
                holder.setDate(dateTime[0])
                holder.setDateTimeVisibility(true)
            }
            else{
                holder.setTime("")
                holder.setDate("")
                holder.setDateTimeVisibility(false)
            }

        }
    }

    class ChargeViewHolder : RecyclerView.ViewHolder {

        val item: ChargeTransactionItem

        var chargeSale: ChargeSale? = null

        constructor(view: View, onChargeTransactionItemClickListener: OnChargeTransactionItemClickListener) : super(view) {
            item = view as ChargeTransactionItem

            item.setOnShowMoreClickListener(object : ShowMoreClickListener {
                override fun onShowMoreClicked(isShowMore: Boolean) {
                    onChargeTransactionItemClickListener.onShowMoreClickListener(isShowMore, chargeSale)
                }
            })

//            view.setOnClickListener(View.OnClickListener { onChargeTransactionItemClickListener.onChargeTransactionItemClicked(view, adapterPosition, chargeSale) })
        }

        fun setPhoneNumber(phone: String) {
            item.setPhoneNumber(phone)
        }

        fun setResult(newResult: Int) {
            item.setResult(newResult)
        }

        fun setType(newType: String) {
            item.setType(newType)
        }

        fun setDate(date: String) {
            if(!date.isNullOrEmpty()) {
                val parts = date.split("/")

                item.setDate("" + parts[2].toInt() + " " + JalaliCalendar.getPersianMonthName(parts[1].toInt()))
            }
            else{
                item.setDate("")
            }
        }

        fun setTime(time: String) {
            item.setTime(time)
        }

        fun setValue(newValue: Int) {
            item.setTransactionValue(newValue)
        }

        fun setMoreDetail(paymentId: String) {
            item.setShowMore(chargeSale!!.isShowMore)
            item.setMoreDetails(paymentId)
        }

        fun setDateTimeVisibility(visibility: Boolean) {

            if(visibility) {
                item.mDateIcon.visibility = View.VISIBLE
                item.mTimeIcon.visibility = View.VISIBLE
            }
            else{
                item.mDateIcon.visibility = View.INVISIBLE
                item.mTimeIcon.visibility = View.INVISIBLE
            }
        }
    }
}