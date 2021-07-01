package com.srp.eways.repository.chargetransaction.inquiry

import com.srp.eways.model.transaction.inquiry.InquiryTopup
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

/**
 * Created by Eskafi on 9/8/2019.
 */
interface InquiryTransactionRepository {

    var mList: PublishSubject<ArrayList<InquiryTopup>>
    fun getErrorCode(): Observable<Int>
    fun isLoading(): Observable<Boolean>
    fun hasMore(): Observable<Boolean>

    fun loadMore(mobileNumber: String, startDate: String, endDate: String)

    fun getTransactions(): Observable<ArrayList<InquiryTopup>>

    fun clearData()

}