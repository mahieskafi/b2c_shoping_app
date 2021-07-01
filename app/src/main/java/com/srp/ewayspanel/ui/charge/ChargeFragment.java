package com.srp.ewayspanel.ui.charge;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.Observer;

import com.srp.eways.ui.charge.MainChargePagerAdapter;
import com.srp.eways.ui.charge.MainChargeFragment;
import com.srp.eways.ui.deposit.DepositFragment;
import com.srp.eways.ui.main.MainRootIds;
import com.srp.eways.ui.navigation.NavigationViewType;
import com.srp.eways.ui.view.toolbar.WeiredToolbar;
import com.srp.ewayspanel.R;
import com.srp.ewayspanel.di.DI;
import com.srp.ewayspanel.model.shopcart.ShopCartItemModel;
import com.srp.ewayspanel.model.storepage.filter.FilterProductRequest;
import com.srp.ewayspanel.ui.shopcart.ShopCartFragment;
import com.srp.ewayspanel.ui.shopcart.ShopCartViewModel;
import com.srp.ewayspanel.ui.store.search.SearchFragment;

import java.util.ArrayList;

import ir.abmyapp.androidsdk.IABResources;


public class ChargeFragment extends MainChargeFragment {


    public static ChargeFragment newInstance() {
        ChargeFragment fragment = new ChargeFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public MainChargePagerAdapter getAdapter() {
        return new ChargePagerAdapter(getChildFragmentManager());
    }

    public void setupWeiredToolbar() {
        IABResources resources = DI.getABResources();

        weiredToolbar.setTitle(resources.getString(R.string.buycharge_title));
        weiredToolbar.setShowTitle(false);
        weiredToolbar.setShowDeposit(true);
        weiredToolbar.setShowShop(true);
        weiredToolbar.setBackgroundColor(resources.getColor(R.color.buycharge_toolbar_background));

        weiredToolbar.setOnNavigationDrawerClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                toggleDrawer();
            }

        });

        getViewModel().getCreditLiveData().observe(this, new Observer<Long>() {

            @Override
            public void onChanged(Long deposit) {
                if (deposit != null) {
                    weiredToolbar.setDeposit(deposit);
                }
            }

        });

        weiredToolbar.setOnSearchClickListener(new WeiredToolbar.SearchTextListener() {
            @Override
            public void onSearchListener(String text) {
                FilterProductRequest filterProductRequest = new FilterProductRequest();
                filterProductRequest.setText(text);

                openFragment(SearchFragment.newInstance(filterProductRequest));
            }
        });

        DI.getViewModel(ShopCartViewModel.class).getShopCartProductList().observe(this, new Observer<ArrayList<ShopCartItemModel>>() {
            @Override
            public void onChanged(ArrayList<ShopCartItemModel> shopCartItemModels) {
                weiredToolbar.setProductCount(shopCartItemModels.size());
            }
        });

        weiredToolbar.setShopIconAction(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                v.setClickable(false);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        v.setClickable(true);
                    }
                }, 400);
                if(!DI.getViewModel(ShopCartViewModel.class).hasAnythingToChange()) {
                    openFragment(ShopCartFragment.Companion.newInstance(),
                            NavigationViewType.NAVIGATION_VIEW_TYPE_HAS_TOOLBAR);
                }
                else{
                    Toast.makeText(getContext(), DI.getABResources().getString(R.string.shop_cart_product_list_is_updating_error_message), Toast.LENGTH_SHORT).show();
                }
            }
        });

        weiredToolbar.setDepositActionClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSwitchRoot(MainRootIds.ROOT_DEPOSIT);
            }
        });

    }

}
