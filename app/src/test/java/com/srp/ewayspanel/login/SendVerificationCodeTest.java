package com.srp.ewayspanel.login;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.srp.ewayspanel.BuildConfig;
import com.srp.ewayspanel.DIM;
import com.srp.ewayspanel.ui.login.LoginViewModel;

import org.junit.Assert;
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
import static com.srp.ewayspanel.repository.remote.login.LoginApiImplementation.ERROR_WRONG_USERNAME;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertNotNull;

/**
 * Created by ErfanG on 15/10/2019.
 */
@Config(constants = BuildConfig.class , sdk = 21)
@RunWith(RobolectricTestRunner.class)
public class SendVerificationCodeTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    LoginViewModel mViewModel;

    @Before
    public void setUp(){

        mViewModel = new LoginViewModel();

        assertNotNull(mViewModel);

        DIM.getLoginApi().setHasAbResources(false);

    }

    @Test
    public void sendVerificationCodeSuccessTest(){

        DIM.getLoginApi().setMode(ERROR_WRONG_USERNAME);
        mViewModel.confirmUsernameChanged("s.moradiiii");
        mViewModel.sendVerificationCode();
        assertTrue(mViewModel.isLoading().getValue());
        Robolectric.flushForegroundThreadScheduler();
        assertEquals(ERROR_WRONG_USERNAME, mViewModel.getError().getValue().getFirst().intValue());


        DIM.getLoginApi().setMode(SUCCESS);
        mViewModel.confirmUsernameChanged("s.moradiii");
        mViewModel.sendVerificationCode();
        Robolectric.flushForegroundThreadScheduler();
        assertEquals(SUCCESS, mViewModel.getForgetPasswordResponse().getValue().component3());
    }

    @Test
    public void errorConnectionTest(){

        DIM.getLoginApi().setMode(ERROR_CONNECTION);
        mViewModel.confirmUsernameChanged("s.moradiii");
        mViewModel.sendVerificationCode();
        Robolectric.flushForegroundThreadScheduler();
        Assert.assertEquals(ERROR_CONNECTION, mViewModel.getError().getValue().getFirst().intValue());
        Assert.assertEquals(null, mViewModel.getForgetPasswordResponse().getValue());

    }

    @Test
    public void errorUndefinedTest(){

        DIM.getLoginApi().setMode(ERROR_UNDEFINED);
        mViewModel.confirmUsernameChanged("s.moradiii");
        mViewModel.sendVerificationCode();
        Robolectric.flushForegroundThreadScheduler();
        Assert.assertEquals(ERROR_UNDEFINED, mViewModel.getError().getValue().getFirst().intValue());
        Assert.assertEquals(null, mViewModel.getForgetPasswordResponse().getValue());
    }

    @Test
    public void errorUnSupportedApiTest(){
        DIM.getLoginApi().setMode(ERROR_UNSUPPORTED_API);
        mViewModel.confirmUsernameChanged("s.moradiii");
        mViewModel.sendVerificationCode();
        Robolectric.flushForegroundThreadScheduler();
        Assert.assertEquals(ERROR_UNSUPPORTED_API, mViewModel.getError().getValue().getFirst().intValue());
        Assert.assertEquals(null, mViewModel.getForgetPasswordResponse().getValue());

    }

    @Test
    public void errorServerProblemTest(){
        DIM.getLoginApi().setMode(ERROR_SERVER_PROBLEM);
        mViewModel.confirmUsernameChanged("s.moradiii");
        mViewModel.sendVerificationCode();
        Robolectric.flushForegroundThreadScheduler();
        Assert.assertEquals(ERROR_SERVER_PROBLEM, mViewModel.getError().getValue().getFirst().intValue());
        Assert.assertEquals(null, mViewModel.getForgetPasswordResponse().getValue());

    }

    @Test
    public void noInternetTest(){
        DIM.getLoginApi().setMode(ERROR_NO_INTERNET);
        mViewModel.confirmUsernameChanged("s.moradiii");
        mViewModel.sendVerificationCode();
        Robolectric.flushForegroundThreadScheduler();
        Assert.assertEquals(ERROR_NO_INTERNET, mViewModel.getError().getValue().getFirst().intValue());
        Assert.assertEquals(null, mViewModel.getForgetPasswordResponse().getValue());

    }

    @Test
    public void timeoutTest(){
        DIM.getLoginApi().setMode(ERROR_TIMEOUT);
        mViewModel.confirmUsernameChanged("s.moradiii");
        mViewModel.sendVerificationCode();
        Robolectric.flushForegroundThreadScheduler();
        Assert.assertEquals(ERROR_TIMEOUT, mViewModel.getError().getValue().getFirst().intValue());
        Assert.assertEquals(null, mViewModel.getForgetPasswordResponse().getValue());

    }
}
