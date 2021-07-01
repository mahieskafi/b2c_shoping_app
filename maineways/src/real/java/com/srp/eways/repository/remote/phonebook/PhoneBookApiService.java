package com.srp.eways.repository.remote.phonebook;

import com.srp.eways.model.phonebook.AddOrRemovePhoneBookResponse;
import com.srp.eways.model.phonebook.UserPhoneBook;
import com.srp.eways.network.CallBackWrapper;
import com.srp.eways.model.phonebook.PhoneBookResult;
import com.srp.eways.model.phonebook.search.PhoneBookSearchResult;

public interface PhoneBookApiService {

    void getUserPhoneBook(int pageIndex, int pagSize, CallBackWrapper<PhoneBookResult> callBack);

    void getUserPhoneBookSearchResult(String searchValue, CallBackWrapper<PhoneBookSearchResult> callBackWrapper);

    void addUserPhoneBook(UserPhoneBook userPhoneBook , CallBackWrapper<AddOrRemovePhoneBookResponse> callBack);

    void removeUserPhoneBook(UserPhoneBook userPhoneBook , CallBackWrapper<AddOrRemovePhoneBookResponse> callBack);
}
