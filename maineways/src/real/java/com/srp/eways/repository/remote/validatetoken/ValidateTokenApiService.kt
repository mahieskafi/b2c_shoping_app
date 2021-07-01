package com.srp.eways.repository.remote.validatetoken

import com.srp.eways.model.login.LoginResponse
import com.srp.eways.network.CallBackWrapper

/**
 * Created by Eskafi on 8/25/2019.
 */
interface ValidateTokenApiService {

    fun validateToken(callBack: CallBackWrapper<LoginResponse>)
}
