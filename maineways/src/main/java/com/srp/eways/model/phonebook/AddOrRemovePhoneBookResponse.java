package com.srp.eways.model.phonebook;

import com.google.gson.annotations.SerializedName;


public class AddOrRemovePhoneBookResponse {
    @SerializedName("Status")
    private int status;

    @SerializedName("Description")
    private String description;


    public int getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}