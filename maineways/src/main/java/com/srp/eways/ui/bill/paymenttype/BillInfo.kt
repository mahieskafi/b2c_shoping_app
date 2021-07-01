package com.srp.eways.ui.bill.paymenttype

/**
 * Created by ErfanG on 5/19/2020.
 */
data class BillInfo(
        val id: Int = 0,
        val billTypeId: Int,
        val billId: Long,
        val billPayId: Long,
        val billPrice: Long,
        val inquiryNumber: String = "-"
) {
    constructor(id: Int, billTypeId: Int, billId: Long, billPayId: Long, billPrice: Long) : this(id, billTypeId, billId, billPayId, billPrice,"-")
}