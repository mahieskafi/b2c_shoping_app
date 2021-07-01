package com.srp.ewayspanel.ui.transaction.order;



import androidx.collection.SparseArrayCompat;

import com.srp.eways.base.BaseViewModel;
import com.srp.eways.ui.navigation.BackStackMember;
import com.srp.eways.ui.navigation.NavigationFragment;
import com.srp.ewayspanel.R;
import com.srp.ewayspanel.ui.transaction.order.list.OrderTransactionListFragment;

/**
 * Created by Eskafi on 2/10/2020.
 */
public class OrderTransactionFragment extends NavigationFragment<BaseViewModel> {


    public final static int ORDER_TRANSACTION_LIST_DETAIL_ID = 0;

    public static OrderTransactionFragment newInstance() {

        return new OrderTransactionFragment();
    }

    @Override
    protected int getRootTabId() {
        return ORDER_TRANSACTION_LIST_DETAIL_ID;
    }

    @Override
    protected SparseArrayCompat<BackStackMember> createNavigationRoots() {
        SparseArrayCompat<BackStackMember> roots = new SparseArrayCompat<>(1);

        roots.put(ORDER_TRANSACTION_LIST_DETAIL_ID, new BackStackMember(OrderTransactionListFragment.newInstance()));

        return roots;
    }

    @Override
    protected int getFragmentHostContainerId() {
        return R.id.f_container;
    }

    @Override
    protected int getNavigationType() {
        return NAVIGATION_TYPE_TAB;
    }

    @Override
    public OrderTransactionViewModel acquireViewModel() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_order_transaction;
    }

    @Override
    public int getNavigationViewType() {
        return -1;
    }

    @Override
    public boolean onBackPress() {
        return super.onBackPress();
    }
}
