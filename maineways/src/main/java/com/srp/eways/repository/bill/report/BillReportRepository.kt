package com.srp.eways.repository.bill.report

import androidx.lifecycle.MutableLiveData
import com.srp.eways.model.bill.report.BillReportResponse

/**
 * Created by ErfanG on 5/23/2020.
 */
interface BillReportRepository {

    fun getBillList() : MutableLiveData<BillReportResponse>
    fun isLoading() : MutableLiveData<Boolean>
    fun hasMore() : MutableLiveData<Boolean>
    fun getError() : MutableLiveData<String>


    fun getBillReports()
    fun consumeData()
}