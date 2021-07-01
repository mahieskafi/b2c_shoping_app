package com.srp.ewayspanel.sale;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import com.srp.ewayspanel.ui.sale.SaleReportViewModel;
import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertNotNull;

/**
 * Created by Eskafi on 1/11/2020.
 */

@RunWith(RobolectricTestRunner.class)
public class SaleSummaryReportViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    SaleReportViewModel mViewModel;

    @Before
    public void setUp() {

        MockitoAnnotations.initMocks(this);
        mViewModel = Mockito.spy(new SaleReportViewModel());
        assertNotNull(mViewModel);
    }
}
