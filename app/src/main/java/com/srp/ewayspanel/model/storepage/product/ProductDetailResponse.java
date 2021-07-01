package com.srp.ewayspanel.model.storepage.product;

import java.io.Serializable;

public class ProductDetailResponse implements Serializable {

    public final ProductDetailModel productDetailModel;

    public final int errorCode;

    public final String errorMessage;

    public ProductDetailResponse(ProductDetailModel productDetailModel, int errorCode,String errorMessage) {
        this.productDetailModel = productDetailModel;

        this.errorCode = errorCode;

        this.errorMessage = errorMessage;
    }

}
