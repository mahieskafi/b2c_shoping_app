package com.srp.eways.ui.bill.archive;

import android.os.AsyncTask;

import androidx.lifecycle.MutableLiveData;

import com.srp.eways.model.bill.BillPaymentResponse;
import com.srp.eways.model.bill.archivedList.BillCounter;
import com.srp.eways.model.bill.archivedList.BillTemp;
import com.srp.eways.model.bill.archivedList.BillTempRemoveResponse;
import com.srp.eways.model.bill.archivedList.BillTempResponse;
import com.srp.eways.network.BaseCallBackWrapper;
import com.srp.eways.repository.bill.archive.BillArchiveRepository;
import com.srp.eways.ui.bill.MainBillViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public abstract class MainBillArchiveViewModel extends MainBillViewModel {

    private BillArchiveRepository mBillRepository;

    private final MutableLiveData<ArrayList<BillTemp>> mBillArchivedListLiveData;
    private final MutableLiveData<BillTempResponse> mOnlineBillArchivedListLiveData;
    private final MutableLiveData<BillTempRemoveResponse> mRemoveBillResponseLiveData;
    private final MutableLiveData<BillCounter> mBillPaymentLiveData;
    private final MutableLiveData<BillPaymentResponse> mSaveBillsResponseLiveData;

    protected ArrayList<BillTemp> mBillPaymentList;

    public MainBillArchiveViewModel() {

        mBillRepository = getBillArchivedRepository();

        mBillRepository.clearTempBillList();

        mBillArchivedListLiveData = new MutableLiveData<>();
        mOnlineBillArchivedListLiveData = new MutableLiveData<>();
        mBillPaymentLiveData = new MutableLiveData<>();
        mRemoveBillResponseLiveData = new MutableLiveData<>();

        mSaveBillsResponseLiveData = new MutableLiveData<>();
    }

    public void saveBill(BillTemp billTable) {

        mBillRepository.insertBill(billTable)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Long>() {


                    @Override
                    public void onSubscribe(Disposable d) {
                        String s = "success";
                    }

                    @Override
                    public void onSuccess(Long aLong) {
                        String s = "success";
                    }

                    @Override
                    public void onError(Throwable e) {
                        String s = "error";
                    }
                });
    }

    public void saveAllBills(final List<BillTemp> billTemps) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                mBillRepository.insertAllBills(billTemps);
                return null;
            }
        }.execute();
    }

    public void deleteBill(final int id) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                mBillRepository.deleteBill(id);
                return null;
            }
        }.execute();
    }

    public void deleteAllBills() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                mBillRepository.deleteAll();
                return null;
            }
        }.execute();

    }

    public void getAllArchivedBills() {
        mBillRepository.getAllBills()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArrayList<BillTemp>>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ArrayList<BillTemp> billTables) {
                        mBillArchivedListLiveData.setValue(billTables);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void getAllOnlineArchivedBills() {
        mBillRepository.getTempBills(new BaseCallBackWrapper<BillTempResponse>(this) {
            @Override
            public void onError(int errorCode, String errorMessage) {
                BillTempResponse billTempResponse = new BillTempResponse(errorCode, errorMessage);

                mOnlineBillArchivedListLiveData.setValue(billTempResponse);
            }

            @Override
            public void onSuccess(BillTempResponse responseBody) {
                mOnlineBillArchivedListLiveData.setValue(responseBody);
            }
        });
    }

    public void removeTempBill(int id) {
        setLoading(true);

        mBillRepository.removeTempBills(id, new BaseCallBackWrapper<BillTempRemoveResponse>(this) {
            @Override
            public void onError(int errorCode, String errorMessage) {
                setLoading(false);

                BillTempRemoveResponse billTempResponse = new BillTempRemoveResponse(errorCode, errorMessage);

                mRemoveBillResponseLiveData.setValue(billTempResponse);
            }

            @Override
            public void onSuccess(BillTempRemoveResponse responseBody) {
                setLoading(false);

                mRemoveBillResponseLiveData.setValue(responseBody);
            }
        });
    }


    public void clearSelectedBillPaymentList() {
        if (mBillPaymentList == null) {
            mBillPaymentList = new ArrayList<>();
        } else {
            mBillPaymentList.clear();
        }

        calculatePaymentPrice();
    }

    public void setChangeBillPaymentList(boolean isAdded, BillTemp billTemp) {
        if (mBillPaymentList == null) {
            mBillPaymentList = new ArrayList<>();
        }

        if (isAdded) {
            mBillPaymentList.add(billTemp);
        } else {
            mBillPaymentList.remove(billTemp);
        }

        calculatePaymentPrice();
    }

    private void calculatePaymentPrice() {
        int count = mBillPaymentList.size();
        Long price = 0L;

        for (BillTemp bill : mBillPaymentList) {
            price += bill.getPrice();
        }

        mBillPaymentLiveData.setValue(new BillCounter(count, price));
    }

    public void resetPage() {
        mBillRepository.clearTempBillList();
    }

    public boolean hasMore() {
        return mBillRepository.hasMoreTempBill();
    }

    public int getPageIndex() {
        return mBillRepository.getPageIndexTempBillList();
    }

    public MutableLiveData<ArrayList<BillTemp>> getBillArchivedListLiveData() {
        return mBillArchivedListLiveData;
    }

    public MutableLiveData<BillTempResponse> getOnlineBillArchivedListLiveData() {
        return mOnlineBillArchivedListLiveData;
    }

    public MutableLiveData<BillTempRemoveResponse> getRemoveBillResponseLiveData() {
        return mRemoveBillResponseLiveData;
    }

    public MutableLiveData<BillPaymentResponse> getSaveBillsResponseLiveData() {
        return mSaveBillsResponseLiveData;
    }


    public MutableLiveData<BillCounter> getPaymentLiveData() {
        return mBillPaymentLiveData;
    }

    public void consumeOnlineBillArchivedListLiveData() {
        mOnlineBillArchivedListLiveData.setValue(null);
    }

    public void consumeRemoveBillResponseLiveData() {
        mRemoveBillResponseLiveData.setValue(null);
    }

    public void consumeBillArchivedListLiveData() {
        mBillArchivedListLiveData.setValue(null);
    }

    public void consumePaymentListLiveData() {
        mBillPaymentLiveData.setValue(null);
        if (mBillPaymentList == null) {
            mBillPaymentList = new ArrayList<>();
        }
        mBillPaymentList.clear();
    }

    public ArrayList<BillTemp> getBillPaymentList() {
        return mBillPaymentList;
    }

    public void consumeSaveBillsResponseLiveData() {
        mSaveBillsResponseLiveData.setValue(null);
    }

    protected abstract BillArchiveRepository getBillArchivedRepository();

}
