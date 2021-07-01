package com.srp.eways.model.deposit

/**
 * Created by Eskafi on 8/27/2019.
 */
data class ConfirmPaymentRequest(
        val RequestId: String?,
        val Authority: String?,
        val Status: Int?,
        val Message: String? = "",
        val Response: MPLResponse?
)