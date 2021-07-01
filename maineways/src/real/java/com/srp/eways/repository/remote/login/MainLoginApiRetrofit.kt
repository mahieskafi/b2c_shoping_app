package com.srp.eways.repository.remote.login

import com.srp.eways.model.login.LoginRequest
import com.srp.eways.model.login.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface MainLoginApiRetrofit {
    @POST("service/v{version}/user/login")
    fun login(
            @Path("version")
            version: Int,
            @Body
            body: LoginRequest
    ): Call<LoginResponse>
}