package com.srp.eways.model.charge;

import com.google.gson.annotations.Expose;
import com.srp.eways.model.charge.result.TopupChargeItem;

import java.util.List;

public class ChargeResult {

    @Expose
    private List<TopupChargeItem> Items;

    @Expose
    private Integer Status;

    @Expose
    private String Description;

    public List<TopupChargeItem> getItems() {
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

}
