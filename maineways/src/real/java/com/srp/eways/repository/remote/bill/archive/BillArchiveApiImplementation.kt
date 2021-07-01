package com.srp.eways.repository.remote.bill.archive

import com.srp.eways.AppConfig
import com.srp.eways.di.DIMain
import com.srp.eways.model.bill.archivedList.BillTempRemoveResponse
import com.srp.eways.model.bill.archivedList.BillTempResponse
import com.srp.eways.network.CallBackWrapper
import com.srp.eways.repository.remote.DefaultRetroCallback

class BillArchiveApiImplementation : BillArchiveApiService {

    private val billArchiveApiRetrofit: BillArchiveApiRetrofit = DIMain.provideApi(BillArchiveApiRetrofit::class.java)

    companion object {
        val instant = BillArchiveApiImplementation()
    }

    override fun getTempBills(pageIndex: Int, pageSize: Int, callBackWrapper: CallBackWrapper<BillTempResponse>) {
        billArchiveApiRetrofit.getTempBills(AppConfig.SERVER_VERSION, pageIndex, pageSize).enqueue(object : DefaultRetroCallback<BillTempResponse>(callBackWrapper) {
            override fun checkResponseForError(response: BillTempResponse?, errorInfo: ErrorInfo?) {
                if (response?.status != 0) {
                    errorInfo?.errorCode = response?.status
                    errorInfo?.errorMessage = response?.description
                }
            }
        })
    }

    override fun removeTempBills(id: Int, callBackWrapper: CallBackWrapper<BillTempRemoveResponse>) {
        billArchiveApiRetrofit.removeTempBill(AppConfig.SERVER_VERSION, id).enqueue(object : DefaultRetroCallback<BillTempRemoveResponse>(callBackWrapper) {
            override fun checkResponseForError(response: BillTempRemoveResponse?, errorInfo: ErrorInfo?) {
                if (response?.status != 0) {
                    errorInfo?.errorCode = response?.status
                    errorInfo?.errorMessage = response?.description
                }
            }
        })
    }
}