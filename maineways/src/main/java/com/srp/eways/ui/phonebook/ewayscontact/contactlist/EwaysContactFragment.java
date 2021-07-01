package com.srp.eways.ui.phonebook.ewayscontact.contactlist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.srp.eways.base.BaseRecyclerAdapter;
import com.srp.eways.base.BaseRecyclerAdapter2;
import com.srp.eways.model.phonebook.AddOrRemovePhoneBookResponse;
import com.srp.eways.model.phonebook.search.PhoneBookSearchItem;
import com.srp.eways.network.NetworkResponseCodes;
import com.srp.eways.ui.navigation.NavigationMemberFragment;
import com.srp.eways.R;
import com.srp.eways.model.phonebook.PhoneBookResultWrapper;
import com.srp.eways.model.phonebook.UserPhoneBook;
import com.srp.eways.model.phonebook.search.PhoneBookSearchResult;
import com.srp.eways.di.DIMain;
import com.srp.eways.ui.phonebook.PhoneBookViewModel;
import com.srp.eways.ui.phonebook.ewayscontact.ContactRemoveDialog;
import com.srp.eways.ui.view.EmptyView;
import com.srp.eways.ui.view.LoadingStateView;
import com.srp.eways.ui.view.SearchView;
import com.srp.eways.util.Utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ir.abmyapp.androidsdk.IABResources;

import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;
import static com.srp.eways.repository.phonebook.PhoneBookRepositoryImplementation.PHONEBOOKEWAYSSOURCE;

