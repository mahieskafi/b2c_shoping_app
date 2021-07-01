package com.srp.ewayspanel.logout;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.srp.eways.di.DIMain;
import com.srp.ewayspanel.BuildConfig;
import com.srp.ewayspanel.di.DI;
import com.srp.eways.ui.navigationdrawer.NavigationDrawerViewModel;
import com.srp.eways.util.Constants;
import com.srp.ewayspanel.di.DICommon;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static com.srp.eways.network.NetworkResponseCodes.ERROR_TIMEOUT;
import static com.srp.eways.network.NetworkResponseCodes.SUCCESS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Eskafi on 9/25/2019.
 */
@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricTestRunner.class)
public class LogoutTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    NavigationDrawerViewModel mViewModel;

    @Before
    public void setUp() {

        mViewModel = new NavigationDrawerViewModel();

        assertNotNull(mViewModel);
    }

    @Test
    public void successLogoutTest() {
        mViewModel.logout();

        assertEquals("", DIMain.getPreferenceCache().getString(Constants.TOKEN_KEY));

        DI.getLogoutApi().setMode(SUCCESS);

        Robolectric.flushForegroundThreadScheduler();

        assertTrue(mViewModel.getIsSuccessLiveData().getValue());
    }


    @Test
    public void errorLogoutTest() {
        DI.getLogoutApi().setMode(ERROR_TIMEOUT);

        mViewModel.logout();

        assertEquals("", DIMain.getPreferenceCache().getString(Constants.TOKEN_KEY));

        Robolectric.flushForegroundThreadScheduler();

        assertFalse(mViewModel.getIsSuccessLiveData().getValue());
    }
}
