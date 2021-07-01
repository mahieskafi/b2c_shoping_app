package com.srp.ewayspanel.repository.address;

import com.srp.eways.network.CallBackWrapper;
import com.srp.ewayspanel.model.address.CityResponse;
import com.srp.ewayspanel.model.address.ProvinceResponse;
import com.srp.ewayspanel.model.login.Address;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by Eskafi on 12/28/2019.
 */
public interface AddressRepository {

    Single<Long> insertAddress(Address address);

    void deleteAddress(int id);

    Observable<List<Address>> getAllAddress(Long userId);

    void update(Address newAddress);

    void getProvinces(CallBackWrapper<ProvinceResponse> callBackWrapper);

    void getCities(int provinceId, CallBackWrapper<CityResponse> callBackWrapper);
}
