package com.srp.eways.repository.remote.deposit

import android.os.Handler
import com.srp.eways.model.deposit.*
import com.srp.eways.network.CallBackWrapper
import com.srp.eways.network.NetworkResponseCodes.SUCCESS
import com.srp.eways.repository.remote.BaseApiImplementation

/**
 * Created by Eskafi on 8/26/2019.
 */
class DepositApiImplementation : BaseApiImplementation(), DepositApiService {

    companion object {

        private var instance: DepositApiImplementation? = null
        fun getInstance(): DepositApiImplementation {
            if (instance == null)
                instance = DepositApiImplementation()
            return instance as DepositApiImplementation
        }

    }

    override fun getBankList(callBack: CallBackWrapper<BankListResponse>) {
        if (handleCall(callBack))
            return
        Handler().postDelayed({
            callBack.onSuccess(createSuccessResultBankList())
        }, getResponseDelay())
    }

    override fun increaseDeposit(request: IncreaseDepositRequest, callBackWrapper: CallBackWrapper<IncreaseDepositResponse>) {
        if (handleCall(callBackWrapper))
            return
        Handler().postDelayed({
            callBackWrapper.onSuccess(createSuccessResultIncreaseDeposit())
        }, getResponseDelay())
    }

    override fun getStatus(uId: String, uUId: String, callBackWrapper: CallBackWrapper<IncreaseDepositStatusResponse>) {
        if (handleCall(callBackWrapper))
            return
        Handler().postDelayed({
            callBackWrapper.onSuccess(createSuccessResultIncreaseStatusDeposit())
        }, getResponseDelay())
    }

    private fun createSuccessResultBankList(): BankListResponse {
        var bankList: ArrayList<Bank> = ArrayList()
        bankList.add(Bank(gId = 0, pName = "پارسیان", eName = "parsian", bankType = 11))
        bankList.add(Bank(gId = 7, pName = "ملت", eName = "mellat", bankType = 12))
        bankList.add(Bank(gId = 11, pName = "جیرینگ", eName = "jiring", bankType = 13))

        return BankListResponse(status = SUCCESS, items = bankList)
    }

    private fun createSuccessResultIncreaseDeposit(): IncreaseDepositResponse {
        return IncreaseDepositResponse(status = SUCCESS, url = "/Credit/ExtRedirectToGateway/98350/8/0/5a76a795-8f73-4c6e-9c8e-295324c53b56/200000", description = "")
    }

    private fun createSuccessResultIncreaseStatusDeposit(): IncreaseDepositStatusResponse {
        return IncreaseDepositStatusResponse(status = SUCCESS, amount = 10000, paymentId = "1145452", description = "")
    }
}