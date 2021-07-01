package com.srp.ewayspanel.ui.shopcart

import android.os.Handler
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.srp.eways.base.BaseViewModel
import com.srp.eways.model.deposit.Bank
import com.srp.eways.network.BaseCallBackWrapper
import com.srp.eways.network.NetworkResponseCodes
import com.srp.eways.network.NetworkUtil
import com.srp.ewayspanel.di.DI
import com.srp.ewayspanel.model.login.Address
import com.srp.ewayspanel.model.shopcart.*
import com.srp.ewayspanel.model.shopcart.addproduct.AddOrUpdateProductRequest
import com.srp.ewayspanel.model.shopcart.addproduct.AddOrUpdateProductResponse
import com.srp.ewayspanel.model.shopcart.buy.BuyRequest
import com.srp.ewayspanel.model.shopcart.buy.BuyResponse
import com.srp.ewayspanel.model.storepage.product.ProductInventoryAddCheckModel
import com.srp.ewayspanel.model.storepage.product.ProductInventoryCheckModel
import com.srp.ewayspanel.model.storepage.product.ProductInventoryResponse
import com.srp.ewayspanel.repository.shopcart.ShopCartRepository

/**
 * Created by ErfanG on 12/3/2019.
 */
object ShopCartViewModel : BaseViewModel() {

    private const val DELIVERY_TYPE_PEIK = 1
    private const val DELIVERY_TYPE_PISHTAZ = 2
    private const val DELIVERY_TYPE_SEFARESHI = 3
    private const val DELIVERY_TYPE_HOZORI = 8
    private const val DELIVERY_TYPE_SAYER = 9

    private const val COUPON_CODE_ERROR_CODE = -21

    private const val UPDATE_PRODUCT_TIMER_PERIOD = 2000L

    private val mShopCartRepository: ShopCartRepository = DI.getShopCartRepository()

    private var mType = DELIVERY_TYPE_HOZORI

    private val mShopCartItem = MutableLiveData<ShopCartModel>()
    private val mShopCartItemsList = MutableLiveData<ArrayList<ShopCartItemModel>>()
    private val mShopCartItemForAddress = MutableLiveData<ShopCartModel>()
    private val mBasketLoadingLiveData = MutableLiveData<Boolean>()

    private val mBuyResponseLiveData = MutableLiveData<BuyResponse>()
    private var mBuyResponseError = MutableLiveData<String>()
    private var mBuyInProgress = MutableLiveData<Boolean>()

    private val mChangedProducts = ArrayList<Int>()
    private var mInUpdatingProgress = false
    private var mHasAnythingToChange = MutableLiveData<Boolean>()

    private var mIsLoadingMainList = MutableLiveData<Boolean>()
    private var mMainListErrorCode = MutableLiveData<Int>()
    private var mCouponError = MutableLiveData<String>()

    private var mIsLoadingShippingPrice = MutableLiveData<Boolean>()
    private var mShippingPriceError = MutableLiveData<String>()
    private var mShippingPrice = MutableLiveData<Long>()

    private var mIsEditAddress = MutableLiveData<Boolean>()

    private var mInventoryForAddLiveData = MutableLiveData<ProductInventoryAddCheckModel>()
    private var mInventoryForOpenFragmentLiveData = MutableLiveData<ProductInventoryCheckModel>()
    private var mInventoryForOpenFragmentProgress = MutableLiveData<Boolean>()

    private var mSelectedAddress: Address? = null
    private  var mSelectedPostType: PostType? = null
    private var mDescription: String = ""

    private var mDiscountCode: String? = null
    private var mDiscountCodeResponseLiveData: MutableLiveData<String> = MutableLiveData<String>()
    private var mBank: Bank? = null

    private var mSelectedProvinceId: Int = 0
    private var mSelectedCityId: Int = 0

    private val mStackIndependentLoading = MutableLiveData<Boolean>()
    private var mStackIndependentError: String? = null
    private var mStackIndependentRequestInProgress = false

    private val mMainPageUnavailableError = MutableLiveData<String>()

