package com.srp.eways.repository.remote.phonebook;

import android.os.Handler;

import com.srp.eways.model.phonebook.PhoneBookResult;
import com.srp.eways.model.phonebook.UserPhoneBook;
import com.srp.eways.model.phonebook.search.PhoneBookSearchItem;
import com.srp.eways.model.phonebook.search.PhoneBookSearchResult;
import com.srp.eways.network.CallBackWrapper;
import com.srp.eways.repository.remote.BaseApiImplementation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eskafi on 1/11/2020.
 */
public class PhoneBookApiImplementation extends BaseApiImplementation implements PhoneBookApiService {

    private static PhoneBookApiImplementation sInstance;

    public static PhoneBookApiImplementation getInstance() {

        if (sInstance == null) {
            sInstance = new PhoneBookApiImplementation();
        }

        return sInstance;
    }


    @Override
    public void getUserPhoneBook(int pageIndex, int pagSize, final CallBackWrapper<PhoneBookResult> callBack) {
        if (!handleCall(callBack)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    callBack.onSuccess(createSuccessResult());
                }
            }, getResponseDelay());
        }
    }

    @Override
    public void getUserPhoneBookSearchResult(String searchValue, final CallBackWrapper<PhoneBookSearchResult> callBackWrapper) {

        if (!handleCall(callBackWrapper)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    callBackWrapper.onSuccess(createSuccessSearchResult());
                }
            }, getResponseDelay());
        }

    }

    private PhoneBookResult createSuccessResult() {
        List<UserPhoneBook> list = new ArrayList<>();
        list.add(new UserPhoneBook("user1", "0214410003"));
        list.add(new UserPhoneBook("user2", "0214410003"));
        list.add(new UserPhoneBook("user3", "0214410003"));

        PhoneBookResult response = new PhoneBookResult();
        response.setStatus(0);
        response.setPhoneBooks(list);

        return response;
    }

    private PhoneBookSearchResult createSuccessSearchResult() {

        ArrayList<PhoneBookSearchItem> list = new ArrayList<>();
        list.add(new PhoneBookSearchItem("user1", "0214410003"));
        list.add(new PhoneBookSearchItem("user2", "0214410003"));
        list.add(new PhoneBookSearchItem("user3", "0214410003"));

        PhoneBookSearchResult response = new PhoneBookSearchResult();
        response.setStatus(0);
        response.setItems(list);

        return response;
    }
}
