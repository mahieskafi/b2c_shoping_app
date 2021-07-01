package com.srp.ewayspanel.ui.transaction.order.detail;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.srp.eways.base.BaseViewHolder;
import com.srp.ewayspanel.R;
import com.srp.ewayspanel.model.transaction.order.OrderDetail;
import com.srp.eways.base.BaseRecyclerAdapter2;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Eskafi on 2/8/2020.
 */
public class OrderProductAdapter extends BaseRecyclerAdapter2<OrderDetail, BaseViewHolder> {

    private Context mContext;
    private OrderDetail mData;

    interface onOrderProductClickListener{
        void onItemClick(int id);
    }

    onOrderProductClickListener clickListener;

    public OrderProductAdapter(@NotNull Context context, @NotNull BaseRecyclerAdapter2.RetryClickListener retryListener , onOrderProductClickListener listener) {
        super(context, retryListener);

        clickListener=listener;
        mContext = context;
    }

    @Override
    public BaseViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        return new OrderProductViewHolder(new OrderProductItemView(parent.getContext()));
    }

    public class OrderProductViewHolder extends BaseViewHolder<OrderDetail, View> implements View.OnClickListener {



        public OrderProductViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onBind(OrderDetail item) {
            mData = item;


            final OrderProductItemView view = (OrderProductItemView) getView();

            view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();

                    layoutParams.setMargins(
                            0,
                            view.getContext().getResources().getDimensionPixelSize(R.dimen.shop_cart_fragment_product_item_margin_top),
                            0,
                            view.getContext().getResources().getDimensionPixelSize(R.dimen.shop_cart_fragment_product_item_margin_bottom));

                    view.setLayoutParams(layoutParams);
                }
            });

            view.setOnClickListener(this);
            view.bindData(item);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition());
        }
    }
}
