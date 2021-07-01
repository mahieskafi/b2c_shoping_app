package com.srp.eways.repository.bill.archive;

import com.srp.eways.model.bill.archivedList.BillTemp;
import com.srp.eways.model.bill.archivedList.BillTempRemoveResponse;
import com.srp.eways.model.bill.archivedList.BillTempResponse;
import com.srp.eways.network.CallBackWrapper;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

public interface BillArchiveRepository {

    Single<Long> insertBill(BillTemp billTable);

    void deleteBill(int id);

    void insertAllBills(List<BillTemp> billTemps);

    void deleteAll();

    Observable<ArrayList<BillTemp>> getAllBills();

    void getTempBills(CallBackWrapper<BillTempResponse> callBack);

    void removeTempBills(int id, CallBackWrapper<BillTempRemoveResponse> callBackWrapper);

    Boolean hasMoreTempBill();

    int getPageIndexTempBillList();

    void clearTempBillList();

}
