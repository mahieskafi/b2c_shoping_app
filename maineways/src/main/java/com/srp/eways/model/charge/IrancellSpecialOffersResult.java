package com.srp.eways.model.charge;

import com.google.gson.annotations.Expose;
import com.srp.eways.model.charge.result.IrancellSpecialItemOffer;

import java.util.List;

public class IrancellSpecialOffersResult {

    @Expose
    private List<IrancellSpecialItemOffer> Items;

    @Expose
    private Integer Status;

    @Expose
    private String Description;

    public List<IrancellSpecialItemOffer> getItems(int productId) {
        if (Items != null) {
            for (IrancellSpecialItemOffer item: Items) {
                item.setProductId(productId);
            }
        }

        return Items;
    }

    public Integer getStatus() {
        return Status;
    }

    public String getDescription() {
        return Description;
    }

    public void setStatus(Integer status) {
        Status = status;
    }

    public void setItems(List<IrancellSpecialItemOffer> items) {
        Items = items;
    }

    public List<IrancellSpecialItemOffer> getItems() {
        return Items;
    }

}
