package com.srp.ewayspanel.repository.storepage.filter;

import com.srp.eways.network.CallBackWrapper;
import com.srp.ewayspanel.model.storepage.filter.FilterProductRequest;
import com.srp.ewayspanel.model.storepage.product.ProductDetailModel;
import com.srp.ewayspanel.ui.store.filter.FilteredProductViewModel;

/**
 * Created by Eskafi on 10/27/2019.
 */
public interface FilteredProductRepository {

    void getFilteredProductList(FilterProductRequest request, FilteredProductViewModel.FilteredProductResponseListener callBack);

    void searchProducts(FilterProductRequest request, FilteredProductViewModel.FilteredProductResponseListener callBack);

    void getProductDetail(long productId, CallBackWrapper<ProductDetailModel> callBack);

    Boolean hasMore();

    void clear();
}
