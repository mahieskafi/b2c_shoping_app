package com.srp.ewayspanel.bill;


import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.srp.eways.di.DIMainCommon;
import com.srp.eways.model.bill.archivedList.BillTemp;
import com.srp.ewayspanel.BuildConfig;
import com.srp.ewayspanel.DIM;
import com.srp.ewayspanel.ui.bill.archive.BillArchiveViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static com.srp.eways.network.NetworkResponseCodes.ERROR_NO_INTERNET;
import static com.srp.eways.network.NetworkResponseCodes.ERROR_SERVER_PROBLEM;
import static com.srp.eways.network.NetworkResponseCodes.ERROR_TIMEOUT;
import static com.srp.eways.network.NetworkResponseCodes.SUCCESS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricTestRunner.class)
public class BillViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    BillArchiveViewModel mViewModel;

    @Before
    public void setUp() {

        mViewModel = new BillArchiveViewModel();

        assertNotNull(mViewModel);

        DIMainCommon.getBillApi().setHasAbResources(false);
    }

    @Test
    public void onNoInternetErrorTest() {

        DIMainCommon.getBillApi().setMode(ERROR_NO_INTERNET);

        mViewModel.getAllOnlineArchivedBills();

        Robolectric.flushForegroundThreadScheduler();

        assertNull(mViewModel.getOnlineBillArchivedListLiveData().getValue().getData());
    }

    @Test
    public void onServerErrorTest() {

        DIMainCommon.getBillApi().setMode(ERROR_SERVER_PROBLEM);

        mViewModel.getAllOnlineArchivedBills();

        Robolectric.flushForegroundThreadScheduler();

        assertNull(mViewModel.getOnlineBillArchivedListLiveData().getValue().getData());
    }
    @Test
    public void onSuccessTest() {

        DIMainCommon.getBillApi().setMode(SUCCESS);

        mViewModel.getAllOnlineArchivedBills();

        Robolectric.flushForegroundThreadScheduler();

        assertNotNull(mViewModel.getOnlineBillArchivedListLiveData().getValue());
        assertEquals(0, mViewModel.getOnlineBillArchivedListLiveData().getValue().getStatus());
    }

    @Test
    public void onSuccessRemoveTest() {

        DIMainCommon.getBillApi().setMode(SUCCESS);

        mViewModel.removeTempBill(1);

        Robolectric.flushForegroundThreadScheduler();

        assertNotNull(mViewModel.getRemoveBillResponseLiveData().getValue());
        assertEquals(0, mViewModel.getRemoveBillResponseLiveData().getValue().getStatus());
    }

    @Test
    public void onSuccessSaveTest() {

        DIMainCommon.getBillApi().setMode(SUCCESS);

        BillTemp billTemp = new BillTemp(1, "425345645",  "425345645", "2020-04-25T07:47:27.852Z",
                "450000", "","",450000,5,"",0,"",
                true,false,0,"2020-04-25T07:47:27.852Z","",false);

        mViewModel.saveBill(billTemp);

        Robolectric.flushForegroundThreadScheduler();

        assertNotNull(mViewModel.getSaveBillsResponseLiveData().getValue());
        assertEquals(0, mViewModel.getSaveBillsResponseLiveData().getValue().getStatus());

        assertNotNull( mViewModel.getSaveBillsResponseLiveData().getValue().getResult());
    }

    @Test
    public void successGetBankListTest() {
        DIMainCommon.getBillApi().setMode(SUCCESS);

        mViewModel.getBankList();
        assertTrue(mViewModel.isLoading().getValue());
        Robolectric.flushForegroundThreadScheduler();
        assertEquals(false, mViewModel.isLoading().getValue());
        assertEquals(mViewModel.getBankListLiveData().getValue().getStatus(), SUCCESS);
        assertNotNull(mViewModel.getBankListLiveData().getValue().getItems());
    }

    @Test
    public void timeoutGetBankListTest() {
        DIMainCommon.getBillApi().setMode(ERROR_TIMEOUT);

        mViewModel.getBankList();

        Robolectric.flushForegroundThreadScheduler();

        assertNotNull(mViewModel.getBankListLiveData().getValue());
        assertEquals(ERROR_TIMEOUT, mViewModel.getBankListLiveData().getValue().getStatus());
    }
}
