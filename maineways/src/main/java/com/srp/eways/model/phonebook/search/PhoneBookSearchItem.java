package com.srp.eways.model.phonebook.search;

import com.google.gson.annotations.SerializedName;

public class PhoneBookSearchItem {

    @SerializedName("Id")
    private int id;

    @SerializedName("FirstName")
    private String firstName;

    @SerializedName("LastName")
    private String lastName;

    @SerializedName("CellPhone")
    private String cellPhone;

    @SerializedName("Debt")
    private String debt;

    public PhoneBookSearchItem(String name, String cellPhone) {
        this.firstName = name;
        this.cellPhone = cellPhone;
    }

    public PhoneBookSearchItem(String name ,String lastName, String cellPhone) {
        this.firstName = name;
        this.lastName=lastName;
        this.cellPhone = cellPhone;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getCellPhone() {
        return cellPhone;
    }

    public String getDebt() {
        return debt;
    }
}
