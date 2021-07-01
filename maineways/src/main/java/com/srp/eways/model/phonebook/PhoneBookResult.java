package com.srp.eways.model.phonebook;

import com.google.gson.annotations.Expose;

import java.util.List;

public class PhoneBookResult {

    @Expose
    private List<UserPhoneBook> PhoneBooks;

    @Expose
    private int Status;

    @Expose
    private String Description;

    public List<UserPhoneBook> getPhoneBooks() {
        return PhoneBooks;
    }

    public int getStatus() {
        return Status;
    }

    public String getDescription() {
        return Description;
    }

    public void setPhoneBooks(List<UserPhoneBook> phoneBooks) {
        PhoneBooks = phoneBooks;
    }

    public void setStatus(int status) {
        Status = status;
    }
}
