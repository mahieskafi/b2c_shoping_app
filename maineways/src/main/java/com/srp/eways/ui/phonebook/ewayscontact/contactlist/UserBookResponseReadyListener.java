package com.srp.eways.ui.phonebook.ewayscontact.contactlist;

import com.srp.eways.model.phonebook.PhoneBookResultWrapper;

/**
 * Created by ErfanG on 3/2/2020.
 */
public interface UserBookResponseReadyListener {

    void onResponseReady(PhoneBookResultWrapper result);

}