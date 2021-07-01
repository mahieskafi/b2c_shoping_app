package com.srp.eways.repository.remote.bill.archive

import com.srp.eways.model.bill.archivedList.BillTempRemoveResponse
import com.srp.eways.model.bill.archivedList.BillTempResponse
import com.srp.eways.network.CallBackWrapper

interface BillArchiveApiService {
    fun getTempBills(pageIndex: Int, pageSize: Int, callBackWrapper: CallBackWrapper<BillTempResponse>)

    fun removeTempBills(id: Int, callBackWrapper: CallBackWrapper<BillTempRemoveResponse>)
}