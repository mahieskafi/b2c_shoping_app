package com.srp.ewayspanel.transactions;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.srp.ewayspanel.BuildConfig;
import com.srp.ewayspanel.RxImmediateSchedulerRule;
import com.srp.ewayspanel.di.DI;
import com.srp.eways.ui.transaction.charge.inquiry.InquiryTransactionViewModel;

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
 * Created by Eskafi on 9/11/2019.
 */
@Config(constants = BuildConfig.class)
@RunWith(RobolectricTestRunner.class)
public class InquiryTransactionViewModelTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Rule
    public RxImmediateSchedulerRule rxImmediateSchedulerRule = new RxImmediateSchedulerRule();

    InquiryTransactionViewModel mViewModel;
    String phoneNumber;
    String startDate;
    String endDate;

    @Before
    public void setUp() {
        DI.getChargeTransApi().setMode(SUCCESS);

        mViewModel = DI.getViewModel(InquiryTransactionViewModel.class);

        assertNotNull(mViewModel);

        phoneNumber = "09187888582";
        startDate = "1398/06/12";
        endDate = "1398/06/20";
    }


    @Test
    public void successLoadTest() {
        mViewModel.loadMore(phoneNumber, startDate, endDate);
        assertTrue(mViewModel.isListLoading().getValue());
        Robolectric.getForegroundThreadScheduler().advanceBy(5000L, TimeUnit.MILLISECONDS);

        assertEquals(10, mViewModel.getTransactions().getValue().size());
        assertNotNull( mViewModel.getTransactions().getValue().get(3).component5());
        assertFalse(mViewModel.isListLoading().getValue());

        DI.getInquiryTransactionRepo().clearData();
        mViewModel.clearData();
    }

    @Test
    public void errorLoadTest() {
        DI.getChargeTransApi().setMode(ERROR_TIMEOUT);

        assertFalse(mViewModel.isListLoading().getValue());

        mViewModel.loadMore(phoneNumber, startDate, endDate);

        assertTrue(mViewModel.isListLoading().getValue());

        Robolectric.getForegroundThreadScheduler().advanceBy(5000L, TimeUnit.MILLISECONDS);

        assertEquals(0, mViewModel.getTransactions().getValue().size());
        assertFalse(mViewModel.isListLoading().getValue());

        DI.getInquiryTransactionRepo().clearData();
        mViewModel.clearData();
    }
}
