package com.srp.ewayspanel.repository.remote.shopcart

import android.os.Handler
import com.google.gson.Gson
import com.srp.eways.network.CallBackWrapper
import com.srp.ewayspanel.model.shopcart.ShopCartItemListRequest
import com.srp.ewayspanel.model.shopcart.ShopCartItemModel
import com.srp.ewayspanel.model.shopcart.ShopCartModel
import com.srp.ewayspanel.model.shopcart.addproduct.AddOrUpdateProductRequest
import com.srp.ewayspanel.model.shopcart.addproduct.AddOrUpdateProductResponse
import com.srp.ewayspanel.model.shopcart.buy.BuyRequest
import com.srp.ewayspanel.model.shopcart.buy.BuyResponse
import com.srp.eways.repository.remote.BaseApiImplementation

/**
 * Created by ErfanG on 12/3/2019.
 */
object ShopCartApiImplementation : BaseApiImplementation(), ShopCartApiService {

    const val ERROR_ADD_NOT_IN_STOCK = -5
    const val ERROR_REMOVE_PRODUCT_NOT_EXISTS = -6


    private val mShopCartModel : ShopCartModel

    private var mModel62844 : ShopCartItemModel
    private var mModel71462 : ShopCartItemModel
    private var mModel212502 : ShopCartItemModel
    private var mModel64858 : ShopCartItemModel
    init {

        val modelString = """{
              "Basket": [],
              "DeliverTypes": {
                "CategoryId": 318,
                "Title": "",
                "PostTypes": [
                  {
                    "Id": 1,
                    "Title": "پیک موتوری",
                    "Description": ""
                  },
                  {
                    "Id": 2,
                    "Title": "پیشتاز",
                    "Description": null
                  },
                  {
                    "Id": 3,
                    "Title": "سفارشی",
                    "Description": null
                  },
                  {
                    "Id": 8,
                    "Title": "تحویل حضوری",
                    "Description": "جهت تحويل کالاي خريداري شده مي بايست شخصا مراجعه نماييد"
                  },
                  {
                    "Id": 9,
                    "Title": "سایر",
                    "Description": null
                  }
                ]
              },
              "ShippingPrice": 0,
              "ShippingType": 8,
              "LackList": [
                {
                  "Id": 62844,
                  "Name": "تبدیل برل USB مادگی رویال",
                  "SeoName": "تبدیل-برل-USB-مادگی-رویال",
                  "Price": 19000,
                  "OldPrice": 79000,
                  "ImageUrl": "https://staticcontent.eways.co//upload/ProductPictures/62844.jpg",
                  "Stock": 0,
                  "Availability": false,
                  "MinOrder": 1,
                  "MaxOrder": 1000,
                  "OverInventoryCount": 0,
                  "Point": 0,
                  "Discount": 76,
                  "LawId": 380294,
                  "IsSim": false
                }
              ],
              "OrderPrice": 491001,
              "PayingPrice": 491001,
              "Status": 0,
              "Description": ""
            }"""
        mShopCartModel = Gson().fromJson(modelString, ShopCartModel::class.java)

        val modelStringProduct62844 = """{
                  "Id": 810408,
                  "ProductId": 62844,
                  "Count": 1,
                  "ProductInfo": {
                    "Id": 62844,
                    "Name": "تبدیل برل USB مادگی رویال",
                    "SeoName": "تبدیل-برل-USB-مادگی-رویال",
                    "Price": 19000,
                    "OldPrice": 79000,
                    "ImageUrl": "https://staticcontent.eways.co//upload/ProductPictures/62844.jpg",
                    "Stock": 0,
                    "Availability": false,
                    "MinOrder": 1,
                    "MaxOrder": 1000,
                    "OverInventoryCount": 0,
                    "Point": 0,
                    "Discount": 76,
                    "LawId": 380294,
                    "IsSim": false
                  }
                }"""
        val modelStringProduct71462 = """{
                  "Id": 810410,
                  "ProductId": 71462,
                  "Count": 1,
                  "ProductInfo": {
                    "Id": 71462,
                    "Name": "رم فله King Star U1-32GB(گارانتی متین)",
                    "SeoName": "رم-فله-King-Star-U1-32GB(گارانتی-متین)",
                    "Price": 357000,
                    "OldPrice": 357000,
                    "ImageUrl": "https://staticcontent.eways.co//upload/ProductPictures/71462.jpg",
                    "Stock": 219,
                    "Availability": true,
                    "MinOrder": 1,
                    "MaxOrder": 1000,
                    "OverInventoryCount": 0,
                    "Point": 0,
                    "Discount": 0,
                    "LawId": 385816,
                    "IsSim": false
                  }
                }"""
        val modelStringProduct212502 = """{
                  "Id": 810424,
                  "ProductId": 212502,
                  "Count": 1,
                  "ProductInfo": {
                    "Id": 212502,
                    "Name": "کابل شارژ اندروید تسکو TC-A79 - زرد",
                    "SeoName": "کابل-شارژ-اندروید-تسکو-TC-A79---زرد",
                    "Price": 115000,
                    "OldPrice": 155000,
                    "ImageUrl": "https://staticcontent.eways.co//upload/ProductPictures/212502.jpg",
                    "Stock": 622,
                    "Availability": true,
                    "MinOrder": 1,
                    "MaxOrder": 1000,
                    "OverInventoryCount": 0,
                    "Point": 0,
                    "Discount": 26,
                    "LawId": 383558,
                    "IsSim": false
                  }
                }"""
        val modelStringProduct64858 = """{
                  "Id": 810791,
                  "ProductId": 64858,
                  "Count": 1,
                  "ProductInfo": {
                    "Id": 64858,
                    "Name": "جاسوئیچی چرم قهوه ای -کد5",
                    "SeoName": "جاسوئیچی-چرم-قهوه-ای--کد5",
                    "Price": 1,
                    "OldPrice": 1,
                    "ImageUrl": "https://staticcontent.eways.co//upload/ProductPictures/64858.JPG",
                    "Stock": 62,
                    "Availability": true,
                    "MinOrder": 1,
                    "MaxOrder": 1000,
                    "OverInventoryCount": 0,
                    "Point": 4900,
                    "Discount": 0,
                    "LawId": 264860,
                    "IsSim": false
                  }
                }"""

        mModel62844 = Gson().fromJson(modelStringProduct62844, ShopCartItemModel::class.java)
        mModel71462 = Gson().fromJson(modelStringProduct71462, ShopCartItemModel::class.java)
        mModel212502 = Gson().fromJson(modelStringProduct212502, ShopCartItemModel::class.java)
        mModel64858 = Gson().fromJson(modelStringProduct64858, ShopCartItemModel::class.java)

        mShopCartModel.basket.add(mModel62844)
        mShopCartModel.basket.add(mModel71462)
        mShopCartModel.basket.add(mModel212502)
        mShopCartModel.basket.add(mModel64858)

    }

