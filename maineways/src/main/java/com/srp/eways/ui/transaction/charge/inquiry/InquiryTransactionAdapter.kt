package com.srp.eways.ui.transaction.charge.inquiry

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.srp.eways.R
import com.srp.eways.base.BaseRecyclerAdapter
import com.srp.eways.di.DIMain
import com.srp.eways.model.transaction.charge.ChargeSale
import com.srp.eways.model.transaction.inquiry.InquiryTopup
import com.srp.eways.ui.bill.ShowMoreClickListener
import com.srp.eways.ui.transaction.charge.OnChargeTransactionItemClickListener
import com.srp.eways.ui.view.inquiryitem.ChargeInquiryItem
import com.srp.eways.ui.view.transacionitem.ChargeTransactionItem
import com.srp.eways.util.JalaliCalendar

/**
 * Created by Eskafi on 9/8/2019.
 */
class InquiryTransactionAdapter : BaseRecyclerAdapter<InquiryTopup, InquiryTransactionAdapter.InquiryViewHolder> {

    interface RepeatTransactionClickListener {
        fun onItemRepeatTransactionClicked(inquiryTopup: Any)
    }

    private val mContext: Context
    private var mRepeatTransactionClickListener: RepeatTransactionClickListener
    private val mOnChargeTransactionItemClickListener: ChargeInquiryItem.OnChargeTopUpItemClickListener

    constructor(context: Context, retryListener: RetryClickListener, repeatTransactionClickListener: RepeatTransactionClickListener,
                onChargeTransactionItemClickListener: ChargeInquiryItem.OnChargeTopUpItemClickListener) : super(context, retryListener) {
        mContext = context
        mRepeatTransactionClickListener = repeatTransactionClickListener
        mOnChargeTransactionItemClickListener = onChargeTransactionItemClickListener
    }

    override fun onCreateViewHolder2(parent: ViewGroup, viewType: Int): InquiryViewHolder {
        val view = ChargeInquiryItem(context = mContext)

        val abResources = DIMain.getABResources()

        val marginLayoutParams = ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        marginLayoutParams.setMargins(abResources.getDimenPixelSize(R.dimen.item_charge_transaction_result_width),
                abResources.getDimenPixelSize(R.dimen.salereport_item_view_margin_top),
                abResources.getDimenPixelSize(R.dimen.item_charge_transaction_result_width),
                abResources.getDimenPixelSize(R.dimen.salereport_item_view_margin_top))

        view.layoutParams = marginLayoutParams

        return InquiryViewHolder(view, mOnChargeTransactionItemClickListener)
    }

    override fun onBindViewHolder2(holder: RecyclerView.ViewHolder, position: Int) {
        val inquiryTopup = getData().get(position)

        if (holder is InquiryViewHolder && inquiryTopup != null) {

            holder.inquiry = inquiryTopup
            holder.setInquiryTopup(inquiryTopup)
//            holder.setPhoneNumber(inquiryTopup.customerPhoneNumber)
            holder.setValue(inquiryTopup.price!!.toInt())

            //TODO: it is wrong.server must return a number for status. but we did this for temporary version
            if (inquiryTopup.status?.trim().equals("موفق")) {
                holder.setResult(2)
            } else {
                holder.setResult(5)
            }
//            holder.setResult(inquiryTopup.status!!.toInt())
            inquiryTopup.packageName?.let { holder.setType(it) }
//            inquiryTopup.type?.let { holder.setType(it) }

            holder.setRetryVisibility(true)
            holder.setRetryListener(mRepeatTransactionClickListener)
            holder.setMoreDetail(inquiryTopup.paymentId.toString(), inquiryTopup.serialNo.toString())

            val dateTime = inquiryTopup.requestDate!!.split(" ")
            holder.setTime(dateTime[1].substring(0, 5))
            holder.setDate(dateTime[0])
        }
    }

    class InquiryViewHolder : RecyclerView.ViewHolder {
        val item: ChargeInquiryItem

        var inquiry: InquiryTopup? = null

        constructor(view: View, onChargeTransactionItemClickListener: ChargeInquiryItem.OnChargeTopUpItemClickListener) : super(view) {

            item = view as ChargeInquiryItem

            item.setOnShowMoreClickListener(object : ShowMoreClickListener {
                override fun onShowMoreClicked(isShowMore: Boolean) {
                    onChargeTransactionItemClickListener.onShowMoreClickListener(isShowMore, inquiry)
                }
            })

//            view.setOnClickListener(View.OnClickListener { onChargeTransactionItemClickListener.onChargeTransactionItemClicked(view, adapterPosition, chargeSale) })
        }

        fun setInquiryTopup(inquiryTopup: InquiryTopup) {
            item.setInquiryTopup(inquiryTopup)
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

        fun setRetryVisibility(visibility: Boolean) {
            item.setRetryVisibility(visibility)
        }

        fun setMoreDetail(paymentId: String, serialNo: String) {
            item.setMoreIconVisibility(true)
            item.setShowMore(inquiry!!.isShowMore)
            item.setMoreDetails(paymentId, serialNo)
        }

        fun setRetryListener(repeatTransactionClickListener: RepeatTransactionClickListener) {
            item.setRepeatTransactionClickListener(repeatTransactionClickListener)
        }
    }

}