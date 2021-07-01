package com.srp.ewayspanel.storepage;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.srp.ewayspanel.BuildConfig;
import com.srp.ewayspanel.di.DI;
import com.srp.ewayspanel.ui.store.StoreViewModel;

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
 * Created by ErfanG on 23/10/2019.
 */
@Config(constants = BuildConfig.class , sdk = 21)
@RunWith(RobolectricTestRunner.class)
public class StorePageViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    StoreViewModel mViewModel;

    @Before
    public void setUp(){

        mViewModel = new StoreViewModel();

        assertNotNull(mViewModel);

        DI.getStorePageApi().setHasAbResources(false);
    }

    @Test
    public void onErrorTest(){

        DI.getStorePageApi().setMode(ERROR_NO_INTERNET);

        mViewModel.getCategoryList();

        Robolectric.flushForegroundThreadScheduler();

        assertNull(mViewModel.getCategoryData().getValue());


        DI.getStorePageApi().setMode(ERROR_SERVER_PROBLEM);

        mViewModel.getCategoryList();

        Robolectric.flushForegroundThreadScheduler();

        assertNull(mViewModel.getCategoryData().getValue());
    }

    @Test
    public void onSuccessTest(){

        DI.getStorePageApi().setMode(SUCCESS);

        mViewModel.getCategoryList();

        Robolectric.flushForegroundThreadScheduler();

        assertNotNull(mViewModel.getCategoryData().getValue());
    }



}
