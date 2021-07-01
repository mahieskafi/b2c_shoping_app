package com.srp.ewayspanel.ui.club;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;

import com.srp.eways.base.BaseViewHolder;
import com.srp.ewayspanel.di.DI;
import com.srp.ewayspanel.model.storepage.filter.ProductInfo;
import com.srp.eways.ui.view.ViewUtils;
import com.srp.ewayspanel.model.storepage.mainpage.Item;
import com.srp.ewayspanel.ui.shopcart.ShopCartViewModel;
import com.srp.ewayspanel.ui.store.product.ProductItemClickListener;

public class ClubProductItemViewHolder extends BaseViewHolder<ProductInfo, View> implements View.OnClickListener {

    private long mLoyaltyScore;
    private ProductItemClickListener mProductClickListener;
    private ProductInfo mData;
    private ShopCartViewModel mShopCartViewModel = DI.getViewModel(ShopCartViewModel.class);


    public ClubProductItemViewHolder(View itemView, ProductItemClickListener listener, long loyaltyScore) {
        super(itemView, listener);

        mLoyaltyScore = loyaltyScore;

        mProductClickListener = listener;
    }

    @Override
    public void onBind(final ProductInfo item) {
        mData=item;
        ClubItem view = (ClubItem) getView();

        view.bind(item);
        view.setUserPoint(mLoyaltyScore);

        view.setProductName(item.getName());

        if (item.getImageUrl() != null) {
            view.setProductImage(item.getImageUrl());
        }

        view.setPriceBefforOff(item.getOldPrice());
        view.setPrice(item.getPrice());
        view.setOffValue(item.getDiscount());
        view.setClickAction(this);
        view.setCountListener(mProductClickListener, item);
        view.setUserPoint(item.getPoint(), mLoyaltyScore);

        view.setCount(mShopCartViewModel.getProductCount(item.getId()), false);
    }

    @Override
    public void onClick(View v) {
        mProductClickListener.onItemClick(getAdapterPosition(), mData);
    }
}
