package com.srp.eways.repository.remote.contact;

import com.srp.eways.AppConfig;
import com.srp.eways.di.DIMain;
import com.srp.eways.network.CallBackWrapper;
import com.srp.eways.repository.remote.BaseApiImplementation;
import com.srp.eways.model.user.ContactSaleExpertResponse;
import com.srp.eways.repository.remote.DefaultRetroCallback;

public class ContactSaleExpertApiImplementation extends BaseApiImplementation implements ContactSaleExpertApiService {

    private static ContactSaleExpertApiImplementation INSTANCE;

    public static ContactSaleExpertApiImplementation getInstance() {

        if (INSTANCE == null) {
            INSTANCE = new ContactSaleExpertApiImplementation();
        }

        return INSTANCE;
    }

    private ContactSaleExpertApiRetrofit mContactApi;

    private ContactSaleExpertApiImplementation() {
        mContactApi = DIMain.provideApi(ContactSaleExpertApiRetrofit.class);
    }

    @Override
    public void getContactInfo(CallBackWrapper<ContactSaleExpertResponse> callBack) {
        mContactApi.getContactInfo(AppConfig.SERVER_VERSION).enqueue(new DefaultRetroCallback<ContactSaleExpertResponse>(callBack) {

            @Override
            protected void checkResponseForError(ContactSaleExpertResponse contactSaleExpertResponse, ErrorInfo errorInfo) {

                if (contactSaleExpertResponse.getStatus() != 0) {
                    errorInfo.errorCode = contactSaleExpertResponse.getStatus();
                    errorInfo.errorMessage = contactSaleExpertResponse.getDescription();
                }
            }

        });
    }
}