    init {

        mIsLoadingMainList.value = false
        mMainListErrorCode.value = NetworkUtil.SUCCESS
        mHasAnythingToChange.value = false

        mShopCartItemsList.value = ArrayList<ShopCartItemModel>()
        mShopCartItem.value = ShopCartModel()
        mShopCartItemForAddress.value = ShopCartModel()

        callGetShopCartList()

        val handler = Handler()

        handler.postDelayed(object : Runnable {
            override fun run() {
                if (!mInUpdatingProgress) {
                    updateProducts()
                }
                handler.postDelayed(this, UPDATE_PRODUCT_TIMER_PERIOD)

            }
        }, UPDATE_PRODUCT_TIMER_PERIOD)

    }


    fun getIndependentLoading() = mStackIndependentLoading
    fun getIndependentError() = mStackIndependentError
    fun getShopCartItem() = mShopCartItem
    fun getShopCartItemForAddress() = mShopCartItemForAddress
    fun getShopCartProductList() = mShopCartItemsList
    fun isLoadingMainList() = mIsLoadingMainList
    fun getMainListError() = mMainListErrorCode
    fun hasAnythingToChange() = mInUpdatingProgress || mStackIndependentRequestInProgress
    fun hasAnythingToChangeLiveData() = mHasAnythingToChange
    fun isLoadingShippingPrice() = mIsLoadingShippingPrice
    fun getShippingPriceError() = mShippingPriceError
    fun getShippingPrice() = mShippingPrice
    fun getBuyResponse() = mBuyResponseLiveData
    fun getBuyResponseError() = mBuyResponseError
    fun isBuyInProgress() = mBuyInProgress
    fun getBasketLoading() = mBasketLoadingLiveData
    fun isInventoryForOpenFragmentProgress() = mInventoryForOpenFragmentProgress
    fun consumeInventoryForOpenFragmentProgress() {
        mInventoryForOpenFragmentProgress.value = null
    }

    fun consumeBasketLoading() {
        mBasketLoadingLiveData.value = null
    }

    fun consumeShippingPriceError() {
        mShippingPriceError.value = null
    }

    fun consumeBuyResponseError() {
        mBuyResponseError.value = null
    }

    fun consumeBuyResponseLiveData() {
        mBuyResponseLiveData.value = null
    }

    fun getCouponError() = mCouponError
    fun consumeCouponError() {
        mCouponError.value = null
    }

    fun getSelectedPostType() = mSelectedPostType
    fun setSelectedPostType(postType: PostType) {
        mSelectedPostType = postType
        mType = mSelectedPostType!!.id
    }

    fun getIsEditAddress() = mIsEditAddress
    fun setIsEditAddress() {
        mIsEditAddress.value = true
    }

    fun consumeIsEditAddress() {
        mIsEditAddress.value = null
    }

    fun setBank(bank: Bank?) {
        mBank = bank
    }

    fun getInventoryForAddLiveData() = mInventoryForAddLiveData
    fun consumeInventoryForAddLiveData() {
        mInventoryForAddLiveData.value = null
    }

    fun getInventoryForOpenFragmentLiveData() = mInventoryForOpenFragmentLiveData
    fun consumeInventoryForOpenFragmentLiveData() {
        mInventoryForOpenFragmentLiveData.value = null
    }

    fun getMainPageUnavailableProductError() = mMainPageUnavailableError
    fun consumeMainPageUnavailableProductError() {
        mMainPageUnavailableError.value = null
    }

    fun getDiscountCode() = mDiscountCode

    fun consumeShopCartItemForAddressLiveData() {
        mShopCartItemForAddress.value = null
    }

