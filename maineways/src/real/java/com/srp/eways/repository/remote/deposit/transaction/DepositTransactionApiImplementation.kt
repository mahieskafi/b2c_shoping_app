package com.srp.eways.repository.remote.deposit.transaction

import com.srp.eways.AppConfig
import com.srp.eways.di.DIMain
import com.srp.eways.model.deposit.transaction.DepositTransactionResponse
import com.srp.eways.network.CallBackWrapper
import com.srp.eways.repository.remote.BaseApiImplementation
import com.srp.eways.repository.remote.DefaultRetroCallback

/**
 * Created by Eskafi on 9/3/2019.
 */
class DepositTransactionApiImplementation : BaseApiImplementation(),DepositTransactionApiService {

    private val mDepositTransactionRetrofit: DepositTransactionApiRetrofit

    companion object {

        private var instance: DepositTransactionApiImplementation? = null

        fun get(): DepositTransactionApiImplementation {

            if (instance == null) {
                instance = DepositTransactionApiImplementation()
            }
            return instance as DepositTransactionApiImplementation
        }
    }

    init {
        mDepositTransactionRetrofit = DIMain.provideApi(DepositTransactionApiRetrofit::class.java)
    }

    override fun getTransactionList(pageIndex: Int, pageSize: Int, callback: CallBackWrapper<DepositTransactionResponse>) {
        mDepositTransactionRetrofit.getTransactionList(AppConfig.SERVER_VERSION,pageIndex,pageSize).enqueue(DefaultRetroCallback(callback))
    }
}