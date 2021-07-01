package com.srp.eways.repository.contact;

import com.srp.eways.network.CallBackWrapper;
import com.srp.eways.di.DIMain;
import com.srp.eways.model.user.ContactSaleExpertResponse;
import com.srp.eways.repository.remote.contact.ContactSaleExpertApiImplementation;

/**
 * Created by Eskafi on 1/5/2020.
 */
public class ContactSaleExpertRepositoryImplementation implements ContactSaleExpertRepository {

    private static ContactSaleExpertRepositoryImplementation INSTANCE = null;

    private ContactSaleExpertApiImplementation mContactApiService;


    public static ContactSaleExpertRepositoryImplementation getInstance() {

        if (INSTANCE == null) {

            INSTANCE = new ContactSaleExpertRepositoryImplementation();
        }

        return INSTANCE;
    }

    private ContactSaleExpertRepositoryImplementation() {

        mContactApiService = DIMain.getContactSaleExpertApiImplementation();
    }

    @Override
    public void contactInfo(CallBackWrapper<ContactSaleExpertResponse> callBack) {
        mContactApiService.getContactInfo(callBack);
    }
}
