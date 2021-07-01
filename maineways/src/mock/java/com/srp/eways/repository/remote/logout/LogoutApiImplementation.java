package com.srp.eways.repository.remote.logout;

import android.os.Handler;

import com.srp.eways.network.CallBackWrapper;
import com.srp.eways.model.logout.LogoutResponse;
import com.srp.eways.repository.remote.BaseApiImplementation;

/**
 * Created by Eskafi on 9/23/2019.
 */
public class LogoutApiImplementation extends BaseApiImplementation implements LogoutApiService {

    private static LogoutApiImplementation sInstance;

    public static LogoutApiImplementation getInstance() {
        if (sInstance == null) {
            sInstance = new LogoutApiImplementation();
        }

        return sInstance;
    }


    public LogoutApiImplementation() {
    }

    @Override
    public void logout(CallBackWrapper<LogoutResponse> callBackWrapper) {
        if (!handleCall(callBackWrapper)) {
            handleCallInternal(callBackWrapper);
        }else
        {
            callBackWrapper.onError(getMode(),"");
        }
    }

    private void handleCallInternal(final CallBackWrapper<LogoutResponse> callback) {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(createSuccessResult());
            }
        }, getResponseDelay());

    }

    private LogoutResponse createSuccessResult() {

        return new LogoutResponse("success", 0);
    }
}
