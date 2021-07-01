package com.srp.eways.repository.chargetransaction.inquiry

import android.annotation.SuppressLint
import android.os.Handler
import com.srp.eways.AppConfig
import com.srp.eways.BuildConfig
import com.srp.eways.di.DIMain
import com.srp.eways.model.transaction.inquiry.InquiryRequest
import com.srp.eways.model.transaction.inquiry.InquiryTopup
import com.srp.eways.model.transaction.inquiry.InquiryTopupNumberResponse
import com.srp.eways.network.CallBackWrapper
import com.srp.eways.network.NetworkResponseCodes
import com.srp.eways.repository.remote.chargetransaction.ChargeTransactionApiService
import com.srp.eways.util.Constants
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

/**
 * Created by Eskafi on 9/8/2019.
 */
class InquiryTransactionRepositoryImplementation : InquiryTransactionRepository {

    val mChargeTransService: ChargeTransactionApiService

    var mPageNumber: Int = 0

    var mPageSize: Int = 20

    override lateinit var mList: PublishSubject<ArrayList<InquiryTopup>>
    private val mIsLoading: PublishSubject<Boolean>
    private val mHasMore: PublishSubject<Boolean>
    private val mErrorCode: PublishSubject<Int>

    private var mData: ArrayList<InquiryTopup> = arrayListOf()


    init {

        mChargeTransService = DIMain.getChargeTransApi()

        mList = PublishSubject.create()
        mIsLoading = PublishSubject.create()
        mHasMore = PublishSubject.create()
        mErrorCode = PublishSubject.create()

        mList.onNext(mData)

        mIsLoading.onNext(false)
        mHasMore.onNext(true)
        mErrorCode.onNext(NetworkResponseCodes.SUCCESS)
    }

    companion object {

        val instance = InquiryTransactionRepositoryImplementation()
    }

    override fun getTransactions(): Observable<ArrayList<InquiryTopup>> {

        return mList
    }

    @SuppressLint("CheckResult")
    override fun loadMore(mobileNumber: String, startDate: String, endDate: String) {

        mIsLoading.onNext(true)

        if (BuildConfig.FLAVOR.equals(Constants.MOCK_FLAVER)) {

            Handler().postDelayed(Runnable {

                callAndDecode(mobileNumber, startDate, endDate)
            }, 3000)
        } else {

            callAndDecode(mobileNumber, startDate, endDate)
        }
    }

    private fun callAndDecode(mobileNumber: String, startDate: String, endDate: String) {

        var request = InquiryRequest(startDate = startDate, endDate = endDate)
//        val newValue = mChargeTransService.loadData(AppConfig.SERVER_VERSION, mobileNumber, request)

        mChargeTransService.loadData(AppConfig.SERVER_VERSION, mobileNumber, request, object : CallBackWrapper<InquiryTopupNumberResponse> {
            override fun onError(errorCode: Int, errorMessage: String?) {

                mIsLoading.onNext(false)
                mErrorCode.onNext(errorCode)
            }

            override fun onSuccess(responseBody: InquiryTopupNumberResponse) {

                mHasMore.onNext(false)

                mIsLoading.onNext(false)
                mErrorCode.onNext(NetworkResponseCodes.SUCCESS)

                mData.addAll(responseBody.topupList!!)
                mList.onNext(responseBody.topupList!!)
            }

        })


//        CompositeDisposable().add(newValue
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith(object : RxCallBack<InquiryTopupNumberResponse>() {
//
//                    override fun onComplete() {
//                    }
//
//                    override fun onNext(response: InquiryTopupNumberResponse) {
//
//                        if(response.topupList != null) {
//
//                            mData.addAll(response.topupList!!)
//                            mList.onNext(response.topupList!!)
//
//                            mHasMore.onNext(false)
//
//                            mIsLoading.onNext(false)
//                            mHasError.onNext(false)
//                        }
//                        else{
//                            mHasError.onNext(true)
//                            mIsLoading.onNext(false)
//                        }
//
//                    }
//
//            override fun onError(errorCode: Int, errorMessage: String?) {
//                mHasError.onNext(true)
//                mIsLoading.onNext(false)
//            }
//
//        })
    }

    override fun getErrorCode(): Observable<Int> {
        return mErrorCode
    }

    override fun isLoading(): Observable<Boolean> {
        return mIsLoading
    }

    override fun hasMore(): Observable<Boolean> {
        return mHasMore
    }


    override fun clearData() {

        mPageNumber = 0

        mData = ArrayList<InquiryTopup>()
        mList.onNext(ArrayList<InquiryTopup>())

        mIsLoading.onNext(false)
        mHasMore.onNext(true)
        mErrorCode.onNext(NetworkResponseCodes.SUCCESS)
    }

}