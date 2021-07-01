package com.srp.eways.repository.remote.phonebook;

import com.srp.eways.model.phonebook.PhoneBookResult;
import com.srp.eways.model.phonebook.search.PhoneBookSearchResult;
import com.srp.eways.network.CallBackWrapper;

/**
 * Created by Eskafi on 1/11/2020.
 */
public interface PhoneBookApiService {
    void getUserPhoneBook(int pageIndex, int pagSize, CallBackWrapper<PhoneBookResult> callBack);

    void getUserPhoneBookSearchResult(String searchValue, CallBackWrapper<PhoneBookSearchResult> callBackWrapper);
}
