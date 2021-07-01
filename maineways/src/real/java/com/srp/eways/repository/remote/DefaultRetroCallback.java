package com.srp.eways.repository.remote;

import android.util.Log;

import com.srp.eways.network.BaseCallBackWrapper;
import com.srp.eways.network.CallBackWrapper;
import com.srp.eways.network.NetworkResponseCodes;
import com.srp.eways.network.NetworkUtil;
import com.srp.eways.util.Utils;

import java.io.IOException;
import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DefaultRetroCallback<T> implements Callback<T>, NetworkResponseCodes {

    private static final String TAG = "DefaultRetroCallback";

    private CallBackWrapper<T> mCallback;

    public DefaultRetroCallback(CallBackWrapper<T> callback) {
        mCallback = callback;
    }

    @Override
    public final void onResponse(Call<T> call, Response<T> response) {
        ErrorInfo errorInfo = new ErrorInfo();

        getErrorCodeForResponse(response, errorInfo);

        if (errorInfo.errorCode != SUCCESS) {
            handleError(errorInfo.errorCode, errorInfo.errorMessage);
        } else {
            mCallback.onSuccess(response.body());
        }
    }

    private void getErrorCodeForResponse(Response<T> response, ErrorInfo errorInfo) {
        int statusCode = response.code();

        int errorCode = SUCCESS;

        if (statusCode >= 500) {
            errorCode = ERROR_SERVER_PROBLEM;
        }

        switch (statusCode) {
            case 406:
            case 405:
            case 404:
            case 400:
                errorCode = ERROR_UNSUPPORTED_API;
                break;
            case 403:
            case 401:
                errorCode = ERROR_AUTHORIZATION_FAILED;
                break;
        }

        if (errorCode == SUCCESS && statusCode >= 300) {
            errorCode = ERROR_UNDEFINED;
        }

        if (errorCode == SUCCESS && !response.isSuccessful()) {
            errorCode = ERROR_UNDEFINED;
        }

        if (errorCode != SUCCESS) {
            Log.e(TAG, "API call (" + response.raw().request().url() + ") failed with http response code: " + statusCode);

            errorInfo.errorCode = errorCode;
            return;
        }

        checkResponseForError(response.body(), errorInfo);
    }

    protected void checkResponseForError(T response, ErrorInfo errorInfo) {
        errorInfo.errorCode = SUCCESS;
    }

    @Override
    public final void onFailure(Call<T> call, Throwable t) {
        Log.e(TAG, "Network call failed: " + call.request().url().toString(), t);

        int errorCode;

        if (!Utils.isInternetAvailable()) {
            errorCode = ERROR_NO_INTERNET;
        } else if (t instanceof SocketTimeoutException) {
            errorCode = ERROR_TIMEOUT;
        }

//        else if (t instanceof IOException) {
//            // TODO Must check for network connectivity through network info. Later!
//            errorCode = ERROR_NO_INTERNET;
//        }
        else {
            errorCode = ERROR_UNDEFINED;
        }

        handleError(errorCode, null);
    }

    private void handleError(int errorCode, String errorMessage) {
        if (errorMessage == null) {
            errorMessage = NetworkUtil.getErrorText(errorCode);
        }

        if (errorCode == ERROR_AUTHORIZATION_FAILED) {
            if (mCallback instanceof BaseCallBackWrapper) {
                ((BaseCallBackWrapper) mCallback).onAuthorizationFailed();
            }
        } else {
            mCallback.onError(errorCode, errorMessage);
        }
    }

    public static class ErrorInfo {

        public int errorCode;
        public String errorMessage;

    }

}
