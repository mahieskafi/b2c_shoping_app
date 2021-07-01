package com.srp.ewayspanel.repository.local.bill;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.srp.eways.model.bill.archivedList.BillTemp;

import java.util.List;


@Dao
public interface BillDao {

    @Insert
    Long insertBill(BillTemp billTable);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<BillTemp> billTemps);

    @Query("DELETE FROM bill WHERE billId = :billId ")
    void deleteBill(int billId);

    @Query("DELETE FROM bill")
    void deleteAll();

    @Query("SELECT * FROM bill")
    List<BillTemp> getAllBills();
}
