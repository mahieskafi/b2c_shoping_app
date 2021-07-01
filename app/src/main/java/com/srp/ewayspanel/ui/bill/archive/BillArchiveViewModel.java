package com.srp.ewayspanel.ui.bill.archive;

import com.srp.eways.repository.bill.archive.BillArchiveRepository;
import com.srp.eways.ui.bill.archive.MainBillArchiveViewModel;
import com.srp.ewayspanel.di.DI;

public class BillArchiveViewModel extends MainBillArchiveViewModel {

    public BillArchiveViewModel() {
        super();
    }

    @Override
    protected BillArchiveRepository getBillArchivedRepository() {
        return DI.getBillRepository();
    }
}
