package com.srp.ewayspanel.deposit;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.srp.ewayspanel.DIM;
import com.srp.eways.ui.deposit.increasedeposit.IncreaseDepositViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static com.srp.eways.network.NetworkResponseCodes.ERROR_TIMEOUT;
import static com.srp.eways.network.NetworkResponseCodes.SUCCESS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Eskafi on 9/24/2019.
 */
@RunWith(RobolectricTestRunner.class)
public class IncreaseDepositViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    IncreaseDepositViewModel mViewModel;

    @Before
    public void setUp() {

        MockitoAnnotations.initMocks(this);
        mViewModel = Mockito.spy(new IncreaseDepositViewModel());
        assertNotNull(mViewModel);
    }

    @Test
    public void successGetBankListTest() {
        DIM.getDepositApi().setMode(SUCCESS);

        mViewModel.onAmountChanged(1000);
        assertFalse(mViewModel.getAmountValidate().getValue());

        mViewModel.onAmountChanged(50000);
        assertTrue(mViewModel.getAmountValidate().getValue());

        mViewModel.getBankList();
        assertTrue(mViewModel.isLoading().getValue());
        Robolectric.flushForegroundThreadScheduler();
        assertEquals(false, mViewModel.isLoading().getValue());
        assertEquals(mViewModel.getBankListLiveData().getValue().getStatus(), SUCCESS);
        assertNotNull(mViewModel.getBankListLiveData().getValue().getItems());
    }

    @Test
    public void successIncreaseDepositTest() {
        DIM.getDepositApi().setMode(SUCCESS);

        mViewModel.onAmountChanged(1000);
        assertFalse(mViewModel.getAmountValidate().getValue());

        mViewModel.onAmountChanged(50000);
        assertTrue(mViewModel.getAmountValidate().getValue());

        mViewModel.setBankId(7);

        mViewModel.increaseDeposit();
        assertTrue(mViewModel.isLoading().getValue());
        Robolectric.flushForegroundThreadScheduler();
        assertEquals(false, mViewModel.isLoading().getValue());
        assertEquals(mViewModel.getIncreaseDepositLiveData().getValue().getStatus(), SUCCESS);
        assertNotNull(mViewModel.getIncreaseDepositLiveData().getValue().getUrl());
    }

    @Test
    public void successIncreaseDepositStatusTest() {
        DIM.getDepositApi().setMode(SUCCESS);

        mViewModel.getStatus("22222","3333333");

        assertTrue(mViewModel.isLoading().getValue());
        Robolectric.flushForegroundThreadScheduler();
        assertEquals(false, mViewModel.isLoading().getValue());
        assertEquals(mViewModel.getIncreaseDepositStatusLiveData().getValue().getStatus(), SUCCESS);
        assertNotNull(mViewModel.getIncreaseDepositStatusLiveData().getValue().getPaymentId());
    }
//    @Test
//    public void successConfirmPaymentTest() {
//        createConfirmPaymentRequestTest();
//
//        mViewModel.confirmPayment();
//        assertTrue(mViewModel.isLoading().getValue());
//
//        Robolectric.flushForegroundThreadScheduler();
//
//        assertEquals(false, mViewModel.isLoading().getValue());
//        assertEquals(mViewModel.getConfirmPaymentLiveData().getValue().getStatus().intValue(), SUCCESS);
//        assertNotNull(mViewModel.getConfirmPaymentLiveData().getValue().component2());
//        assertNotNull(mViewModel.getConfirmPaymentLiveData().getValue().component1());
//
//    }

    @Test
    public void amountChangedTest() {
        mViewModel.onAmountChanged(1000);
        assertFalse(mViewModel.getAmountValidate().getValue());

        mViewModel.onAmountChanged(50000);
        assertTrue(mViewModel.getAmountValidate().getValue());

//        mViewModel.onAmountChanged(20000000);
//        assertFalse(mViewModel.getAmountValidate().getValue());
    }

//    @Test
//    public void createConfirmPaymentRequestTest() {
//        DIM.getDepositApi().setMode(SUCCESS);
//        mViewModel.onAmountChanged(50000);
//        mViewModel.getMPLToken();
//        Robolectric.flushForegroundThreadScheduler();
//
//        assertNotNull(mViewModel.getAuthority());
//        assertNotEquals(mViewModel.getAuthority(), "");
//
//        assertNotNull(mViewModel.getRequestId());
//        assertNotEquals(mViewModel.getRequestId(), "");
//
//        assertNull(mViewModel.getConfirmPaymentRequest());
//        mViewModel.setMPLResponse(new MPLResponse("payInfo", "PayData", "DataSign"),0);
//        assertEquals(SUCCESS, mViewModel.getConfirmPaymentRequest().component3().intValue());
//        assertNotNull(mViewModel.getConfirmPaymentRequest().component1());
//        assertNotNull(mViewModel.getConfirmPaymentRequest().component2());
//    }

    @Test
    public void timeoutGetBankListTest() {
        DIM.getDepositApi().setMode(ERROR_TIMEOUT);

        mViewModel.onAmountChanged(50000);
        mViewModel.getBankList();

        Robolectric.flushForegroundThreadScheduler();

        assertNotNull(mViewModel.getBankListLiveData().getValue());
        assertEquals(ERROR_TIMEOUT, mViewModel.getBankListLiveData().getValue().getStatus());
    }

//    @Test
//    public void timeoutConfirmPaymentTest() {
//        createConfirmPaymentRequestTest();
//
//        DIM.getDepositApi().setMode(ERROR_TIMEOUT);
//
//        mViewModel.confirmPayment();
//
//        Robolectric.flushForegroundThreadScheduler();
//
//        assertNotNull(mViewModel.getConfirmPaymentLiveData().getValue());
//        assertEquals(ERROR_TIMEOUT, mViewModel.getConfirmPaymentLiveData().getValue().getStatus().intValue());
//    }
}
