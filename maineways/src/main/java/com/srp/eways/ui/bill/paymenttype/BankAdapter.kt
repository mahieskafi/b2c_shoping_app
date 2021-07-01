package com.srp.eways.ui.bill.paymenttype

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.srp.eways.model.deposit.Bank
import com.srp.eways.ui.view.bill.paymenttype.BankItemView

class BankAdapter : RecyclerView.Adapter<BankAdapter.BankViewHolder> {

    interface BankItemClickListener {
        fun onBankClickListener(gId: Int)
    }

    private var mBankList: List<Bank> = arrayListOf()
    private var mBankClickListener: BankItemClickListener

    constructor(bankItemSelectedListener: BillPaymentTypeFragment.BankItemSelectedListener) : super() {

        mBankClickListener = object : BankItemClickListener {
            override fun onBankClickListener(gId: Int) {
                if (mBankList.isNotEmpty()) {
                    for (i in mBankList.indices) {
                        when (mBankList[i].gId == gId) {
                            true -> {
                                mBankList[i].selcted = true
                                bankItemSelectedListener.onBankItemSelected(mBankList[i])
                            }
                            false -> mBankList[i].selcted = false
                        }
                    }
                    notifyDataSetChanged()
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BankViewHolder {
        val view: View = BankItemView(parent.context)

        return BankViewHolder(view, mBankClickListener)
    }

    override fun onBindViewHolder(holder: BankViewHolder, position: Int) {
        holder.bind(mBankList[position])

    }

    override fun getItemCount(): Int {
        return mBankList.size
    }

    fun setData(bankList: List<Bank>) {
        mBankList = bankList
        notifyDataSetChanged()
    }

    class BankViewHolder : RecyclerView.ViewHolder {

        var mListener: BankItemClickListener

        constructor(itemView: View, listener: BankItemClickListener) : super(itemView) {
            mListener = listener
        }

        fun bind(bank: Bank) {
            var bankItemView: BankItemView = (itemView as BankItemView)

            bankItemView.setBank(bank)
            bankItemView.setBankClickListener(listener = mListener)
        }
    }
}