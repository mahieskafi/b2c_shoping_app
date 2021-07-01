package com.srp.ewayspanel.model.storepage.product;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ProductAttribute  implements Serializable {

    @SerializedName("Id")
    private Long Id;

    @SerializedName("Title")
    private String Title;

    @SerializedName("IsGroup")
    private Boolean IsGroup;

    @SerializedName("Items")
    private List<ProductAttributeItem> Items;

    public Long getId() {
        return Id;
    }

    public String getTitle() {
        return Title;
    }

    public Boolean getGroup() {
        return IsGroup;
    }

    public List<ProductAttributeItem> getItems() {
        return Items;
    }

}
