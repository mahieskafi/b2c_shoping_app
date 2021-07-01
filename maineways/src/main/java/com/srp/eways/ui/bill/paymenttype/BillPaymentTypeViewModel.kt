package com.srp.eways.ui.bill.paymenttype

import androidx.lifecycle.MutableLiveData
import com.srp.eways.model.bill.BillPaymentResponse
import com.srp.eways.model.bill.archivedList.BillPaymentDetail
import com.srp.eways.ui.bill.MainBillViewModel

class BillPaymentTypeViewModel : MainBillViewModel() {

    private var mDeepLinkResponseReceivedLiveData = MutableLiveData<String>()

    private val mPayBillsResponseLiveData = MutableLiveData<BillPaymentResponse>()

    private val mBillPayLoading = MutableLiveData<Boolean>()

    private var mSelectedPage = -1

    private val checkLoading=MutableLiveData<Boolean>()
    private val clearBillInput=MutableLiveData<Boolean>()


    companion object {
        private var instance: BillPaymentTypeViewModel? = null

        fun getInstance(): BillPaymentTypeViewModel {

            if (instance == null) {
                instance = BillPaymentTypeViewModel()
            }
            return instance as BillPaymentTypeViewModel
        }
    }

    fun getPayResponseLiveData() = mPayBillsResponseLiveData

    fun isBillPayRequestInProgress() = mBillPayLoading

    fun pay(billDetail: ArrayList<BillPaymentDetail>, bankId: Int) {

        mBillPayLoading.value = true

        payBills(billDetail, bankId, mPayBillsResponseLiveData, mBillPayLoading)

    }

    fun deepLinkResponseReceived(requestId: String) {
        mDeepLinkResponseReceivedLiveData.value = requestId
    }

    fun getDeepLinkResponseReceivedLiveData(): MutableLiveData<String> {
        return mDeepLinkResponseReceivedLiveData
    }

    fun consumeDeepLinkResponseReceivedLiveData() {
        mDeepLinkResponseReceivedLiveData.value = null
    }

    fun consumePayResponseLiveData() {
        mPayBillsResponseLiveData.value = null
        mBillPayLoading.value = false
    }

    fun setSelectedPageToPay(selectedPage: Int) {
        mSelectedPage = selectedPage
    }

    fun getSelectedPage(): Int {
        return mSelectedPage
    }

    fun setCheckLoading(ch : Boolean){
        checkLoading.value = ch
    }

    fun getCheckLoading(): MutableLiveData<Boolean>{
        return checkLoading
    }

    fun setClearBillInput(ch : Boolean){
        clearBillInput.value = ch
    }

    fun getClearBillInput(): MutableLiveData<Boolean>{
        return clearBillInput
    }
}