package com.srp.eways.repository.remote.contact;

import com.srp.eways.model.user.ContactSaleExpertResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ContactSaleExpertApiRetrofit {

    @GET("service/v{version}/support/contactsaleexpret")
    Call<ContactSaleExpertResponse> getContactInfo(@Path("version") int version);
}