    override fun getItemList(request: ShopCartItemListRequest, callback: CallBackWrapper<ShopCartModel>) {

        if(!handleCall(callback)){
            handleShopCartListInternal(request, callback)
        }

    }

    override fun removeProduct(productId: Int, callback: CallBackWrapper<AddOrUpdateProductResponse>) {

        if(!handleCall(callback)){
            handleRemoveProductInternal(productId, callback)
        }
    }

    private fun handleShopCartListInternal(body : ShopCartItemListRequest, callback : CallBackWrapper<ShopCartModel>) {

        Handler().postDelayed({
            callback.onSuccess(mShopCartModel)
        }, getResponseDelay())

    }

    private fun handleAddOrUpdateInternal(request: AddOrUpdateProductRequest, callback: CallBackWrapper<AddOrUpdateProductResponse>) {

        val response = AddOrUpdateProductResponse()


        if(getMode() == ERROR_ADD_NOT_IN_STOCK){

            when {
                request.count == 0 -> {
                    response.description = null
                    response.status = -3
                }
                request.count < 0 -> {
                    response.description = "حداکثر تعداد مجاز جهت خرید 1 عدد می باشد"
                    response.status = -4
                }
                else -> {
                    response.description = "حداکثر تعداد مجاز جهت خرید 0 عدد می باشد"
                    response.status = -5
                }
            }

            Handler().postDelayed({
                callback.onError(response.status, response.description)
            }, getResponseDelay())
        }
        else{

            var found = false

            for (item in mShopCartModel.basket!!) {

                if (item.productId == request.productId) {

                    if (request.count != 0) {

                        mShopCartModel.orderPrice += (request.count - item.count) * item.productInfo.oldPrice
                        mShopCartModel.payingPrice += (request.count - item.count) * item.productInfo.price

                        item.count = request.count

                        found = true
                        break
                    }
                }
            }

            if(!found){

                when(request.productId){
                    62844 ->{
                        mShopCartModel.basket.add(mModel62844)
                    }
                    71462 ->{
                        mShopCartModel.basket.add(mModel71462)
                    }
                    212502 ->{
                        mShopCartModel.basket.add(mModel212502)
                    }
                    64858 ->{
                        mShopCartModel.basket.add(mModel64858)
                    }
                }
            }

            response.description = "با موفقیت به سبد خرید اضافه شد"
            response.status = 0
            response.items = mShopCartModel.basket

            Handler().postDelayed({
                callback.onSuccess(response)
            }, getResponseDelay())
        }
    }

    private fun handleRemoveProductInternal(productId: Int, callback: CallBackWrapper<AddOrUpdateProductResponse>) {

        val response = AddOrUpdateProductResponse()

        if(getMode() == ERROR_REMOVE_PRODUCT_NOT_EXISTS){
            response.description = "همچین کالایی در سبد خرید شما نمیباشد"
            response.status = ERROR_REMOVE_PRODUCT_NOT_EXISTS

            Handler().postDelayed({
                callback.onError(response.status, response.description)
            }, getResponseDelay())

        }
        else{
            for (item in mShopCartModel.basket) {

                if (item.productId == productId) {

                    if (item.count != 0) {

                        mShopCartModel.orderPrice -= item.count * item.productInfo.oldPrice
                        mShopCartModel.payingPrice -= item.count * item.productInfo.price

                        mShopCartModel.basket.remove(item)
                    }
                    mShopCartModel.basket.remove(item)
                    break
                }
            }

            response.description = "با موفقیت انجام شد"
            response.status = 0
            response.items = mShopCartModel.basket

            Handler().postDelayed({
                callback.onSuccess(response)
            }, getResponseDelay())
        }
    }

    override fun buyProductList(buyRequest: BuyRequest, callback: CallBackWrapper<BuyResponse>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addProduct(request: AddOrUpdateProductRequest, callback: CallBackWrapper<AddOrUpdateProductResponse>) {
        //todo
    }

    override fun updateProduct(request: AddOrUpdateProductRequest, callback: CallBackWrapper<AddOrUpdateProductResponse>) {
        //todo
    }
}