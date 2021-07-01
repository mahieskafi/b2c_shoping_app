package com.srp.ewayspanel.ui.store.filter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.srp.eways.base.BaseViewModel;
import com.srp.eways.ui.navigation.NavigationMemberFragment;
import com.srp.eways.ui.view.dialog.BottomDialog;
import com.srp.ewayspanel.R;
import com.srp.ewayspanel.di.DI;
import com.srp.ewayspanel.model.storepage.filter.FilterProductRequest;
import com.srp.ewayspanel.model.storepage.filter.SortItem;
import com.srp.eways.ui.charge.buy.RadioOptionModel;
import com.srp.ewayspanel.ui.store.GridProductFragment;
import com.srp.ewayspanel.ui.store.mainpage.StoreMainPage;
import com.srp.ewayspanel.ui.store.search.SearchFragment;
import com.srp.eways.ui.view.NColumnRadioGroup;
import com.srp.ewayspanel.ui.view.store.StoreFilterHeaderView;
import com.srp.ewayspanel.ui.view.store.sort.SortRadioGroup;
import com.srp.eways.ui.charge.dialog.DialogContentView;

import ir.abmyapp.androidsdk.IABResources;

import static android.app.Activity.RESULT_OK;
import static com.srp.ewayspanel.ui.store.GridProductFragment.FILTER_PRODUCT_REQUEST;
import static com.srp.ewayspanel.ui.store.GridProductFragment.IS_SEARCH_PRODUCT_REQUEST;
import static com.srp.ewayspanel.ui.store.StoreFragment.REQUEST_CODE_FILTER;
import static com.srp.ewayspanel.ui.store.filter.FilterActivity.FILTER_REQUEST_EXTERA;

/**
 * Created by Eskafi on 10/27/2019.
 */
