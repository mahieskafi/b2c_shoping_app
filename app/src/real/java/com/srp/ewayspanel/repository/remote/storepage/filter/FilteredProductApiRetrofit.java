package com.srp.ewayspanel.repository.remote.storepage.filter;

import com.srp.ewayspanel.model.storepage.filter.FilterProductRequest;
import com.srp.ewayspanel.model.storepage.filter.FilteredProduct;
import com.srp.ewayspanel.model.storepage.product.ProductDetailModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Eskafi on 10/28/2019.
 */
public interface FilteredProductApiRetrofit {

    @POST("service/v{version}/store/GetFilteredProducts")
    Call<FilteredProduct> getCategoryList(@Path("version") int version,
                                          @Body FilterProductRequest request);

    @GET("service/v{version}/store/GetProduct/{productId}")
    Call<ProductDetailModel> getProductDetail(@Path("version") int version,
                                              @Path("productId") long productId);

    @POST("service/v{version}/store/searchproducts")
    Call<FilteredProduct> searchProducts(@Path("version") int version,
                                          @Body FilterProductRequest request);
}
