package com.srp.ewayspanel.ui.store.product;

import com.srp.eways.base.BaseRecyclerListener;
import com.srp.ewayspanel.model.storepage.filter.ProductInfo;

/**
 * Created by Eskafi on 10/28/2019.
 */
public interface ProductItemClickListener extends BaseRecyclerListener {
    void onItemClick(int position, ProductInfo item);

    void onRemoveClicked(int productId, int newCount, boolean isTotalCount);

    void onAddClicked(com.srp.ewayspanel.model.shopcart.ProductInfo productInfo, int newCount, boolean isTotalCount);

    void onDeleteClicked(int productId);
}
