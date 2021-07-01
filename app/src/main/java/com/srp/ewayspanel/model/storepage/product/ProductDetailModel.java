package com.srp.ewayspanel.model.storepage.product;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ProductDetailModel implements Serializable {

    @SerializedName("Product")
    private ProductDetail Product;

    @SerializedName("Status")
    private int Status;

    @SerializedName("Description")
    private String Description;

    public ProductDetail getProduct() {
        return Product;
    }

    public int getStatus() {
        return Status;
    }

    public String getDescription() {
        return Description;
    }

}
