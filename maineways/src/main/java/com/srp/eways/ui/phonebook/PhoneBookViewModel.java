package com.srp.eways.ui.phonebook;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.srp.eways.base.BaseViewModel;
import com.srp.eways.model.phonebook.AddOrRemovePhoneBookResponse;
import com.srp.eways.model.phonebook.UserPhoneBook;
import com.srp.eways.network.BaseCallBackWrapper;
import com.srp.eways.di.DIMain;
import com.srp.eways.model.phonebook.PhoneBookResultWrapper;
import com.srp.eways.model.phonebook.search.PhoneBookSearchResult;
import com.srp.eways.repository.phonebook.PhoneBookRepository;
import com.srp.eways.ui.phonebook.ewayscontact.contactlist.UserBookResponseReadyListener;


public class PhoneBookViewModel extends BaseViewModel {

    private static PhoneBookViewModel sInstance = null;

    public static PhoneBookViewModel newInstance() {

        if (sInstance == null) {
            sInstance = new PhoneBookViewModel();
        }
        return sInstance;
    }

    private PhoneBookRepository mRepository;

    private MutableLiveData<PhoneBookResultWrapper> mPhoneBookResultWrapperLive = new MutableLiveData<>();
    private MutableLiveData<PhoneBookSearchResult> mPhoneBookSearchResultLiveData = new MutableLiveData<>();
    private MutableLiveData<AddOrRemovePhoneBookResponse> mPhoneBookAddContactLiveData = new MutableLiveData<>();
    private MutableLiveData<AddOrRemovePhoneBookResponse> mPhoneBookRemoveContactLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> mContactListLoading = new MutableLiveData<>();
    private MutableLiveData<Boolean> mIsListReset = new MutableLiveData<>();

    private MutableLiveData<UserPhoneBook> mEditableUserPhoneBookLiveData = new MutableLiveData<>();

    private PhoneBookResultWrapper mUserPhoneBookResult;

    private MutableLiveData<Boolean> mIsLoadingMoreData = new MutableLiveData<>();

    private String mPhoneBookSource = null;
    private UserPhoneBook mEditedUserPhoneBook = null;

    public PhoneBookViewModel() {
        mRepository = DIMain.getPhoneBookRepository();
    }

    public void loadUserPhoneBook(final boolean loadFirstPage, String PhoneBooksource) {

        mPhoneBookSource = PhoneBooksource;

        if (loadFirstPage) {
            setIsLoadingFirstPage(true);

            setIsLoadingMore(false);
        } else {
            setIsLoadingFirstPage(false);

            setIsLoadingMore(true);
        }

        mRepository.getUserPhoneBook(new UserBookResponseReadyListener() {
            @Override
            public void onResponseReady(PhoneBookResultWrapper result) {
                if (loadFirstPage) {
                    setContactListLoading(false);
                } else {
                    setIsLoadingMore(false);
                }

                mUserPhoneBookResult = result;

                mPhoneBookResultWrapperLive.setValue(result);
            }
        }, PhoneBooksource);
    }

    public void searchUserPhoneBook(final String searchValue) {

        setContactListLoading(true);

        mRepository.getUserPhoneBookSearchResult(searchValue, new BaseCallBackWrapper<PhoneBookSearchResult>(this) {
            @Override
            public void onError(int errorCode, String errorMessage) {
                setContactListLoading(false);
            }

            @Override
            public void onSuccess(PhoneBookSearchResult responseBody) {
                setContactListLoading(false);

                mPhoneBookSearchResultLiveData.setValue(responseBody);
            }
        }, mPhoneBookSource);
    }

    public void addUserPhoneBook(UserPhoneBook contact) {

        if (contact.getId() != 0L) {
            setEditedUserPhoneBook(contact);
        }

        setLoading(true);
        mRepository.addUserPhoneBook(contact, new BaseCallBackWrapper<AddOrRemovePhoneBookResponse>(this) {
            @Override
            public void onError(int errorCode, String errorMessage) {
                AddOrRemovePhoneBookResponse addPhoneBookResponse = new AddOrRemovePhoneBookResponse();
                addPhoneBookResponse.setStatus(errorCode);
                addPhoneBookResponse.setDescription(errorMessage);

                mPhoneBookAddContactLiveData.setValue(addPhoneBookResponse);

            }

            @Override
            public void onSuccess(AddOrRemovePhoneBookResponse responseBody) {
                setLoading(false);
                mPhoneBookAddContactLiveData.setValue(responseBody);
            }
        });

    }

