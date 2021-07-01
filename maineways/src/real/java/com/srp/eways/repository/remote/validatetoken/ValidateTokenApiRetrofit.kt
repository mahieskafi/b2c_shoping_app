package com.srp.eways.repository.remote.validatetoken

import com.srp.eways.model.login.LoginResponse
import com.srp.eways.model.user.ContactSaleExpertResponse

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by Eskafi on 8/25/2019.
 */
interface ValidateTokenApiRetrofit {

    @GET("service/v{version}/user/validtoken")
    fun validateToken(@Path("version") version: Int): Call<LoginResponse>

    @GET("service/v{version}/support/contactsaleexpret")
    fun contactInfo(@Path("version") version: Int): Call<ContactSaleExpertResponse>
}
