package com.srp.ewayspanel.repository.login;

import com.srp.eways.network.CallBackWrapper;
import com.srp.ewayspanel.di.DI;
import com.srp.ewayspanel.model.login.ForgetPasswordRequest;
import com.srp.ewayspanel.model.login.ForgetPasswordResponse;
import com.srp.eways.model.login.LoginRequest;
import com.srp.eways.model.login.LoginResponse;
import com.srp.ewayspanel.model.login.ResetPasswordRequest;
import com.srp.ewayspanel.model.login.ResetPasswordResponse;
import com.srp.ewayspanel.model.login.VerifyCodeRequest;
import com.srp.ewayspanel.model.login.VerifyCodeResponse;
import com.srp.ewayspanel.model.login.authenticate.AuthenticateRequest;
import com.srp.ewayspanel.model.login.authenticate.AuthenticateResponse;
import com.srp.ewayspanel.model.login.registerwithotp.RegisterWithOTPRequest;
import com.srp.ewayspanel.model.login.registerwithotp.RegisterWithOTPResponse;
import com.srp.ewayspanel.model.login.sms.LoggedSmsConfirmResponse;
import com.srp.ewayspanel.model.login.sms.LoggedSmsRequestResponse;
import com.srp.ewayspanel.model.login.sms.OutSmsRequest;
import com.srp.ewayspanel.model.login.sms.OutSmsResponse;
import com.srp.ewayspanel.model.login.sms.OutSmsVerifiRequest;
import com.srp.ewayspanel.model.login.sms.OutSmsVerifyResponse;
import com.srp.ewayspanel.model.login.verifyotp.VerifyOTPRequest;
import com.srp.ewayspanel.model.login.verifyotp.VerifyOTPResponse;
import com.srp.ewayspanel.repository.remote.login.LoginApiService;

/**
 * Created by ErfanG on 14/08/2019.
 */
public class LoginRepositoryImplementation implements LoginRepository{

    private static LoginRepositoryImplementation INSTANCE = null;

    public LoginApiService mLoginApiService;


    public static LoginRepositoryImplementation getInstance(){

        if(INSTANCE == null) {

            INSTANCE = new LoginRepositoryImplementation();
        }

        return INSTANCE;
    }

    private LoginRepositoryImplementation(){

        mLoginApiService = DI.getLoginApi();
    }

    @Override
    public void login(LoginRequest body, CallBackWrapper<LoginResponse> callBack) {

        mLoginApiService.login(body, callBack);
    }

    @Override
    public void sendVerificationCode(ForgetPasswordRequest requestBody, CallBackWrapper<ForgetPasswordResponse> callback) {

        mLoginApiService.sendVerificationCode(requestBody, callback);
    }

    @Override
    public void verifyCode(VerifyCodeRequest requestBody, CallBackWrapper<VerifyCodeResponse> callback) {

        mLoginApiService.verifyCode(requestBody, callback);
    }

    @Override
    public void resetPassword(ResetPasswordRequest requestBody, CallBackWrapper<ResetPasswordResponse> callback) {

        mLoginApiService.resetPassword(requestBody, callback);
    }

    @Override
    public void authenticate(AuthenticateRequest request, CallBackWrapper<AuthenticateResponse> callback) {

        mLoginApiService.authenticate(request, callback);
    }

    @Override
    public void verifyOTP(VerifyOTPRequest request, CallBackWrapper<VerifyOTPResponse> callback) {

        mLoginApiService.verifyOTP(request, callback);

    }

    @Override
    public void registerWithOTP(RegisterWithOTPRequest request, CallBackWrapper<RegisterWithOTPResponse> callback) {

        mLoginApiService.registerWithOTP(request, callback);

    }

    @Override
    public void verifyMobileNumber(OutSmsRequest requestBody, CallBackWrapper<OutSmsResponse> callback) {
        mLoginApiService.verifyMobileNumber(requestBody,callback);
    }

    @Override
    public void verifyOtpNumber(OutSmsVerifiRequest requestBody, CallBackWrapper<OutSmsVerifyResponse> callback) {
        mLoginApiService.verifyOtpNumber(requestBody,callback);
    }

    @Override
    public void sendMobileConfirmRequest(String inputNumber, CallBackWrapper<LoggedSmsRequestResponse> callback) {
        mLoginApiService.sendMobileConfirmRequest(inputNumber, callback);
    }

    @Override
    public void confirmMobileNumber(String inputNumber, String token, CallBackWrapper<LoggedSmsConfirmResponse> callback) {
        mLoginApiService.confirmMobileNumber(inputNumber,token, callback);
    }
}