    public void removeUserPhoneBook(UserPhoneBook contact) {

        setContactListLoading(true);
        mRepository.removeUserPhoneBook(contact, new BaseCallBackWrapper<AddOrRemovePhoneBookResponse>(this) {
            @Override
            public void onError(int errorCode, String errorMessage) {
                AddOrRemovePhoneBookResponse addPhoneBookResponse = new AddOrRemovePhoneBookResponse();
                addPhoneBookResponse.setStatus(errorCode);
                addPhoneBookResponse.setDescription(errorMessage);

                setContactListLoading(false);

                mPhoneBookRemoveContactLiveData.setValue(addPhoneBookResponse);

            }

            @Override
            public void onSuccess(AddOrRemovePhoneBookResponse responseBody) {
                setContactListLoading(false);
                mPhoneBookRemoveContactLiveData.setValue(responseBody);
            }
        });

    }

    public LiveData<PhoneBookResultWrapper> getPhoneBookResultWrapperLive() {
        return mPhoneBookResultWrapperLive;
    }


    public MutableLiveData<PhoneBookSearchResult> getPhoneBookSearchResultLiveData() {
        return mPhoneBookSearchResultLiveData;
    }

    public MutableLiveData<AddOrRemovePhoneBookResponse> getPhoneBookAddContactLiveData() {
        return mPhoneBookAddContactLiveData;
    }

    public void consumedPhoneBookAddContactLiveData() {
        mPhoneBookAddContactLiveData.setValue(null);
    }

    public MutableLiveData<Boolean> getIsListResetLiveData() {
        return mIsListReset;
    }

    public void setIsListResetLiveData(Boolean isListReset) {
         mIsListReset.setValue(isListReset);
    }

    public void consumedIsListResetLiveData() {
        mIsListReset.setValue(null);
    }

    public MutableLiveData<AddOrRemovePhoneBookResponse> getPhoneBookRemoveContactLiveData() {
        return mPhoneBookRemoveContactLiveData;
    }

    public void onPhoneBookResultDataConsumed() {
        mPhoneBookResultWrapperLive.setValue(null);
    }

    public MutableLiveData<Boolean> isLoadingMoreData() {
        return mIsLoadingMoreData;
    }

    public void setLoadingConsumed() {
        setLoading(null);
    }

    private void setIsLoadingFirstPage(boolean isLoadingFirstPage) {
        setContactListLoading(isLoadingFirstPage);
    }

    private void setIsLoadingMore(boolean isLoadingMore) {
        mIsLoadingMoreData.setValue(isLoadingMore);
    }

    public void onListScrolled(int lastVisibleItemAdapterPosition) {
        Boolean isLoadingUserPhoneBook = isLoadingMoreData().getValue();

        if (isLoadingUserPhoneBook != null && isLoadingUserPhoneBook) {
            return;
        }

        if (!mRepository.hasMore()) {
            return;
        }

        PhoneBookResultWrapper phoneBookResultWrapper = getUserPhoneBookResult();

        if (phoneBookResultWrapper != null && phoneBookResultWrapper.userPhoneBooks.size() > 0 && lastVisibleItemAdapterPosition == phoneBookResultWrapper.userPhoneBooks.size() - 1) {
            loadUserPhoneBook(false, mPhoneBookSource);
        }
    }

    private PhoneBookResultWrapper getUserPhoneBookResult() {
        return mUserPhoneBookResult;
    }

    public void reset() {
        mRepository.reset();
    }

    public MutableLiveData<UserPhoneBook> getEditableUserPhoneBookLiveData() {
        return mEditableUserPhoneBookLiveData;
    }

    public void setEditableUserPhoneBookLiveData(UserPhoneBook userPhoneBook) {
        mEditableUserPhoneBookLiveData.setValue(userPhoneBook);
    }

    public MutableLiveData<Boolean> isContactListLoading() {
        return mContactListLoading;
    }

    public void setContactListLoading(Boolean isLoading) {
        mContactListLoading.setValue(isLoading);
    }

    public void setContactListLoadingConsumed() {
        mContactListLoading.setValue(null);
    }

    public UserPhoneBook getEditedUserPhoneBook() {
        return mEditedUserPhoneBook;
    }

    public void setEditedUserPhoneBook(UserPhoneBook userPhoneBook) {
        mEditedUserPhoneBook = userPhoneBook;
    }
}
