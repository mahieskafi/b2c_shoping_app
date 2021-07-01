package com.srp.ewayspanel.ui.contact;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;

import com.srp.eways.model.user.ContactSaleExpertResponse;
import com.srp.eways.ui.contact.MainContactFragment;


public class ContactFragment extends MainContactFragment {

    public static ContactFragment newInstance() {
        return new ContactFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getViewModel().getContactInfo();
        getViewModel().getContactInfoLiveData().observe(getViewLifecycleOwner(), new Observer<ContactSaleExpertResponse>() {
            @Override
            public void onChanged(ContactSaleExpertResponse contactSaleExpertResponse) {
                setContactSaleExpert(contactSaleExpertResponse.getItems());
            }
        });
    }
}
