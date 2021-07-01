package com.srp.eways.repository.remote.deposit.transaction

import com.srp.eways.model.deposit.transaction.DepositTransactionResponse
import com.srp.eways.network.CallBackWrapper

/**
 * Created by Eskafi on 9/3/2019.
 */

interface DepositTransactionApiService {
    fun getTransactionList(pageIndex: Int, pageSize: Int, callBack : CallBackWrapper<DepositTransactionResponse>)
}