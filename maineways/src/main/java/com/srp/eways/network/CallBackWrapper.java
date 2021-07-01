package com.srp.eways.network;


/**
 * Created by Eskafi on 8/3/2019.
 */
public interface CallBackWrapper<T> {

    void onError(int errorCode, String errorMessage);

    void onSuccess(T responseBody);
}
