package com.srp.ewayspanel.repository.remote.storepage.filter;

import com.srp.eways.network.CallBackWrapper;
import com.srp.ewayspanel.AppConfig;
import com.srp.ewayspanel.di.DI;
import com.srp.ewayspanel.model.storepage.filter.FilterProductRequest;
import com.srp.ewayspanel.model.storepage.filter.FilteredProduct;
import com.srp.ewayspanel.model.storepage.product.ProductDetailModel;
import com.srp.eways.repository.remote.DefaultRetroCallback;

/**
 * Created by Eskafi on 10/28/2019.
 */
public class FilteredProductApiImplementation implements FilteredProductApiService {

    private static FilteredProductApiImplementation sInstance;

    public static FilteredProductApiImplementation getInstance() {
        if (sInstance == null) {
            sInstance = new FilteredProductApiImplementation();
        }
        return sInstance;
    }

    private FilteredProductApiRetrofit mFilteredProductRetrofit;

    public FilteredProductApiImplementation() {
        mFilteredProductRetrofit = DI.provideApi(FilteredProductApiRetrofit.class);
    }

    @Override
    public void getFilteredProductList(FilterProductRequest request, CallBackWrapper<FilteredProduct> callBack) {

        mFilteredProductRetrofit.getCategoryList(AppConfig.SERVER_VERSION, request).enqueue(new DefaultRetroCallback<FilteredProduct>(callBack) {

            @Override
            protected void checkResponseForError(FilteredProduct result, ErrorInfo errorInfo) {
                if (result.getStatus() != 0) {
                    errorInfo.errorCode = result.getStatus();
                    errorInfo.errorMessage = result.getDescription();
                }
            }

        });
    }

    @Override
    public void getProductDetail(long productId, CallBackWrapper<ProductDetailModel> callBack) {
        mFilteredProductRetrofit.getProductDetail(AppConfig.SERVER_VERSION, productId).enqueue(new DefaultRetroCallback<ProductDetailModel>(callBack) {

            @Override
            protected void checkResponseForError(ProductDetailModel result, ErrorInfo errorInfo) {
                if (result.getStatus() != 0) {
                    errorInfo.errorCode = result.getStatus();
                    errorInfo.errorMessage = result.getDescription();
                }
            }
        });
    }


    @Override
    public void searchProducts(FilterProductRequest request, CallBackWrapper<FilteredProduct> callBack) {
        mFilteredProductRetrofit.searchProducts(AppConfig.SERVER_VERSION, request).enqueue(new DefaultRetroCallback<FilteredProduct>(callBack) {

            @Override
            protected void checkResponseForError(FilteredProduct result, ErrorInfo errorInfo) {
                if (result.getStatus() != 0) {
                    errorInfo.errorCode = result.getStatus();
                    errorInfo.errorMessage = result.getDescription();
                }
            }

        });
    }

}
