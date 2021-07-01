package com.srp.eways.repository.contact;

import com.srp.eways.model.user.ContactSaleExpertResponse;
import com.srp.eways.network.CallBackWrapper;

/**
 * Created by Eskafi on 1/5/2020.
 */
public interface ContactSaleExpertRepository {

    void contactInfo(CallBackWrapper<ContactSaleExpertResponse> callBack);
}
