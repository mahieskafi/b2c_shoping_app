package com.srp.ewayspanel.ui.store.product;

import android.view.View;

import com.srp.eways.base.BaseViewHolder;
import com.srp.ewayspanel.di.DI;
import com.srp.ewayspanel.model.storepage.filter.ProductInfo;
import com.srp.ewayspanel.ui.shopcart.ShopCartViewModel;
import com.srp.ewayspanel.ui.view.shopitem.ShopItemView;

/**
 * Created by Eskafi on 10/28/2019.
 */
public class ProductItemViewHolder extends BaseViewHolder<ProductInfo, View> implements View.OnClickListener {

    private ProductItemClickListener mListener;

    private ProductInfo mData;

    private ShopCartViewModel mShopCartViewModel = DI.getViewModel(ShopCartViewModel.class);

    private int mParentPosition = -1;

    public ProductItemViewHolder(View itemView, ProductItemClickListener listener) {
        super(itemView, listener);

        mListener = listener;
    }

    @Override
    public void onBind(ProductInfo item) {
        mData = item;

        ShopItemView view = (ShopItemView) getView();

        view.setProductName(item.getName());

        if (item.getImageUrl() != null) {
            view.setProductImage(item.getImageUrl());
        }

        view.setPriceBeforeOff(String.valueOf(item.getOldPrice()));

        view.setPriceAfterOff(String.valueOf(item.getPrice()));

        view.setOffValue(item.getDiscount());

        view.setClickAction(this);

        view.setCountListener(mListener, item);

        view.bind(item);

        view.setCount(mShopCartViewModel.getProductCount(item.getId()), false);

//        view.setOnClickListener(this);
    }

    public void setParentPosition(int position) {
        mParentPosition = position;
    }

    @Override
    public void onClick(View v) {
        if (mParentPosition != -1) {
            mListener.onItemClick(mParentPosition, mData);
        } else {
            mListener.onItemClick(getAdapterPosition(), mData);
        }
    }

}
