package com.srp.ewayspanel.shopcart;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.srp.ewayspanel.BuildConfig;
import com.srp.ewayspanel.RxImmediateSchedulerRule;
import com.srp.ewayspanel.di.DI;
import com.srp.ewayspanel.ui.shopcart.ShopCartViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.concurrent.TimeUnit;

import static com.srp.eways.network.NetworkResponseCodes.*;
import static com.srp.ewayspanel.repository.remote.shopcart.ShopCartApiImplementation.ERROR_ADD_NOT_IN_STOCK;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

/**
 * Created by ErfanG on 12/9/2019.
 */
@Config(constants = BuildConfig.class)
@RunWith(RobolectricTestRunner.class)
public class ShopCartViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Rule
    public RxImmediateSchedulerRule rxImmediateSchedulerRule = new RxImmediateSchedulerRule();

    ShopCartViewModel mViewModel;

    @Before
    public void setUp(){
        DI.getShopCartApi().setMode(SUCCESS);

        mViewModel = DI.getViewModel(ShopCartViewModel.class);

        assertNotNull(mViewModel);
    }

    @Test
    public void getShopCartListSuccessTest() {

        mViewModel.callGetShopCartList("");
        assertTrue(mViewModel.isLoading().getValue());
        Robolectric.getForegroundThreadScheduler().advanceBy(5000L, TimeUnit.MILLISECONDS);

        assertNotNull(mViewModel.getShopCartProductList().getValue());
        assertEquals(4, mViewModel.getShopCartItem().getValue().getBasket().size());
        assertEquals(8, mViewModel.getShopCartItem().getValue().getShippingType());
        assertFalse(mViewModel.isLoading().getValue());

        mViewModel.clearData();
    }

    @Test
    public void getShopCartListFailure(){

        DI.getShopCartApi().setMode(ERROR_CONNECTION);

        mViewModel.callGetShopCartList("");
        assertTrue(mViewModel.isLoading().getValue());
        Robolectric.getForegroundThreadScheduler().advanceBy(5000L, TimeUnit.MILLISECONDS);
        assertNull(mViewModel.getShopCartItem().getValue());
        assertFalse(mViewModel.isLoading().getValue());


        DI.getShopCartApi().setMode(ERROR_NO_INTERNET);
        mViewModel.callGetShopCartList("");
        Robolectric.getForegroundThreadScheduler().advanceBy(5000L, TimeUnit.MILLISECONDS);
        assertNull(mViewModel.getShopCartItem().getValue());

        DI.getShopCartApi().setMode(ERROR_TIMEOUT);
        mViewModel.callGetShopCartList("");
        Robolectric.getForegroundThreadScheduler().advanceBy(5000L, TimeUnit.MILLISECONDS);
        assertNull(mViewModel.getShopCartItem().getValue());

        mViewModel.clearData();

    }

    @Test
    public void addRemoveTest(){

        mViewModel.callGetShopCartList("");
        assertTrue(mViewModel.isLoading().getValue());
        Robolectric.getForegroundThreadScheduler().advanceBy(5000L, TimeUnit.MILLISECONDS);
        assertNotNull(mViewModel.getShopCartItem().getValue());
        assertEquals(4, mViewModel.getShopCartItem().getValue().getBasket().size());
        assertFalse(mViewModel.isLoading().getValue());


        mViewModel.removeProduct(62844);
        Robolectric.getForegroundThreadScheduler().advanceBy(5000L, TimeUnit.MILLISECONDS);
        assertEquals(4, mViewModel.getShopCartItem().getValue().getBasket().size());

        mViewModel.removeFromCart(62844);
        Robolectric.getForegroundThreadScheduler().advanceBy(5000L, TimeUnit.MILLISECONDS);
        assertEquals(3, mViewModel.getShopCartItem().getValue().getBasket().size());

        mViewModel.removeAllProducts();
        Robolectric.getForegroundThreadScheduler().advanceBy(5000L, TimeUnit.MILLISECONDS);
        assertNotNull(mViewModel.getShopCartItem().getValue());
        assertEquals(0, mViewModel.getShopCartItem().getValue().getBasket().size());

        mViewModel.addProduct(62844,1);
        Robolectric.getForegroundThreadScheduler().advanceBy(5000L, TimeUnit.MILLISECONDS);
        assertEquals(1, mViewModel.getShopCartItem().getValue().getBasket().size());
        assertEquals(1, mViewModel.getShopCartItem().getValue().getBasket().get(0).getCount());

        mViewModel.addProduct(62844,1);
        Robolectric.getForegroundThreadScheduler().advanceBy(5000L, TimeUnit.MILLISECONDS);
        assertNotNull(mViewModel.getShopCartItem().getValue());
        assertEquals(1, mViewModel.getShopCartItem().getValue().getBasket().size());
        assertEquals(2, mViewModel.getShopCartItem().getValue().getBasket().get(0).getCount());

        mViewModel.addProduct(62844,2);
        Robolectric.getForegroundThreadScheduler().advanceBy(5000L, TimeUnit.MILLISECONDS);
        assertNotNull(mViewModel.getShopCartItem().getValue());
        assertEquals(1, mViewModel.getShopCartItem().getValue().getBasket().size());
        assertEquals(3, mViewModel.getShopCartItem().getValue().getBasket().get(0).getCount());


        mViewModel.addProduct(71462,1);
        Robolectric.getForegroundThreadScheduler().advanceBy(5000L, TimeUnit.MILLISECONDS);
        assertNotNull(mViewModel.getShopCartItem().getValue());
        assertEquals(2, mViewModel.getShopCartItem().getValue().getBasket().size());


        DI.getShopCartApi().setMode(ERROR_ADD_NOT_IN_STOCK);
        mViewModel.addProduct(64858,1);
        Robolectric.getForegroundThreadScheduler().advanceBy(5000L, TimeUnit.MILLISECONDS);
        assertNotNull(mViewModel.getShopCartItem().getValue());
        assertEquals(2, mViewModel.getShopCartItem().getValue().getBasket().size());

        mViewModel.clearData();
    }

}
