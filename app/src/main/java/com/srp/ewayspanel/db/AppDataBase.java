package com.srp.ewayspanel.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.srp.eways.model.bill.archivedList.BillTemp;
import com.srp.ewayspanel.repository.local.bill.BillDao;
import com.srp.ewayspanel.model.login.Address;
import com.srp.ewayspanel.repository.local.address.AddressDao;

/**
 * Created by Eskafi on 12/24/2019.
 */
@Database(entities = {Address.class, BillTemp.class}, version = 2, exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {
    public abstract AddressDao addressDao();

    public abstract BillDao billDao();
}
