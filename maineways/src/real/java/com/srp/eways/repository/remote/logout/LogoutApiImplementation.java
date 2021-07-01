package com.srp.eways.repository.remote.logout;

import com.srp.eways.AppConfig;
import com.srp.eways.di.DIMain;
import com.srp.eways.network.CallBackWrapper;
import com.srp.eways.model.logout.LogoutResponse;
import com.srp.eways.repository.remote.BaseApiImplementation;
import com.srp.eways.repository.remote.DefaultRetroCallback;

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

    private LogoutApiRetrofit mLogoutApiRetrofit;

    public LogoutApiImplementation() {
        mLogoutApiRetrofit =  DIMain.provideApi(LogoutApiRetrofit.class);
    }

    @Override
    public void logout(CallBackWrapper<LogoutResponse> callBackWrapper) {
        mLogoutApiRetrofit.logout(AppConfig.SERVER_VERSION).enqueue(new DefaultRetroCallback<LogoutResponse>(callBackWrapper) {

            @Override
            protected void checkResponseForError(LogoutResponse response, ErrorInfo errorInfo) {
                if (response.getStatus() != 0) {
                    errorInfo.errorCode = response.getStatus();
                    errorInfo.errorMessage = response.getDescription();
                }
            }

        });
    }
}
