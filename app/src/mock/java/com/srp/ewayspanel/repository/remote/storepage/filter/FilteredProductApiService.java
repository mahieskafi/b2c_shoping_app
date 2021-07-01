package com.srp.ewayspanel.repository.remote.storepage.filter;

import com.srp.eways.network.CallBackWrapper;
import com.srp.ewayspanel.model.storepage.filter.FilterProductRequest;
import com.srp.ewayspanel.model.storepage.filter.FilteredProduct;
import com.srp.ewayspanel.model.storepage.product.ProductDetailModel;

/**
 * Created by Eskafi on 10/28/2019.
 */
public interface FilteredProductApiService {
    void getFilteredProductList(FilterProductRequest request, CallBackWrapper<FilteredProduct> callBack);

    void getProductDetail(long productId, CallBackWrapper<ProductDetailModel> callBack);

    void searchProducts(FilterProductRequest request, CallBackWrapper<FilteredProduct> filteredProductCallBackWrapper);
}
