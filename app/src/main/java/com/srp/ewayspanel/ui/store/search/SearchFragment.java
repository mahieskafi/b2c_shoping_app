package com.srp.ewayspanel.ui.store.search;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;

import com.srp.eways.base.BaseViewModel;
import com.srp.eways.ui.deposit.DepositFragment;
import com.srp.eways.ui.main.MainRootIds;
import com.srp.eways.ui.navigation.NavigationMemberFragment;
import com.srp.eways.ui.navigation.NavigationViewType;
import com.srp.eways.ui.view.toolbar.WeiredToolbar;
import com.srp.eways.util.Utils;
import com.srp.ewayspanel.R;
import com.srp.ewayspanel.di.DI;
import com.srp.ewayspanel.model.shopcart.ShopCartItemModel;
import com.srp.ewayspanel.model.storepage.filter.FilterProductRequest;
import com.srp.eways.ui.login.UserInfoViewModel;
import com.srp.ewayspanel.model.storepage.product.ProductInventoryCheckModel;
import com.srp.ewayspanel.ui.shopcart.ShopCartFragment;
import com.srp.ewayspanel.ui.shopcart.ShopCartViewModel;
import com.srp.ewayspanel.ui.store.filter.ProductListFragment;
import com.srp.ewayspanel.ui.store.product.InventoryNotExistDialog;
import com.srp.ewayspanel.ui.store.product.detail.ProductDetailFragment;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import ir.abmyapp.androidsdk.IABResources;

import static com.srp.ewayspanel.ui.store.GridProductFragment.FILTER_PRODUCT_REQUEST;
import static com.srp.ewayspanel.ui.store.GridProductFragment.IS_SEARCH_PRODUCT_REQUEST;

/**
 * Created by Eskafi on 11/24/2019.
 */
public class SearchFragment extends NavigationMemberFragment<BaseViewModel> {

    private final static int PRODUCT_LIST_ID = 0;

    private WeiredToolbar mToolbar;

    private UserInfoViewModel mUserInfoViewModel;

    private FilterProductRequest mFilterProductListener;

    private FragmentTransaction mTransaction;
    private List<String> mSearchTextList = new ArrayList<>();
    private String mSearchText;

    public static SearchFragment newInstance(FilterProductRequest filterProduct) {
        SearchFragment fragment = new SearchFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(FILTER_PRODUCT_REQUEST, filterProduct);

        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        mFilterProductListener = (FilterProductRequest) getArguments().getSerializable(FILTER_PRODUCT_REQUEST);
        mSearchText=mFilterProductListener.getText();
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Utils.hideKeyboard(getBaseActivity());
        mToolbar = view.findViewById(R.id.search_toolbar);
        setupToolbar();

        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.container);

        if (currentFragment == null) {
            currentFragment = ProductListFragment.newInstance(mFilterProductListener, true);
            transaction.add(R.id.container, currentFragment);
        } else {
            transaction.replace(R.id.container, ProductListFragment.newInstance(mFilterProductListener, true));
        }
        transaction.commit();

        mSearchTextList.add(mFilterProductListener.getText());

        final ShopCartViewModel shopCartViewModel = DI.getViewModel(ShopCartViewModel.class);

