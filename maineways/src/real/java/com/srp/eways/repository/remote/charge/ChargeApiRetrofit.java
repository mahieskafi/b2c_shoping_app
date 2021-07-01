package com.srp.eways.repository.remote.charge;

import com.srp.eways.model.charge.BuyChargeResult;
import com.srp.eways.model.charge.ChargeResult;
import com.srp.eways.model.charge.IrancellSpecialOffersResult;
import com.srp.eways.model.charge.request.BuyChargeRequest;
import com.srp.eways.model.charge.result.BuyCashChargeResult;
import com.srp.eways.model.charge.result.topinquiry.TopInquiriesResult;
import com.srp.eways.model.deposit.BankListResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by ErfanG on 26/08/2019.
 */
public interface ChargeApiRetrofit {


    @GET("service/v1.0/charge/GetTopupTypes")
    Call<ChargeResult> getChargeData();

    @GET("service/v1.0/charge/GetSpecialOffer/{number}")
    Call<IrancellSpecialOffersResult> getIrancellSpecialOffers(@Path("number") String phoneNumber);

    @POST("service/v1.0/charge/TopupNumber")
    Call<BuyChargeResult> buyCharge(@Body BuyChargeRequest buyChargeRequest);

    @POST("service/v{version}/charge/topupnumbergateway")
    Call<BuyCashChargeResult> buyCashCharge(@Path("version") int version, @Body BuyChargeRequest buyChargeRequest);

    @GET("service/v1/charge/inquirytopup")
    Call<TopInquiriesResult> topInquiries();

    @GET("service/v{version}/credit/GetBanks")
    Call<BankListResponse> getBankList(@Path("version") int version);
}
