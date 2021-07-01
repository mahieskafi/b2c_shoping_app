package com.srp.eways.model.user;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Eskafi on 1/5/2020.
 */
public class ContactSaleExpertResponse {


    @SerializedName("Status")
    private int status;

    @SerializedName("Description")
    private String description;

    @SerializedName("Items")
    private List<ContactSaleExpert> items;

    public int getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public List<ContactSaleExpert> getItems() {
        return items;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setItems(List<ContactSaleExpert> items) {
        this.items = items;
    }

    public static class ContactSaleExpert {

        public ContactSaleExpert(int type, String title, String value) {
            this.type = type;
            this.title = title;
            this.value = value;
        }

        @SerializedName("Type")
        private int type;

        @SerializedName("Title")
        private String title;

        @SerializedName("Value")
        private String value;

        public int getType() {
            return type;
        }

        public String getTitle() {
            return title;
        }

        public String getValue() {
            return value;
        }
    }
}