        shopCartViewModel.getInventoryForOpenFragmentLiveData().observe(getViewLifecycleOwner(), new Observer<ProductInventoryCheckModel>() {
            @Override
            public void onChanged(ProductInventoryCheckModel productInventoryCheckModel) {
                if (productInventoryCheckModel != null) {

                    if (productInventoryCheckModel.getStatus()) {
                        openFragment(ProductDetailFragment.newInstance(productInventoryCheckModel.getProductInfo()), NavigationViewType.NAVIGATION_VIEW_TYPE_HAS_TOOLBAR);
                    } else {
                        if (productInventoryCheckModel.getDescription() != null) {
                            Toast.makeText(getContext(), getString(R.string.loadingstateview_loading_connectiontimeout_error_text) + " " +
                                            getString(R.string.loadingstateview_loading_error_text)
                                    , Toast.LENGTH_SHORT).show();
                        } else {
                            new InventoryNotExistDialog(getContext()).show();
                        }
                    }
                    shopCartViewModel.consumeInventoryForOpenFragmentLiveData();
                }
            }
        });
    }

    private void setupToolbar() {
        IABResources resources = DI.getABResources();

        mToolbar.setTitle(resources.getString(R.string.store_page_search_title));
        mToolbar.setShowTitle(false);
        mToolbar.setShowDeposit(true);
        mToolbar.setBackgroundColor(resources.getColor(R.color.store_page_toolbar_background));
        mToolbar.setBackgroundColor(resources.getColor(R.color.store_page_toolbar_background));
        mToolbar.showNavigationDrawer(true);
        mToolbar.setShowShop(true);
        if (!mSearchText.isEmpty()){
            mToolbar.setSearchWord(mSearchText);
        }

        mToolbar.setOnNavigationDrawerClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                toggleDrawer();
            }

        });
        mUserInfoViewModel.getCreditLiveData().observe(this, new Observer<Long>() {

            @Override
            public void onChanged(Long deposit) {
                if (deposit != null) {
                    mToolbar.setDeposit(deposit);
                }
            }

        });

        final ShopCartViewModel shopCartViewModel = DI.getViewModel(ShopCartViewModel.class);
        if (shopCartViewModel != null) {
            shopCartViewModel.getShopCartProductList().observe(this, new Observer<ArrayList<ShopCartItemModel>>() {
                @Override
                public void onChanged(ArrayList<ShopCartItemModel> shopCartItemModels) {
                    mToolbar.setProductCount(shopCartItemModels.size());
                }
            });

            shopCartViewModel.getBasketLoading().observe(this, new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean isLoading) {
                        if (isLoading) {
                            mToolbar.showProgress();
                        }else
                        {
                            mToolbar.stopProgress();
                        }
                }
            });
        }

        mToolbar.setShopIconAction(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                v.setClickable(false);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        v.setClickable(true);
                    }
                }, 400);
                if (!DI.getViewModel(ShopCartViewModel.class).hasAnythingToChange()) {
                    openFragment(ShopCartFragment.Companion.newInstance(),
                            NavigationViewType.NAVIGATION_VIEW_TYPE_HAS_TOOLBAR);
                } else {
                    Toast.makeText(getContext(), DI.getABResources().getString(R.string.shop_cart_product_list_is_updating_error_message), Toast.LENGTH_SHORT).show();
                }
            }
        });

        mToolbar.setDepositActionClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSwitchRoot(MainRootIds.ROOT_DEPOSIT);
            }
        });

        mToolbar.setOnSearchClickListener(new WeiredToolbar.SearchTextListener() {
            @Override
            public void onSearchListener(String text) {
                mSearchTextList.add(text);

                FilterProductRequest filterProductRequest = new FilterProductRequest();
                filterProductRequest.setText(text);

                mTransaction = getChildFragmentManager().beginTransaction();
                mTransaction.add(R.id.container, ProductListFragment.newInstance(filterProductRequest, true));
                mTransaction.commit();
                mTransaction.addToBackStack(null);
            }
        });
    }

    @Override
    public BaseViewModel acquireViewModel() {
        mUserInfoViewModel = DI.getViewModel(UserInfoViewModel.class);
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_search;
    }

//    @Override
//    protected int getRootTabId() {
//        return PRODUCT_LIST_ID;
//    }

//    @Override
//    protected SparseArrayCompat<BackStackMember> createNavigationRoots() {
//        SparseArrayCompat<BackStackMember> roots = new SparseArrayCompat<>(1);
//
//        Bundle bundle = new Bundle();
//        bundle.putSerializable(FILTER_PRODUCT_REQUEST, mFilterProductListener);
//        bundle.putBoolean(IS_SEARCH_PRODUCT_REQUEST, true);
//
//        roots.put(PRODUCT_LIST_ID, new BackStackMember(ProductListFragment.newInstance(mFilterProductListener,true), bundle));
//        return roots;
//    }


//    @Override
//    protected int getFragmentHostContainerId() {
//        return R.id.container;
//    }
//
//    @Override
//    protected int getNavigationType() {
//        return NAVIGATION_TYPE_TAB;
//    }

    @Override
    public int getNavigationViewType() {
        return NavigationViewType.NAVIGATION_VIEW_TYPE_HAS_TOOLBAR_AND_BOTTOM_NAVIGATION;
    }

    @Override
    public boolean onBackPress() {
        if (getChildFragmentManager().getBackStackEntryCount() == 0) {
            return super.onBackPress();
        } else {
            mSearchTextList.remove(mSearchTextList.size() - 1);

            getChildFragmentManager().popBackStack();
            mToolbar.setSearchText(mSearchTextList.get(mSearchTextList.size() - 1));
            return true;
        }
    }
}
