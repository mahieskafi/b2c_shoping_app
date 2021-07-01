package com.srp.eways.ui.transaction.charge.inquiry

import androidx.lifecycle.MutableLiveData
import com.srp.eways.base.BaseViewModel
import com.srp.eways.di.DIMain
import com.srp.eways.model.transaction.inquiry.InquiryTopup
import com.srp.eways.network.NetworkResponseCodes
import com.srp.eways.repository.chargetransaction.inquiry.InquiryTransactionRepository
import com.srp.ewayspanel.network.RxCallBack
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable

/**
 * Created by Eskafi on 9/8/2019.
 */
class InquiryTransactionViewModel : BaseViewModel() {

    private val mData = MutableLiveData<ArrayList<InquiryTopup>>()
    private var mListIsLoading = MutableLiveData<Boolean>()
    private var mHasMoreData = MutableLiveData<Boolean>()
    private var mErrorCode = MutableLiveData<Int>()

    private val mInquiryRepo: InquiryTransactionRepository

    var dispos: Disposable

    init {

        mData.value = arrayListOf()
        mListIsLoading.value = false
        mHasMoreData.value = false

        mInquiryRepo = DIMain.getInquiryTransactionRepo()

        dispos = mInquiryRepo.getTransactions()
                .subscribeOn(schedulerProvider.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : RxCallBack<ArrayList<InquiryTopup>>() {
                    override fun onComplete() {
                    }

                    override fun onNext(response: ArrayList<InquiryTopup>) {
                        mData.value = response
                    }

                    override fun onError(e: Throwable) {
                    }

                })

        compositeDisposable.add(dispos)

        compositeDisposable.add(mInquiryRepo.isLoading()
                .subscribeOn(schedulerProvider.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : RxCallBack<Boolean>() {
                    override fun onComplete() {
                    }

                    override fun onNext(t: Boolean) {
                        mListIsLoading.value = t
                    }

                    override fun onError(e: Throwable) {
                    }

                }))

        compositeDisposable.add(mInquiryRepo.getErrorCode()
                .subscribeOn(schedulerProvider.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : RxCallBack<Int>() {
                    override fun onComplete() {
                    }

                    override fun onNext(t: Int) {
                        if (t == NetworkResponseCodes.ERROR_AUTHORIZATION_FAILED) {
                            setIsNeededToLogin(true)
                            return
                        }

                        mErrorCode.value = t
                    }

                    override fun onError(e: Throwable) {
                    }

                }))

        compositeDisposable.add(mInquiryRepo.hasMore()
                .subscribeOn(schedulerProvider.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : RxCallBack<Boolean>() {
                    override fun onComplete() {
                    }

                    override fun onNext(t: Boolean) {
                        mHasMoreData.value = t
                    }

                    override fun onError(e: Throwable) {
                    }

                }))

    }

    fun getTransactions(): MutableLiveData<ArrayList<InquiryTopup>> {
        return mData
    }

    fun isListLoading(): MutableLiveData<Boolean> {
        return mListIsLoading
    }

    fun getErrorCode(): MutableLiveData<Int> {
        return mErrorCode
    }

    fun hasMoreData(): MutableLiveData<Boolean> {
        return mHasMoreData
    }

    fun loadMore(mobileNumber: String, startDate: String, endDate: String) {

        mInquiryRepo.loadMore(mobileNumber, startDate, endDate)
    }

    fun clearData() {
        mData.value = ArrayList<InquiryTopup>()
        mListIsLoading.value = false
        mErrorCode.value = NetworkResponseCodes.SUCCESS
        mHasMoreData.value = false
    }
}