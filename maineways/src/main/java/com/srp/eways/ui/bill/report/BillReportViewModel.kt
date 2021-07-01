package com.srp.eways.ui.bill.report

import androidx.lifecycle.MutableLiveData
import com.srp.eways.base.BaseViewModel
import com.srp.eways.di.DIMain
import com.srp.eways.ui.bill.MainBillViewModel
import com.srp.eways.ui.bill.receipt.ReceiptViewModel

/**
 * Created by ErfanG on 5/23/2020.
 */
class BillReportViewModel : MainBillViewModel() {

    companion object {
        private var instance: BillReportViewModel? = null
        fun getInstance(): BillReportViewModel {

            if (instance == null) {
                instance = BillReportViewModel()
            }
            return instance as BillReportViewModel
        }
    }

    private val mBillReportRepo = DIMain.getBillReportRepo()


    fun getBills(){

        mBillReportRepo.getBillReports()

    }

    fun refreshBillList(){
        mBillReportRepo.consumeData()

        getBills()
    }


    fun getBillsLive() = mBillReportRepo.getBillList()
    fun hasMore() = mBillReportRepo.hasMore()
    fun getError() = mBillReportRepo.getError()
    override fun isLoading(): MutableLiveData<Boolean> = mBillReportRepo.isLoading()

    fun clearData(){
        mBillReportRepo.consumeData()
    }

}