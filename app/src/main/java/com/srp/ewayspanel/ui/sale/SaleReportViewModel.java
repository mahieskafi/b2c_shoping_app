package com.srp.ewayspanel.ui.sale;

import androidx.lifecycle.MutableLiveData;

import com.srp.eways.base.BaseViewModel;
import com.srp.eways.network.BaseCallBackWrapper;
import com.srp.ewayspanel.di.DI;
import com.srp.ewayspanel.model.sale.SaleSummaryReportRequest;
import com.srp.ewayspanel.model.sale.SaleSummaryReportResponse;
import com.srp.ewayspanel.model.sale.detail.SaleDetailReportRequest;
import com.srp.ewayspanel.model.sale.detail.SaleDetailReportResponse;
import com.srp.ewayspanel.repository.sale.SaleReportRepository;
import com.srp.eways.util.Utils;


/**
 * Created by Eskafi on 1/11/2020.
 */
public class SaleReportViewModel extends BaseViewModel {

    private static final int SALE_CHANNEL_TYPE = 0;

    private SaleReportRepository mSaleReportRepo;

    private SaleSummaryReportRequest mRequest;

    private MutableLiveData<SaleSummaryReportResponse> mSaleReportResponseLiveData;
    private MutableLiveData<Boolean> mIsLoadingLiveData;
    private MutableLiveData<String> mErrorLiveData;

    private MutableLiveData<SaleDetailReportResponse> mSaleDetailReportResponseLiveData;

    public SaleReportViewModel() {
        mSaleReportRepo = DI.getSaleReportRepo();

        mSaleReportResponseLiveData = new MutableLiveData<>();
        mIsLoadingLiveData = new MutableLiveData<>();
        mErrorLiveData = new MutableLiveData<>();
        mSaleDetailReportResponseLiveData = new MutableLiveData<>();
    }

    public void getSaleSummaryReport(int mainGroupType, int deliverStatus, String startDate, String endDate) {

        mIsLoadingLiveData.setValue(true);

        mSaleReportRepo.getSalesSummaryReport(createRequest(mainGroupType, deliverStatus, startDate, endDate), new BaseCallBackWrapper<SaleSummaryReportResponse>(this) {
            @Override
            public void onError(int errorCode, String errorMessage) {
                mIsLoadingLiveData.setValue(false);

                mErrorLiveData.setValue(errorMessage);
            }

            @Override
            public void onSuccess(SaleSummaryReportResponse responseBody) {
                mIsLoadingLiveData.setValue(false);

                mSaleReportResponseLiveData.setValue(responseBody);
            }
        });
    }

    private SaleSummaryReportRequest createRequest(int mainGroupType, int deliverStatus, String startDate, String endDate) {
        mRequest = new SaleSummaryReportRequest(SALE_CHANNEL_TYPE, mainGroupType, deliverStatus, Utils.toEnglishNumber(startDate)
                , Utils.toEnglishNumber(endDate));
        return mRequest;
    }

    public void getSaleDetailReport(int mainGroupType, int deliverStatus, String startDate, String endDate) {

        mIsLoadingLiveData.setValue(true);

        mSaleReportRepo.getSalesDetailReport(createDetailRequest(mainGroupType, deliverStatus, startDate, endDate), new BaseCallBackWrapper<SaleDetailReportResponse>(this) {
            @Override
            public void onError(int errorCode, String errorMessage) {
//                mIsLoadingLiveData.setValue(false);

//                mErrorLiveData.setValue(errorMessage);
            }

            @Override
            public void onSuccess(SaleDetailReportResponse responseBody) {
//                mIsLoadingLiveData.setValue(false);

                mSaleDetailReportResponseLiveData.setValue(responseBody);
            }
        });
    }

    private SaleDetailReportRequest createDetailRequest(int mainGroupType, int deliverStatus, String startDate, String endDate) {
        return new SaleDetailReportRequest(SALE_CHANNEL_TYPE, mainGroupType, deliverStatus, Utils.toEnglishNumber(startDate)
                , Utils.toEnglishNumber(endDate), 0, 0);
    }

    private SaleSummaryReportRequest getRequest() {
        return mRequest;
    }

    public MutableLiveData<SaleSummaryReportResponse> getSaleReportResponseLiveData() {
        return mSaleReportResponseLiveData;
    }

    public MutableLiveData<SaleDetailReportResponse> getSaleDetailReportResponseLiveData() {
        return mSaleDetailReportResponseLiveData;
    }

    public MutableLiveData<Boolean> getIsLoadingLiveData() {
        return mIsLoadingLiveData;
    }

    public MutableLiveData<String> getErrorLiveData() {
        return mErrorLiveData;
    }

    public void consumeErrorLiveData() {
        mErrorLiveData.setValue(null);
    }

    public void consumeSaleReportResponseLiveData() {
        mSaleReportResponseLiveData.setValue(null);
    }

    public void consumeSaleDetailReportResponseLiveData() {
        mSaleDetailReportResponseLiveData.setValue(null);
    }

    public boolean hasMoreDetailList() {
        return mSaleReportRepo.hasMoreDetailList();
    }

    public int getPageIndexDetailList() {
        return mSaleReportRepo.getPageIndexDetailList();
    }

    public void resetDetailList() {
        mSaleReportRepo.clearDetailList();
    }

}
