package com.srp.eways.repository.bill.archive;

import com.srp.eways.di.DIMain;
import com.srp.eways.model.bill.archivedList.BillTemp;
import com.srp.eways.model.bill.archivedList.BillTempRemoveResponse;
import com.srp.eways.model.bill.archivedList.BillTempResponse;
import com.srp.eways.network.CallBackWrapper;
import com.srp.eways.repository.local.bill.BillTableHelper;
import com.srp.eways.repository.remote.bill.archive.BillArchiveApiService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.Single;


public abstract class MainBillArchiveRepositoryImplementation implements BillArchiveRepository {

    private static final int PAGE_SIZE = 10;

    protected BillTableHelper mBillHelper;
    protected BillArchiveApiService mBillApiService;

    private int mPageIndexTempList = 1;
    private boolean mHasMoreTempList = true;

    protected abstract BillTableHelper getBillHelper();

    public MainBillArchiveRepositoryImplementation() {
        mBillHelper = getBillHelper();
        mBillApiService = DIMain.getBillArchiveApi();
    }

    @Override
    public Single<Long> insertBill(final BillTemp billTable) {
        return Single.fromCallable(new Callable<Long>() {
            @Override
            public Long call() {
                return mBillHelper.saveBill(billTable);
            }
        });
    }

    @Override
    public void insertAllBills(List<BillTemp> billTemps) {
        mBillHelper.saveAllBills(billTemps);
    }

    @Override
    public void deleteBill(int id) {
        mBillHelper.deleteBill(id);
    }

    @Override
    public void deleteAll() {
        mBillHelper.deleteAll();
    }

    @Override
    public Observable<ArrayList<BillTemp>> getAllBills() {
        return Observable.fromCallable(new Callable<ArrayList<BillTemp>>() {
            @Override
            public ArrayList<BillTemp> call() {
                return mBillHelper.getAllBills();
            }
        });
    }


    @Override
    public void getTempBills(final CallBackWrapper<BillTempResponse> callBack) {
        mBillApiService.getTempBills(mPageIndexTempList, PAGE_SIZE, new CallBackWrapper<BillTempResponse>() {
            @Override
            public void onError(int errorCode, String errorMessage) {
                callBack.onError(errorCode, errorMessage);
            }

            @Override
            public void onSuccess(BillTempResponse responseBody) {

                if (responseBody.getData() != null && responseBody.getData().size() > 0) {
                    mHasMoreTempList = responseBody.getData().size() >= PAGE_SIZE;

//                    if (mHasMoreTempList) {
                    mPageIndexTempList += 1;
//                    }
                }

                callBack.onSuccess(responseBody);

            }
        });
    }

    @Override
    public Boolean hasMoreTempBill() {
        return mHasMoreTempList;
    }

    @Override
    public int getPageIndexTempBillList() {
        return mPageIndexTempList;
    }

    @Override
    public void clearTempBillList() {
        mPageIndexTempList = 1;
        mHasMoreTempList = false;
    }

    @Override
    public void removeTempBills(int id, final CallBackWrapper<BillTempRemoveResponse> callBackWrapper) {
        mBillApiService.removeTempBills(id, new CallBackWrapper<BillTempRemoveResponse>() {
            @Override
            public void onError(int errorCode, String errorMessage) {
                callBackWrapper.onError(errorCode, errorMessage);
            }

            @Override
            public void onSuccess(BillTempRemoveResponse responseBody) {
                callBackWrapper.onSuccess(responseBody);

            }
        });
    }
}
