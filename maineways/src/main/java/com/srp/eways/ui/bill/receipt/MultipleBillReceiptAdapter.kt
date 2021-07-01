package com.srp.eways.ui.bill.receipt

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.srp.eways.R
import com.srp.eways.di.DIMain
import com.srp.eways.model.bill.archivedList.BillTemp
import com.srp.eways.util.BillUtil

/**
 * Created by ErfanG on 5/10/2020.
 */
class MultipleBillReceiptAdapter(private val mContext : Context)
    : RecyclerView.Adapter<MultipleBillReceiptAdapter.ReceiptViewHolder>() {


    private lateinit var mData : ArrayList<BillTemp>

    fun setData(bills: ArrayList<BillTemp>) {
        mData = bills
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReceiptViewHolder {

        return ReceiptViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.item_bill_receipt_multiple_list, parent, false) as MultipleBillReceiptItemView)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: ReceiptViewHolder, position: Int) {

        holder.bind(mData[position], position)

    }


    inner class ReceiptViewHolder(private val view : MultipleBillReceiptItemView) : RecyclerView.ViewHolder(view){

        private val resources = DIMain.getABResources()

        fun bind(billTemp: BillTemp, position: Int) {

            if(position % 2 == 0){
                view.setBackgroundColor(resources.getColor(R.color.bill_receipt_multiple_item_background_first))
            }
            else{
                view.setBackgroundColor(resources.getColor(R.color.bill_receipt_multiple_item_background_second))
            }

            view.setBillName(BillUtil.getServiceName(billTemp.billTypeId))
            view.setBillPrice(billTemp.price)
            view.setBillId(billTemp.billId)
            view.setPayId(billTemp.paymentId)
//            view.setStatus(billTemp.status)

        }

    }

}