package com.srp.ewayspanel.storetransaction;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.srp.ewayspanel.BuildConfig;
import com.srp.ewayspanel.di.DI;
import com.srp.ewayspanel.ui.transaction.order.OrderTransactionViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static com.srp.eways.network.NetworkResponseCodes.ERROR_NO_INTERNET;
import static com.srp.eways.network.NetworkResponseCodes.ERROR_SERVER_PROBLEM;
import static com.srp.eways.network.NetworkResponseCodes.SUCCESS;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Created by Eskafi on 2/3/2020.
 */
@Config(constants = BuildConfig.class , sdk = 21)
@RunWith(RobolectricTestRunner.class)
public class OrderTransactionViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    OrderTransactionViewModel mViewModel;

    @Before
    public void setUp(){

        mViewModel = new OrderTransactionViewModel();

        assertNotNull(mViewModel);

        DI.getOrderTransactionApi().setHasAbResources(false);
    }

    @Test
    public void onErrorTest(){

        DI.getOrderTransactionApi().setMode(ERROR_NO_INTERNET);

        mViewModel.getStoreTransactionList();

        Robolectric.flushForegroundThreadScheduler();

        assertNull(mViewModel.getStoreTransactionLiveData().getValue().getOrderItems());


        DI.getStorePageApi().setMode(ERROR_SERVER_PROBLEM);

        mViewModel.getStoreTransactionList();

        Robolectric.flushForegroundThreadScheduler();

        assertNull(mViewModel.getStoreTransactionLiveData().getValue().getOrderItems());
    }

    @Test
    public void onSuccessTest(){

        DI.getStorePageApi().setMode(SUCCESS);

        mViewModel.getStoreTransactionList();

        Robolectric.flushForegroundThreadScheduler();

        assertNotNull(mViewModel.getStoreTransactionLiveData().getValue());
    }
}
