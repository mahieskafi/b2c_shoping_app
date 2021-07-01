package com.srp.eways.repository.remote.logout;

import com.srp.eways.model.logout.LogoutResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Eskafi on 9/23/2019.
 */
public interface LogoutApiRetrofit {
    @GET("service/v{version}/user/logout")
    Call<LogoutResponse> logout(@Path("version") int version);
}
