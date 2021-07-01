package com.srp.eways.ui.bill.payment

import androidx.lifecycle.MutableLiveData
import com.srp.eways.di.DIMain
import com.srp.eways.model.bill.BillPaymentResponse
import com.srp.eways.model.bill.archivedList.BillPaymentDetail
import com.srp.eways.model.bill.payment.BillInquiryPayResponse
import com.srp.eways.network.BaseCallBackWrapper
import com.srp.eways.ui.bill.MainBillViewModel

class BillPaymentViewModel : MainBillViewModel() {
    private var mBillPaymentDetailLiveData: MutableLiveData<BillPaymentDetail> = MutableLiveData()
    private val mSaveBillsResponseLiveData = MutableLiveData<BillPaymentResponse>()
    private val mPayBillsResponseLiveData = MutableLiveData<BillPaymentResponse>()
    private val mBillPayLoading = MutableLiveData<Boolean>()

    private val mInquiryResponseLiveData = MutableLiveData<BillInquiryPayResponse>()

    private val mBillSaveLoading = MutableLiveData<Boolean>()

    private val mBillInquiryPayRepo = DIMain.getBillInquiryPayRepo()

    companion object {
        private var instance: BillPaymentViewModel? = null
        fun getInstance(): BillPaymentViewModel {

            if (instance == null) {
                instance = BillPaymentViewModel()
            }
            return instance as BillPaymentViewModel
        }
    }

    fun setBillPaymentDetail(billPaymentDetail: BillPaymentDetail) {
        mBillPaymentDetailLiveData.value = billPaymentDetail
    }

    fun getPaymentLiveData(): MutableLiveData<BillPaymentDetail> {
        return mBillPaymentDetailLiveData
    }

    fun getInquiryPaymentLiveData(): MutableLiveData<BillInquiryPayResponse> {
        return mInquiryResponseLiveData
    }

    fun consumeBillPaymentDetailLiveData() {
        mBillPaymentDetailLiveData.value = null
    }

    fun consumeBillInquiryPaymentLiveData() {
        mInquiryResponseLiveData.value = null
    }

    fun consumeSaveResponseLiveData() {
        mSaveBillsResponseLiveData.value = null
    }

    fun getSaveResponseLiveData() = mSaveBillsResponseLiveData
    fun getPayResponseLiveData() = mPayBillsResponseLiveData

    fun isBillPayRequestInProgress() = mBillPayLoading
    fun isBillSaveRequestInProgress() = mBillSaveLoading


    fun save(billDetail: BillPaymentDetail) {
        mBillSaveLoading.value = true

        val saveList = ArrayList<BillPaymentDetail>()

        saveList.add(BillPaymentDetail(billDetail.billId, billDetail.payId))

        saveTempBills(saveList, mSaveBillsResponseLiveData, mBillSaveLoading)
    }

    fun pay(billDetail: BillPaymentDetail, bankId: Int) {

        mBillPayLoading.value = true

        val payList = ArrayList<BillPaymentDetail>()
        payList.add(billDetail)

        payBills(payList, bankId, mPayBillsResponseLiveData, mBillPayLoading)

    }

    fun getBillDetail(billId: String, payId: String) {
        mBillInquiryPayRepo.getBillDetail(billId, payId, object : BaseCallBackWrapper<BillInquiryPayResponse>(this) {
            override fun onError(errorCode: Int, errorMessage: String) {
                var billInquiryPayResponse = BillInquiryPayResponse(null, errorCode, errorMessage)

                mInquiryResponseLiveData.value = billInquiryPayResponse
            }

            override fun onSuccess(responseBody: BillInquiryPayResponse?) {
                mInquiryResponseLiveData.value = responseBody
            }

        })
    }

    fun consumeData() {
        mPayBillsResponseLiveData.value = null
        mSaveBillsResponseLiveData.value = null

        mBillSaveLoading.value = false
        mBillPayLoading.value = false

        mInquiryResponseLiveData.value = null
        mBillPaymentDetailLiveData.value = null
    }
}