package com.srp.eways.repository.remote.contact;

import com.srp.eways.model.user.ContactSaleExpertResponse;
import com.srp.eways.network.CallBackWrapper;

public interface ContactSaleExpertApiService {
    void getContactInfo(CallBackWrapper<ContactSaleExpertResponse> callBack);
}
