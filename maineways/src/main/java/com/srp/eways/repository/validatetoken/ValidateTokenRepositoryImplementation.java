package com.srp.eways.repository.validatetoken;

import com.srp.eways.di.DIMain;
import com.srp.eways.network.CallBackWrapper;
import com.srp.eways.model.login.LoginResponse;
import com.srp.eways.repository.remote.validatetoken.ValidateTokenApiService;

/**
 * Created by Eskafi on 8/25/2019.
 */
public class ValidateTokenRepositoryImplementation implements ValidateTokenRepository {
    
    private static ValidateTokenRepositoryImplementation INSTANCE = null;

    private ValidateTokenApiService mValidateTokenApiService;


    public static ValidateTokenRepositoryImplementation getInstance() {

        if (INSTANCE == null) {

            INSTANCE = new ValidateTokenRepositoryImplementation();
        }

        return INSTANCE;
    }

    private ValidateTokenRepositoryImplementation(){

        mValidateTokenApiService = DIMain.getValidateTokenApi();
    }

    @Override
    public void validateToken(CallBackWrapper<LoginResponse> callBack) {

        mValidateTokenApiService.validateToken(callBack);
    }

}
