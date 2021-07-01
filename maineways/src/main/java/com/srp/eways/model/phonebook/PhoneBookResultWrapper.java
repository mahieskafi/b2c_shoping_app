package com.srp.eways.model.phonebook;

import com.srp.eways.network.NetworkResponseCodes;

import java.util.List;

public class PhoneBookResultWrapper {

    public final List<UserPhoneBook> userPhoneBooks;

    public final int errorCode;
    public final String errorMessage;

    public PhoneBookResultWrapper(List<UserPhoneBook> userPhoneBooks, int errorCode, String errorMessage) {
        this.userPhoneBooks = userPhoneBooks;

        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public boolean hasError() {
        return errorCode != NetworkResponseCodes.SUCCESS;
    }

}
