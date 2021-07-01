package com.srp.ewayspanel.deposit;



import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.srp.ewayspanel.DIM;
import com.srp.eways.ui.login.UserInfoViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;


import static com.srp.eways.network.NetworkResponseCodes.ERROR_NO_INTERNET;
import static com.srp.eways.network.NetworkResponseCodes.ERROR_TIMEOUT;
import static com.srp.eways.network.NetworkResponseCodes.SUCCESS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Eskafi on 8/27/2019.
 */
@RunWith(RobolectricTestRunner.class)
public class UserInfoViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();


    @Before
    public void setUp() {

        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void successGetCreditTest() {
        DIM.getValidateTokenApi().setMode(SUCCESS);


        UserInfoViewModel viewModel = Mockito.spy(new UserInfoViewModel());
        assertNotNull(viewModel);

        viewModel.getCredit();

        Robolectric.flushForegroundThreadScheduler();

        assertNotNull(viewModel.getCreditLiveData().getValue());
        assertNotNull(viewModel.getUserInfoLiveData().getValue());
        assertFalse(viewModel.getCreditInvalidated());
        assertEquals(System.currentTimeMillis(), viewModel.getLastCreditUpdateTime());
        assertFalse(viewModel.shouldGetCredit());

//        Robolectric.getForegroundThreadScheduler().advanceBy(122000L, TimeUnit.MILLISECONDS);
//        try {
//            Thread.sleep(122000);
//            assertTrue(viewModel.shouldGetCredit());
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

    }

    @Test
    public void timeoutGetCreditTest() {
        DIM.getValidateTokenApi().setMode(ERROR_TIMEOUT);

        UserInfoViewModel viewModel = Mockito.spy(new UserInfoViewModel());
        assertNotNull(viewModel);

        viewModel.getCredit();

        Robolectric.flushForegroundThreadScheduler();

        assertNull(viewModel.getCreditLiveData().getValue());
        assertNull(viewModel.getUserInfoLiveData().getValue());
        assertTrue(viewModel.shouldGetCredit());
    }

    @Test
    public void noInternetGetCreditTest() {
        DIM.getValidateTokenApi().setMode(ERROR_NO_INTERNET);

        UserInfoViewModel viewModel = Mockito.spy(new UserInfoViewModel());
        assertNotNull(viewModel);

        viewModel.getCredit();

        Robolectric.flushForegroundThreadScheduler();

        assertNull(viewModel.getCreditLiveData().getValue());
        assertNull(viewModel.getUserInfoLiveData().getValue());
        assertTrue(viewModel.shouldGetCredit());
    }
}
