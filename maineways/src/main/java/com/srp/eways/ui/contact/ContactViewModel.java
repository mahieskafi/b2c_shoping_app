package com.srp.eways.ui.contact;

import androidx.lifecycle.MutableLiveData;

import com.srp.eways.base.BaseViewModel;
import com.srp.eways.network.BaseCallBackWrapper;
import com.srp.eways.network.CallBackWrapper;
import com.srp.eways.di.DIMain;
import com.srp.eways.model.user.ContactSaleExpertResponse;
import com.srp.eways.repository.contact.ContactSaleExpertRepository;

/**
 * Created by Eskafi on 8/29/2019.
 */
public class ContactViewModel extends BaseViewModel {

    private final ContactSaleExpertRepository mContactRepository;
    private final MutableLiveData<ContactSaleExpertResponse> mContactInfoLiveData;

    public ContactViewModel() {
        mContactRepository = DIMain.getContactSaleExpertRepository();

        mContactInfoLiveData = new MutableLiveData<>();
    }

    public void getContactInfo() {
        setLoading(true);

        mContactRepository.contactInfo(new BaseCallBackWrapper<ContactSaleExpertResponse>(this) {
            @Override
            public void onError(int errorCode, String errorMessage) {
                setLoading(false);
            }

            @Override
            public void onSuccess(ContactSaleExpertResponse responseBody) {
                setLoading(false);

                if (responseBody != null) {
                    mContactInfoLiveData.setValue(responseBody);
                }
            }
        });
    }

    public MutableLiveData<ContactSaleExpertResponse> getContactInfoLiveData() {
        return mContactInfoLiveData;
    }
}
