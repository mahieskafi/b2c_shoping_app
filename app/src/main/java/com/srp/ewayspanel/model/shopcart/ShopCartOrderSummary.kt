package com.srp.ewayspanel.model.shopcart


/**
 * Created by Eskafi on 12/8/2019.
 */

data class ShopCartOrderSummary(

        var totalScore: Long = 0,
        var totalPrice: Long = 0,
        var totalDiscount: Long = 0,
        var totalPayable: Long = 0
)