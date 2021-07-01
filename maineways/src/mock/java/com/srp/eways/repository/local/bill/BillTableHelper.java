package com.srp.eways.repository.local.bill;

import com.srp.eways.model.bill.archivedList.BillTemp;

import java.util.ArrayList;
import java.util.List;

public interface BillTableHelper {

    Long saveBill(BillTemp billTable);

    void saveAllBills(List<BillTemp> billTemps);

    void deleteBill(int id);

    void deleteAll();

    ArrayList<BillTemp> getAllBills();

}
