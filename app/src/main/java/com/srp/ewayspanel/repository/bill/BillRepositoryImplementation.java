package com.srp.ewayspanel.repository.bill;

import com.srp.eways.repository.bill.archive.MainBillArchiveRepositoryImplementation;
import com.srp.eways.repository.local.bill.BillTableHelper;
import com.srp.ewayspanel.di.DI;


public class BillRepositoryImplementation extends MainBillArchiveRepositoryImplementation {

    private static BillRepositoryImplementation sInstance;

    public static BillRepositoryImplementation getInstance() {

        if (sInstance == null) {
            sInstance = new BillRepositoryImplementation();
        }
        return sInstance;
    }

    public BillRepositoryImplementation() {
        super();
    }

    @Override
    protected BillTableHelper getBillHelper() {
        return DI.getBillHelper();
    }

}
