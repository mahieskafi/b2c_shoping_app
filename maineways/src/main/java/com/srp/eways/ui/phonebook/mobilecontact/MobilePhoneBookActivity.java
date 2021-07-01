package com.srp.eways.ui.phonebook.mobilecontact;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.view.ViewCompat;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.srp.eways.base.BaseActivity;
import com.srp.eways.base.BaseRecyclerAdapter;
import com.srp.eways.ui.phonebook.PhoneBookViewModel;
import com.srp.eways.ui.view.toolbar.WeiredToolbar;
import com.srp.eways.R;
import com.srp.eways.model.phonebook.PhoneBookResultWrapper;
import com.srp.eways.model.phonebook.UserPhoneBook;
import com.srp.eways.model.phonebook.search.PhoneBookSearchItem;
import com.srp.eways.model.phonebook.search.PhoneBookSearchResult;
import com.srp.eways.di.DIMain;
import com.srp.eways.ui.login.UserInfoViewModel;
import com.srp.eways.ui.view.EmptyView;
import com.srp.eways.ui.view.LoadingStateView;
import com.srp.eways.ui.view.SearchView;
import com.srp.eways.util.Utils;

import java.util.ArrayList;
import java.util.List;

import ir.abmyapp.androidsdk.IABResources;

import static android.view.View.GONE;
import static com.srp.eways.repository.phonebook.PhoneBookRepositoryImplementation.PHONEBOOKLOCALSOURCE;

public class MobilePhoneBookActivity extends BaseActivity<PhoneBookViewModel> implements MobileContactListAdapter.MobilePhoneBookClickListener {

