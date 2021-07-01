package com.srp.b2b2cEwaysPannel.ui;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;

import com.srp.eways.model.user.ContactSaleExpertResponse;
import com.srp.eways.ui.contact.MainContactFragment;

import java.util.ArrayList;
import java.util.List;

public class ContactFragment extends MainContactFragment {

    private static final String PHONE_NUMBER = "02164004";

    public static ContactFragment newInstance() {
        return new ContactFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<ContactSaleExpertResponse.ContactSaleExpert> contactSaleExpertList = new ArrayList<>();

        ContactSaleExpertResponse.ContactSaleExpert contactSaleExpert = new ContactSaleExpertResponse.ContactSaleExpert(PHONE_TYPE, "", PHONE_NUMBER);

        contactSaleExpertList.add(contactSaleExpert);

        setContactSaleExpert(contactSaleExpertList);
    }
}
