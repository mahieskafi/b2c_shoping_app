package com.srp.ewayspanel.ui.store.product.detail;

import androidx.lifecycle.MutableLiveData;

import com.srp.eways.base.BaseViewModel;
import com.srp.eways.network.BaseCallBackWrapper;
import com.srp.ewayspanel.di.DI;
import com.srp.ewayspanel.model.storepage.product.ProductDetailModel;
import com.srp.ewayspanel.model.storepage.product.ProductDetailResponse;
import com.srp.ewayspanel.repository.storepage.filter.FilteredProductRepository;

public class ProductViewModel extends BaseViewModel {

    private MutableLiveData<ProductDetailResponse> mProductResponseLive = new MutableLiveData<>();

    private FilteredProductRepository mProductRepository;

    public ProductViewModel() {
        mProductRepository = DI.getFilteredProductRepo();
    }

    public void getProductDetail(long productId) {
        if (isLoading().getValue() != null && isLoading().getValue()) {
            return;
        }

        setLoading(true);
        mProductRepository.getProductDetail(productId, new BaseCallBackWrapper<ProductDetailModel>(this) {

            @Override
            public void onSuccess(ProductDetailModel productDetailModel) {
                setLoading(false);

                ProductDetailResponse response = new ProductDetailResponse(productDetailModel, 0, null);
                mProductResponseLive.setValue(response);
            }

            @Override
            public void onError(int errorCode, String errorMessage) {
                setLoading(false);

                ProductDetailResponse response = new ProductDetailResponse(null, errorCode, errorMessage);
                mProductResponseLive.setValue(response);
            }

        });
    }

    public MutableLiveData<ProductDetailResponse> getProductDetail() {
        return mProductResponseLive;
    }

}
