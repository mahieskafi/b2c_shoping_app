package com.srp.ewayspanel.repository.local.address;

import android.os.AsyncTask;

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
            new AsyncTask<Void, Void, Void>(){

                @Override
                protected Void doInBackground(Void... voids) {
                    sInstance.insertAddress(createAddress());
                    return null;
                }
            }.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);

        }

        return sInstance;
    }

    public AddressTableHelperImplementation() {
        mAddressDao = DI.getAddressDao();
    }

    @Override
    public Long insertAddress(Address address) {
        return mAddressDao.insertAddress(createAddress());
    }

    @Override
    public void deleteAddress(int id) {
        mAddressDao.deleteAddress(id);
    }

    @Override
    public List<Address> getAllAddress(Long userId) {
        return mAddressDao.getAllAddress();
    }

    @Override
    public void update(Address newAddress) {
        mAddressDao.update(createNewAddress());
    }


    private static Address createAddress() {
        Address address = new Address();
        address.setAddressId(5);
        address.setAddressName("آدرس اول");
        address.setAddress("باغ فیض خیابان مهستان باغ فیض خیابان مهستان");
        address.setCityName("تهران");
        address.setFullName("مهدیه اسکافی");
        address.setMobile("09192125525");
        address.setPhoneNumber("02144455862");
        address.setPostCode("445379651");
        address.setStateName("تهران");

        return address;
    }

    private Address createNewAddress() {
        Address address = new Address();
        address.setAddressId(5);
        address.setAddressName("آدرس دوم");
        address.setAddress("میدان ونک گاندی شمالی میدان ونک گاندی شمالی");
        address.setCityName("تهران");
        address.setFullName("شرکت هفت هشتاد");
        address.setMobile("09192125525");
        address.setPhoneNumber("02144455862");
        address.setPostCode("445379651");
        address.setStateName("تهران");

        return address;
    }
}
