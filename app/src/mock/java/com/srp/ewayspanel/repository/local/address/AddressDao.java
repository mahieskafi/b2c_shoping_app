package com.srp.ewayspanel.repository.local.address;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.srp.ewayspanel.model.login.Address;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

/**
 * Created by Eskafi on 12/24/2019.
 */
@Dao
public interface AddressDao {

    @Insert
    Long insertAddress(Address address);

    @Query("DELETE FROM address WHERE addressId=:id")
    void deleteAddress(int id);

    @Query("SELECT * FROM address")
    List<Address> getAllAddress();

    @Insert(onConflict = REPLACE)
    void update(Address newAddress);
}