public class ProductListFragment extends NavigationMemberFragment<BaseViewModel>
        implements StoreFilterHeaderView.FilterHeaderViewClickListener, GridProductFragment.ProductListFragmentListener {


    private StoreFilterHeaderView mFilterHeaderView;

    private FilterProductRequest mFilterProductRequest;

    //    private ConfirmationDialog mSortDialog;
    private DialogContentView mContentView;
    private SortRadioGroup mSortRadioGroup;
    private SortItem mSortItems;
    private boolean isSearch;

    private BottomDialog bottomDialog;

    private boolean isHorizontal = false;
    SharedPreferences.Editor editor;

    public static ProductListFragment newInstance(FilterProductRequest filterProduct, boolean isSearch) {
        ProductListFragment fragment = new ProductListFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(FILTER_PRODUCT_REQUEST, filterProduct);
        bundle.putBoolean(IS_SEARCH_PRODUCT_REQUEST, isSearch);

        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        IABResources abResources = DI.getABResources();

        SharedPreferences sharedpreferences = getContext().getSharedPreferences("MyPREFERENCES", getContext().MODE_PRIVATE);
        editor = sharedpreferences.edit();
        isHorizontal = sharedpreferences.getBoolean("orirnted", false);

        if (getArguments() != null && getArguments().getSerializable(FILTER_PRODUCT_REQUEST) != null) {
            mFilterProductRequest = (FilterProductRequest) (getArguments().getSerializable(FILTER_PRODUCT_REQUEST));
            isSearch = getArguments().getBoolean(IS_SEARCH_PRODUCT_REQUEST, false);
            replaceFragment();
        }

        mFilterHeaderView = view.findViewById(R.id.filteritemvview);
        mFilterHeaderView.setListener(this);

//        mSortDialog = new ConfirmationDialog(view.getContext());

        mSortRadioGroup = new SortRadioGroup(view.getContext());
        mSortItems = new SortItem();
        mSortRadioGroup.setData(mSortItems.getRadioList());

        bottomDialog = new BottomDialog();
        bottomDialog.setIcon(abResources.getDrawable(R.drawable.ic_filteritem_sort_new));
        bottomDialog.setTitle(abResources.getString(R.string.filter_sort_dialog_title));

        mContentView = new DialogContentView(view.getContext());

        mContentView.setIconVisibility(false);
        mContentView.setChoiceTitleVisibility(false);
        mContentView.showRial(false);
        mContentView.setBottomDialig(true);

        mContentView.setChildContentView(mSortRadioGroup);
        bottomDialog.setChildContentView(mContentView);

        bottomDialog.setListener(new BottomDialog.ConfirmationBottomDialogItemClickListener() {
            @Override
            public void onConfirmClicked() {
                bottomDialog.dismiss();

                replaceFragment();
            }

            @Override
            public void onCancelClicked() {
                bottomDialog.dismiss();
            }
        });


    }

    private void replaceFragment() {
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.container);

        if (currentFragment == null) {
            currentFragment = GridProductFragment.newInstance(mFilterProductRequest, isSearch, isHorizontal);
            transaction.add(R.id.container, currentFragment);
        } else {
            transaction.replace(R.id.container, GridProductFragment.newInstance(mFilterProductRequest, isSearch, isHorizontal));
        }
        transaction.commit();
    }

    public void setNewFilter(FilterProductRequest filterProductRequest) {
        mFilterProductRequest = filterProductRequest;
        replaceFragment();

    }

    @Override
    protected void getDataFromServer() {

    }

    @Override
    public BaseViewModel acquireViewModel() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_filtered_product;
    }

    @Override
    public void onFilterClicked() {
        startActivityForResult(FilterActivity.getIntent(getContext(), mFilterProductRequest), REQUEST_CODE_FILTER);
    }

    @Override
    public void onSortClicked() {

        int selectedPosition = mSortItems.getSelectedPosition(mFilterProductRequest.getOrder(), mFilterProductRequest.getSort());
        if (selectedPosition != -1) {
            mSortRadioGroup.setSelectedRadioButton(selectedPosition);
            bottomDialog.setButtonEnable(true);
        }

        mSortRadioGroup.setOnItemSelectedListener(new NColumnRadioGroup.RadioGroupListener() {
            @Override
            public void onItemSelected(int index, RadioOptionModel data) {
                bottomDialog.setButtonEnable(true);

                mFilterProductRequest.setSort(((SortItem.Item) data.option).getSort());
                mFilterProductRequest.setOrder(((SortItem.Item) data.option).getOrder());

                mFilterHeaderView.setSortSubtitle(((SortItem.Item) data.option).getTitle());
            }

            @Override
            public void onItemRemoved(int index, RadioOptionModel removedData) {
            }
        });

        bottomDialog.show(getParentFragmentManager(), "TAG");
    }

    @Override
    public void onChangeViewClicked() {

        if (isHorizontal) {
            isHorizontal = false;
            editor.putBoolean("orirnted", false);
        } else {
            isHorizontal = true;
            editor.putBoolean("orirnted", true);
        }
        mFilterHeaderView.changeChangeViewIcon(isHorizontal);
        editor.apply();

        replaceFragment();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != REQUEST_CODE_FILTER) {
            return;
        }

        if (getParentFragment() != null && getParentFragment() instanceof StoreMainPage) {

            if (resultCode == RESULT_OK) {
                mFilterProductRequest = (FilterProductRequest) data.getSerializableExtra(FILTER_REQUEST_EXTERA);
                replaceFragment();
            }
            return;
        }

        if (getParentFragment() != null && !(getParentFragment() instanceof SearchFragment)) {
            getParentFragment().onActivityResult(requestCode, resultCode, data);
        } else {
            if (resultCode == RESULT_OK) {
                mFilterProductRequest = (FilterProductRequest) data.getSerializableExtra(FILTER_REQUEST_EXTERA);
                replaceFragment();
            }

        }
    }

    @Override
    public void onShowFilterHeaderView() {

        mFilterHeaderView.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
    }

    @Override
    public void onHideFilterHeaderView() {

        mFilterHeaderView.animate().translationY(-2 * mFilterHeaderView.getHeight()).setInterpolator(new AccelerateInterpolator(2)).start();
    }

    @Override
    public View getFilterHeaderView() {
        return mFilterHeaderView;
    }
}
