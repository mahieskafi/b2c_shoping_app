package com.srp.eways.ui.charge.buy.a

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.srp.eways.R
import com.srp.eways.di.DIMain
import com.srp.eways.model.charge.result.topinquiry.Item
import com.srp.eways.ui.bill.ShowMoreClickListener
import com.srp.eways.ui.transaction.charge.inquiry.InquiryTransactionAdapter
import com.srp.eways.ui.view.inquiryitem.ChargeInquiryItem
import com.srp.eways.util.Utils

/**
 * Created by ErfanG on 1/11/2020.
 */
class ChargeTopInquiryAdapter : RecyclerView.Adapter<ChargeTopInquiryAdapter.TopInquiryViewHolder> {

    private val mContext: Context
    private var mRepeatTransactionClickListener: InquiryTransactionAdapter.RepeatTransactionClickListener

    constructor(
        context: Context,
        repeatTransactionClickListener: InquiryTransactionAdapter.RepeatTransactionClickListener
    ) {
        mContext = context
        mRepeatTransactionClickListener = repeatTransactionClickListener
    }

    private var mData = ArrayList<Item>()

    fun setData(itemList: ArrayList<Item>) {
        mData = itemList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopInquiryViewHolder {

        val abResources = DIMain.getABResources()

        val itemView: View = ChargeInquiryItem(context = mContext)

        val marginLayoutParams = ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        marginLayoutParams.setMargins(abResources.getDimenPixelSize(R.dimen.item_charge_transaction_result_width),
                abResources.getDimenPixelSize(R.dimen.salereport_item_view_margin_top),
                abResources.getDimenPixelSize(R.dimen.item_charge_transaction_result_width),
                abResources.getDimenPixelSize(R.dimen.salereport_item_view_margin_top))

        marginLayoutParams.height = abResources.getDimenPixelSize(R.dimen.item_charge_transaction_height)
        itemView.layoutParams = marginLayoutParams

        return TopInquiryViewHolder(itemView)

    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: TopInquiryViewHolder, position: Int) {

        val inquiryItem = mData[position]

        holder.setTopInquiry(inquiryItem)
        holder.setPhoneNumber(inquiryItem.customerMobile)
        holder.setValue(inquiryItem.saleUnitPrice.toInt())
        holder.setResult(inquiryItem.deliverStatus)
        holder.setType(inquiryItem.productName)
        holder.setRetryListener(mRepeatTransactionClickListener)

        val date: Array<String> = inquiryItem.requestDate.split(" ").toTypedArray()
        holder.setTime(date[1].substring(0, 5))
        holder.setDate(date[0])
    }


    class TopInquiryViewHolder : RecyclerView.ViewHolder {

        val item: ChargeInquiryItem


        constructor(view: View) : super(view) {
            item = view as ChargeInquiryItem

//            item.setOnShowMoreClickListener(object : ShowMoreClickListener {
//                override fun onShowMoreClicked(isShowMore: Boolean) {
//                    onChargeTransactionItemClickListener.onShowMoreClickListener(isShowMore, chargeSale)
//                }
//            })
        }

        fun setPhoneNumber(phone: String) {
            item.setPhoneNumber(phone)
        }

        fun setResult(newResult: Int) {
            item.setResult(newResult)
        }

        fun setType(newType: String) {

//            var chargValue = newType.filter { it.isDigit() }
//
//            if (chargValue.isNotEmpty()){
//                val parts = newType.split(chargValue)
//                chargValue=Utils.toPersianPriceNumber(chargValue)
//                item.setType(parts[0]+chargValue + parts[1])
//            }else{
                item.setType(newType)
//            }
        }

        fun setTopInquiry(topInquiry: Item) {
            item.setMoreIconVisibility(false)
            item.setInquiryTopup(topInquiry)
        }

        fun setDate(date: String) {
            val parts = date.split("/")

            item.setDate(Utils.getFormattedDate(DIMain.getContext(), parts[0].toInt(), parts[1].toInt() - 1, parts[2].toInt()))
        }


        fun setTime(time: String) {
            item.setTime(time)
        }

        fun setValue(newValue: Int) {
            item.setTransactionValue(newValue)
        }

        fun setRetryListener(repeatTransactionClickListener: InquiryTransactionAdapter.RepeatTransactionClickListener) {
            item.setRepeatTransactionClickListener(repeatTransactionClickListener)
        }
    }
}