    fun callGetShopCartList(couponCode: String? = null) {

        mIsLoadingMainList.value = true
        mShopCartRepository.getShopCartList(getShopCartItemListRequest(mType, couponCode), object : BaseCallBackWrapper<ShopCartModel>(this) {

            override fun onError(errorCode: Int, errorMessage: String?) {

                mMainListErrorCode.value = errorCode
                mIsLoadingMainList.value = false
                mDiscountCode = null

                mShippingPriceError.value = errorMessage
                //TODO handle error
            }

            override fun onSuccess(responseBody: ShopCartModel) {

                for (item in responseBody.basket) {
                    if (item.productInfo == null) {
                        mIsLoadingMainList.value = false
                        mDiscountCode = null

                        return
                    }
                }

                mShopCartItem.value = responseBody
                mShopCartItemForAddress.value = responseBody
                mShopCartItemsList.value = responseBody.basket

                mMainListErrorCode.value = NetworkUtil.SUCCESS
                mIsLoadingMainList.value = false

                mDiscountCode = couponCode

                if (responseBody.status == COUPON_CODE_ERROR_CODE) {
                    mCouponError.value = responseBody.description
                    mDiscountCode = null
                    mDiscountCodeResponseLiveData.value = null
                } else {
                    if (mDiscountCode != null) {
                        mDiscountCodeResponseLiveData.value = mDiscountCode
                    }
                    mCouponError.value = null
                }

            }

        })

    }

    private fun getShopCartItemListRequest(type: Int, couponCode: String?): ShopCartItemListRequest {

        return if (mSelectedProvinceId == 0 && mSelectedCityId == 0) {
            ShopCartItemListRequest(type, couponCode)
        } else {
            ShopCartItemListRequest(type, couponCode, mSelectedProvinceId, mSelectedCityId)
        }
    }

    fun getShippingPrice(deliveryId: Int) {
        mIsLoadingShippingPrice.value = true

        mShopCartRepository.getShopCartList(getShopCartItemListRequest(deliveryId, null), object : BaseCallBackWrapper<ShopCartModel>(this) {

            override fun onError(errorCode: Int, errorMessage: String?) {
                mIsLoadingShippingPrice.value = false

                mShippingPriceError.value = errorMessage
            }

            override fun onSuccess(responseBody: ShopCartModel) {
                mIsLoadingShippingPrice.value = false

                if (responseBody.status != NetworkResponseCodes.SUCCESS) {
                    mShippingPriceError.value = responseBody.description
                } else {
                    mShippingPrice.value = responseBody.shippingPrice
                }

            }

        })
    }

    fun removeAllProducts() {

        val iterator = mShopCartItem.value!!.basket.iterator()

        while (iterator.hasNext()) {
            val item = iterator.next()
            iterator.remove()
            mShopCartItemsList.value!!.remove(item)
            addToChangedList(item.productId)
        }
    }

    fun removeFromCart(productId: Int) {

        for (item in mShopCartItemsList.value!!) {
            if (item.productId == productId) {
//                mShopCartItem.value?.basket!!.remove(item)
                mShopCartItemsList.value!!.remove(item)
                addToChangedList(productId)
                break
            }
        }
    }

    fun removeProduct(productId: Int, count: Int = 1, isTotalCount: Boolean = false) {

        var shouldRemoveItem: ShopCartItemModel? = null
        for (item in mShopCartItemsList.value!!) {

            if (item.productId == productId) {

                if (isTotalCount) {
                    if (count == 0) {
                        shouldRemoveItem = item
                    } else {
                        item.count = count
                        addToChangedList(productId)
                    }
                    break
                } else {
                    if (item.count > count) {
                        item.count -= count
                        addToChangedList(productId)
                    } else {
                        shouldRemoveItem = item
                    }
                    break
                }
            }
        }

        if (shouldRemoveItem != null) {
            mBasketLoadingLiveData.value = true

            mShopCartItemsList.value!!.remove(shouldRemoveItem)
            addToChangedList(shouldRemoveItem.productId)
        }

    }

    fun getInventoryAndOpenDetail(productInfo: com.srp.ewayspanel.model.storepage.filter.ProductInfo) {
        mInventoryForOpenFragmentProgress.value = true

        mShopCartRepository.getGoodInventory(productInfo.id, object : BaseCallBackWrapper<ProductInventoryResponse>(this) {
            override fun onSuccess(responseBody: ProductInventoryResponse?) {
                mInventoryForOpenFragmentProgress.value = false

                if (responseBody?.status == NetworkResponseCodes.SUCCESS) {

                    mInventoryForOpenFragmentLiveData.value = ProductInventoryCheckModel(productInfo = productInfo,
                            status = !(responseBody.status == 0 && responseBody.maxOrder == 0L && responseBody.minOrder == 0))
                } else {
                    mInventoryForOpenFragmentLiveData.value = ProductInventoryCheckModel(productInfo = null,
                            status = false, description = "error")
                }
            }

            override fun onError(errorCode: Int, errorMessage: String?) {
                mInventoryForOpenFragmentProgress.value = false

                mInventoryForOpenFragmentLiveData.value = ProductInventoryCheckModel(productInfo = null,
                        status = false, description = "error")
            }

        })
    }

