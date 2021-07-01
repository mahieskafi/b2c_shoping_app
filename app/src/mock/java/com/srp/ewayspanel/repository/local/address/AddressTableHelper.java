package com.srp.ewayspanel.repository.local.address;


import com.srp.ewayspanel.model.login.Address;

import java.util.List;


/**
 * Created by Eskafi on 12/28/2019.
 */
public interface AddressTableHelper {

    Long insertAddress(Address address);

    void deleteAddress(int id);

    List<Address> getAllAddress(Long userId);

    void update(Address newAddress);
}
