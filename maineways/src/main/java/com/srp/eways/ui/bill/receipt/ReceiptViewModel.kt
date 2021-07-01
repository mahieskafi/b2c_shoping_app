package com.srp.eways.ui.bill.receipt

import com.srp.eways.base.BaseViewModel

/**
 * Created by ErfanG on 5/10/2020.
 */
class ReceiptViewModel : BaseViewModel() {

    companion object {
        private var instance: ReceiptViewModel? = null
        fun getInstance(): ReceiptViewModel {

            if (instance == null) {
                instance = ReceiptViewModel()
            }
            return instance as ReceiptViewModel
        }
    }
}