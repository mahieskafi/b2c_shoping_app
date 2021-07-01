package com.srp.eways.repository.remote.deposit.transaction

import com.srp.eways.model.deposit.transaction.DepositTransactionItem
import com.srp.eways.model.deposit.transaction.DepositTransactionResponse
import com.srp.eways.network.CallBackWrapper
import com.srp.eways.network.NetworkResponseCodes.SUCCESS
import com.srp.eways.repository.remote.BaseApiImplementation

/**
 * Created by Eskafi on 9/3/2019.
 */
class DepositTransactionApiImplementation : BaseApiImplementation(), DepositTransactionApiService {
    private var mHalfResult = false

    companion object {
        private var instance: DepositTransactionApiImplementation? = null

        fun get(): DepositTransactionApiImplementation {
            if (instance == null)
                instance = DepositTransactionApiImplementation()
            return instance as DepositTransactionApiImplementation
        }
    }

    override fun getTransactionList(pageIndex: Int, pageSize: Int, callBack: CallBackWrapper<DepositTransactionResponse>) {
        if (!handleCall()) {
            callBack.onSuccess(createSuccessResultTransaction(pageIndex, pageSize))
        }else {
            callBack.onError(getMode(), "")
        }

    }

    public fun setHalfResult(bool: Boolean) {
        mHalfResult = bool
    }

    private fun createSuccessResultTransaction(pageIndex: Int, pageSize: Int): DepositTransactionResponse {
        val list = ArrayList<DepositTransactionItem>()

        var start = pageIndex * pageSize
        var end: Int =
                if (!mHalfResult)
                    pageIndex * pageSize + pageSize
                else
                    pageIndex * pageSize + (pageSize / 2).toInt()

        for (i in start until end) {
            list.add(DepositTransactionItem(status = SUCCESS, payment = 50000, requestDate = "14شهریور", paymentId = 446454
                    , bankName = "ملت", bankLogo = "string"))
        }

        var data = DepositTransactionResponse(status = SUCCESS, rowCount = 0, items = list)

        return data
    }

}