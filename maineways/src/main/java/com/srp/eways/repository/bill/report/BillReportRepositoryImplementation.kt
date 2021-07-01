package com.srp.eways.repository.bill.report

import androidx.lifecycle.MutableLiveData
import com.srp.eways.di.DIMain
import com.srp.eways.model.bill.report.BillReportItem
import com.srp.eways.model.bill.report.BillReportRequest
import com.srp.eways.model.bill.report.BillReportResponse
import com.srp.eways.network.CallBackWrapper
import com.srp.eways.ui.login.UserInfoViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by ErfanG on 5/23/2020.
 */
class BillReportRepositoryImplementation : BillReportRepository {

    private val mBillReportApiService = DIMain.getBillReportApi()

    //todo
    private var mBillsLive = MutableLiveData<BillReportResponse>()
    private var mLoading = MutableLiveData<Boolean>()
    private var mHasMore = MutableLiveData<Boolean>()
    private var mError = MutableLiveData<String>()

    private val mPageSize = 20
    private var mPageIndex = 0

    init {
        mHasMore.value = true
    }

    override fun getBillReports() {

        if(mHasMore.value != null && mHasMore.value!!){

            mLoading.value = true

            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val date = Calendar.getInstance()

            val to = dateFormat.format(date.time)

            date.add(Calendar.DATE, -15)
            val from = dateFormat.format(date.time)



            val body = BillReportRequest(userId = DIMain.getViewModel(UserInfoViewModel::class.java).getUserInfoLiveData().value!!.userId!!,
                    pageIndex = mPageIndex, pageSize = mPageSize,
                    createDateFrom = from, createDateTo = to)

            val callBack = object : CallBackWrapper<BillReportResponse>{
                override fun onError(errorCode: Int, errorMessage: String?) {
                    mLoading.value = false
                    mError.value = errorMessage
                }

                override fun onSuccess(responseBody: BillReportResponse) {

                    mLoading.value = false
                    mError.value = null
                    mHasMore.value = (mPageIndex + 1) * mPageSize < responseBody.rowCount

                    mBillsLive.value = responseBody

                    if (responseBody.data.size == mPageSize){
                        mPageIndex++
                    }
                }
            }

            mBillReportApiService.getBillReports(body, callBack)
        }
        else{
            //not more items exit
        }
    }




    override fun getBillList() = mBillsLive
    override fun isLoading() = mLoading
    override fun hasMore() = mHasMore
    override fun getError() = mError

    override fun consumeData() {

        mPageIndex = 0

        mLoading.value = false
        mHasMore.value = true
        mError.value = null

        if(mBillsLive.value != null){
            mBillsLive.value = null
        }

    }
}