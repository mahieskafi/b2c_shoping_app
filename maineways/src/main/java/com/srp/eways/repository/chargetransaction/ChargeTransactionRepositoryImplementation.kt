package com.srp.eways.repository.chargetransaction

import android.annotation.SuppressLint
import android.os.Handler
import com.srp.eways.AppConfig
import com.srp.eways.BuildConfig
import com.srp.eways.di.DIMain
import com.srp.eways.model.transaction.charge.ChargeSale
import com.srp.eways.model.transaction.charge.ChargeTransactionRequest
import com.srp.eways.model.transaction.charge.ChargeTransactionResponse
import com.srp.eways.network.CallBackWrapper
import com.srp.eways.network.NetworkResponseCodes
import com.srp.eways.util.Constants
import com.srp.eways.repository.remote.chargetransaction.ChargeTransactionApiService
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

/**
 * Created by ErfanG on 01/09/2019.
 */
class ChargeTransactionRepositoryImplementation : ChargeTransactionRepository {

    val mChargeTransService: ChargeTransactionApiService

    var mPageNumber: Int = 0

    var mPageSize: Int = 20

    override lateinit var mList: PublishSubject<ArrayList<ChargeSale>>
    private val mIsLoading: PublishSubject<Boolean>
    private val mHasMore: PublishSubject<Boolean>
    private val mHasError: PublishSubject<Int>

    private var mData: ArrayList<ChargeSale> = arrayListOf()


    init {

        mChargeTransService = DIMain.getChargeTransApi()

        mList = PublishSubject.create()
        mIsLoading = PublishSubject.create()
        mHasMore = PublishSubject.create()
        mHasError = PublishSubject.create()

        mList.onNext(mData)

        mIsLoading.onNext(false)
        mHasMore.onNext(true)
        mHasError.onNext(NetworkResponseCodes.SUCCESS)
    }


    companion object {

        val instance = ChargeTransactionRepositoryImplementation()
    }


    override fun getTransactions(): Observable<ArrayList<ChargeSale>> {

        return mList
    }

    override fun getErrorCode(): Observable<Int> {
        return mHasError
    }

    override fun isLoading(): Observable<Boolean> {
        return mIsLoading
    }

    override fun hasMore(): Observable<Boolean> {
        return mHasMore
    }

    @SuppressLint("CheckResult")
    override fun loadMore(groupType: Int, deliveryStatus: Int, startDate: String, endDate: String) {

        mIsLoading.onNext(true)

        if (BuildConfig.FLAVOR.equals(Constants.MOCK_FLAVER)) {

            Handler().postDelayed({

                callAndDecode(groupType, deliveryStatus, startDate, endDate)
            }, 3000)
        } else {

            callAndDecode(groupType, deliveryStatus, startDate, endDate)
        }
    }

    private fun callAndDecode(groupType: Int, deliveryStatus: Int, startDate: String, endDate: String) {

//        val newValue = mChargeTransService.loadData(AppConfig.SERVER_VERSION, ChargeTransactionRequest(
//                deliveryStatus, groupType, startDate, endDate, mPageNumber, mPageSize, 2))

        mChargeTransService.loadData(AppConfig.SERVER_VERSION, ChargeTransactionRequest(
                DeliverStatus = deliveryStatus, MainGroupType = groupType, MinRequestDate = startDate, MaxRequestDate = endDate, PageIndex = mPageNumber, PageSize = mPageSize, SaleChannelType = 0), object : CallBackWrapper<ChargeTransactionResponse> {
            override fun onError(errorCode: Int, errorMessage: String?) {

                mIsLoading.onNext(false)
                mHasError.onNext(errorCode)

            }

            override fun onSuccess(responseBody: ChargeTransactionResponse) {

                mPageNumber++

                mIsLoading.onNext(false)
                mHasError.onNext(NetworkResponseCodes.SUCCESS)

                if (responseBody.chargeSales?.size!! < mPageSize){
                    mHasMore.onNext(false)
                }

                if (responseBody.chargeSales != null) {

                    for(item in responseBody.chargeSales!!){
                        if(!mData.contains(item)){
                            mData.add(item)
                        }
                    }
                    mList.onNext(mData)
                }

            }

        })

//        CompositeDisposable().add(newValue
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith(object : RxCallBack<ChargeTransactionResponse>() {
//
//                    override fun onComplete() {
//                    }
//
//                    override fun onNext(t: ChargeTransactionResponse) {
//
//
//                        if (t.chargeSales != null) {
//                            mPageNumber++
//
//                            mData.addAll(t.chargeSales)
//                            mList.onNext(mData)
//
//                            if (t.chargeSales.size < mPageSize)
//                                mHasMore.onNext(false)
//
//                            mIsLoading.onNext(false)
//                            mHasError.onNext(false)
//
//                        } else {
//                            mHasError.onNext(true)
//                            mIsLoading.onNext(false)
//                        }
//                    }
//
//                    override fun onError(e: Throwable) {
//
//                        mHasError.onNext(true)
//                        mIsLoading.onNext(false)
//                    }
//                }))
    }


    override fun clearData() {

        mPageNumber = 0

        mData = ArrayList<ChargeSale>()
        mList.onNext(mData)

        mIsLoading.onNext(false)
        mHasMore.onNext(true)
        mHasError.onNext(NetworkResponseCodes.SUCCESS)
    }

    override fun resetToFirstPage() {
        mPageNumber = 0

        mData = arrayListOf()
        mList.onNext(mData)
    }

}