    fun getInventoryAndAddProduct(productInfo: ProductInfo, count: Int = 1, isTotalCount: Boolean = false, observerId: Int) {
        mShopCartRepository.getGoodInventory(productInfo.id, object : BaseCallBackWrapper<ProductInventoryResponse>(this) {
            override fun onSuccess(responseBody: ProductInventoryResponse?) {

                if (responseBody?.status == NetworkResponseCodes.SUCCESS) {
                    if (responseBody.status == 0 && responseBody.maxOrder == 0L && responseBody.minOrder == 0) {
                        mInventoryForAddLiveData.value = ProductInventoryAddCheckModel(productInfo, observerId)
                    } else {
                        addProduct(productInfo, count, isTotalCount)
                    }
                }

            }

        })
    }

    fun addProduct(productInfo: ProductInfo, count: Int = 1, isTotalCount: Boolean = false) {

        var found = false

        for (item in mShopCartItemsList.value!!) {

            if (item.productId == productInfo.id) {
                found = true

                if (isTotalCount) {
                    item.count = count
                } else {
                    item.count += count
                }

                break
            }
        }
        if (!found) {
            mBasketLoadingLiveData.value = true

            mShopCartItemsList.value!!.add(ShopCartItemModel(count = count, productId = productInfo.id, id = -1, productInfo = productInfo))
        }

        addToChangedList(productInfo.id)
    }

    //should update total count
    //when this method called, totalCount of productId will be changed
    //return false if count == 0 and this product not exist in shopCart
    fun updateProductDirect(productId: Int, productCount: Int): Boolean {

        var model: ShopCartItemModel? = null
        if (mShopCartItemsList.value != null) {
            for (item in mShopCartItemsList.value!!) {

                if (item.productId == productId) {
                    model = item

                    break
                }
            }
        }


        if (model == null && productCount <= 0) {
            return false
        }

        mStackIndependentRequestInProgress = true
        mStackIndependentLoading.value = true

        if (productCount <= 0) {
            removeProductWithServer(productId, true)
        } else if (model == null) {
            addWithServer(318, productId, productCount, true)
        } else {
            updateProductWithServer(318, productId, productCount, true)
        }


        return true

    }

    private fun addToChangedList(productId: Int) {
        if (!mChangedProducts.contains(productId)) {
            mChangedProducts.add(productId)
        }
    }

    fun setSelectedAddress(address: Address) {
        mSelectedAddress = address

        mSelectedProvinceId = address.stateId
        mSelectedCityId = address.cityId
    }

    fun setSelectedProvinceAndCity(provinceId: Int, cityId: Int) {
        if (mSelectedAddress != null) {
            mSelectedAddress!!.cityId = cityId
            mSelectedAddress!!.stateId = provinceId
        }

        mSelectedProvinceId = provinceId
        mSelectedCityId = cityId
    }

    fun getSelectedAddress() = mSelectedAddress

