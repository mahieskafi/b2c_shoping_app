package com.srp.eways.repository.remote.deposit

import com.srp.eways.model.deposit.*
import com.srp.eways.network.CallBackWrapper

/**
 * Created by Eskafi on 8/26/2019.
 */
interface DepositApiService {

     fun getBankList(callBack: CallBackWrapper<BankListResponse>)

     fun increaseDeposit(request: IncreaseDepositRequest, callBackWrapper: CallBackWrapper<IncreaseDepositResponse>)

     fun getStatus(uId: String, uUId: String, callBackWrapper: CallBackWrapper<IncreaseDepositStatusResponse>)
}