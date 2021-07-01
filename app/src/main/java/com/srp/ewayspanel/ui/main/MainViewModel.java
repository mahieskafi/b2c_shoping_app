package com.srp.ewayspanel.ui.main;

import com.srp.eways.base.BaseViewModel;
import com.srp.eways.util.rx.SchedulerProvider;
import com.srp.ewayspanel.ui.transaction.order.OrderTransactionViewModel;

/**
 * Created by Eskafi on 7/20/2019.
 */
public class MainViewModel extends BaseViewModel {

    public MainViewModel(SchedulerProvider schedulerProvider) {
        super(schedulerProvider);
    }

    public MainViewModel(){
        super();
    }
}
