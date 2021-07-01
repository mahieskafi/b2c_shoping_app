package com.srp.ewayspanel.repository.sale;

import com.srp.eways.network.CallBackWrapper;
import com.srp.ewayspanel.model.sale.SaleSummaryReportRequest;
import com.srp.ewayspanel.model.sale.SaleSummaryReportResponse;
import com.srp.ewayspanel.di.DI;
import com.srp.ewayspanel.model.sale.detail.SaleDetailReportRequest;
import com.srp.ewayspanel.model.sale.detail.SaleDetailReportResponse;
import com.srp.ewayspanel.repository.remote.sale.SaleReportApiService;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Eskafi on 1/11/2020.
 */
public class SaleReportRepositoryImplementation implements SaleReportRepository {

    private static SaleReportRepositoryImplementation sINSTANCE = null;
    private static final int PAGE_SIZE = 10;

    private SaleReportApiService mSaleReportApiService;

    private int mPageIndex = 0;
    private boolean mHasMore = true;

    public static SaleReportRepositoryImplementation getInstance() {

        if (sINSTANCE == null) {

            sINSTANCE = new SaleReportRepositoryImplementation();
        }

        return sINSTANCE;
    }

    private SaleReportRepositoryImplementation() {

        mSaleReportApiService = DI.getSaleReportApi();
    }

    @Override
    public void getSalesSummaryReport(@NotNull SaleSummaryReportRequest request, @NotNull CallBackWrapper<SaleSummaryReportResponse> callback) {
        mSaleReportApiService.getSalesSummaryReport(request, callback);
    }

    @Override
    public void getSalesDetailReport(@NotNull SaleDetailReportRequest request, @NotNull final CallBackWrapper<SaleDetailReportResponse> callBackWrapper) {

        request.setPageIndex(mPageIndex);
        request.setPageSize(PAGE_SIZE);

        mSaleReportApiService.getSalesDetailReport(request, new CallBackWrapper<SaleDetailReportResponse>() {
            @Override
            public void onError(int errorCode, String errorMessage) {
                callBackWrapper.onError(errorCode, errorMessage);
            }

            @Override
            public void onSuccess(SaleDetailReportResponse responseBody) {
                if (responseBody.getSaleList() != null) {
                      int nextPageIndex = mPageIndex + 1;

                    if (mPageIndex != nextPageIndex) {
                        mPageIndex = nextPageIndex;

                        mHasMore = responseBody.getSaleList().size() >= PAGE_SIZE;

                    }
                    callBackWrapper.onSuccess(responseBody);
                }
            }
        });
    }

    @Override
    public boolean hasMoreDetailList() {
        return mHasMore;
    }

    @Override
    public int getPageIndexDetailList() {
        return mPageIndex;
    }

    @Override
    public void clearDetailList() {

        mPageIndex = 0;
        mHasMore = false;
    }
}
