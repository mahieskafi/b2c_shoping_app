package com.srp.ewayspanel.transactions;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.srp.ewayspanel.BuildConfig;
import com.srp.ewayspanel.RxImmediateSchedulerRule;
import com.srp.ewayspanel.di.DI;
import com.srp.eways.ui.transaction.charge.ChargeTransactionViewModel;

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
 * Created by ErfanG on 03/09/2019.
 */
@Config(constants = BuildConfig.class)
@RunWith(RobolectricTestRunner.class)
public class ChargeTransactionViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Rule
    public RxImmediateSchedulerRule rxImmediateSchedulerRule = new RxImmediateSchedulerRule();

    ChargeTransactionViewModel mViewModel;

    @Before
    public void setUp(){
        DI.getChargeTransApi().setMode(SUCCESS);

        mViewModel = DI.getViewModel(ChargeTransactionViewModel.class);

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

        DI.getChargeTransRepo().clearData();
        mViewModel.clearData();
    }

    @Test
    public void halfResultLoadTest() {

        DI.getChargeTransApi().setHalfResult(true);

        mViewModel.loadMore();
        assertTrue(mViewModel.isListLoading().getValue());
        Robolectric.getForegroundThreadScheduler().advanceBy(5000L, TimeUnit.MILLISECONDS);

        assertEquals(10, mViewModel.getTransactions().getValue().size());
        assertFalse(mViewModel.isListLoading().getValue());
        assertFalse(mViewModel.isHasMore().getValue());

        DI.getChargeTransRepo().clearData();
        DI.getChargeTransApi().setHalfResult(false);
        mViewModel.clearData();

    }


    @Test
    public void errorLoadTest(){

        DI.getChargeTransApi().setMode(ERROR_TIMEOUT);

        assertFalse(mViewModel.isListLoading().getValue());

        mViewModel.loadMore();

        assertTrue(mViewModel.isListLoading().getValue());

        Robolectric.getForegroundThreadScheduler().advanceBy(5000L, TimeUnit.MILLISECONDS);

        assertEquals(0, mViewModel.getTransactions().getValue().size());
        assertFalse(mViewModel.isListLoading().getValue());
        assertTrue(mViewModel.isHasMore().getValue());

        DI.getChargeTransRepo().clearData();
        mViewModel.clearData();
    }
}