    private fun updateProducts() {
        if (mChangedProducts.size != 0) {
            mInUpdatingProgress = true

            if (!mHasAnythingToChange.value!!) {
                mHasAnythingToChange.value = true
            }

            var itemPositionInShopCart = -1
            var itemCount = 0

            for (item in mShopCartItemsList.value!!) {
                if (item.productId == mChangedProducts[0]) {
                    itemPositionInShopCart = mShopCartItemsList.value!!.indexOf(item)
                    itemCount = item.count
                    break
                }
            }

            if (itemPositionInShopCart == -1) {
                removeProductWithServer(mChangedProducts[0])
            } else {

                var existInCart = false

                for (baseItem in mShopCartItemsList.value!!) {
                    if (baseItem.productId == mChangedProducts[0] && baseItem.id != -1) {
                        existInCart = true
                        break
                    }
                }

                if (existInCart) {
                    updateProductWithServer(318, mChangedProducts[0], itemCount)
                } else {
                    addWithServer(318, mChangedProducts[0], itemCount)
                }
            }


//            while(iterator.hasNext()){
//                val item = iterator.next()
//
//                if(item.productId == mChangedProducts[0]){
//                    if(item.count == 0){
//                        removeProductWithServer(mChangedProducts[0])
//                    }
//                    else{
//                        addOrUpdateProductWithServer(318, mChangedProducts[0], item.count)
//                    }
//                    break
//                }
//            }
        } else {
            mInUpdatingProgress = false
            mBasketLoadingLiveData.value = false

            if (mHasAnythingToChange.value!!) {
                mHasAnythingToChange.value = false
            }
        }
    }

    private fun addWithServer(categoryId: Int, productId: Int, count: Int, isDirect: Boolean = false) {

        mShopCartRepository.addProduct(AddOrUpdateProductRequest(categoryId, count, productId), object : BaseCallBackWrapper<AddOrUpdateProductResponse>(this) {
            override fun onError(errorCode: Int, errorMessage: String?) {

                //TODO: -6 error is related to a situation that server in main page products can not recognize
                // unavailable product so if server handle this bug in future we should delete this line
                if (isDirect || errorCode == -6) {
                    mStackIndependentRequestInProgress = false
                    mStackIndependentLoading.value = false
                    mStackIndependentError = errorMessage

                    if (errorCode == -6) {
                        mChangedProducts.remove(productId)
                        mMainPageUnavailableError.value = errorMessage
                        callGetShopCartList(mDiscountCode)
                        updateProducts()
                    }
                } else {
                    updateProducts()
                }

                //todo show proper message (for esample lack of internet)
            }

            override fun onSuccess(responseBody: AddOrUpdateProductResponse) {

                if (isDirect) {
                    mStackIndependentRequestInProgress = false
                    mStackIndependentLoading.value = false
                    mStackIndependentError = null
                } else {
                    mChangedProducts.remove(productId)
                }

                if (mChangedProducts.size == 0) {
                    mShopCartItem.value?.basket = responseBody.items
                    mShopCartItemsList.value = responseBody.items

                }
                updateProducts()
            }

        })

    }

    private fun updateProductWithServer(categoryId: Int, productId: Int, count: Int, isDirect: Boolean = false) {
        mShopCartRepository.updateProduct(AddOrUpdateProductRequest(categoryId, count, productId), object : BaseCallBackWrapper<AddOrUpdateProductResponse>(this) {
            override fun onError(errorCode: Int, errorMessage: String?) {

                if (isDirect) {
                    mStackIndependentRequestInProgress = false
                    mStackIndependentLoading.value = false
                    mStackIndependentError = errorMessage
                } else {
                    updateProducts()
                }

                //todo show proper message (for esample lack of internet)
            }

            override fun onSuccess(responseBody: AddOrUpdateProductResponse) {

                if (isDirect) {
                    mStackIndependentRequestInProgress = false
                    mStackIndependentLoading.value = false
                    mStackIndependentError = null
                } else {
                    mChangedProducts.remove(productId)
                }

                if (mChangedProducts.size == 0) {

                    mShopCartItem.value?.basket = responseBody.items
                    mShopCartItemsList.value = responseBody.items

                }
                updateProducts()
            }
        })
    }

    private fun createOrderSummaryReport(basketItems: List<ShopCartItemModel>) {

        var totalScore = 0L
        var totalPrice = 0L
        var totalDiscount = 0L
        var totalPayable = 0L

        for (item in basketItems) {
            val productInfo = item.productInfo

            totalScore += item.count * productInfo!!.point
            totalPrice += item.count * productInfo.oldPrice
            totalDiscount += item.count * (productInfo.oldPrice - productInfo.price)
            totalPayable += item.count * productInfo.price
        }

//        mShopCartOrderSummary.value = ShopCartOrderSummary(totalScore, totalPrice, totalDiscount, totalPayable)
    }

