package com.srp.ewayspanel.ui.transaction.order.list;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.srp.ewayspanel.R;
import com.srp.ewayspanel.di.DI;
import com.srp.ewayspanel.model.followorder.FollowOrderItem;
import com.srp.ewayspanel.model.transaction.order.OrderItem;
import com.srp.ewayspanel.model.transaction.order.OrderTransactionListResponse;
import com.srp.eways.base.BasePageableListFragment;
import com.srp.eways.base.BaseRecyclerAdapter2;
import com.srp.ewayspanel.ui.transaction.order.OrderTransactionViewModel;
import com.srp.ewayspanel.ui.transaction.order.detail.OrderTransactionDetailFragment;
import com.srp.eways.ui.view.LoadingStateView;

import java.util.ArrayList;


/**
 * Created by Eskafi on 2/2/2020.
 */
public class OrderTransactionListFragment extends BasePageableListFragment<OrderTransactionViewModel>
        implements OrderItemClickListener, LoadingStateView.RetryListener, BasePageableListFragment.OnRecyclereScrollListener {

    public static OrderTransactionListFragment newInstance() {

        return new OrderTransactionListFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = view.findViewById(R.id.order_transaction_list);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);

        mLoadingStateView = view.findViewById(R.id.loadingstateview);
        mEmptyView = view.findViewById(R.id.emptyview);

        mAdapter = new OrderTransactionListAdapter(getContext(), new BaseRecyclerAdapter2.RetryClickListener() {
            @Override
            public void onClicked() {
                getViewModel().getStoreTransactionList();
            }
        }, this);
        mRecyclerView.setAdapter(mAdapter);

        setLoadingRetryListener(this);
        setPaginationScrollListener(this);

        if (getViewModel().getPageIndex() == 0) {
            setLoadingState(LoadingStateView.STATE_LOADING);
        }

        getViewModel().getStoreTransactionList();
        getViewModel().getStoreTransactionLiveData().observe(this, new Observer<OrderTransactionListResponse>() {
            @Override
            public void onChanged(OrderTransactionListResponse orderTransactionListResponse) {


                if (orderTransactionListResponse != null) {
                    ArrayList<FollowOrderItem> orderItems = orderTransactionListResponse.getOrderItems();

                    int status = orderTransactionListResponse.getStatus();
                    mErrorMessage = orderTransactionListResponse.getDescription();

                    checkDataStatus(orderItems,status);

                    getViewModel().consumeStoreTransactionLiveData();
                }
                else{
                    mAdapter.setNewData(new ArrayList());
                }
            }
        });

        getViewModel().getReNewLive().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean != null){

                    if(aBoolean){

                        mAdapter.setNewData(new ArrayList());

                        setLoadingState(LoadingStateView.STATE_LOADING);
                        getViewModel().getStoreTransactionList();

                    }

                    getViewModel().consumeReNewData();
                }
            }
        });
    }

    @Override
    public OrderTransactionViewModel acquireViewModel() {
        return DI.getViewModel(OrderTransactionViewModel.class);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_order_transaction_list;
    }

    @Override
    protected void getDataFromServer() {
        getViewModel().getStoreTransactionList();
    }

    @Override
    public void onItemClickListener(FollowOrderItem orderItem) {
        getViewModel().consumeOrderSummaryLiveData();
        getViewModel().consumeOrderDetailLiveData();
        getViewModel().setSelectedOrderTransaction(orderItem);

        openFragment(OrderTransactionDetailFragment.newInstance());
    }

    @Override
    public void onRetryButtonClicked() {
        getViewModel().getStoreTransactionList();
    }

    @Override
    public boolean hasMoreItems() {
        return getViewModel().hasMore();
    }

    @Override
    public void loadMoreItems() {
        getViewModel().getStoreTransactionList();
    }
}
