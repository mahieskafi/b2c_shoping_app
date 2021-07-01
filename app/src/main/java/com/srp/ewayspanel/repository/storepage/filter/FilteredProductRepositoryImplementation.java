package com.srp.ewayspanel.repository.storepage.filter;

import com.srp.eways.network.CallBackWrapper;
import com.srp.ewayspanel.di.DI;
import com.srp.ewayspanel.model.storepage.filter.FilterProductRequest;
import com.srp.ewayspanel.model.storepage.filter.FilteredProduct;
import com.srp.ewayspanel.model.storepage.filter.ProductInfo;
import com.srp.ewayspanel.model.storepage.product.ProductDetailModel;
import com.srp.ewayspanel.repository.remote.storepage.filter.FilteredProductApiService;
import com.srp.ewayspanel.ui.store.filter.FilteredProductViewModel;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Eskafi on 10/27/2019.
 */
public class FilteredProductRepositoryImplementation implements FilteredProductRepository {

    private static int PAGE_SIZE;

    private List<ProductInfo> mData = new ArrayList<>();

    private int mPageIndex = 0;

    private boolean mIsLoading = false;
    private boolean mHasMore = true;

    private static FilteredProductRepositoryImplementation sInstance;

    private FilteredProductApiService mFilterProductApiService;


    public static FilteredProductRepositoryImplementation getInstance() {

        if (sInstance == null) {
            sInstance = new FilteredProductRepositoryImplementation();
        }

        return sInstance;
    }

    public FilteredProductRepositoryImplementation() {

        mFilterProductApiService = DI.getFilteredProductApi();
    }

    @Override
    public void getFilteredProductList(final FilterProductRequest request, final FilteredProductViewModel.FilteredProductResponseListener callBack) {
        request.setPageIndex(mPageIndex);
        if (request.getPageSize() == 0) {
            PAGE_SIZE = 10;
            request.setPageSize(PAGE_SIZE);
        } else {
            PAGE_SIZE = request.getPageSize();
        }

        mFilterProductApiService.getFilteredProductList(request, new CallBackWrapper<FilteredProduct>() {
            @Override
            public void onError(int errorCode, String errorMessage) {
                FilteredProduct filteredProduct = new FilteredProduct(errorCode, errorMessage);

                callBack.onResponseReady(filteredProduct);
            }

            @Override
            public void onSuccess(FilteredProduct responseBody) {

//                mData.addAll(responseBody.getProducts());
                int nextPageIndex = responseBody.getPageIndex() + 1;

                if (mPageIndex != nextPageIndex) {
                    mPageIndex = nextPageIndex;
                    if (responseBody.getProducts() != null) {
                        mHasMore = responseBody.getProducts().size() >= PAGE_SIZE;
                    }

                    callBack.onResponseReady(responseBody);
                }
            }
        });
    }

    @Override
    public void searchProducts(FilterProductRequest request, final FilteredProductViewModel.FilteredProductResponseListener callBack) {

        request.setPageIndex(mPageIndex);
        if (request.getPageSize() == 0) {
            PAGE_SIZE = 10;
            request.setPageSize(PAGE_SIZE);
        } else {
            PAGE_SIZE = request.getPageSize();
        }

        mFilterProductApiService.searchProducts(request, new CallBackWrapper<FilteredProduct>() {
            @Override
            public void onError(int errorCode, String errorMessage) {
                FilteredProduct filteredProduct = new FilteredProduct(errorCode, errorMessage);

                callBack.onResponseReady(filteredProduct);
            }

            @Override
            public void onSuccess(FilteredProduct responseBody) {

                int nextPageIndex = responseBody.getPageIndex() + 1;

                if (mPageIndex != nextPageIndex) {
                    mPageIndex = nextPageIndex;
                    if (responseBody.getProducts() != null) {
                        mHasMore = responseBody.getProducts().size() >= PAGE_SIZE;
                    }

                    callBack.onResponseReady(responseBody);
                }
            }
        });

    }

    @Override
    public void getProductDetail(long productId, CallBackWrapper<ProductDetailModel> callBack) {
        mFilterProductApiService.getProductDetail(productId, callBack);
    }

    @Override
    public Boolean hasMore() {
        return mHasMore;
    }

    @Override
    public void clear() {
        mPageIndex = 0;
        mData = new ArrayList<>();

        mHasMore = false;
        mIsLoading = false;
    }
}
