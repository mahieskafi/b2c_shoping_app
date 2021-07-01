package com.srp.ewayspanel.ui.store.filter;

import androidx.lifecycle.MutableLiveData;

import com.srp.eways.network.NetworkResponseCodes;
import com.srp.ewayspanel.di.DI;
import com.srp.ewayspanel.model.storepage.filter.FilterProductRequest;
import com.srp.ewayspanel.model.storepage.filter.FilteredProduct;
import com.srp.ewayspanel.repository.storepage.filter.FilteredProductRepository;
import com.srp.eways.base.BaseViewModel;

/**
 * Created by Eskafi on 10/27/2019.
 */
public class FilteredProductViewModel extends BaseViewModel {

    public interface FilteredProductResponseListener {

        void onResponseReady(FilteredProduct result);

    }

    private FilteredProductRepository mFilteredProductRepo;

    private final MutableLiveData<FilteredProduct> mFilteredProductLiveData;
    private final MutableLiveData<Integer> mLoadingLiveData;

    public FilteredProductViewModel() {

        mFilteredProductRepo = DI.getFilteredProductRepo();
        mFilteredProductRepo.clear();

        mFilteredProductLiveData = new MutableLiveData<>();
        mLoadingLiveData = new MutableLiveData<>();
    }

    public void getFilteredProductList(final FilterProductRequest request) {

        if (request != null) {
            if (request.getMinPrice() == null && request.getMaxPrice() == null) {
                request.setMinPrice(0L);
                request.setMaxPrice(2000000000L);
            }


            mFilteredProductRepo.getFilteredProductList(request, new FilteredProductResponseListener() {
                @Override
                public void onResponseReady(FilteredProduct responseBody) {
                    if (responseBody.getStatus() == NetworkResponseCodes.ERROR_AUTHORIZATION_FAILED) {
                        setIsNeededToLogin(true);
                        return;
                    }

                    mFilteredProductLiveData.setValue(responseBody);
                }
            });
        }
    }

    public void searchProducts(final FilterProductRequest request) {

        if (request.getMinPrice() == null && request.getMaxPrice() == null) {
            request.setMinPrice(0L);
            request.setMaxPrice(2000000000L);
        }
        mFilteredProductRepo.searchProducts(request, new FilteredProductResponseListener() {
            @Override
            public void onResponseReady(FilteredProduct responseBody) {
                if (responseBody.getStatus() == NetworkResponseCodes.ERROR_AUTHORIZATION_FAILED) {
                    setIsNeededToLogin(true);
                    return;
                }

                mFilteredProductLiveData.setValue(responseBody);
            }
        });
    }

    public boolean hasMore() {
        return mFilteredProductRepo.hasMore();
    }

    public MutableLiveData<FilteredProduct> getFilteredProductLiveData() {
        return mFilteredProductLiveData;
    }

    public void clearData() {
        mFilteredProductRepo.clear();
    }

    public void consumeFilteredProductLiveData() {
        mFilteredProductLiveData.setValue(null);
    }

    public MutableLiveData<Integer> getLoadingLiveData() {
        return mLoadingLiveData;
    }
}