public class EwaysContactFragment extends NavigationMemberFragment<PhoneBookViewModel>
        implements EwaysContactListAdapter.UserPhoneBookClickListener {

    public static final String EXTRA_USERPHONEBOOK = "extra_userPhoneBook";

    public static EwaysContactFragment getInstance() {
        return new EwaysContactFragment();
    }

    private EmptyView mEmptyView;
    private LoadingStateView mLoadingStateView;

    private RecyclerView mRecyclerView;
    private EwaysContactListAdapter mAdapter;

    private ArrayList<UserPhoneBook> mData;

    private PhoneBookViewModel mUserPhoneBookViewModel;

    private SearchView mSearchView;

    private View mSearchViewContainer;

    private Observer<PhoneBookSearchResult> mSearchResultLiveDataObserver;

    private UserPhoneBook mDeletedUserPhoneBook;

    private ContactRemoveDialog mRemoveDialog = null;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_ewaysphonebook;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final IABResources abResources = DIMain.getABResources();

        mSearchView = view.findViewById(R.id.searchview);

        mSearchViewContainer = view.findViewById(R.id.container_searchview);
        mSearchViewContainer.setVisibility(View.VISIBLE);
        mSearchView.setBackground(R.drawable.eways_phone_book_search_view_background);

        mRecyclerView = view.findViewById(R.id.contactlist);
        mEmptyView = view.findViewById(R.id.emptyview);
        mLoadingStateView = view.findViewById(R.id.loadingstateview);

        FrameLayout.LayoutParams containerLp = (FrameLayout.LayoutParams) mRecyclerView.getLayoutParams();
        containerLp.leftMargin = abResources.getDimenPixelSize(R.dimen.userphonebook_listcontainer_margin_left);
        containerLp.topMargin = abResources.getDimenPixelSize(R.dimen.userphonebook_listcontainer_margin_top);
        containerLp.rightMargin = abResources.getDimenPixelSize(R.dimen.userphonebook_listcontainer_margin_left);
        containerLp.bottomMargin = abResources.getDimenPixelSize(R.dimen.userphonebook_listcontainer_margin_bottom);
        mRecyclerView.setLayoutParams(containerLp);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        mAdapter = new EwaysContactListAdapter(getContext(), new BaseRecyclerAdapter2.RetryClickListener() {
            @Override
            public void onClicked() {
                mUserPhoneBookViewModel.loadUserPhoneBook(false, PHONEBOOKEWAYSSOURCE);
            }
        }, this);

        mLoadingStateView.setRetryListener(new LoadingStateView.RetryListener() {
            @Override
            public void onRetryButtonClicked() {
                mUserPhoneBookViewModel.loadUserPhoneBook(true, PHONEBOOKEWAYSSOURCE);
            }
        });

        mRecyclerView.setAdapter(mAdapter);


        if (!isNetworkConnected()) {
            //Todo: register networkStateChanged
        } else {
            //Todo: unregister networkStateChanged

            mUserPhoneBookViewModel.isContactListLoading().observe(this, new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean isLoading) {
                    if (isLoading == null) {
                        return;
                    }

                    if (isLoading) {
                        mEmptyView.setVisibility(GONE);
                        mRecyclerView.setVisibility(GONE);

                        mLoadingStateView.setVisibility(View.VISIBLE);
                        mLoadingStateView.setStateAndDescription(LoadingStateView.STATE_LOADING,
                                abResources.getString(R.string.loading_message),
                                true);
                    } else {
                        mLoadingStateView.setVisibility(GONE);
                    }

                    mUserPhoneBookViewModel.setContactListLoadingConsumed();
                }
            });

            mUserPhoneBookViewModel.isLoadingMoreData().observe(this, new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean isLoading) {
                    mAdapter.setHasMoreData(isLoading);
                }
            });

            mUserPhoneBookViewModel.reset();
            registerObserverForUserPhoneBook();

            mUserPhoneBookViewModel.loadUserPhoneBook(true, PHONEBOOKEWAYSSOURCE);
        }

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager layoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
                int lastVisibleItemAdapterPosition = layoutManager.findLastCompletelyVisibleItemPosition();

                mUserPhoneBookViewModel.onListScrolled(lastVisibleItemAdapterPosition);
            }
        });

        registerObserverForSearchResult();

        mSearchView.setSearchViewListener(new SearchView.SearchViewListener() {
            @Override
            public void onSearchTextChanged(String searchText) {
                if (!searchText.isEmpty()) {

                    mUserPhoneBookViewModel.getPhoneBookSearchResultLiveData().observe(EwaysContactFragment.this, mSearchResultLiveDataObserver);
                    mUserPhoneBookViewModel.searchUserPhoneBook(Utils.toEnglishNumber(searchText));
                } else {
                    setMainList();
                }
            }
        });

        mUserPhoneBookViewModel.getPhoneBookRemoveContactLiveData().observe(this, new Observer<AddOrRemovePhoneBookResponse>() {
            @Override
            public void onChanged(AddOrRemovePhoneBookResponse addOrRemovePhoneBookResponse) {
                if (addOrRemovePhoneBookResponse != null && mData != null) {
                    if (addOrRemovePhoneBookResponse.getStatus() == NetworkResponseCodes.SUCCESS) {

                        mRecyclerView.setVisibility(View.VISIBLE);

                        Iterator iterator = mData.iterator();
                        while (iterator.hasNext()) {
                            UserPhoneBook userPhoneBook = (UserPhoneBook) iterator.next();
                            if (userPhoneBook.getId() == mDeletedUserPhoneBook.getId()) {
                                iterator.remove();
                                break;
                            }
                        }

                        ArrayList<UserPhoneBook> userPhoneBooks = (ArrayList<UserPhoneBook>) mAdapter.getData();
                        for (int i = 0; i < userPhoneBooks.size(); i++) {
                            if (userPhoneBooks.get(i).getId() == mDeletedUserPhoneBook.getId()) {
                                mAdapter.notifyDataRemovedAt(i);
                                return;
                            }
                        }
                    }
                }
            }
        });

        mUserPhoneBookViewModel.getIsListResetLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isListReset) {
                UserPhoneBook editedUserPhoneBook = mUserPhoneBookViewModel.getEditedUserPhoneBook();

                if (isListReset != null && isListReset) {

                    if (editedUserPhoneBook != null) {
                        for (int i = 0; i < mData.size(); i++) {
                            if (mData.get(i).getId() == editedUserPhoneBook.getId()) {
                                mData.set(i, editedUserPhoneBook);
                                mSearchView.setText("");
                                break;
                            }
                        }

                        ArrayList<UserPhoneBook> userPhoneBooks = (ArrayList<UserPhoneBook>) mAdapter.getData();
                        for (int i = 0; i < userPhoneBooks.size(); i++) {
                            if (userPhoneBooks.get(i).getId() == editedUserPhoneBook.getId()) {
                                mAdapter.notifyItemChanged(i, editedUserPhoneBook);
                                break;
                            }
                        }

                        mUserPhoneBookViewModel.setEditedUserPhoneBook(null);
                    } else {
                        mUserPhoneBookViewModel.reset();
                        mUserPhoneBookViewModel.loadUserPhoneBook(true, PHONEBOOKEWAYSSOURCE);
                    }
                    mUserPhoneBookViewModel.consumedIsListResetLiveData();
                }
            }
        });
    }


    private void registerObserverForUserPhoneBook() {
        mUserPhoneBookViewModel.getPhoneBookResultWrapperLive().observe(this, new Observer<PhoneBookResultWrapper>() {
            @Override
            public void onChanged(PhoneBookResultWrapper phoneBookResultWrapper) {
                if (phoneBookResultWrapper == null) {
                    return;
                }

                List<UserPhoneBook> userPhoneBooks = phoneBookResultWrapper.userPhoneBooks;
                String errorString = phoneBookResultWrapper.errorMessage;

                boolean hasData = (userPhoneBooks != null && userPhoneBooks.size() > 0);

                if (hasData) {
                    mLoadingStateView.setVisibility(GONE);
                    mEmptyView.setVisibility(GONE);

                    mRecyclerView.setVisibility(View.VISIBLE);

                    mAdapter.setNewData((ArrayList<UserPhoneBook>) userPhoneBooks);
                    mData = (ArrayList<UserPhoneBook>) userPhoneBooks;

                    if (errorString != null) {
//                        mAdapter.setHasError(true);
                    } else {
//                        mAdapter.setHasError(false);
                    }
                } else {
                    mRecyclerView.setVisibility(GONE);

                    if (errorString != null) {
                        mEmptyView.setVisibility(GONE);

                        mLoadingStateView.setVisibility(View.VISIBLE);
                        mLoadingStateView.setStateAndDescription(LoadingStateView.STATE_ERROR, errorString, true);
                    } else {
                        mLoadingStateView.setVisibility(GONE);

                        mEmptyView.setVisibility(View.VISIBLE);
                    }
                }

                mUserPhoneBookViewModel.onPhoneBookResultDataConsumed();
            }
        });
    }

    private void registerObserverForSearchResult() {
        mSearchResultLiveDataObserver = new Observer<PhoneBookSearchResult>() {
            @Override
            public void onChanged(PhoneBookSearchResult phoneBookSearchResult) {
                if (phoneBookSearchResult.getItems().size() > 0) {
                    mLoadingStateView.setVisibility(GONE);
                    mEmptyView.setVisibility(GONE);

                    mRecyclerView.setVisibility(View.VISIBLE);

                    ArrayList<UserPhoneBook> userPhoneBooks = new ArrayList<>();
                    for (PhoneBookSearchItem item : phoneBookSearchResult.getItems()) {
                        UserPhoneBook userPhoneBook =
                                new UserPhoneBook(item.getId(), item.getFirstName(), item.getLastName(), item.getCellPhone(), 0L, "");

                        userPhoneBooks.add(userPhoneBook);
                    }

                    mAdapter.setViewType(EwaysContactListAdapter.VIEW_TYPE_SEARCHITEM);
                    mAdapter.setNewData(userPhoneBooks);
                    mRecyclerView.setAdapter(mAdapter);
                }
            }
        };

    }

    private void setMainList() {
        mUserPhoneBookViewModel.getPhoneBookSearchResultLiveData().removeObserver(mSearchResultLiveDataObserver);

        mLoadingStateView.setVisibility(GONE);
        mEmptyView.setVisibility(GONE);

        mRecyclerView.setVisibility(View.VISIBLE);

        mAdapter.setViewType(EwaysContactListAdapter.VIEW_TYPE_CONTACT_ITEM);
        mAdapter.setNewData(mData);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public PhoneBookViewModel acquireViewModel() {
        return mUserPhoneBookViewModel = DIMain.getViewModel(PhoneBookViewModel.class);
    }

    @Override
    public void onPhoneBookItemClicked(UserPhoneBook userPhoneBook) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_USERPHONEBOOK, userPhoneBook);

        Utils.hideKeyboard(getActivity());

        getActivity().setResult(RESULT_OK, intent);

        getActivity().finish();
    }

    @Override
    public void onPhoneBookItemRemoved(UserPhoneBook userPhoneBook) {
        mDeletedUserPhoneBook = userPhoneBook;
        mRemoveDialog = new ContactRemoveDialog(getContext(), new ContactRemoveDialog.ContactRemoveDialogActionListeners() {


            @Override
            public void onKeep() {
                mRemoveDialog.dismiss();
                mRemoveDialog = null;
            }

            @Override
            public void onRemove() {

                mUserPhoneBookViewModel.removeUserPhoneBook(mDeletedUserPhoneBook);
                mRemoveDialog.dismiss();
                mRemoveDialog = null;
            }

            @Override
            public void onClose() {
                mRemoveDialog.dismiss();
                mRemoveDialog = null;
            }
        });
        mRemoveDialog.show();

    }

    @Override
    public void onPhoneBookItemEdited(UserPhoneBook userPhoneBook) {
        mUserPhoneBookViewModel.setEditableUserPhoneBookLiveData(searchOnManList(userPhoneBook));
    }

    @Override
    public void onPhoneBookItemShowMore(boolean isShowMore, UserPhoneBook userPhoneBook) {
        ArrayList<UserPhoneBook> userPhoneBooks = (ArrayList<UserPhoneBook>) mAdapter.getData();

        for (int i = 0; i < userPhoneBooks.size(); i++) {
            if (userPhoneBooks.get(i).getId() == userPhoneBook.getId()) {
                if (userPhoneBooks.get(i).getShowMore() != isShowMore) {
                    mAdapter.getData().get(i).setShowMore(isShowMore);
                }
            } else {
                mAdapter.getData().get(i).setShowMore(false);
            }
        }
        mAdapter.notifyDataSetChanged();
    }


    private UserPhoneBook searchOnManList(UserPhoneBook userPhoneBook) {
        for (int i = 0; i < mData.size(); i++) {
            if (mData.get(i).getId() == userPhoneBook.getId()) {
                return mData.get(i);
            }
        }
        return userPhoneBook;
    }

    @Override
    public boolean onBackPress() {
        if (mData != null) {
            if (mAdapter.getData().size() == mData.size()) {
                return super.onBackPress();
            } else {
                mSearchView.setText("");
                return true;
            }
        } else {
            return super.onBackPress();
        }

    }
}
