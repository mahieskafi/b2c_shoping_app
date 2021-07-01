package com.srp.eways.repository.remote.contact;

import android.os.Handler;

import com.srp.eways.model.user.ContactSaleExpertResponse;
import com.srp.eways.network.CallBackWrapper;
import com.srp.eways.repository.remote.BaseApiImplementation;

import java.util.ArrayList;
import java.util.List;

public class ContactSaleExpertApiImplementation extends BaseApiImplementation implements ContactSaleExpertApiService {

    private static ContactSaleExpertApiImplementation INSTANCE;

    public static ContactSaleExpertApiImplementation getInstance() {

        if (INSTANCE == null) {
            INSTANCE = new ContactSaleExpertApiImplementation();
        }

        return INSTANCE;
    }

    @Override
    public void getContactInfo(final CallBackWrapper<ContactSaleExpertResponse> callBack) {
        if (!handleCall(callBack)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    callBack.onSuccess(createSuccessResult());
                }
            }, getResponseDelay());
        }
    }

    private ContactSaleExpertResponse createSuccessResult() {

        List<ContactSaleExpertResponse.ContactSaleExpert> list = new ArrayList<>();
        list.add(new ContactSaleExpertResponse.ContactSaleExpert(2, "phone", "0214410003"));
        list.add(new ContactSaleExpertResponse.ContactSaleExpert(5, "email", "info@eways.com"));
        list.add(new ContactSaleExpertResponse.ContactSaleExpert(4, "telegram", "@eways"));

        ContactSaleExpertResponse response = new ContactSaleExpertResponse();
        response.setStatus(0);
        response.setItems(list);

        return response;
    }
}
