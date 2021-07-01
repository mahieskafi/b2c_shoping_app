package com.srp.ewayspanel.filter;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.srp.ewayspanel.BuildConfig;
import com.srp.ewayspanel.DIM;
import com.srp.ewayspanel.ui.store.filter.FilteredProductViewModel;
import com.srp.ewayspanel.di.DI;

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

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)

public class FilterProductViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    FilteredProductViewModel mViewModel;

    @Before
    public void setUp() {

        mViewModel = new FilteredProductViewModel();

        assertNotNull(mViewModel);

        DIM.getFilterProductApi().setHasAbResources(false);

    }

    @Test
    public void onSuccessGetFilteredProductTest() {

        DI.getFilteredProductApi().setMode(SUCCESS);

        mViewModel.getFilteredProductList(null);

        Robolectric.flushForegroundThreadScheduler();

        assertNotNull(mViewModel.getFilteredProductLiveData().getValue());
    }

    @Test
    public void onSuccessSearchTest() {

        DI.getFilteredProductApi().setMode(SUCCESS);

        mViewModel.getFilteredProductList(null);

        Robolectric.flushForegroundThreadScheduler();

        assertNotNull(mViewModel.getFilteredProductLiveData().getValue());
    }

    @Test
    public void onErrorTest() {

        DI.getFilteredProductApi().setMode(ERROR_NO_INTERNET);

        mViewModel.getFilteredProductList(null);

        Robolectric.flushForegroundThreadScheduler();

        assertNull(mViewModel.getFilteredProductLiveData().getValue());


        DI.getFilteredProductApi().setMode(ERROR_SERVER_PROBLEM);

        mViewModel.getFilteredProductList(null);

        Robolectric.flushForegroundThreadScheduler();

        assertNull(mViewModel.getFilteredProductLiveData().getValue());
    }
}
