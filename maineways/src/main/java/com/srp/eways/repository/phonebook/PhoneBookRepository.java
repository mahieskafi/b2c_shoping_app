package com.srp.eways.repository.phonebook;

import com.srp.eways.model.phonebook.AddOrRemovePhoneBookResponse;
import com.srp.eways.model.phonebook.UserPhoneBook;
import com.srp.eways.model.phonebook.search.PhoneBookSearchResult;
import com.srp.eways.network.CallBackWrapper;
import com.srp.eways.ui.phonebook.ewayscontact.contactlist.UserBookResponseReadyListener;


public interface PhoneBookRepository {

    void getUserPhoneBook(UserBookResponseReadyListener callBack , String source);

    void getUserPhoneBookSearchResult(String searchValue, CallBackWrapper<PhoneBookSearchResult> callBack ,String source);

    void addUserPhoneBook(UserPhoneBook contact, CallBackWrapper<AddOrRemovePhoneBookResponse> callBack);

    void removeUserPhoneBook(UserPhoneBook contact, CallBackWrapper<AddOrRemovePhoneBookResponse> callBack);

    boolean hasMore();

    void reset();

}
