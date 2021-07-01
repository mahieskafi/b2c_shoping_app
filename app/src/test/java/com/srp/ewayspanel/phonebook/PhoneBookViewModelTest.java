package com.srp.ewayspanel.phonebook;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.srp.ewayspanel.DIM;
import com.srp.eways.ui.phonebook.PhoneBookViewModel;

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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Eskafi on 1/11/2020.
 */

@RunWith(RobolectricTestRunner.class)
public class PhoneBookViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    PhoneBookViewModel mViewModel;

    @Before
    public void setUp() {

        MockitoAnnotations.initMocks(this);
        mViewModel = Mockito.spy(new PhoneBookViewModel());
        assertNotNull(mViewModel);
    }

    @Test
    public void successGetPhoneBookListTest() {
        DIM.getPhoneBookApi().setMode(SUCCESS);

        mViewModel.loadUserPhoneBook(true);
        assertTrue(mViewModel.isLoading().getValue());
        Robolectric.flushForegroundThreadScheduler();
        assertEquals(false, mViewModel.isLoading().getValue());
        assertEquals(mViewModel.getPhoneBookResultWrapperLive().getValue().errorCode, SUCCESS);
        assertNotNull(mViewModel.getPhoneBookResultWrapperLive().getValue().userPhoneBooks);
    }

    @Test
    public void successGetPhoneBookSearchListTest() {
        DIM.getPhoneBookApi().setMode(SUCCESS);

        mViewModel.searchUserPhoneBook("test");
        assertTrue(mViewModel.isLoading().getValue());
        Robolectric.flushForegroundThreadScheduler();
        assertEquals(false, mViewModel.isLoading().getValue());
        assertEquals(mViewModel.getPhoneBookSearchResultLiveData().getValue().getStatus(), SUCCESS);
        assertNotNull(mViewModel.getPhoneBookSearchResultLiveData().getValue().getItems());
    }

    @Test
    public void timeoutGetContactListTest() {
        DIM.getPhoneBookApi().setMode(ERROR_TIMEOUT);

        mViewModel.loadUserPhoneBook(true);

        Robolectric.flushForegroundThreadScheduler();

        assertEquals(0, mViewModel.getPhoneBookResultWrapperLive().getValue().userPhoneBooks.size());
    }
}
