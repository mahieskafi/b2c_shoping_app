package com.srp.eways.repository.chargetransaction

import com.srp.eways.model.transaction.charge.ChargeSale
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

/**
 * Created by ErfanG on 01/09/2019.
 */
interface ChargeTransactionRepository {

    var mList : PublishSubject<ArrayList<ChargeSale>>
    fun getErrorCode(): Observable<Int>
    fun isLoading(): Observable<Boolean>
    fun hasMore(): Observable<Boolean>

    fun loadMore(groupType : Int, deliveryStatus : Int, startDate : String, endDate : String)

    fun getTransactions() : Observable<ArrayList<ChargeSale>>

    fun resetToFirstPage()

    fun clearData()

}