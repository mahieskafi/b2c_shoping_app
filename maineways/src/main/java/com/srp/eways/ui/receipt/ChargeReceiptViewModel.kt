package com.srp.eways.ui.receipt

import androidx.lifecycle.MutableLiveData
import com.srp.eways.base.BaseViewModel

class ChargeReceiptViewModel :BaseViewModel() {
    companion object {

        private var instance: ChargeReceiptViewModel? = null

        fun getInstance(): ChargeReceiptViewModel {

            if (instance == null) {
                instance = ChargeReceiptViewModel()
            }
            return instance as ChargeReceiptViewModel
        }
    }

    private val isGoToTransactionLiveData = MutableLiveData<Boolean>()

    fun consumeGoToTransaction() {
        isGoToTransactionLiveData.value = null
    }

    fun setGoToTransaction(isGotoTransaction: Boolean) {
        isGoToTransactionLiveData.value = isGotoTransaction
    }

    fun isGoToTransaction(): MutableLiveData<Boolean> {
        return isGoToTransactionLiveData
    }
}