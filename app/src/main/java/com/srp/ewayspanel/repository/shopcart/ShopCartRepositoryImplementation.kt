package com.srp.ewayspanel.repository.shopcart

import com.srp.eways.network.CallBackWrapper
import com.srp.ewayspanel.di.DI
import com.srp.ewayspanel.model.storepage.product.ProductInventoryResponse
import com.srp.ewayspanel.model.shopcart.ShopCartItemListRequest
import com.srp.ewayspanel.model.shopcart.ShopCartModel
import com.srp.ewayspanel.model.shopcart.addproduct.AddOrUpdateProductRequest
import com.srp.ewayspanel.model.shopcart.addproduct.AddOrUpdateProductResponse
import com.srp.ewayspanel.model.shopcart.buy.BuyRequest
import com.srp.ewayspanel.model.shopcart.buy.BuyResponse
import com.srp.ewayspanel.repository.remote.shopcart.ShopCartApiService

/**
 * Created by ErfanG on 12/3/2019.
 */
object ShopCartRepositoryImplementation : ShopCartRepository {

    private val mShopCartApiService: ShopCartApiService = DI.getShopCartApi()

    override fun getShopCartList(request: ShopCartItemListRequest, callback: CallBackWrapper<ShopCartModel>) {
        mShopCartApiService.getItemList(request, callback)
    }

    override fun addProduct(request: AddOrUpdateProductRequest, callback: CallBackWrapper<AddOrUpdateProductResponse>) {
        mShopCartApiService.addProduct(request, callback)
    }

    override fun updateProduct(request: AddOrUpdateProductRequest, callback: CallBackWrapper<AddOrUpdateProductResponse>) {
        mShopCartApiService.updateProduct(request, callback)
    }

    override fun removeProduct(productId: Int, callback: CallBackWrapper<AddOrUpdateProductResponse>) {
        mShopCartApiService.removeProduct(productId, callback)
    }

    override fun buyProductList(buyRequest: BuyRequest, callback: CallBackWrapper<BuyResponse>) {
        mShopCartApiService.buyProductList(buyRequest, callback)
    }

    override fun getGoodInventory(productId: Int, callback: CallBackWrapper<ProductInventoryResponse>) {
        mShopCartApiService.getGoodInventory(productId, callback)
    }
}