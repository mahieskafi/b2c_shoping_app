package com.srp.ewayspanel.repository.remote.shopcart

import com.srp.ewayspanel.model.shopcart.ShopCartItemListRequest
import com.srp.ewayspanel.model.shopcart.ShopCartModel
import com.srp.ewayspanel.model.shopcart.addproduct.AddOrUpdateProductRequest
import com.srp.ewayspanel.model.shopcart.addproduct.AddOrUpdateProductResponse
import com.srp.ewayspanel.model.shopcart.buy.BuyRequest
import com.srp.ewayspanel.model.shopcart.buy.BuyResponse
import com.srp.eways.network.CallBackWrapper
import com.srp.ewayspanel.model.storepage.product.ProductInventoryResponse

/**
 * Created by ErfanG on 12/3/2019.
 */
interface ShopCartApiService {

    fun getItemList(request: ShopCartItemListRequest, callback: CallBackWrapper<ShopCartModel>)

    fun addProduct(request: AddOrUpdateProductRequest, callback: CallBackWrapper<AddOrUpdateProductResponse>)

    fun updateProduct(request: AddOrUpdateProductRequest, callback: CallBackWrapper<AddOrUpdateProductResponse>)

    fun removeProduct(productId: Int, callback: CallBackWrapper<AddOrUpdateProductResponse>)

    fun buyProductList(buyRequest: BuyRequest, callback: CallBackWrapper<BuyResponse>)

    fun getGoodInventory(productId: Int, callback: CallBackWrapper<ProductInventoryResponse>)
}