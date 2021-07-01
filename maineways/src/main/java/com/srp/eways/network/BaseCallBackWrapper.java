package com.srp.eways.network;

import com.srp.eways.base.BaseViewModel;

public abstract class BaseCallBackWrapper<T> implements CallBackWrapper<T> {

    private BaseViewModel mBaseViewModel;

    public BaseCallBackWrapper(BaseViewModel baseViewModel) {
        mBaseViewModel = baseViewModel;
    }

    @Override
    public void onError(int errorCode, String errorMessage) {
        if (errorCode == NetworkResponseCodes.ERROR_AUTHORIZATION_FAILED) {
            onAuthorizationFailed();
        }
    }

    public void onAuthorizationFailed() {
        mBaseViewModel.setIsNeededToLogin(true);
    }
}
