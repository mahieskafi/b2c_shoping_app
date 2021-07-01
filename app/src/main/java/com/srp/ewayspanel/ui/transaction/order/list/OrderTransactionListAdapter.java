package com.srp.ewayspanel.ui.transaction.order.list;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.srp.eways.base.BaseViewHolder;
import com.srp.ewayspanel.model.followorder.FollowOrderItem;
import com.srp.ewayspanel.model.transaction.order.OrderItem;
import com.srp.eways.base.BaseRecyclerAdapter2;
import com.srp.eways.util.JalaliCalendar;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Eskafi on 2/2/2020.
 */
public class OrderTransactionListAdapter extends BaseRecyclerAdapter2<FollowOrderItem, BaseViewHolder> {

    private OrderItemClickListener mItemClickListener;
    private Context mContext;

    public OrderTransactionListAdapter(@NotNull Context context, @NotNull BaseRecyclerAdapter2.RetryClickListener retryListener,
                                       OrderItemClickListener itemClickListener) {
        super(context, retryListener);

        mContext = context;
        mItemClickListener = itemClickListener;
    }

    @Override
    public BaseViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        return new OrderItemViewHolder(new OrderItemView(parent.getContext()), mItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        if (position == getItemCount()) {
            OrderItem item = (OrderItem) new Object();
            holder.onBind(item);
        } else if (position < getData().size()) {
            super.onBindViewHolder(holder, position);
        }
    }

    public class OrderItemViewHolder extends BaseViewHolder<FollowOrderItem, View> implements View.OnClickListener {

        private OrderItemClickListener mListener;

        private FollowOrderItem mData;

        public OrderItemViewHolder(View itemView, OrderItemClickListener listener) {
            super(itemView, listener);

            mListener = listener;
        }

        @Override
        public void onBind(FollowOrderItem item) {
            mData = item;

            OrderItemView view = (OrderItemView) getView();

            view.setType(item.getOrderStatusName());

            if (!item.getPaymentDate().equals("null") && !item.getPaymentDate().isEmpty()) {
                String[] dateTime = item.getPaymentDate().split(" ");
                view.setTime(dateTime[1].substring(0, 5));

                String[] parts = dateTime[0].split("/");

                view.setDate("" + parts[2] + " " + JalaliCalendar.getPersianMonthName(Integer.valueOf(parts[1])));
            }
            view.setTransactionValue(item.getPayment());

            view.setPaymentNumber(String.valueOf(item.getOrderID()));

            view.setResult(item.getOrderStatus());

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onItemClickListener(mData);
        }
    }
}
