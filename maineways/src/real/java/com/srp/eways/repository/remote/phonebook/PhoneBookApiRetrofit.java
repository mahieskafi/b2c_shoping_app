package com.srp.eways.repository.remote.phonebook;

import com.srp.eways.model.phonebook.AddOrRemovePhoneBookResponse;
import com.srp.eways.model.phonebook.PhoneBookResult;
import com.srp.eways.model.phonebook.UserPhoneBook;
import com.srp.eways.model.phonebook.search.PhoneBookSearchResult;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface PhoneBookApiRetrofit {

    @GET("service/v{version}/user/UserPhoneBook/{pageIndex}/{pageSize}")
    Call<PhoneBookResult> getUserPhoneBook(@Path("pageIndex") int pageIndex, @Path("pageSize") int pageSize,@Path("version") int version);

    @POST("service/v{version}/user/searchinphonebook")
    Call<PhoneBookSearchResult> getUserPhoneBookSearchResult(@Path("version") int version, @Body String searchValue);

    @POST("/api/service/v{version}/user/saveuserphonebook")
    Call<AddOrRemovePhoneBookResponse> addUserPhoneBook(@Body UserPhoneBook contact, @Path("version") int version);

    @POST("/api/service/v{version}/user/removeuserphonebook")
    Call<AddOrRemovePhoneBookResponse> removeUserPhoneBook(@Body UserPhoneBook contact, @Path("version") int version);
}
