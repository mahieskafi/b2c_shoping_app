package com.srp.eways.ui.bill.inquiry

import androidx.lifecycle.MutableLiveData
import com.srp.eways.di.DIMain
import com.srp.eways.model.bill.BillPaymentResponse
import com.srp.eways.model.bill.archivedList.BillPaymentDetail
import com.srp.eways.model.bill.inquiry.BillInquiryRequest
import com.srp.eways.model.bill.inquiry.BillInquiryResponse
import com.srp.eways.model.bill.inquiry.TermBill
import com.srp.eways.network.BaseCallBackWrapper
import com.srp.eways.ui.bill.MainBillViewModel
import com.srp.eways.util.Utils

/**
 * Created by ErfanG on 4/28/2020.
 */
class BillInquiryViewModel : MainBillViewModel() {

    companion object {

        fun newInstance() = BillInquiryViewModel()
    }

    private val mSelectedTerm = MutableLiveData<TermBill>()
    private val mBillInquiryRepo = DIMain.getBillInquiryRepo()

    private lateinit var mBillType: BillType
    private lateinit var mPrePhone: String
    private lateinit var mMainNumber: String

    private val mAllowToRequest = MutableLiveData<Boolean>()
    private val mBillRequestLoading = MutableLiveData<Boolean>()
    private val mBillRequestError = MutableLiveData<String>()
    private val mBillDetail = MutableLiveData<BillInquiryResponse>()

    private val mSaveBillsResponseLiveData = MutableLiveData<BillPaymentResponse>()
    private val mPayBillsResponseLiveData = MutableLiveData<BillPaymentResponse>()

    private val mBillPayLoading = MutableLiveData<Boolean>()
    private val mBillSaveLoading = MutableLiveData<Boolean>()

    fun updateBillType(billType: BillType) {
        setBillType(billType)

        mMainNumber = ""
    }

    fun setBillType(billType: BillType){
        mBillType = billType
    }

    fun getBillType() = mBillType

    fun setMainNumber(billOrPhoneNumber: String) {

        mMainNumber = billOrPhoneNumber

        validateBillRequestInformation()
    }

    fun setPrePhone(prePhone: String) {

        mPrePhone = prePhone

        validateBillRequestInformation()
    }

    private fun validateBillRequestInformation() {

        when (mBillType) {
            BillType.GAS, BillType.ELECTRICITY, BillType.WATER -> {
                mAllowToRequest.value = mMainNumber.length == 13

            }
            BillType.MOBILE -> {

                mAllowToRequest.value = Utils.isPhoneNumberValid(mMainNumber)

            }
            BillType.PHONE -> {

                mAllowToRequest.value = (this::mMainNumber.isInitialized && this::mPrePhone.isInitialized)
                        && (mMainNumber.length == 8 && mPrePhone.length == 3)
                //TODO check validity for phone and prePhone numbers
            }
        }
    }


    fun getBillInquiryDetails() {

        mBillRequestLoading.value = true

        val billInquiryBody = createBillInquiryRequest()

        val billInquiryCallback = object : BaseCallBackWrapper<BillInquiryResponse>(this) {
            override fun onError(errorCode: Int, errorMessage: String?) {

                mBillRequestLoading.value = false
                mBillRequestError.value = errorMessage
                mBillDetail.value = null
            }

            override fun onSuccess(responseBody: BillInquiryResponse) {

                mBillRequestLoading.value = false
                mBillRequestError.value = null
                mBillDetail.value = responseBody
            }

        }

        mBillInquiryRepo.getBillDetails(billInquiryBody, billInquiryCallback)

    }

    private fun createBillInquiryRequest(): BillInquiryRequest {

        return when (mBillType) {
            BillType.GAS, BillType.ELECTRICITY, BillType.WATER -> {
                BillInquiryRequest(billId = mMainNumber)

            }
            BillType.MOBILE -> {

                BillInquiryRequest(mobile = mMainNumber)

            }
            BillType.PHONE -> {

                BillInquiryRequest(phone = mMainNumber, areaCode = mPrePhone)
            }
        }
    }


    fun setSelectedTermBill(termBill: TermBill) {

        mSelectedTerm.value = termBill
    }

    fun getSelectedTermBill() = mSelectedTerm

    fun pay(bankId: Int) {

        if (mSelectedTerm.value != null) {

            mBillPayLoading.value = true

            val payList = ArrayList<BillPaymentDetail>()

            payList.add(BillPaymentDetail(mSelectedTerm.value!!.billId!!, mSelectedTerm.value!!.payId!!, getInquiryNumber()))

            payBills(payList, bankId, mPayBillsResponseLiveData, mBillPayLoading)
        }
    }

    fun save() {
        if (mSelectedTerm.value != null) {

            mBillSaveLoading.value = true

            val saveList = ArrayList<BillPaymentDetail>()

            saveList.add(BillPaymentDetail(mSelectedTerm.value!!.billId!!, mSelectedTerm.value!!.payId!!, getInquiryNumber()))

            saveTempBills(saveList, mSaveBillsResponseLiveData, mBillSaveLoading)
        }
    }

    private fun getInquiryNumber(): String {
        var inquiryNumber = ""
        if (mBillType == BillType.MOBILE) {
            inquiryNumber = mMainNumber
        } else
            if (mBillType == BillType.PHONE) {
                inquiryNumber = mPrePhone + mMainNumber
            }
        return inquiryNumber
    }

    fun isAllowedToRequest() = mAllowToRequest
    fun isBillRequestInProgress() = mBillRequestLoading
    fun getBillDetails() = mBillDetail
    fun getBillRequestError() = mBillRequestError

    fun getSaveResponseLiveData() = mSaveBillsResponseLiveData
    fun getPayResponseLiveData() = mPayBillsResponseLiveData

    fun isBillPayRequestInProgress() = mBillPayLoading
    fun consumeSaveResponseLiveData() {
        mSaveBillsResponseLiveData.value = null
    }

    fun consumeError() {
        mBillRequestError.value = null
    }

    fun consumeBillDetailLiveData() {
        mBillDetail.value = null
    }

    fun isBillSaveRequestInProgress() = mBillSaveLoading


    fun consumeData() {
        mAllowToRequest.value = false
        mBillRequestLoading.value = false
        mBillDetail.value = null
        mBillRequestError.value = null

        mSaveBillsResponseLiveData.value = null
        mPayBillsResponseLiveData.value = null
        mBillPayLoading.value = false
        mBillSaveLoading.value = false

        mSelectedTerm.value = null

        mPrePhone = ""
        mMainNumber = ""
    }

}