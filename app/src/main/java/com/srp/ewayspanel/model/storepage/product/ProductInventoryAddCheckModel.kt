package com.srp.ewayspanel.model.storepage.product

import com.srp.ewayspanel.model.shopcart.ProductInfo

data class ProductInventoryAddCheckModel
(
        var productInfo: ProductInfo,

        var observerId: Int
)