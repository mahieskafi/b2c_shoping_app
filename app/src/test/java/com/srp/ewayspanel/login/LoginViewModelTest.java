package com.srp.ewayspanel.login;


import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.srp.ewayspanel.BuildConfig;
import com.srp.ewayspanel.DIM;
import com.srp.ewayspanel.ui.login.LoginViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;


import static com.srp.eways.network.NetworkResponseCodes.ERROR_CONNECTION;
import static com.srp.eways.network.NetworkResponseCodes.ERROR_NO_INTERNET;
import static com.srp.eways.network.NetworkResponseCodes.ERROR_SERVER_PROBLEM;
import static com.srp.eways.network.NetworkResponseCodes.ERROR_TIMEOUT;
import static com.srp.eways.network.NetworkResponseCodes.ERROR_UNDEFINED;
import static com.srp.eways.network.NetworkResponseCodes.ERROR_UNSUPPORTED_API;
import static com.srp.eways.network.NetworkResponseCodes.SUCCESS;
import static com.srp.eways.repository.remote.login.MainLoginApiImplementation.ERROR_USERNAME_PASSWORD;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;


/**
 * Created by ErfanG on 11/08/2019.
 */

@Config(constants = BuildConfig.class , sdk = 21)
@RunWith(RobolectricTestRunner.class)
public class LoginViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    LoginViewModel mViewModel;

    @Before
    public void setUp(){

        mViewModel = new LoginViewModel();

        assertNotNull(mViewModel);

        DIM.getLoginApi().setHasAbResources(false);


//        These two option for post delay in test
//        Robolectric.getForegroundThreadScheduler().advanceBy(7000L, TimeUnit.MILLISECONDS);
//        Robolectric.flushForegroundThreadScheduler();
    }

    @Test
    public void loginSuccessTest(){

        DIM.getLoginApi().setMode(ERROR_USERNAME_PASSWORD);
        mViewModel.onUsernameChanged("s.moradiii");
        mViewModel.onPasswordChanged("135792");
        mViewModel.login();
        assertTrue(mViewModel.isLoading().getValue());
        Robolectric.flushForegroundThreadScheduler();
        assertEquals(ERROR_USERNAME_PASSWORD, mViewModel.getError().getValue().getFirst().intValue());

        DIM.getLoginApi().setMode(ERROR_USERNAME_PASSWORD);
        mViewModel.onUsernameChanged("s.moradiiii");
        mViewModel.onPasswordChanged("123456");
        mViewModel.login();
        assertTrue(mViewModel.isLoading().getValue());
        Robolectric.flushForegroundThreadScheduler();
        assertEquals(ERROR_USERNAME_PASSWORD, mViewModel.getError().getValue().getFirst().intValue());


        DIM.getLoginApi().setMode(SUCCESS);
        mViewModel.onUsernameChanged("s.moradiii");
        mViewModel.onPasswordChanged("123456");
        mViewModel.login();
        Robolectric.flushForegroundThreadScheduler();
        assertEquals(SUCCESS, mViewModel.getLoginResponse().getValue().component2().longValue());
        assertNotNull("Response from server should not be null", mViewModel.getLoginResponse().getValue().component4());

        mViewModel.onLoginResultConsumed();

        assertNull(mViewModel.getLoginResponse().getValue());
        assertNull(mViewModel.getError().getValue());

    }

    @Test
    public void loginErrorConnectionTest(){

        DIM.getLoginApi().setMode(ERROR_CONNECTION);
        mViewModel.onUsernameChanged("s.moradiii");
        mViewModel.onPasswordChanged("123456");
        mViewModel.login();
        Robolectric.flushForegroundThreadScheduler();
        assertEquals(ERROR_CONNECTION, mViewModel.getError().getValue().getFirst().intValue());
        assertEquals(null, mViewModel.getLoginResponse().getValue());


        mViewModel.onLoginResultConsumed();

        assertNull(mViewModel.getLoginResponse().getValue());
        assertNull(mViewModel.getError().getValue());
    }

    @Test
    public void loginErrorUndefinedTest(){

        DIM.getLoginApi().setMode(ERROR_UNDEFINED);
        mViewModel.onUsernameChanged("s.moradiii");
        mViewModel.onPasswordChanged("123456");
        mViewModel.login();
        Robolectric.flushForegroundThreadScheduler();
        assertEquals(ERROR_UNDEFINED, mViewModel.getError().getValue().getFirst().intValue());
        assertEquals(null, mViewModel.getLoginResponse().getValue());

        mViewModel.onLoginResultConsumed();

        assertNull(mViewModel.getLoginResponse().getValue());
        assertNull(mViewModel.getError().getValue());
    }

    @Test
    public void loginErrorUnSupportedApiTest(){
        DIM.getLoginApi().setMode(ERROR_UNSUPPORTED_API);
        mViewModel.onUsernameChanged("s.moradiii");
        mViewModel.onPasswordChanged("123456");
        mViewModel.login();
        Robolectric.flushForegroundThreadScheduler();
        assertEquals(ERROR_UNSUPPORTED_API, mViewModel.getError().getValue().getFirst().intValue());
        assertEquals(null, mViewModel.getLoginResponse().getValue());

        mViewModel.onLoginResultConsumed();

        assertNull(mViewModel.getLoginResponse().getValue());
        assertNull(mViewModel.getError().getValue());
    }

    @Test
    public void loginErrorServerProblemTest(){
        DIM.getLoginApi().setMode(ERROR_SERVER_PROBLEM);
        mViewModel.onUsernameChanged("s.moradiii");
        mViewModel.onPasswordChanged("123456");
        mViewModel.login();
        Robolectric.flushForegroundThreadScheduler();
        assertEquals(ERROR_SERVER_PROBLEM, mViewModel.getError().getValue().getFirst().intValue());
        assertEquals(null, mViewModel.getLoginResponse().getValue());

        mViewModel.onLoginResultConsumed();

        assertNull(mViewModel.getLoginResponse().getValue());
        assertNull(mViewModel.getError().getValue());
    }

    @Test
    public void loginNoInternetTest(){
        DIM.getLoginApi().setMode(ERROR_NO_INTERNET);
        mViewModel.onUsernameChanged("s.moradiii");
        mViewModel.onPasswordChanged("123456");
        mViewModel.login();
        Robolectric.flushForegroundThreadScheduler();
        assertEquals(ERROR_NO_INTERNET, mViewModel.getError().getValue().getFirst().intValue());
        assertEquals(null, mViewModel.getLoginResponse().getValue());

        mViewModel.onLoginResultConsumed();

        assertNull(mViewModel.getLoginResponse().getValue());
        assertNull(mViewModel.getError().getValue());
    }

    @Test
    public void loginTimeoutTest(){
        DIM.getLoginApi().setMode(ERROR_TIMEOUT);
        mViewModel.onUsernameChanged("s.moradiii");
        mViewModel.onPasswordChanged("123456");
        mViewModel.login();
        Robolectric.flushForegroundThreadScheduler();
        assertEquals(ERROR_TIMEOUT, mViewModel.getError().getValue().getFirst().intValue());
        assertEquals(null, mViewModel.getLoginResponse().getValue());

        mViewModel.onLoginResultConsumed();

        assertNull(mViewModel.getLoginResponse().getValue());
        assertNull(mViewModel.getError().getValue());
    }

    @Test
    public void usernamePasswordChangeTest(){

        mViewModel.onPasswordChanged("");
        mViewModel.onUsernameChanged("");

        assertFalse(mViewModel.canLogin().getValue());

        mViewModel.onPasswordChanged("ad");
        mViewModel.onUsernameChanged("  ");

        assertFalse(mViewModel.canLogin().getValue());

        mViewModel.onPasswordChanged("ad");
        mViewModel.onUsernameChanged("  dcs  ");

        assertTrue(mViewModel.canLogin().getValue());

        mViewModel.onPasswordChanged("  ");
        mViewModel.onUsernameChanged("  dcs  ");

        assertFalse(mViewModel.canLogin().getValue());

    }
}
