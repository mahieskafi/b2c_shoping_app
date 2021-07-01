package com.srp.ewayspanel.ui.store.mobilelist

import androidx.lifecycle.MutableLiveData
import com.srp.eways.base.BaseViewModel
import com.srp.eways.network.NetworkResponseCodes
import com.srp.ewayspanel.di.DI
import com.srp.ewayspanel.model.storepage.filter.FilterProductRequest
import com.srp.ewayspanel.model.storepage.filter.FilteredProduct
import com.srp.ewayspanel.model.storepage.filter.ProductInfo
import com.srp.ewayspanel.repository.storepage.filter.FilteredProductRepository
import com.srp.ewayspanel.ui.store.filter.FilteredProductViewModel

class StoreMobileListViewModel : BaseViewModel() {

    private val mFilteredProductRepo: FilteredProductRepository = DI.getFilteredProductRepo()
    private val mBrandListLiveData: MutableLiveData<MutableMap<Int, String>> = MutableLiveData()
    private val mProductListLiveData: MutableLiveData<MutableMap<Int, ArrayList<ProductInfo>>> = MutableLiveData()
    private val mErrorLiveData: MutableLiveData<String> = MutableLiveData()

    init {
        mFilteredProductRepo.clear()
    }

    fun getFilteredProductList(request: FilterProductRequest) {
        mFilteredProductRepo.getFilteredProductList(request, object : FilteredProductViewModel.FilteredProductResponseListener {
            override fun onResponseReady(result: FilteredProduct) {
                if (result.status == NetworkResponseCodes.SUCCESS) {

                    val productInfoList = result.products
                    if (productInfoList != null && productInfoList.size > 0) {
                        createList(productInfoList)
                    } else {
                        mBrandListLiveData.value = mutableMapOf()
                        mProductListLiveData.value = mutableMapOf()
                    }

                } else {
                    mErrorLiveData.value = result.description
                }
            }

        })
    }

    fun getProductLiveData(): MutableLiveData<MutableMap<Int, ArrayList<ProductInfo>>> {
        return mProductListLiveData
    }

    fun consumeProductLiveData() {
        mProductListLiveData.value = null
    }

    fun getBrandLiveData(): MutableLiveData<MutableMap<Int, String>> {
        return mBrandListLiveData
    }

    fun consumeBrandLiveData() {
        mBrandListLiveData.value = null
    }

    fun getErrorLiveData(): MutableLiveData<String> {
        return mErrorLiveData
    }

    fun consumeErrorLiveData() {
        mErrorLiveData.value = null
    }

    private fun createList(productList: ArrayList<ProductInfo>) {
        val brandMap: MutableMap<Int, String> = mutableMapOf()
        val productMap: MutableMap<Int, ArrayList<ProductInfo>> = mutableMapOf()

        for (product in productList) {
            brandMap[product.brandId] = product.brandName

            if (productMap[product.brandId] == null) {
                productMap[product.brandId] = ArrayList()
            }

            productMap[product.brandId]?.add(product)
        }

        mBrandListLiveData.value = brandMap.toSortedMap()
        mProductListLiveData.value = productMap.toSortedMap()
    }
}