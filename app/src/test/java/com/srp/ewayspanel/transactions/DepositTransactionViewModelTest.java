package com.srp.ewayspanel.transactions;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.srp.ewayspanel.BuildConfig;
import com.srp.ewayspanel.RxImmediateSchedulerRule;
import com.srp.ewayspanel.di.DI;
import com.srp.eways.ui.transaction.deposit.DepositTransactionViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.concurrent.TimeUnit;

import static com.srp.eways.network.NetworkResponseCodes.ERROR_TIMEOUT;
import static com.srp.eways.network.NetworkResponseCodes.SUCCESS;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;

/**
 * Created by Eskafi on 9/25/2019.
 */
@Config(constants = BuildConfig.class)
@RunWith(RobolectricTestRunner.class)
public class DepositTransactionViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Rule
    public RxImmediateSchedulerRule rxImmediateSchedulerRule = new RxImmediateSchedulerRule();

    DepositTransactionViewModel mViewModel;

    @Before
    public void setUp(){
        DI.getDepositTransactionApi().setMode(SUCCESS);

        mViewModel = DI.getViewModel(DepositTransactionViewModel.class);

        assertNotNull(mViewModel);
    }


    @Test
    public void successLoadTest() {

        mViewModel.loadMore();
        assertTrue(mViewModel.isListLoading().getValue());
        Robolectric.getForegroundThreadScheduler().advanceBy(5000L, TimeUnit.MILLISECONDS);

        assertEquals(20, mViewModel.getTransactions().getValue().size());
        assertFalse(mViewModel.isListLoading().getValue());
        assertTrue(mViewModel.isHasMore().getValue());

        DI.getDepositTransactionRepo().clearData();
        mViewModel.clearData();
    }

    @Test
    public void halfResultLoadTest() {

        DI.getDepositTransactionApi().setHalfResult(true);

        mViewModel.loadMore();
        assertTrue(mViewModel.isListLoading().getValue());
        Robolectric.getForegroundThreadScheduler().advanceBy(5000L, TimeUnit.MILLISECONDS);

        assertEquals(10, mViewModel.getTransactions().getValue().size());
        assertFalse(mViewModel.isListLoading().getValue());
        assertFalse(mViewModel.isHasMore().getValue());

        DI.getDepositTransactionRepo().clearData();
        DI.getDepositTransactionApi().setHalfResult(false);
        mViewModel.clearData();

    }

    @Test
    public void errorLoadTest(){

        DI.getDepositTransactionApi().setMode(ERROR_TIMEOUT);

        assertFalse(mViewModel.isListLoading().getValue());

        mViewModel.loadMore();

        assertTrue(mViewModel.isListLoading().getValue());

        Robolectric.flushForegroundThreadScheduler();

        assertEquals(0, mViewModel.getTransactions().getValue().size());
        assertFalse(mViewModel.isListLoading().getValue());
        assertTrue(mViewModel.isHasMore().getValue());

        DI.getDepositTransactionRepo().clearData();
        mViewModel.clearData();
    }
}
