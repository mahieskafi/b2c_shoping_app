package com.srp.ewayspanel.repository.remote.shopcart

import com.srp.ewayspanel.AppConfig
import com.srp.ewayspanel.di.DI
import com.srp.ewayspanel.model.shopcart.ShopCartItemListRequest
import com.srp.ewayspanel.model.shopcart.ShopCartModel
import com.srp.ewayspanel.model.shopcart.addproduct.AddOrUpdateProductRequest
import com.srp.ewayspanel.model.shopcart.addproduct.AddOrUpdateProductResponse
import com.srp.ewayspanel.model.shopcart.buy.BuyRequest
import com.srp.ewayspanel.model.shopcart.buy.BuyResponse
import com.srp.eways.network.CallBackWrapper
import com.srp.eways.network.NetworkResponseCodes
import com.srp.eways.repository.remote.BaseApiImplementation
import com.srp.eways.repository.remote.DefaultRetroCallback
import com.srp.ewayspanel.model.storepage.product.ProductInventoryResponse

/**
 * Created by ErfanG on 12/3/2019.
 */
object ShopCartApiImplementation : BaseApiImplementation(), ShopCartApiService {

    private val mShopCartApiRetrofit: ShopCartApiRetrofit = DI.provideApi(ShopCartApiRetrofit::class.java)


    override fun getItemList(request: ShopCartItemListRequest, callback: CallBackWrapper<ShopCartModel>) {

        mShopCartApiRetrofit.getItemList(AppConfig.SERVER_VERSION, request).enqueue(object : DefaultRetroCallback<ShopCartModel>(callback) {

            override fun checkResponseForError(response: ShopCartModel?, errorInfo: ErrorInfo?) {

                if (response?.basket != null && response.basket.size > 0) {
                    errorInfo?.errorCode = 0
                    errorInfo?.errorMessage = response.description
                } else {
                    errorInfo?.errorCode = response?.status
                    errorInfo?.errorMessage = response?.description
                }
            }

        })
    }

    override fun addProduct(request: AddOrUpdateProductRequest, callback: CallBackWrapper<AddOrUpdateProductResponse>) {

        mShopCartApiRetrofit.addProduct(AppConfig.SERVER_VERSION, request).enqueue(object : DefaultRetroCallback<AddOrUpdateProductResponse>(callback) {

            override fun checkResponseForError(response: AddOrUpdateProductResponse?, errorInfo: ErrorInfo?) {
                if (response?.items != null && response.items.size > 0) {
                    errorInfo?.errorCode = 0
                    errorInfo?.errorMessage = response.description
                } else {
                    errorInfo?.errorCode = response?.status
                    errorInfo?.errorMessage = response?.description
                }
            }

        })
    }

    override fun updateProduct(request: AddOrUpdateProductRequest, callback: CallBackWrapper<AddOrUpdateProductResponse>) {
        mShopCartApiRetrofit.updateProduct(AppConfig.SERVER_VERSION, request).enqueue(object : DefaultRetroCallback<AddOrUpdateProductResponse>(callback) {

            override fun checkResponseForError(response: AddOrUpdateProductResponse?, errorInfo: ErrorInfo?) {
                if (response?.items != null && response.items.size > 0) {
                    errorInfo?.errorCode = 0
                    errorInfo?.errorMessage = response.description
                } else {
                    errorInfo?.errorCode = response?.status
                    errorInfo?.errorMessage = response?.description
                }
            }

        })
    }

    override fun removeProduct(productId: Int, callback: CallBackWrapper<AddOrUpdateProductResponse>) {

        mShopCartApiRetrofit.removeProduct(AppConfig.SERVER_VERSION, productId).enqueue(object : DefaultRetroCallback<AddOrUpdateProductResponse>(callback) {

            override fun checkResponseForError(response: AddOrUpdateProductResponse?, errorInfo: ErrorInfo?) {
                if (response?.items != null) {
                    errorInfo?.errorCode = 0
                    errorInfo?.errorMessage = response.description
                } else {
                    errorInfo?.errorCode = response?.status
                    errorInfo?.errorMessage = response?.description
                }
            }

        })
    }

    override fun buyProductList(buyRequest: BuyRequest, callback: CallBackWrapper<BuyResponse>) {
        if (buyRequest.gateway == 0 && buyRequest.gatewayType == 0) {
            buyByDeposit(buyRequest, callback)
        } else {
            buyByGetway(buyRequest, callback)
        }
    }

    override fun getGoodInventory(productId: Int, callback: CallBackWrapper<ProductInventoryResponse>) {
        mShopCartApiRetrofit.getGoodInventory(AppConfig.SERVER_VERSION, productId).enqueue(object : DefaultRetroCallback<ProductInventoryResponse>(callback) {
            override fun checkResponseForError(response: ProductInventoryResponse?, errorInfo: ErrorInfo?) {
                if (response?.status != NetworkResponseCodes.SUCCESS) {
                    errorInfo?.errorCode = response?.status
                    errorInfo?.errorMessage = response?.description
                }
            }
        })
    }

    private fun buyByDeposit(buyRequest: BuyRequest, callback: CallBackWrapper<BuyResponse>) {
        mShopCartApiRetrofit.buy(AppConfig.SERVER_VERSION, buyRequest).enqueue(object : DefaultRetroCallback<BuyResponse>(callback) {
            override fun checkResponseForError(response: BuyResponse?, errorInfo: ErrorInfo?) {
                if (response?.status != 0) {
                    errorInfo?.errorCode = response?.status
                    errorInfo?.errorMessage = response?.description
                }
            }
        })
    }


    private fun buyByGetway(buyRequest: BuyRequest, callback: CallBackWrapper<BuyResponse>) {
        mShopCartApiRetrofit.buyByGetway(AppConfig.SERVER_VERSION, buyRequest).enqueue(object : DefaultRetroCallback<BuyResponse>(callback) {
            override fun checkResponseForError(response: BuyResponse?, errorInfo: ErrorInfo?) {
                if (response?.status != 0) {
                    errorInfo?.errorCode = response?.status
                    errorInfo?.errorMessage = response?.description
                }
            }
        })
    }

}