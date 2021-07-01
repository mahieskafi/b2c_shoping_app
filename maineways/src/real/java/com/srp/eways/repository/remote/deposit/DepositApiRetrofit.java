package com.srp.eways.repository.remote.deposit;

import com.google.gson.JsonObject;
import com.srp.eways.model.deposit.BankListResponse;
import com.srp.eways.model.deposit.ConfirmPaymentRequest;
import com.srp.eways.model.deposit.ConfirmPaymentResponse;
import com.srp.eways.model.deposit.IncreaseDepositRequest;
import com.srp.eways.model.deposit.IncreaseDepositResponse;
import com.srp.eways.model.deposit.IncreaseDepositStatusResponse;
import com.srp.eways.model.deposit.MPLTokenResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Eskafi on 8/26/2019.
 */
public interface DepositApiRetrofit {

    @GET("service/v{version}/credit/GetBanks")
    Call<BankListResponse> getBankList(@Path("version") int version);

    @POST("service/v{version}/credit/IncreaseCredit")
    Call<IncreaseDepositResponse> increaseDeposit(@Path("version") int version, @Body IncreaseDepositRequest request);

    @GET("service/v{version}/credit/IncreaseCreditGetStatus/{uId}/{uUId}")
    Call<IncreaseDepositStatusResponse> getStatus(@Path("version") int version, @Path("uId") String uId, @Path("uUId") String uUId);

    @POST("service/v{version}/credit/getmpltoken")
    Call<MPLTokenResponse> getMplToken(@Path("version") int version, @Body JsonObject body);

    @POST("service/v{version}/credit/confirmpayment")
    Call<ConfirmPaymentResponse> confirmPayment(@Path("version") int version, @Body ConfirmPaymentRequest request);
}
