package com.srp.ewayspanel.login;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.srp.ewayspanel.BuildConfig;
import com.srp.ewayspanel.DIM;
import com.srp.ewayspanel.model.login.VerifyCodeResponse;
import com.srp.ewayspanel.repository.remote.login.LoginApiImplementation;
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
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertNotNull;

/**
 * Created by ErfanG on 15/10/2019.
 */
@Config(constants = BuildConfig.class , sdk = 21)
@RunWith(RobolectricTestRunner.class)
public class ResetPasswordTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    LoginViewModel mViewModel;

    @Before
    public void setUp() {

        mViewModel = new LoginViewModel();

        assertNotNull(mViewModel);

        DIM.getLoginApi().setHasAbResources(false);

        mViewModel.getVerifyCodeResponse().setValue(new VerifyCodeResponse());
    }

    @Test
    public void verifyCodeSuccessTest(){

        DIM.getLoginApi().setMode(SUCCESS);
        mViewModel.resetPasswordFirstInputChanged("654321");
        mViewModel.resetPasswordSecondInputChanged("654321");
        mViewModel.resetPassword();
        assertTrue( mViewModel.isLoading().getValue());
        Robolectric.flushForegroundThreadScheduler();
        assertEquals(SUCCESS, mViewModel.getResetPasswordResponse().getValue().component2());
        assertFalse( mViewModel.isLoading().getValue());

//        LoginApiImplementation.Companion.setPASSWORD("654321");
    }

    @Test
    public void errorConnectionTest(){

        DIM.getLoginApi().setMode(ERROR_CONNECTION);
        mViewModel.resetPasswordFirstInputChanged("654321");
        mViewModel.resetPasswordSecondInputChanged("654321");
        mViewModel.resetPassword();
        assertTrue( mViewModel.isLoading().getValue());
        Robolectric.flushForegroundThreadScheduler();
        Assert.assertEquals(ERROR_CONNECTION, mViewModel.getError().getValue().getFirst().intValue());
        Assert.assertEquals(null, mViewModel.getResetPasswordResponse().getValue());
        assertFalse( mViewModel.isLoading().getValue());

    }

    @Test
    public void errorUndefinedTest(){

        DIM.getLoginApi().setMode(ERROR_UNDEFINED);
        mViewModel.resetPasswordFirstInputChanged("654321");
        mViewModel.resetPasswordSecondInputChanged("654321");
        mViewModel.resetPassword();
        Robolectric.flushForegroundThreadScheduler();
        Assert.assertEquals(ERROR_UNDEFINED, mViewModel.getError().getValue().getFirst().intValue());
        Assert.assertEquals(null, mViewModel.getResetPasswordResponse().getValue());
    }

    @Test
    public void errorUnSupportedApiTest(){
        DIM.getLoginApi().setMode(ERROR_UNSUPPORTED_API);
        mViewModel.resetPasswordFirstInputChanged("654321");
        mViewModel.resetPasswordSecondInputChanged("654321");
        mViewModel.resetPassword();
        Robolectric.flushForegroundThreadScheduler();
        Assert.assertEquals(ERROR_UNSUPPORTED_API, mViewModel.getError().getValue().getFirst().intValue());
        Assert.assertEquals(null, mViewModel.getResetPasswordResponse().getValue());

    }

    @Test
    public void errorServerProblemTest(){
        DIM.getLoginApi().setMode(ERROR_SERVER_PROBLEM);
        mViewModel.resetPasswordFirstInputChanged("654321");
        mViewModel.resetPasswordSecondInputChanged("654321");
        mViewModel.resetPassword();
        Robolectric.flushForegroundThreadScheduler();
        Assert.assertEquals(ERROR_SERVER_PROBLEM, mViewModel.getError().getValue().getFirst().intValue());
        Assert.assertEquals(null, mViewModel.getResetPasswordResponse().getValue());

    }

    @Test
    public void noInternetTest(){
        DIM.getLoginApi().setMode(ERROR_NO_INTERNET);
        mViewModel.resetPasswordFirstInputChanged("654321");
        mViewModel.resetPasswordSecondInputChanged("654321");
        mViewModel.resetPassword();
        Robolectric.flushForegroundThreadScheduler();
        Assert.assertEquals(ERROR_NO_INTERNET, mViewModel.getError().getValue().getFirst().intValue());
        Assert.assertEquals(null, mViewModel.getResetPasswordResponse().getValue());

    }

    @Test
    public void timeoutTest(){
        DIM.getLoginApi().setMode(ERROR_TIMEOUT);
        mViewModel.resetPasswordFirstInputChanged("654321");
        mViewModel.resetPasswordSecondInputChanged("654321");
        mViewModel.resetPassword();
        Robolectric.flushForegroundThreadScheduler();
        Assert.assertEquals(ERROR_TIMEOUT, mViewModel.getError().getValue().getFirst().intValue());
        Assert.assertEquals(null, mViewModel.getResetPasswordResponse().getValue());

    }
}
