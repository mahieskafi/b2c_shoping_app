package com.srp.ewayspanel.repository.local.bill;

import com.srp.eways.model.bill.archivedList.BillTemp;
import com.srp.eways.repository.local.bill.BillTableHelper;
import com.srp.ewayspanel.di.DI;

import java.util.ArrayList;
import java.util.List;


public class BillTableHelperImplementation implements BillTableHelper {

    private static BillTableHelperImplementation sInstance;

    protected BillDao mBillDao;

    public static BillTableHelperImplementation getInstance() {

        if (sInstance == null) {
            sInstance = new BillTableHelperImplementation();
        }

        return sInstance;
    }

    public BillTableHelperImplementation() {
        mBillDao = DI.getBillDao();
    }

    @Override
    public Long saveBill(BillTemp billTable) {
        return mBillDao.insertBill(billTable);
    }

    @Override
    public void saveAllBills(List<BillTemp> billTemps) {
        mBillDao.insertAll(billTemps);
    }

    @Override
    public void deleteBill(int id) {
        mBillDao.deleteBill(id);
    }

    @Override
    public void deleteAll() {
        mBillDao.deleteAll();
    }

    @Override
    public ArrayList<BillTemp> getAllBills() {

        return new ArrayList<>(mBillDao.getAllBills());
    }
//    @Override
//    public BillDao getBillDao() {
//        return DI.getBillDao();
//    }
}
