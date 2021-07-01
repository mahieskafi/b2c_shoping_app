package com.srp.ewayspanel.contact;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.srp.ewayspanel.DIM;
import com.srp.eways.ui.contact.ContactViewModel;

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
public class ContactViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    ContactViewModel mViewModel;

    @Before
    public void setUp() {

        MockitoAnnotations.initMocks(this);
        mViewModel = Mockito.spy(new ContactViewModel());
        assertNotNull(mViewModel);
    }

    @Test
    public void successGetContactListTest() {
        DIM.getContactApi().setMode(SUCCESS);

        mViewModel.getContactInfo();
        assertTrue(mViewModel.isLoading().getValue());
        Robolectric.flushForegroundThreadScheduler();
        assertEquals(false, mViewModel.isLoading().getValue());
        assertEquals(mViewModel.getContactInfoLiveData().getValue().getStatus(), SUCCESS);
        assertNotNull(mViewModel.getContactInfoLiveData().getValue().getItems());
    }

    @Test
    public void timeoutGetContactListTest() {
        DIM.getContactApi().setMode(ERROR_TIMEOUT);

        mViewModel.getContactInfo();

        Robolectric.flushForegroundThreadScheduler();

        assertNull(mViewModel.getContactInfoLiveData().getValue());
    }
}
