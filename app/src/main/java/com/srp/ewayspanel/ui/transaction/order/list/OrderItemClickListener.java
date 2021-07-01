package com.srp.ewayspanel.ui.transaction.order.list;

import com.srp.eways.base.BaseRecyclerListener;
import com.srp.ewayspanel.model.followorder.FollowOrderItem;
import com.srp.ewayspanel.model.transaction.order.OrderItem;

/**
 * Created by Eskafi on 2/2/2020.
 */
public interface OrderItemClickListener extends BaseRecyclerListener {

    void onItemClickListener(FollowOrderItem orderItem);
}
