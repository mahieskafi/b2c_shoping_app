package com.srp.eways.ui.transaction.charge

import androidx.lifecycle.MutableLiveData
import com.srp.eways.base.BaseViewModel
import com.srp.eways.di.DIMain
import com.srp.eways.model.transaction.charge.ChargeSale
import com.srp.eways.network.NetworkResponseCodes
import com.srp.eways.repository.chargetransaction.ChargeTransactionRepository
import com.srp.eways.util.JalaliCalendar
import com.srp.ewayspanel.network.RxCallBack
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * Created by ErfanG on 01/09/2019.
 */
class ChargeTransactionViewModel : BaseViewModel() {


    private val mData = MutableLiveData<ArrayList<ChargeSale>>()
    private val mListIsLoading = MutableLiveData<Boolean>()
    private val mHasMoreData = MutableLiveData<Boolean>()
    private val mErrorCode = MutableLiveData<Int>()

    val mChargeTransRepo: ChargeTransactionRepository

    private var mChargeTransactionInvalidated: Boolean
    private var mLastDataUpdateTime: Long

    private var mIsNextPageLoadData: Boolean

    companion object {
        const val GET_TRANSACTION_INTERVAL = 2 * 60 * 1000

        private var sINSTANCE: ChargeTransactionViewModel? = null

        fun getInstance(): ChargeTransactionViewModel {

            if (sINSTANCE == null) {
                sINSTANCE = ChargeTransactionViewModel()
            }
            return sINSTANCE as ChargeTransactionViewModel
        }
    }

    init {

        mData.value = arrayListOf()
        mListIsLoading.value = false
        mHasMoreData.value = true

        mIsNextPageLoadData = false

        mChargeTransactionInvalidated = true
        mLastDataUpdateTime = 0L

        mChargeTransRepo = DIMain.getChargeTransRepo()



        compositeDisposable.add(mChargeTransRepo.getTransactions()
                .subscribeOn(schedulerProvider.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : RxCallBack<ArrayList<ChargeSale>>() {
                    override fun onComplete() {
                    }

                    override fun onNext(t: ArrayList<ChargeSale>) {
                        mData.value = t

                        mChargeTransactionInvalidated = false
                        mLastDataUpdateTime = System.currentTimeMillis()
                        mIsNextPageLoadData = false
                    }

                    override fun onError(e: Throwable) {
                    }

                }))
        compositeDisposable.add(mChargeTransRepo.isLoading()
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

        compositeDisposable.add(mChargeTransRepo.getErrorCode()
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

        compositeDisposable.add(mChargeTransRepo.hasMore()
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

    fun getTransactions(): MutableLiveData<ArrayList<ChargeSale>> {
        return mData
    }

    fun isListLoading() = mListIsLoading

    fun getErrorCode() = mErrorCode

    fun isHasMore() = mHasMoreData

    fun loadMore() {

        if (getChargeTransaction()) {
            if (!mIsNextPageLoadData) {
                resetToFirstPage()
            }

            val calendar = JalaliCalendar()

            val endTime = "" + calendar.get(JalaliCalendar.YEAR) + "/" + (calendar.get(JalaliCalendar.MONTH) + 1) + "/" + calendar.get(JalaliCalendar.DAY_OF_MONTH)

            calendar.add(JalaliCalendar.DATE, -3)

            val startTime = "" + calendar.get(JalaliCalendar.YEAR) + "/" + (calendar.get(JalaliCalendar.MONTH) + 1) + "/" + calendar.get(JalaliCalendar.DAY_OF_MONTH)

            var mainGroupType = 2
            var deliveryStatus = -1

//        mChargeTransRepo.loadMore(mainGroupType, deliveryStatus, "1398/06/12", "1398/06/09")
            mChargeTransRepo.loadMore(mainGroupType, deliveryStatus, startTime, endTime)
        }
    }

    fun resetToFirstPage() {
        mChargeTransRepo.resetToFirstPage()

        mData.value = arrayListOf()
    }


    fun getChargeTransaction(): Boolean {
        return getChargeTransactionInvalidated() || (System.currentTimeMillis() - getLastDataUpdateTime() > GET_TRANSACTION_INTERVAL) || mIsNextPageLoadData
    }

    fun invalidateChargeTransaction() {
        mChargeTransactionInvalidated = true
    }

    fun getChargeTransactionInvalidated(): Boolean {
        return mChargeTransactionInvalidated
    }

    fun getLastDataUpdateTime(): Long {
        return mLastDataUpdateTime
    }

    fun setIsNextPageLoadData(isNextPageLoadData: Boolean) {
        mIsNextPageLoadData = isNextPageLoadData
    }

    fun clearData() {
        mData.value = ArrayList<ChargeSale>()
        mListIsLoading.value = false
        mErrorCode.value = NetworkResponseCodes.SUCCESS
        mHasMoreData.value = true
    }

}