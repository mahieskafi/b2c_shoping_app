package com.srp.eways.repository.remote.phonebook;

import com.srp.eways.model.phonebook.AddOrRemovePhoneBookResponse;
import com.srp.eways.model.phonebook.UserPhoneBook;
import com.srp.eways.network.CallBackWrapper;
import com.srp.eways.AppConfig;
import com.srp.eways.di.DIMain;
import com.srp.eways.model.phonebook.PhoneBookResult;
import com.srp.eways.model.phonebook.search.PhoneBookSearchResult;
import com.srp.eways.repository.remote.BaseApiImplementation;
import com.srp.eways.repository.remote.DefaultRetroCallback;

public class PhoneBookApiImplementation extends BaseApiImplementation implements PhoneBookApiService {

    private static PhoneBookApiImplementation sInstance;

    public static PhoneBookApiImplementation getInstance() {

        if (sInstance == null) {
            sInstance = new PhoneBookApiImplementation();
        }

        return sInstance;
    }

    private PhoneBookApiRetrofit mPhoneBookApi;

    private PhoneBookApiImplementation() {
        mPhoneBookApi = DIMain.provideApi(PhoneBookApiRetrofit.class);
    }

    @Override
    public void getUserPhoneBook(int pageIndex, int pageSize, CallBackWrapper<PhoneBookResult> callBack) {
        mPhoneBookApi.getUserPhoneBook(pageIndex, pageSize, AppConfig.SERVER_VERSION).enqueue(new DefaultRetroCallback<PhoneBookResult>(callBack) {

            @Override
            protected void checkResponseForError(PhoneBookResult result, ErrorInfo errorInfo) {
                if (result.getStatus() != 0) {
                    errorInfo.errorCode = result.getStatus();
                    errorInfo.errorMessage = result.getDescription();
                }
            }

        });
    }

    @Override
    public void getUserPhoneBookSearchResult(String searchValue, CallBackWrapper<PhoneBookSearchResult> callBackWrapper) {
        mPhoneBookApi.getUserPhoneBookSearchResult(AppConfig.SERVER_VERSION, searchValue).enqueue(new DefaultRetroCallback<PhoneBookSearchResult>(callBackWrapper) {

            @Override
            protected void checkResponseForError(PhoneBookSearchResult result, ErrorInfo errorInfo) {
                if (result.getStatus() != 0) {
                    errorInfo.errorCode = result.getStatus();
                    errorInfo.errorMessage = result.getDescription();
                }
            }

        });
    }

    @Override
    public void addUserPhoneBook(UserPhoneBook userPhoneBook, CallBackWrapper<AddOrRemovePhoneBookResponse> callBack) {
        mPhoneBookApi.addUserPhoneBook(userPhoneBook, AppConfig.SERVER_VERSION).enqueue(new DefaultRetroCallback<AddOrRemovePhoneBookResponse>(callBack){
            @Override
            protected void checkResponseForError(AddOrRemovePhoneBookResponse response, ErrorInfo errorInfo) {
                if (response.getStatus() != 0) {
                    errorInfo.errorCode = response.getStatus();
                    errorInfo.errorMessage = response.getDescription();
                }
            }
        });
    }


    @Override
    public void removeUserPhoneBook(UserPhoneBook userPhoneBook, CallBackWrapper<AddOrRemovePhoneBookResponse> callBack) {
        mPhoneBookApi.removeUserPhoneBook(userPhoneBook, AppConfig.SERVER_VERSION).enqueue(new DefaultRetroCallback<AddOrRemovePhoneBookResponse>(callBack){
            @Override
            protected void checkResponseForError(AddOrRemovePhoneBookResponse response, ErrorInfo errorInfo) {
                if (response.getStatus() != 0) {
                    errorInfo.errorCode = response.getStatus();
                    errorInfo.errorMessage = response.getDescription();
                }
            }
        });
    }
}
