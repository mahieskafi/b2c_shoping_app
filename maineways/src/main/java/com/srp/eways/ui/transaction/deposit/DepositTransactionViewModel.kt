package com.srp.eways.ui.transaction.deposit

import androidx.lifecycle.MutableLiveData
import com.srp.eways.base.BaseViewModel
import com.srp.eways.di.DIMain
import com.srp.eways.model.deposit.transaction.DepositTransactionItem
import com.srp.eways.network.NetworkResponseCodes
import com.srp.eways.repository.deposit.transacton.DepositTransactionRepository
import com.srp.eways.util.rx.SchedulerProvider
import com.srp.ewayspanel.network.RxCallBack
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * Created by ErfanG on 08/09/2019.
 */
open class DepositTransactionViewModel : BaseViewModel {

    private val mData = MutableLiveData<ArrayList<DepositTransactionItem>>()
    private val mListIsLoading = MutableLiveData<Boolean>()
    private val mHasMoreData = MutableLiveData<Boolean>()
    private val mErrorCode = MutableLiveData<Int>()

    private var mDepositTransactionInvalidated: Boolean
    private var mLastDataUpdateTime: Long

    private var mIsNextPageLoadData: Boolean

    val mDepositRepo: DepositTransactionRepository

    constructor() : super()
    constructor(sp: SchedulerProvider) : super(sp)


    companion object {
        const val GET_TRANSACTION_INTERVAL = 2 * 60 * 1000

        private var sINSTANCE: DepositTransactionViewModel? = null

        fun getInstance(): DepositTransactionViewModel {

            if (sINSTANCE == null) {
                sINSTANCE = DepositTransactionViewModel()
            }
            return sINSTANCE as DepositTransactionViewModel
        }
    }

    init {

        mData.value = arrayListOf()
        mListIsLoading.value = false
        mHasMoreData.value = true

        mIsNextPageLoadData = false

        mDepositTransactionInvalidated = true
        mLastDataUpdateTime = 0L

        mDepositRepo = DIMain.getDepositTransactionRepo()

        compositeDisposable.add(mDepositRepo.getTransactions()
                .subscribeOn(schedulerProvider.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : RxCallBack<ArrayList<DepositTransactionItem>>() {
                    override fun onComplete() {
                    }

                    override fun onNext(t: ArrayList<DepositTransactionItem>) {
                        mData.value = t

//                        mDepositTransactionInvalidated = false
//                        mIsNextPageLoadData = false
//                        mLastDataUpdateTime = System.currentTimeMillis()
                    }

                    override fun onError(e: Throwable) {
                    }

                }))

        compositeDisposable.add(mDepositRepo.isLoading()
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

        compositeDisposable.add(mDepositRepo.getErrorCode()
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

        compositeDisposable.add(mDepositRepo.hasMore()
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

    fun getTransactions(): MutableLiveData<ArrayList<DepositTransactionItem>> {
        return mData
    }

    fun isListLoading() = mListIsLoading

    fun getErrorCode() = mErrorCode

    fun isHasMore() = mHasMoreData

    fun loadMore() {
        if (getDepositTransaction()) {
            if (!mIsNextPageLoadData) {
                resetToFirstPage()
            }
            mDepositRepo.loadMore()
        }
    }

    fun getDepositTransaction(): Boolean {
        return getDepositTransactionInvalidated() || (System.currentTimeMillis() - getLastDataUpdateTime() > GET_TRANSACTION_INTERVAL) || mIsNextPageLoadData
    }

    fun resetToFirstPage() {
        mDepositRepo.resetToFirstPage()
    }

    fun invalidateDepositTransaction() {
        mDepositTransactionInvalidated = true
    }

    fun getDepositTransactionInvalidated(): Boolean {
        return mDepositTransactionInvalidated
    }

    fun getLastDataUpdateTime(): Long {
        return mLastDataUpdateTime
    }

    fun setIsNextPageLoadData(isNextPageLoadData: Boolean) {
        mIsNextPageLoadData = isNextPageLoadData
    }

    fun clearData() {

        mData.value = null
        mListIsLoading.value = false
        mErrorCode.value = NetworkResponseCodes.SUCCESS
        mHasMoreData.value = true
    }
}