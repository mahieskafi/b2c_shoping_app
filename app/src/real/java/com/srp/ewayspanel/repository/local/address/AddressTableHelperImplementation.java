package com.srp.ewayspanel.repository.local.address;

import com.srp.ewayspanel.di.DI;
import com.srp.ewayspanel.model.login.Address;

import java.util.List;


/**
 * Created by Eskafi on 12/28/2019.
 */
public class AddressTableHelperImplementation implements AddressTableHelper {

    private static AddressTableHelperImplementation sInstance;

    private AddressDao mAddressDao;

    public static AddressTableHelperImplementation getInstance() {

        if (sInstance == null) {
            sInstance = new AddressTableHelperImplementation();
        }

        return sInstance;
    }

    public AddressTableHelperImplementation() {
        mAddressDao = DI.getAddressDao();
    }

    @Override
    public Long insertAddress(Address address) {
        return mAddressDao.insertAddress(address);
    }

    @Override
    public void deleteAddress(int id) {
        mAddressDao.deleteAddress(id);
    }

    @Override
    public List<Address> getAllAddress(Long userId) {
        return mAddressDao.getAllAddress(userId);
    }

    @Override
    public void update(Address newAddress) {
        mAddressDao.update(newAddress);
    }
}
