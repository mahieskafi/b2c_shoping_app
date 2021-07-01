package com.srp.eways.repository.logout;

import com.srp.eways.network.CallBackWrapper;
import com.srp.eways.model.logout.LogoutResponse;

/**
 * Created by Eskafi on 9/23/2019.
 */
public interface LogoutRepository {
    void logout(CallBackWrapper<LogoutResponse> callBack);
}
