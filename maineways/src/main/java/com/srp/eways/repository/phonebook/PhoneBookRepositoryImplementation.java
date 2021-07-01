package com.srp.eways.repository.phonebook;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.srp.eways.model.phonebook.AddOrRemovePhoneBookResponse;
import com.srp.eways.model.phonebook.search.PhoneBookSearchItem;
import com.srp.eways.network.CallBackWrapper;
import com.srp.eways.network.NetworkResponseCodes;
import com.srp.eways.di.DIMain;
import com.srp.eways.model.phonebook.PhoneBookResult;
import com.srp.eways.model.phonebook.PhoneBookResultWrapper;
import com.srp.eways.model.phonebook.UserPhoneBook;
import com.srp.eways.model.phonebook.search.PhoneBookSearchResult;
import com.srp.eways.repository.remote.phonebook.PhoneBookApiImplementation;
import com.srp.eways.ui.phonebook.ewayscontact.contactlist.UserBookResponseReadyListener;
import com.srp.eways.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class PhoneBookRepositoryImplementation implements PhoneBookRepository {

    private static final int PAGE_SIZE = 20;

    public static final String PHONEBOOKEWAYSSOURCE = "eways";
    public static final String PHONEBOOKLOCALSOURCE = "local";

    private List<UserPhoneBook> mData = new ArrayList<>();

    private int mPageIndex = 0;

    private boolean mIsLoading = false;
    private boolean mHasMore = true;

    private static PhoneBookRepositoryImplementation instance = null;

    public static PhoneBookRepositoryImplementation getInstance() {
        if (instance == null)
            instance = new PhoneBookRepositoryImplementation();

        return instance;
    }

    private PhoneBookApiImplementation mPhoneBookApiImplementation;

    private PhoneBookRepositoryImplementation() {

        mPhoneBookApiImplementation = DIMain.getPhoneBookApiImplementation();

    }

    @Override
    public void getUserPhoneBook(final UserBookResponseReadyListener callBack, final String source) {
        if (mIsLoading) {
            return;
        }

//        mIsLoading = true;

        mPhoneBookApiImplementation.getUserPhoneBook(mPageIndex, PAGE_SIZE, new CallBackWrapper<PhoneBookResult>() {

            @Override
            public void onSuccess(PhoneBookResult responseBody) {

                mIsLoading = false;

                if (source.equals(PHONEBOOKEWAYSSOURCE)) {
                    mData.addAll(responseBody.getPhoneBooks());

                } else if (source.equals(PHONEBOOKLOCALSOURCE)) {
                    mData.clear();
                    mData.addAll(getPhoneBookContact());
                }

                mPageIndex++;
                PhoneBookResultWrapper result = new PhoneBookResultWrapper(mData, NetworkResponseCodes.SUCCESS, null);
                mHasMore = responseBody.getPhoneBooks().size() >= PAGE_SIZE;
                callBack.onResponseReady(result);
            }

            @Override
            public void onError(int errorCode, String errorMessage) {
                mIsLoading = false;

                mHasMore = true;

                PhoneBookResultWrapper result = new PhoneBookResultWrapper(mData, errorCode, errorMessage);
                callBack.onResponseReady(result);
            }

        });
    }

    @Override
    public void getUserPhoneBookSearchResult(String searchValue, CallBackWrapper<PhoneBookSearchResult> callBack, String source) {
        if (source.equals(PHONEBOOKEWAYSSOURCE)) {

            mPhoneBookApiImplementation.getUserPhoneBookSearchResult(searchValue, callBack);
        } else if (source.equals(PHONEBOOKLOCALSOURCE)) {

            PhoneBookSearchResult phoneBookSearchResult = new PhoneBookSearchResult();
            ArrayList<PhoneBookSearchItem> phoneBookSearchItemArrayList = new ArrayList<>();

            for (UserPhoneBook index : mData) {

                if (index.getFullName().toLowerCase().contains(searchValue) || index.getCellPhone().toLowerCase().contains(searchValue)) {
                    PhoneBookSearchItem matchPhoneBookItem = new PhoneBookSearchItem(index.getFirstName(), index.getLastName(), index.getCellPhone());
                    phoneBookSearchItemArrayList.add(matchPhoneBookItem);
                }

            }
            phoneBookSearchResult.setItems(phoneBookSearchItemArrayList);

            callBack.onSuccess(phoneBookSearchResult);
        }
    }

    @Override
    public void addUserPhoneBook(UserPhoneBook contact, CallBackWrapper<AddOrRemovePhoneBookResponse> callBack) {
        mPhoneBookApiImplementation.addUserPhoneBook(contact, callBack);
    }

    @Override
    public void removeUserPhoneBook(UserPhoneBook contact, CallBackWrapper<AddOrRemovePhoneBookResponse> callBack) {
        mPhoneBookApiImplementation.removeUserPhoneBook(contact, callBack);
    }

    public void reset() {

        mPageIndex = 0;
        mData = new ArrayList<>();
    }

    @Override
    public boolean hasMore() {
        return mHasMore;
    }

    public List<UserPhoneBook> getPhoneBookContact() {
        Context context = DIMain.getContext();
        List<UserPhoneBook> data = new ArrayList<>();

        Cursor cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, ContactsContract.Contacts.DISPLAY_NAME);
        cursor.moveToFirst();
        String check = null;

        if (cursor.moveToNext()) {
            do {
                if (Utils.creatStandardPhoneInput(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))).equals(check))
                    continue;
                data.add(new UserPhoneBook(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)), Utils.creatStandardPhoneInput(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)))));
                check = Utils.creatStandardPhoneInput(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
            } while (cursor.moveToNext());

        }

        cursor.close();
        return data;

    }

}
