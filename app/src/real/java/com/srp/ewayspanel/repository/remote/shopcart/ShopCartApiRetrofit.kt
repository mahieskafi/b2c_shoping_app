package com.srp.ewayspanel.repository.remote.shopcart

import com.srp.ewayspanel.model.storepage.product.ProductInventoryResponse
import com.srp.ewayspanel.model.shopcart.ShopCartItemListRequest
import com.srp.ewayspanel.model.shopcart.ShopCartModel
import com.srp.ewayspanel.model.shopcart.addproduct.AddOrUpdateProductRequest
import com.srp.ewayspanel.model.shopcart.addproduct.AddOrUpdateProductResponse
import com.srp.ewayspanel.model.shopcart.buy.BuyRequest
import com.srp.ewayspanel.model.shopcart.buy.BuyResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Created by ErfanG on 12/3/2019.
 */
interface ShopCartApiRetrofit {

    @POST("service/v{version}/store/GetBasketDetails")
    fun getItemList(
            @Path("version")
            version: Int,
            @Body
            body: ShopCartItemListRequest
    ): Call<ShopCartModel>


    @POST("service/v{version}/store/AddToBasket")
    fun addProduct(
            @Path("version")
            version: Int,
            @Body
            body: AddOrUpdateProductRequest
    ): Call<AddOrUpdateProductResponse>


    @POST("service/v{version}/store/UpdateBasket")
    fun updateProduct(
            @Path("version")
            version: Int,
            @Body
            body: AddOrUpdateProductRequest
    ): Call<AddOrUpdateProductResponse>

    @GET("service/v{version}/store/RemoveFromBasket/{productId}")
    fun removeProduct(
            @Path("version")
            version: Int,
            @Path("productId")
            productId: Int
    ): Call<AddOrUpdateProductResponse>


    @POST("service/v{version}/store/buy")
    fun buy(
            @Path("version")
            version: Int,
            @Body
            body: BuyRequest
    ): Call<BuyResponse>

    @POST("service/v{version}/store/buygateway")
    fun buyByGetway(
            @Path("version")
            version: Int,
            @Body
            body: BuyRequest
    ): Call<BuyResponse>

    @GET("service/v{version}/store/getgoodinventory/{goodId}")
    fun getGoodInventory(
            @Path("version")
            version: Int,
            @Path("goodId")
            productId: Int
    ): Call<ProductInventoryResponse>
}