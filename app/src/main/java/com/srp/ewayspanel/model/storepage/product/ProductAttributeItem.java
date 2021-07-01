package com.srp.ewayspanel.model.storepage.product;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ProductAttributeItem implements Serializable {

    @SerializedName("Id")
    private Long Id;

    @SerializedName("Description")
    private String Description;

    @SerializedName("ProductId")
    private Long ProductId;

    public Long getId() {
        return Id;
    }

    public String getDescription() {
        return Description;
    }

    public Long getProductId() {
        return ProductId;
    }

}
