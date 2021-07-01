package com.srp.eways.repository.validatetoken;

import com.srp.eways.network.CallBackWrapper;
import com.srp.eways.model.login.LoginResponse;

/**
 * Created by Eskafi on 8/25/2019.
 */
public interface ValidateTokenRepository {

    void validateToken(CallBackWrapper<LoginResponse> callBack);
}