    private fun removeProductWithServer(productId: Int, isDirect: Boolean = false) {
        mShopCartRepository.removeProduct(productId, object : BaseCallBackWrapper<AddOrUpdateProductResponse>(this) {
            override fun onError(errorCode: Int, errorMessage: String?) {

                if (isDirect) {
                    mStackIndependentRequestInProgress = false
                    mStackIndependentLoading.value = false
                    mStackIndependentError = errorMessage
                } else {
                    updateProducts()
                }

                Log.d("ShopCardViewModel", "failed to remove item with id $productId")
                //todo show proper message for user
            }

            override fun onSuccess(responseBody: AddOrUpdateProductResponse) {

                if (isDirect) {
                    mStackIndependentRequestInProgress = false
                    mStackIndependentLoading.value = false
                    mStackIndependentError = null
                } else {
                    mChangedProducts.remove(productId)
                }


                if (mChangedProducts.size == 0) {
                    mShopCartItem.value?.basket = responseBody.items
                    mShopCartItemsList.value = responseBody.items
                }
                updateProducts()
            }
        })
    }

    public fun calculatePrices(shippingPrice: Long = 0L, newData: ArrayList<ShopCartItemModel>): ArrayList<Long>? {

        val returnArray = arrayListOf<Long>()

        var totalPrice = 0L
        var payPrice = shippingPrice
        var discount = 0L
        var points = 0L
//        var shopCartModel = mShopCartItem.value!!
////        payPrice = shopCartModel.payingPrice

        for (item in newData) {

            if (item.productInfo != null) {
                totalPrice += (item.productInfo!!.oldPrice * item.count)

                payPrice += if (mDiscountCode == null) {
                    (item.productInfo!!.price * item.count)
                } else {
                    (item.productInfo!!.oldPrice * item.count)
                }
                mShopCartItem.value!!.payingPrice = payPrice

                discount += (item.productInfo!!.oldPrice - item.productInfo!!.price) * item.count

                points += item.productInfo!!.point * item.count
            }
        }
        if (mDiscountCode != null) {
            discount = mShopCartItem.value!!.discountPrice
            payPrice -= discount
        }
        return if (totalPrice != 0L) {
            returnArray.add(totalPrice)
            returnArray.add(payPrice)
            returnArray.add(discount)
            returnArray.add(points)

            returnArray
        } else
            null
    }

    fun clearData() {
        mType = DELIVERY_TYPE_PEIK
        mShopCartItem.value = null
        mShopCartItemsList.value = null
    }

    fun buy() {

        mBuyInProgress.value = true

        mShopCartRepository.buyProductList(createBuyRequest()
                , object : BaseCallBackWrapper<BuyResponse>(this) {
            override fun onError(errorCode: Int, errorMessage: String?) {

                mBuyResponseError.value = errorMessage
                mBuyInProgress.value = false
                mBank = null

            }

            override fun onSuccess(responseBody: BuyResponse?) {

                mBuyResponseLiveData.value = responseBody
                mBuyInProgress.value = false
                mBank = null
            }

        })
    }

    fun setDescription(text: String) {
        mDescription = text
    }

    private fun createBuyRequest(): BuyRequest {
        return if (mBank == null) {
            BuyRequest(mType, mDiscountCode, mSelectedAddress!!.address, stateId = mSelectedProvinceId, cityId = mSelectedCityId, description = mDescription)
        } else {
            BuyRequest(mType, mDiscountCode, mSelectedAddress!!.address, mBank!!.gId, mBank!!.bankType, description = mDescription, stateId = mSelectedProvinceId, cityId = mSelectedCityId)
        }
    }

    fun getDiscountCodeLiveData(): MutableLiveData<String> {
        return mDiscountCodeResponseLiveData
    }

    fun consumeDiscountCodeLiveData() {
        mDiscountCodeResponseLiveData.value = null
    }

    fun getProductCount(productId: Int): Int {

        if (mShopCartItemsList.value != null) {
            for (item in mShopCartItemsList.value!!) {
                if (item.productInfo != null && productId == item.productInfo!!.id) {
                    return item.count
                }
            }
        }

        return 0
    }

}