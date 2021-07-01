package com.srp.ewayspanel.repository.login;

import com.srp.eways.network.CallBackWrapper;
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

/**
 * Created by ErfanG on 14/08/2019.
 */
public interface LoginRepository {

    void login(LoginRequest body, CallBackWrapper<LoginResponse> callBack);

    void sendVerificationCode(ForgetPasswordRequest requestBody, CallBackWrapper<ForgetPasswordResponse> callback);

    void verifyCode(VerifyCodeRequest requestBody, CallBackWrapper<VerifyCodeResponse> callback);

    void resetPassword(ResetPasswordRequest requestBody, CallBackWrapper<ResetPasswordResponse> callback);

    void authenticate(AuthenticateRequest request , CallBackWrapper<AuthenticateResponse> callback);

    void verifyOTP(VerifyOTPRequest request  , CallBackWrapper<VerifyOTPResponse> callback );

    void registerWithOTP(RegisterWithOTPRequest request , CallBackWrapper<RegisterWithOTPResponse> callback );

    void verifyMobileNumber(OutSmsRequest requestBody , CallBackWrapper<OutSmsResponse> callback );
    void verifyOtpNumber(OutSmsVerifiRequest requestBody , CallBackWrapper<OutSmsVerifyResponse> callback );

    void sendMobileConfirmRequest(String inputNumber , CallBackWrapper<LoggedSmsRequestResponse> callback );
    void confirmMobileNumber(String inputNumber , String token  , CallBackWrapper<LoggedSmsConfirmResponse>  callback);

}