    public static final String EXTRA_MOBILERPHONEBOOK = "extra_mobilePhoneBook";


    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, MobilePhoneBookActivity.class);

        return intent;
    }

    private EmptyView mEmptyView;
    private LoadingStateView mLoadingStateView;

    private CardView mListContainer;
    private RecyclerView mRecyclerView;
    private MobileContactListAdapter mAdapter;

    private ArrayList<UserPhoneBook> mData;

    private PhoneBookViewModel mUserPhoneBookViewModel;

    private WeiredToolbar mToolbar;
    private SearchView mSearchView;

    private View mSearchViewContainer;

    private Observer<PhoneBookSearchResult> mSearchResultLiveDataObserver;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_userphonebook;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mToolbar = findViewById(R.id.toolbar);
        mSearchView = findViewById(R.id.searchview);

        mSearchViewContainer = findViewById(R.id.container_searchview);
        mSearchViewContainer.setVisibility(View.VISIBLE);
        mSearchView.setDonKeyboard(true);

        setupToolbar();

        final IABResources abResources = DIMain.getABResources();

        mListContainer = findViewById(R.id.container_contactlist);
        mRecyclerView = findViewById(R.id.contactlist);
        mEmptyView = findViewById(R.id.emptyview);
        mLoadingStateView = findViewById(R.id.loadingstateview);

        mListContainer.setRadius(abResources.getDimen(R.dimen.card_radius));
        mListContainer.setCardElevation(abResources.getDimen(R.dimen.card_elevation));

        FrameLayout.LayoutParams containerLp = (FrameLayout.LayoutParams) mListContainer.getLayoutParams();
        containerLp.leftMargin = abResources.getDimenPixelSize(R.dimen.userphonebook_listcontainer_margin_left);
        containerLp.topMargin = abResources.getDimenPixelSize(R.dimen.userphonebook_listcontainer_margin_top);
        containerLp.rightMargin = abResources.getDimenPixelSize(R.dimen.userphonebook_listcontainer_margin_left);
        containerLp.bottomMargin = abResources.getDimenPixelSize(R.dimen.userphonebook_listcontainer_margin_bottom);
        mListContainer.setLayoutParams(containerLp);

        mListContainer.setContentPadding(abResources.getDimenPixelSize(R.dimen.chargecontact_itemview_name_margin_right), 0, abResources.getDimenPixelSize(R.dimen.chargecontact_itemview_phone_margin_left), 0);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        mAdapter = new MobileContactListAdapter(this, new BaseRecyclerAdapter.RetryClickListener() {
            @Override
            public void onClicked() {
                mUserPhoneBookViewModel.loadUserPhoneBook(false, PHONEBOOKLOCALSOURCE);
            }
        }, this);

        mLoadingStateView.setRetryListener(new LoadingStateView.RetryListener() {
            @Override
            public void onRetryButtonClicked() {
                mUserPhoneBookViewModel.loadUserPhoneBook(true, PHONEBOOKLOCALSOURCE);
            }
        });


        mUserPhoneBookViewModel.isContactListLoading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                if (isLoading == null) {
                    return;
                }

                if (isLoading) {
                    mEmptyView.setVisibility(GONE);
                    mListContainer.setVisibility(GONE);

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
                mAdapter.setIsLoading(isLoading);
            }
        });

        mUserPhoneBookViewModel.reset();
        registerObserverForUserPhoneBook();
        mUserPhoneBookViewModel.loadUserPhoneBook(false, PHONEBOOKLOCALSOURCE);
        mRecyclerView.setAdapter(mAdapter);


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

                    mUserPhoneBookViewModel.getPhoneBookSearchResultLiveData().observe(MobilePhoneBookActivity.this, mSearchResultLiveDataObserver);
                    mUserPhoneBookViewModel.searchUserPhoneBook(Utils.toEnglishNumber(searchText));
                } else {
                    setMainList();
                }
            }
        });
    }

    private void setupToolbar() {
        IABResources resources = DIMain.getABResources();

        ViewCompat.setElevation(mToolbar, resources.getDimen(R.dimen.userphonebook_fragment_toolbar_elevation));

        mToolbar.setShowTitle(true);
        mToolbar.setTitle(resources.getString(R.string.userphonebook_title));
        mToolbar.setShowTitle(true);
        mToolbar.setShowShop(false);
        mToolbar.setShowDeposit(false);
        mToolbar.showNavigationDrawer(false);
        mToolbar.setShowNavigationUp(true);
        mToolbar.setShowNavigationUp(true);
        mToolbar.setShowNavigationDrawerMenu(false);
        mToolbar.setOnBackClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mToolbar.setBackgroundColor(resources.getColor(R.color.userinputchoice_fragment_toolbar_background));

        mToolbar.setOnBackClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideKeyboard(MobilePhoneBookActivity.this);
                finish();
            }
        });

        UserInfoViewModel userInfoViewModel = DIMain.getViewModel(UserInfoViewModel.class);
        userInfoViewModel.getCreditLiveData().observe(this, new Observer<Long>() {
            @Override
            public void onChanged(Long credit) {
                if (credit != null) {
                    mToolbar.setDeposit(credit);
                }
            }
        });

        int searchViewContainerPaddingLeft = resources.getDimenPixelSize(R.dimen.userphonebook_searchview_background_paddingleft);
        int searchViewContainerPaddingRight = resources.getDimenPixelSize(R.dimen.userphonebook_searchview_background_paddingright);
        int searchViewContainerPaddingTop = resources.getDimenPixelSize(R.dimen.userphonebook_searchview_background_paddingtop);
        int searchViewContainerPaddingBottom = resources.getDimenPixelSize(R.dimen.userphonebook_searchview_background_paddingbottom);

        mSearchViewContainer.setPadding(searchViewContainerPaddingLeft, searchViewContainerPaddingTop, searchViewContainerPaddingRight, searchViewContainerPaddingBottom);

        ViewCompat.setElevation(mSearchViewContainer, resources.getDimen(R.dimen.userphonebook_fragment_searchview_container_elevation));
        mSearchViewContainer.setBackgroundColor(resources.getColor(R.color.searchview_container_background_color));
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
                    mData = (ArrayList<UserPhoneBook>) userPhoneBooks;
                    mAdapter.setNewData(mData);

                    mLoadingStateView.setVisibility(GONE);
                    mEmptyView.setVisibility(GONE);
                    mListContainer.setVisibility(View.VISIBLE);


                    if (errorString != null) {
//                        mAdapter.setHasError(true);
                    } else {
//                        mAdapter.setHasError(false);
                    }
                } else {
                    mListContainer.setVisibility(GONE);

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

                    mListContainer.setVisibility(View.VISIBLE);

                    ArrayList<UserPhoneBook> userPhoneBooks = new ArrayList<>();
                    for (PhoneBookSearchItem item : phoneBookSearchResult.getItems()) {
                        UserPhoneBook userPhoneBook =
                                new UserPhoneBook(item.getFirstName() + " " + item.getLastName(), item.getCellPhone());

                        userPhoneBooks.add(userPhoneBook);
                    }

                    mAdapter.setNewData(userPhoneBooks);
                    mRecyclerView.setAdapter(mAdapter);
                } else if (phoneBookSearchResult.getItems().size() == 0) {
                    mLoadingStateView.setVisibility(GONE);
                    mEmptyView.setVisibility(View.VISIBLE);

                    mListContainer.setVisibility(GONE);
                }
            }
        };

    }

    private void setMainList() {

        if (mData != null) {

            mUserPhoneBookViewModel.getPhoneBookSearchResultLiveData().removeObserver(mSearchResultLiveDataObserver);
            mAdapter.setNewData(mData);
            mRecyclerView.setAdapter(mAdapter);
            mLoadingStateView.setVisibility(GONE);
            mEmptyView.setVisibility(GONE);
            mListContainer.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public PhoneBookViewModel getViewModel() {
        return mUserPhoneBookViewModel = DIMain.getViewModel(PhoneBookViewModel.class);
    }

    @Override
    public void onBackPressed() {
        if (mData != null) {
            if (mAdapter.getData().size() == mData.size()) {
                 super.onBackPressed();
            } else {
                mSearchView.setText("");
            }
        }else {
            super.onBackPressed();
        }
    }

    @Override
    public void onPhoneBookItemClicked(UserPhoneBook userPhoneBook) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_MOBILERPHONEBOOK, userPhoneBook);

        Utils.hideKeyboard(MobilePhoneBookActivity.this);

        setResult(RESULT_OK, intent);

        finish();
    }
}
