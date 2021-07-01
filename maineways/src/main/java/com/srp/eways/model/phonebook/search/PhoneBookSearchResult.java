package com.srp.eways.model.phonebook.search;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PhoneBookSearchResult {

    @SerializedName("Items")
    private ArrayList<PhoneBookSearchItem> items;

    @SerializedName("Status")
    private int status;

    @SerializedName("Description")
    private String description;

    public ArrayList<PhoneBookSearchItem> getItems() {
        return items;
    }

    public int getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public void setItems(ArrayList<PhoneBookSearchItem> items) {
        this.items = items;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
