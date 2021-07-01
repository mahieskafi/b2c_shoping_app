package com.srp.ewayspanel.model.storepage.product

data class ProductInventoryCheckModel
(
        var productInfo: com.srp.ewayspanel.model.storepage.filter.ProductInfo?,

        var status: Boolean,

        var description: String? = null
)