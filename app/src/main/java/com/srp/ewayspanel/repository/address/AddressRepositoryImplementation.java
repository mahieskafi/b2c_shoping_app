package com.srp.ewayspanel.repository.address;


import com.srp.eways.network.CallBackWrapper;
import com.srp.ewayspanel.di.DI;
import com.srp.ewayspanel.di.DICommon;
import com.srp.ewayspanel.model.address.CityResponse;
import com.srp.ewayspanel.model.address.ProvinceResponse;
import com.srp.ewayspanel.model.login.Address;
import com.srp.ewayspanel.repository.local.address.AddressTableHelper;
import com.srp.ewayspanel.repository.remote.address.AddressApiService;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by Eskafi on 12/28/2019.
 */
public class AddressRepositoryImplementation implements AddressRepository {

    private static AddressRepositoryImplementation sInstance;
    private AddressTableHelper mAddressHelper;
    private AddressApiService mAddressApiService;

    public static AddressRepositoryImplementation getInstance() {

        if (sInstance == null) {
            sInstance = new AddressRepositoryImplementation();
        }
        return sInstance;
    }

    public AddressRepositoryImplementation() {
        mAddressHelper = DI.getAddressHelper();
        mAddressApiService = DICommon.getAddressApi();
    }

    @Override
    public Single<Long> insertAddress(final Address address) {
        return Single.fromCallable(new Callable<Long>() {
            @Override
            public Long call() {
                return mAddressHelper.insertAddress(address);
            }
        });
    }

    @Override
    public void deleteAddress(int id) {
        mAddressHelper.deleteAddress(id);
    }

    @Override
    public Observable<List<Address>> getAllAddress(final Long userId) {
        return Observable.fromCallable(new Callable<List<Address>>() {
            @Override
            public List<Address> call() {
                return mAddressHelper.getAllAddress(userId);
            }
        });
    }

    @Override
    public void update(Address newAddress) {
        mAddressHelper.update(newAddress);
    }

    @Override
    public void getProvinces(CallBackWrapper<ProvinceResponse> callBackWrapper) {
        mAddressApiService.getProvinces(callBackWrapper);
    }

    @Override
    public void getCities(int provinceId, CallBackWrapper<CityResponse> callBackWrapper) {
        mAddressApiService.getCities(provinceId,callBackWrapper);
    }
}
