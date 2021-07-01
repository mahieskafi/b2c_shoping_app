package com.srp.eways.repository.logout;

import com.srp.eways.di.DIMain;
import com.srp.eways.network.CallBackWrapper;
import com.srp.eways.model.logout.LogoutResponse;
import com.srp.eways.repository.remote.logout.LogoutApiService;

/**
 * Created by Eskafi on 9/23/2019.
 */
public class LogoutRepositoryImplementation implements LogoutRepository {

    private static LogoutRepositoryImplementation instance = null;

    public static LogoutRepositoryImplementation getInstance() {
        if (instance == null)
            instance = new LogoutRepositoryImplementation();

        return instance;
    }

    public LogoutApiService mLogoutApiService;

    private LogoutRepositoryImplementation() {

        mLogoutApiService = DIMain.getLogoutApi();
    }

    @Override
    public void logout(CallBackWrapper<LogoutResponse> callBack) {
        mLogoutApiService.logout(callBack);

    }
}
